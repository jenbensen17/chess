package ui;

import chess.ChessGame;
import client.ServerFacade;
import client.State;
import client.websocket.WebSocketFacade;

import java.io.IOException;
import java.util.Arrays;

public class GameplayUI extends UI{

    private final ServerFacade server;
    private final WebSocketFacade websocket;
    private static ChessGame game;
    private static ChessGame.TeamColor teamColor;

    public GameplayUI(ServerFacade server, WebSocketFacade websocket) {
        this.server = server;
        this.websocket = websocket;
    }


    @Override
    public String eval(String input) {
        try {
            var cmd = input;
            return switch (cmd) {
                case "help" -> help();
                case "redraw chess board" -> redraw();
                case "leave" -> leave();
                default -> help();
            };
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private String leave() throws IOException {
        websocket.leave(getAuthData().getAuthToken(), getGameID());
        Repl.setState(State.SIGNEDIN);
        return "";
    }

    private String redraw() {
        BoardPrinter.printBoard(game.getBoard(), teamColor);
        return "";
    }

    private String help() {
        return """
                redraw chess board - redraws chessboard
                leave - leave game
                make move - input move
                resign - forfeit game
                highlight legal moves - show legal moves
                help - with possible commands
                """;
    }

    public static void setGame(ChessGame game) {
        GameplayUI.game = game;
    }

    public static void setColor(ChessGame.TeamColor teamColor) {
        GameplayUI.teamColor = teamColor;
    }

}
