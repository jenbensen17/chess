package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;

public class Connection {

    public String authToken;
    public Session session;
    public int gameID;

    public Connection(String authToken, Session session, int gameID) {
        this.authToken = authToken;
        this.session = session;
        this.gameID = gameID;
    }

    public void send(ServerMessage message) throws IOException {
        session.getRemote().sendString(new Gson().toJson(message));
    }
}
