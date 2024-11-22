package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, Connection> connections
            = new ConcurrentHashMap<>();

    public void add(int gameID, Session session) {
        var connection = new Connection(gameID, session);
        connections.put(gameID, connection);
    }

    public void remove(int gameID) {
        connections.remove(gameID);
    }

    public void broadcast(int excludeGameID, ServerMessage message) throws IOException {
        var removeList = new ArrayList<Connection>();
        for(var c: connections.values()) {
            if(c.session.isOpen()) {
                if(!(c.gameID == excludeGameID)) {
                    c.send(message.toString());
                }
            } else {
                removeList.add(c);
            }
        }

        for(var c: removeList) {
            connections.remove(c.gameID);
        }
    }

}
