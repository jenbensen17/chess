package client.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import ui.BoardPrinter;
import ui.Repl;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGame;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import javax.websocket.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint{
    Session session;
    NotificationHandler notificationHandler;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws Exception {
        try{
            url = url.replace("http", "ws");
            URI socketURI = new URI(url+"/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    switch(serverMessage.getServerMessageType()) {
                        case LOAD_GAME -> {
                            LoadGame loadGame = new Gson().fromJson(message, LoadGame.class);
                            loadGame(loadGame);
                        }
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException e) {
            throw new Exception();
        }
    }

    private void loadGame(LoadGame loadGame) {
        BoardPrinter.printBoard(loadGame.getGame().getBoard(), Repl.getUserColor());
    }

    @Override
    public void onOpen(javax.websocket.Session session, EndpointConfig endpointConfig) {
    }

    public void connect(String authToken, int gameID) throws IOException {
        var message = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
        this.session.getAsyncRemote().sendText(new Gson().toJson(message));
    }
}
