package ui;

import client.ServerFacade;
import client.State;
import model.AuthData;

import java.util.Arrays;
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
        while(!result.toLowerCase().equals("quit") && state == State.SIGNEDIN && !result.equals("logged out")) {
            System.out.print(stateString+" >>> ");
            String line = scanner.nextLine();
            try{
                result = eval(line);
                System.out.println(result);
            } catch(Throwable e) {
                System.out.println(e.toString());
            }
        }
        if(result.equals("logged out")) {
            state = State.SIGNEDOUT;
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
        } catch(Throwable e) {
            System.out.println("Unable to logout");
        }
        return "logged out";
    }

}
