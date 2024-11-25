package server.websocket;

import chess.ChessGame;
import chess.InvalidMoveException;
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
        }
    }

    private void connect( UserGameCommand command, Session session) throws IOException {


        try {
            connections.add(command.getAuthToken(), session);
            Connection connection = connections.getConnection(command.getAuthToken());

            AuthData authData = server.Server.authDAO.getAuth(command.getAuthToken());
            GameData gameData = server.Server.gameDAO.getGame(command.getGameID());

            LoadGame loadGame = new LoadGame(gameData.getGame());

            connection.send(loadGame);
            Notification notification = new Notification(authData.getUsername() + " has joined game " + command.getGameID() + " as ");
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
                throw new InvalidMoveException();
            }
            gameData.getGame().makeMove(command.getMove());
            Server.gameDAO.updateGameState(gameData);
            LoadGame loadGame = new LoadGame(gameData.getGame());
            connection.send(loadGame);
            connections.broadcast(command.getAuthToken(), loadGame);
            Notification notification = new Notification("a new move was made");
            connections.broadcast(command.getAuthToken(), notification);
        } catch (InvalidMoveException | DataAccessException e) {
            Connection connection = connections.getConnection(command.getAuthToken());
            Error err = new Error("Error: invalid move");
            connection.send(err);
        } catch (IOException e) {
                Error err = new Error("Error: bad auth token");
                session.getRemote().sendString(new Gson().toJson(err));
        }

    }

    public ChessGame.TeamColor getUserColor(AuthData authData, GameData gameData) {
        if(gameData.getWhiteUsername().equals(authData.getUsername())) {
            return ChessGame.TeamColor.WHITE;
        } else {
            return ChessGame.TeamColor.BLACK;
        }
    }
}
