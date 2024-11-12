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

import static ui.EscapeSequences.SET_BG_COLOR_BLUE;
import static ui.EscapeSequences.SET_BG_COLOR_DARK_GREY;

public class PostLoginUI {
    private final ServerFacade server;
    private State state = State.SIGNEDIN;
    private final AuthData authData;
    private Map<String, Integer> games;

    public PostLoginUI(ServerFacade server, AuthData authData) {
        this.server = server;
        this.authData = authData;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        var result = "";
        var stateString = state == State.SIGNEDOUT ? "[LOGGED_OUT]" : "[LOGGED_IN]";
        while(!result.toLowerCase().equals("quit") && state == State.SIGNEDIN) {
            System.out.print(stateString+" >>> ");
            String line = scanner.nextLine();
            try{
                result = eval(line);
                System.out.println(result);
            } catch(Throwable e) {
                System.out.println(e.toString());
            }
        }
        if(state == State.SIGNEDOUT) {
            new PreLoginUI(server).run();
        }
    }

    public String eval(String input) {
        try{
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch(cmd) {
                case "quit" -> "quit";
                case "logout" -> logout();
                case "create" -> create(params);
                case "list" -> list();
                case "join" -> join(params);
                case "observe" -> observe(params);
                default -> help();
            };
        } catch(Exception e) {
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
        try{
            server.logout(authData.getAuthToken());
            state = State.SIGNEDOUT;
        } catch(Throwable e) {
            System.out.println("Unable to logout");
        }
        return "logged out";
    }

    private String create(String... params) {
        if(params.length == 0) {
            return "Please enter a game name";
        } else {
            try{
                server.createGame(authData.getAuthToken(), params[0]);
                return "Game successfully created";
            } catch(Throwable e) {
                return "Unable to create game";
            }
        }
    }

    private String list() {
        try{
            HashSet<GameData> gameList = server.listGames(authData.getAuthToken());
            games = new HashMap<String, Integer>();
            String result = "";
            int i = 1;
            for(GameData gameData : gameList) {
                result+=i+". "+gameData.toString()+"\n";
                games.put(""+i,gameData.getGameID());
                i++;
            }
            return result;
        } catch(Throwable e) {
            return "Unable to list games";
        }
    }

    private String join(String... params) {
        if(params.length == 0 || (!Objects.equals(params[1], "white") && !Objects.equals(params[1], "black"))) {
            return "Please enter a valid game ID and your team color";
        } else {
            try{
                ChessGame.TeamColor color = params[1].equals("black") ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
                int gameID = games.get(params[0]);
                server.joinGame(authData.getAuthToken(), color, gameID);
                return "Game successfully joined";
            } catch(Throwable e) {
                return "Unable to join game";
            }
        }
    }

    private String observe(String... params) {
        if(params.length == 0 ) {
            return "Please enter a valid game ID";
        } else {
            try{
                int gameID = games.get(params[0]);
                HashSet<GameData> games = server.listGames(authData.getAuthToken());
                GameData game = null;
                for(GameData g : games) {
                    if(g.getGameID() == gameID) {
                        game = g;
                    }
                }
                printBoard(game.getGame().getBoard(), ChessGame.TeamColor.WHITE);
                printBoard(game.getGame().getBoard(), ChessGame.TeamColor.BLACK);
                return "Game Observed";
            } catch(Throwable e) {
                return "Unable to observe game";
            }
        }
    }

    private void printBoard(ChessBoard board, ChessGame.TeamColor color) {
        System.out.print("|");
        boolean light = true;
        bg(light);
        int limit = color == ChessGame.TeamColor.WHITE ? 8 : 1;
        int inc = color == ChessGame.TeamColor.WHITE ? 1 : -1;
        for (int i = color == ChessGame.TeamColor.WHITE ? 1 : 8; i != limit+inc; i+=inc) {
            for (int j = color == ChessGame.TeamColor.WHITE ? 1 : 8; j != limit+inc; j+=inc) {
                if (board.getPiece(new ChessPosition(i, j)) != null) {
                    ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                    String rep = piece.getPieceType().toString();
                    if (rep == "KNIGHT") {
                        rep = "N";
                    }
                    if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                        rep = rep.toLowerCase();
                    } else {
                        rep = rep.toUpperCase();
                    }
                    System.out.print(rep.charAt(0) + "|");
                } else {
                    System.out.print(" |");
                }
                light = !light;
                bg(light);
            }
            if (i != limit+inc) {
                System.out.print("\n|");
            }
        }
        System.out.print("\n");
    }

    private void bg(boolean light) {
        if (light) {
            System.out.print(SET_BG_COLOR_BLUE);
        } else {
            System.out.print(SET_BG_COLOR_DARK_GREY);
        }
    }


}
