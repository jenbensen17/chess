package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
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
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch(command.getCommandType()) {
            case CONNECT -> connect(command.getAuthToken(), command.getGameID(), session);
        }
    }

    private void connect( String authToken,  int gameID, Session session) throws IOException {


        connections.add(gameID, session);
        var message = String.format("Connected to game:%d", gameID);

    }
}
