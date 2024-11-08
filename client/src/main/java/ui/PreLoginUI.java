package ui;

import client.ServerFacade;
import client.State;

import java.util.Arrays;
import java.util.Scanner;

public class PreLoginUI {
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;

    public PreLoginUI(ServerFacade server) {
        this.server = server;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        var result = "";
        var stateString = state == State.SIGNEDOUT ? "[LOGGED_OUT]" : "[LOGGED_IN]";
        while(!result.toLowerCase().equals("quit") && state == State.SIGNEDOUT) {
            System.out.print(stateString+" >>> ");
            String line = scanner.nextLine();
            try{
                result = eval(line);
                System.out.println(result);
            } catch(Throwable e) {
                System.out.println(e.toString());
            }
        }
        if(state == State.SIGNEDIN) {
            new PostLoginUI(server).run();
        }
    }

    public String eval(String input) {
        try{
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch(cmd) {
              case "help" -> help();
              case "quit" -> "quit";
              case "register" -> register(params);
              case "login" -> login(params);
              default -> help();
            };
        } catch(Exception e) {
            return e.getMessage();
        }
    }

    private String help() {
        String menu = """
                register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                login <USERNAME> <PASSWORD> - to play chess
                quit - playing chess
                help - with possible commands
                """;
        return menu;
    }

    private String register(String... params) {
        if(params.length < 3) {
            return "Please enter a valid username, password, and email address";
        } else {
            try {
                server.register(params[0], params[1], params[2]);
                state = State.SIGNEDIN;
                return "Successfully registered as "+params[0];
            } catch (Exception e) {
                return "User already exists";
            }
        }
    }

    private String login(String... params) {
        if(params.length < 2) {
            return "Please enter a valid username and password";
        } else {
            try{
                server.login(params[0], params[1]);
                state = State.SIGNEDIN;
                return "Successfully logged in as "+params[0];
            } catch (Exception e) {
                return "Invalid username or password";
            }
        }
    }

}
