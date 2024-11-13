package ui;

import client.ServerFacade;
import client.State;

import java.util.Arrays;

public class PreLoginUI extends UI {
    private final ServerFacade server;

    public PreLoginUI(ServerFacade server) {
        this.server = server;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "help" -> help();
                case "quit" -> "quit";
                case "register" -> register(params);
                case "login" -> login(params);
                default -> help();
            };
        } catch (Exception e) {
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
        if (params.length < 3) {
            return "Please enter a valid username, password, and email address";
        } else {
            try {
                setAuthData(server.register(params[0], params[1], params[2]));
                Repl.setState(State.SIGNEDIN);
                return "Successfully registered as " + params[0];
            } catch (Exception e) {
                return "User already exists";
            }
        }
    }

    private String login(String... params) {
        if (params.length < 2) {
            return "Please enter a valid username and password";
        } else {
            try {
                setAuthData(server.login(params[0], params[1]));
                Repl.setState(State.SIGNEDIN);
                return "Successfully logged in as " + params[0];
            } catch (Exception e) {
                return "Invalid username or password";
            }
        }
    }

}
