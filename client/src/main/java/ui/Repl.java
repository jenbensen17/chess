package ui;

import chess.ChessGame;
import client.ServerFacade;
import client.State;
import client.websocket.NotificationHandler;
import client.websocket.WebSocketFacade;

import java.util.Scanner;

public class Repl {

    private final ServerFacade server;
    private static State state = State.SIGNEDOUT;
    private static ChessGame.TeamColor userColor;
    private UI ui;
    private WebSocketFacade ws;
    private final NotificationHandler notificationHandler;

    public Repl(ServerFacade server) throws Exception {
        this.notificationHandler = new NotificationHandler();
        System.out.println("♕ Welcome to 240 Chess. Type Help to get started. ♕\n");
        this.server = server;
        ui = new PreLoginUI(server);
    }

    public void run() throws Exception {
        Scanner scanner = new Scanner(System.in);
        var result = "";

        while (!result.equalsIgnoreCase("quit")) {
            newInput();
            String line = scanner.nextLine();
            try {
                result = ui.eval(line);
                System.out.println(result);
            } catch (Throwable e) {
                System.out.println(e);
            }
            if (state == State.SIGNEDOUT && !(ui instanceof PreLoginUI)) {
                ui = new PreLoginUI(server);
            }
            if (state == State.SIGNEDIN && !(ui instanceof PostLoginUI)) {
                ui = new PostLoginUI(server);
            }
            if (state == State.INGAME && !(ui instanceof GameplayUI)) {
                //ws = new WebSocketFacade(server.getServerUrl(), notificationHandler);
                ws = new WebSocketFacade(server.getServerUrl(), new NotificationHandler());
                ws.connect(ui.getAuthData().getAuthToken(), ui.getGameID());
                ui = new GameplayUI(server, ws);
            }
        }

    }

    public static void newInput() {
        var stateString = state == State.SIGNEDOUT ? "[LOGGED_OUT]" : "[LOGGED_IN]";
        if(state == State.INGAME) {
            stateString = "[PLAYING_GAME]";
        }
        System.out.print(stateString + " >>> ");
    }

    public static void setState(State s) {
        state = s;
    }

    public static void setUserColor(ChessGame.TeamColor color) {
        userColor = color;
    }

    public static ChessGame.TeamColor getUserColor() {
        return userColor;
    }
}
