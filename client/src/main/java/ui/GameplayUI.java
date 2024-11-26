package ui;

import client.ServerFacade;

import java.util.Arrays;

public class GameplayUI extends UI{

    private final ServerFacade server;

    public GameplayUI(ServerFacade server) {
        this.server = server;
    }


    @Override
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "help" -> help();
                default -> help();
            };
        } catch (Exception e) {
            return e.getMessage();
        }
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
}
