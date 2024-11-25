package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections
            = new ConcurrentHashMap<>();

    public void add(String authToken, Session session) {
        var connection = new Connection(authToken, session);
        connections.put(authToken, connection);
    }

    public void remove(String authToken) {
        connections.remove(authToken);
    }

    public Connection getConnection(String authToken) {
        return connections.get(authToken);
    }

    public void broadcast(String excludeToken, ServerMessage message) throws IOException {
        var removeList = new ArrayList<Connection>();
        for(var c: connections.values()) {
            if(c.session.isOpen()) {
                if(!(Objects.equals(c.authToken, excludeToken))) {
                    c.send(message);
                }
            } else {
                removeList.add(c);
            }
        }

        for(var c: removeList) {
            connections.remove(c.authToken);
        }
    }

}
