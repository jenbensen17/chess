package ui;

import client.ServerFacade;
import client.State;

import java.util.Scanner;

public class Repl {

    private final ServerFacade server;
    private static State state = State.SIGNEDOUT;
    private UI ui;

    public Repl(ServerFacade server) {
        System.out.println("♕ Welcome to 240 Chess. Type Help to get started. ♕\n");
        this.server = server;
        ui = new PreLoginUI(server);
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        var result = "";

        while (!result.equalsIgnoreCase("quit")) {
            var stateString = state == State.SIGNEDOUT ? "[LOGGED_OUT]" : "[LOGGED_IN]";
            System.out.print(stateString + " >>> ");
            String line = scanner.nextLine();
            try {
                result = ui.eval(line);
                System.out.println(result);
            } catch (Throwable e) {
                System.out.println(e.toString());
            }
            if (state == State.SIGNEDOUT && !(ui instanceof PreLoginUI)) {
                ui = new PreLoginUI(server);
            }
            if (state == State.SIGNEDIN && !(ui instanceof PostLoginUI)) {
                ui = new PostLoginUI(server);
            }
        }

    }

    public static void setState(State s) {
        state = s;
    }
}
