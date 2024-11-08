package ui;

import client.ServerFacade;
import client.State;

public class PreLoginRepl {
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;
    public PreLoginRepl(ServerFacade server) {
        this.server = server;
    }


    public void run() {
        System.out.println("Pre-login repl");
    }
}
