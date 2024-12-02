package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import client.ServerFacade;
import client.State;
import client.websocket.NotificationHandler;
import client.websocket.WebSocketFacade;
import model.GameData;

import java.util.*;

import static ui.EscapeSequences.*;

public class PostLoginUI extends UI {
    private final ServerFacade server;
    private Map<String, Integer> games;
    private WebSocketFacade ws;

    public PostLoginUI(ServerFacade server) {
        this.server = server;

    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "quit" -> "quit";
                case "logout" -> logout();
                case "create" -> create(params);
                case "list" -> list();
                case "join" -> join(params);
                case "observe" -> observe(params);
                default -> help();
            };
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private String help() {
        return """
                create <NAME> - a game
                list - games
                join <ID> [WHITE|BLACK] - a game
                observe <ID> - a game
                logout - when you are done
                quit - playing chess
                help - with possible commands
                """;
    }

    private String logout() {
        try {
            server.logout(getAuthData().getAuthToken());
            Repl.setState(State.SIGNEDOUT);
            setAuthData(null);
        } catch (Throwable e) {
            System.out.println("Unable to logout");
        }
        return "logged out";
    }

    private String create(String... params) {
        if (params.length == 0) {
            return "Please enter a game name";
        } else {
            try {
                server.createGame(getAuthData().getAuthToken(), params[0]);
                return list();
            } catch (Throwable e) {
                return "Unable to create game";
            }
        }
    }

    private String list() {
        try {
            HashSet<GameData> gameList = server.listGames(getAuthData().getAuthToken());
            games = new HashMap<String, Integer>();
            String result = "";
            int i = 1;
            for (GameData gameData : gameList) {
                result += i + ". " + gameData.toString() + "\n";
                games.put("" + i, gameData.getGameID());
                i++;
            }
            return result;
        } catch (Throwable e) {
            return "Unable to list games";
        }
    }

    private String join(String... params) {
        if (params.length < 2 || (!Objects.equals(params[1], "white") && !Objects.equals(params[1], "black"))) {
            return "Please enter a valid game ID and your team color. To view games, use list command.";
        } else {
            try {
                ChessGame.TeamColor color = params[1].equals("black") ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
                int gameID = games.get(params[0]);
                server.joinGame(getAuthData().getAuthToken(), color, gameID);
                Repl.setState(State.INGAME);
                Repl.setUserColor(color);
                setGameID(gameID);
                setIsPlayer(true);
                //printGame(gameID);
                return "";
            } catch (Throwable e) {
                return "Unable to join game";
            }
        }
    }

    private void printGame(int gameID) {
        HashSet<GameData> games = server.listGames(getAuthData().getAuthToken());
        GameData game = null;
        for (GameData g : games) {
            if (g.getGameID() == gameID) {
                game = g;
            }
        }
        BoardPrinter.printBoard(game.getGame().getBoard(), ChessGame.TeamColor.WHITE);
        BoardPrinter.printBoard(game.getGame().getBoard(), ChessGame.TeamColor.BLACK);
    }

    private String observe(String... params) {
        if (params.length == 0) {
            return "Please enter a valid game ID. To view games, use list command.";
        } else {
            try {
                int gameID = games.get(params[0]);
                //printGame(gameID);
                setGameID(gameID);
                setIsPlayer(false);
                Repl.setState(State.INGAME);
                Repl.setUserColor(ChessGame.TeamColor.WHITE);
                return "Observing game...";
            } catch (Throwable e) {
                return "Unable to observe game";
            }
        }
    }



}
