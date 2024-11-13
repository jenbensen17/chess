package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import client.ServerFacade;
import client.State;
import model.AuthData;
import model.GameData;

import java.util.*;

import static ui.EscapeSequences.*;

public class PostLoginUI extends UI {
    private final ServerFacade server;
    private Map<String, Integer> games;

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
            return "Please enter a valid game ID and your team color";
        } else {
            try {
                ChessGame.TeamColor color = params[1].equals("black") ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
                int gameID = games.get(params[0]);
                server.joinGame(getAuthData().getAuthToken(), color, gameID);
                printGame(gameID);
                return "Game successfully joined";
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
        printBoard(game.getGame().getBoard(), ChessGame.TeamColor.WHITE);
        printBoard(game.getGame().getBoard(), ChessGame.TeamColor.BLACK);
    }

    private String observe(String... params) {
        if (params.length == 0) {
            return "Please enter a valid game ID";
        } else {
            try {
                int gameID = games.get(params[0]);
                printGame(gameID);
                return "Game Observed";
            } catch (Throwable e) {
                return "Unable to observe game";
            }
        }
    }

    private void printBoard(ChessBoard board, ChessGame.TeamColor color) {
        System.out.println();
        printColumn();

        boolean light = true;
        int limit = color == ChessGame.TeamColor.WHITE ? 8 : 1;
        int inc = color == ChessGame.TeamColor.WHITE ? 1 : -1;
        for (int i = color == ChessGame.TeamColor.WHITE ? 1 : 8; i != limit + inc; i += inc) {
            printRow(i);
            for (int j = color == ChessGame.TeamColor.WHITE ? 1 : 8; j != limit + inc; j += inc) {
                bg(light);
                if (board.getPiece(new ChessPosition(i, j)) != null) {
                    ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                    String rep = piece.getPieceType().toString();
                    if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                        printPiece(piece, WHITE_KING, WHITE_QUEEN, WHITE_KNIGHT, WHITE_BISHOP, WHITE_ROOK, WHITE_PAWN, light);
                    } else {
                        printPiece(piece, BLACK_KING, BLACK_QUEEN, BLACK_KNIGHT, BLACK_BISHOP, BLACK_ROOK, BLACK_PAWN, light);
                    }
                } else {
                    System.out.print(EMPTY);
                }
                light = !light;
            }
            printRow(i);
            System.out.print(RESET_BG_COLOR);
            System.out.print("\n");
            light = !light;
        }
        printColumn();
        System.out.print("\n");
    }

    private void printPiece(ChessPiece piece, String king, String queen, String knight, String bishop, String rook, String pawn, boolean light) {
        switch (piece.getPieceType()) {
            case KING -> {
                if (piece.getTeamColor() == ChessGame.TeamColor.WHITE && !light) {
                    System.out.print(king);
                } else {
                    System.out.print(queen);
                }
            }
            case QUEEN -> {
                if (piece.getTeamColor() == ChessGame.TeamColor.WHITE && light) {
                    System.out.print(queen);
                } else {
                    System.out.print(king);
                }
            }
            case KNIGHT -> System.out.print(knight);
            case BISHOP -> System.out.print(bishop);
            case ROOK -> System.out.print(rook);
            case PAWN -> System.out.print(pawn);
        }
    }

    private void bg(boolean light) {
        if (light) {
            System.out.print(SET_BG_COLOR_BLUE);
        } else {
            System.out.print(SET_BG_COLOR_DARK_GREY);
        }
    }

    private void printColumn() {
        System.out.print(SET_TEXT_COLOR_WHITE);
        System.out.print(SET_BG_COLOR_LIGHT_GREY);
        System.out.print(SET_TEXT_BOLD);
        System.out.print(EMPTY);
        for (char c = 97; c < 105; c++) {
            System.out.print(" " + c + " ");
        }
        System.out.print(EMPTY);
        System.out.println(RESET_BG_COLOR);
        System.out.print(RESET_TEXT_BOLD_FAINT);
    }

    private void printRow(int i) {
        System.out.print(SET_TEXT_COLOR_WHITE);
        System.out.print(SET_BG_COLOR_LIGHT_GREY);
        System.out.print(SET_TEXT_BOLD);
        System.out.print(" " + i + " ");
        System.out.print(RESET_BG_COLOR);
        System.out.print(RESET_TEXT_BOLD_FAINT);
    }


}
