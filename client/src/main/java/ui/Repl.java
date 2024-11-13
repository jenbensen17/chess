package ui;

import client.ServerFacade;

public class Repl {

    private final ServerFacade server;
    private final PreLoginUI preLogin;

    public Repl(ServerFacade server) {
        this.server = server;
        this.preLogin = new PreLoginUI(server);
    }

    public void run() {
        System.out.println("♕ Welcome to 240 Chess. Type Help to get started. ♕\n");
        preLogin.run();
    }
}
