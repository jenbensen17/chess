package ui;

import chess.ChessGame;
import client.ServerFacade;

import java.util.Arrays;

public class GameplayUI extends UI{

    private final ServerFacade server;
    private static ChessGame game;
    private static ChessGame.TeamColor teamColor;
    public GameplayUI(ServerFacade server) {
        this.server = server;
    }


    @Override
    public String eval(String input) {
        try {
            var cmd = input;
            return switch (cmd) {
                case "help" -> help();
                case "redraw chess board" -> redraw();
                default -> help();
            };
        } catch (Exception e) {
            return e.getMessage();
        }
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
