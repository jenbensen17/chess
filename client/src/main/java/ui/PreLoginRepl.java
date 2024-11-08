package ui;

import client.ServerFacade;
import client.State;

import java.util.Arrays;
import java.util.Scanner;

public class PreLoginRepl {
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;

    public PreLoginRepl(ServerFacade server) {
        this.server = server;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while(!result.toLowerCase().equals("quit")) {
            String line = scanner.nextLine();
            try{
                result = eval(line);
                System.out.println(result);
            } catch(Throwable e) {
                System.out.println(e.toString());
            }
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
}
