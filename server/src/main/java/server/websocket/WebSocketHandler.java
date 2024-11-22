package server.websocket;

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
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGame;
import websocket.messages.ServerMessage;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketConnect
    public void onConnect(Session session) {
        connections.add(0,session);
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        for(var c: connections.connections.values()) {
            if(c.session.equals(session)) {
                connections.remove(c.gameID);
            }
        }
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch(command.getCommandType()) {
            case CONNECT -> connect(command.getAuthToken(), command.getGameID(), session);
        }
    }

    private void connect( String authToken,  int gameID, Session session) throws IOException, DataAccessException {


        connections.add(gameID, session);

        AuthData authData = server.Server.authDAO.getAuth(authToken);
        GameData gameData = server.Server.gameDAO.getGame(gameID);

        var message = String.format("Player %s has connected to game:%d", authData.getUsername(), gameID);

        LoadGame loadGame = new LoadGame(gameData.getGame());
        System.out.println(loadGame.getGame().getBoard().toString());
        //Connection connection = connections.getConnection(gameID);
        //connection.send(message);
    }
}
