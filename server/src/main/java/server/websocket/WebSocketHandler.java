package server.websocket;

import chess.*;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.Server;
import websocket.commands.MakeMove;
import websocket.commands.UserGameCommand;
import websocket.messages.Error;
import websocket.messages.LoadGame;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();


    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        for(var c: connections.connections.values()) {
            if(c.session.equals(session)) {
                connections.remove(c.authToken);
            }
        }
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException, InvalidMoveException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch(command.getCommandType()) {
            case CONNECT -> connect(command, session);
            case MAKE_MOVE -> makeMove(new Gson().fromJson(message, MakeMove.class), session);
            case LEAVE -> leave(command, session);
            case RESIGN -> resign(command, session);
        }
    }

    private void resign(UserGameCommand command, Session session) throws DataAccessException, IOException {
        Connection connection = connections.getConnection(command.getAuthToken());
        AuthData authData = server.Server.authDAO.getAuth(command.getAuthToken());
        GameData gameData = server.Server.gameDAO.getGame(command.getGameID());
        ChessGame.TeamColor userColor = getUserColor(authData, gameData);
        if(userColor == null) {
            Error err = new Error("Error: not a player");
            connection.send(err);
            return;
        } else if (gameData.getGame().isGameOver()) {
            Error err = new Error("Error: game is over");
            connection.send(err);
            return;
        }
        gameData.getGame().endGame();
        Server.gameDAO.updateGameState(gameData);
        Notification notification = new Notification(authData.getUsername() + " has resigned from the game.");
        connection.send(notification);
        connections.broadcast(authData.getAuthToken(), notification);
    }

    private void connect( UserGameCommand command, Session session) throws IOException {


        try {
            connections.add(command.getAuthToken(), session, command.getGameID());
            Connection connection = connections.getConnection(command.getAuthToken());

            AuthData authData = server.Server.authDAO.getAuth(command.getAuthToken());
            GameData gameData = server.Server.gameDAO.getGame(command.getGameID());
            ChessGame.TeamColor color = getUserColor(authData, gameData);
            LoadGame loadGame = new LoadGame(gameData.getGame());

            connection.send(loadGame);
            Notification notification;
            if(command.isPlayer()) {
                notification = new Notification(authData.getUsername() + " has joined game " + command.getGameID() + " as " + color);
            } else {
                notification = new Notification(authData.getUsername() + " has joined game " + command.getGameID() + " as an observer");
            }
            //connection.send(notification);
            connections.broadcast(command.getAuthToken(), notification);
        } catch (DataAccessException e) {
            Connection connection = connections.getConnection(command.getAuthToken());
            Error errorMessage = new Error("Error: invalid request");
            connection.send(errorMessage);
        }
    }

    public void makeMove(MakeMove command, Session session) throws IOException {

        try {
            Connection connection = connections.getConnection(command.getAuthToken());
            if(connection == null) {
                throw new IOException();
            }
            AuthData authData = Server.authDAO.getAuth(command.getAuthToken());
            GameData gameData = Server.gameDAO.getGame(command.getGameID());
            ChessGame.TeamColor color = getUserColor(authData, gameData);
            if(!(gameData.getGame().getTeamTurn() == color)) {
                throw new InvalidMoveException("Not your turn");
            } else if(gameData.getGame().isGameOver()) {
                throw new InvalidMoveException("Game is over");
            }
            gameData.getGame().makeMove(command.getMove());
            Server.gameDAO.updateGameState(gameData);
            LoadGame loadGame = new LoadGame(gameData.getGame());
            connection.send(loadGame);
            connections.broadcast(command.getAuthToken(), loadGame);
            Notification notification = new Notification(authData.getUsername() + moveToString(loadGame.getGame().getBoard(), command.getMove(), command.getStartPosition(), command.getEndPosition()));
            connections.broadcast(command.getAuthToken(), notification);

            checkGameStatus(gameData, authData, color, connection);

        } catch (InvalidMoveException | DataAccessException e) {
            Connection connection = connections.getConnection(command.getAuthToken());
            Error err = new Error("Error: " + (e.getMessage() == null ? "invalid move" : e.getMessage()));
            connection.send(err);
        } catch (IOException e) {
                Error err = new Error("Error: bad auth token");
                session.getRemote().sendString(new Gson().toJson(err));
        }

    }

    public void checkGameStatus(GameData gameData, AuthData authData, ChessGame.TeamColor teamColor, Connection connection) throws IOException {
        String otherPlayerUsername = teamColor == ChessGame.TeamColor.WHITE ? gameData.getBlackUsername() : gameData.getWhiteUsername();
        ChessGame.TeamColor otherPlayerColor = teamColor == ChessGame.TeamColor.WHITE ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
        Notification notification = null;
        if(gameData.getGame().isInCheckmate(otherPlayerColor)) {
            notification = new Notification(otherPlayerUsername + " is in checkmate\nGame Over");
        } else if(gameData.getGame().isInCheck(otherPlayerColor)) {
            notification = new Notification(otherPlayerUsername + " is in check\nGame Over");
        } else if(gameData.getGame().isInStalemate(otherPlayerColor)) {
            notification = new Notification(otherPlayerUsername + " is in stalemate\nGame Over");
        }

        if(notification != null) {
            connection.send(notification);
            connections.broadcast(authData.getAuthToken(), notification);
            gameData.getGame().endGame();
        }

    }

    public String moveToString(ChessBoard board, ChessMove move, String startPosition, String endPosition) {
        ChessPiece.PieceType pieceType = board.getPiece(move.getEndPosition()).getPieceType();
        ChessGame.TeamColor color = board.getPiece(move.getEndPosition()).getTeamColor();

        return " moved their " + pieceType + " from " + startPosition + " to " + endPosition;
    }

    public void leave(UserGameCommand command, Session session) throws DataAccessException, IOException {
        Connection connection = connections.getConnection(command.getAuthToken());
        AuthData authData = Server.authDAO.getAuth(command.getAuthToken());
        GameData gameData = Server.gameDAO.getGame(command.getGameID());
        ChessGame.TeamColor color = getUserColor(authData, gameData);
        if(command.isPlayer()) {
            Server.gameDAO.removePlayer(gameData.getGameID(), color);
        }
        connection.session.close();
        Notification notification = new Notification(authData.getUsername() + " has left the game");
        connections.broadcast(command.getAuthToken(), notification);


    }

    public ChessGame.TeamColor getUserColor(AuthData authData, GameData gameData) {
        if(gameData.getWhiteUsername() != null && gameData.getWhiteUsername().equals(authData.getUsername())) {
            return ChessGame.TeamColor.WHITE;
        } else if (gameData.getBlackUsername() != null && gameData.getBlackUsername().equals(authData.getUsername())) {
            return ChessGame.TeamColor.BLACK;
        } else {
            return null;
        }
    }
}
