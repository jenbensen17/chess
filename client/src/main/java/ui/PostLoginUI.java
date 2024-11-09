package ui;

import client.ServerFacade;
import client.State;
import model.AuthData;
import model.GameData;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

public class PostLoginUI {
    private final ServerFacade server;
    private State state = State.SIGNEDIN;
    private final AuthData authData;

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
            String result = "";
            int i = 1;
            for(GameData gameData : gameList) {
                result+=i+". "+gameData.toString()+"\n";
                i++;
            }
            return result;
        } catch(Throwable e) {
            return "Unable to list games";
        }
    }

}
