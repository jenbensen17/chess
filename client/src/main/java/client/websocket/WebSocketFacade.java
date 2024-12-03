package client.websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import ui.BoardPrinter;
import ui.GameplayUI;
import ui.Repl;
import ui.UI;
import websocket.commands.MakeMove;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGame;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;
import websocket.messages.Error;

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
                    switch (serverMessage.getServerMessageType()) {
                        case LOAD_GAME -> {
                            LoadGame loadGame = new Gson().fromJson(message, LoadGame.class);
                            loadGame(loadGame);
                        }
                        case NOTIFICATION -> {
                            Notification notification = new Gson().fromJson(message, Notification.class);
                            notificationHandler.notify(notification);
                        }
                        case ERROR -> {
                            Error error = new Gson().fromJson(message, Error.class);
                            System.out.println("\n"+error.toString());
                        }
                    }
                    ui.Repl.newInput();
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException e) {
            throw new Exception();
        }
    }

    private void loadGame(LoadGame loadGame) {
        BoardPrinter.printBoard(loadGame.getGame().getBoard(), Repl.getUserColor(), null);
        GameplayUI.setGame(loadGame.getGame());
        GameplayUI.setColor(Repl.getUserColor());
    }

    @Override
    public void onOpen(javax.websocket.Session session, EndpointConfig endpointConfig) {
    }

    public void connect(String authToken, int gameID) throws IOException {
        var message = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
        message.setIsObserver(!UI.isPlayer());
        this.session.getBasicRemote().sendText(new Gson().toJson(message));
    }

    public void leave(String authToken, int gameID) throws IOException {
        var message = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
        message.setIsObserver(!UI.isPlayer());
        this.session.getBasicRemote().sendText(new Gson().toJson(message));
    }

    public void makeMove(String authToken, int gameID, ChessMove move, String startPosition, String endPosition) throws IOException {
        var message = new MakeMove(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID, move, startPosition, endPosition);
        message.setIsObserver(!UI.isPlayer());
        this.session.getBasicRemote().sendText(new Gson().toJson(message));
    }

    public void resign(String authToken, int gameID) throws IOException {
        var message = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
        message.setIsObserver(!UI.isPlayer());
        this.session.getBasicRemote().sendText(new Gson().toJson(message));
    }

}
