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
    public void onMessage(Session session, String message) throws IOException, DataAccessException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch(command.getCommandType()) {
            case CONNECT -> connect(command.getAuthToken(), command.getGameID(), session);
        }
    }

    private void connect( String authToken, int gameID, Session session) throws IOException, DataAccessException {


        connections.add(authToken, session);
        Connection connection = connections.getConnection(authToken);

        AuthData authData = server.Server.authDAO.getAuth(authToken);
        GameData gameData = server.Server.gameDAO.getGame(gameID);

        LoadGame loadGame = new LoadGame(gameData.getGame());

        connection.send(loadGame);
        Notification notification = new Notification(authData.getUsername() + " has joined game " + gameID + " as ");
        connections.broadcast(authToken, notification);
    }
}
