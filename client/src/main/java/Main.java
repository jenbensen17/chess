import client.ServerFacade;
import ui.Repl;

public class Main {
    public static void main(String[] args) {

        ServerFacade serverFacade = new ServerFacade(8080);
        new Repl(serverFacade).run();
    }
}