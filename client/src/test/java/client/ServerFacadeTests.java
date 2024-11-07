package client;

import dataaccess.DataAccessException;
import model.AuthData;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @BeforeEach
    void setup() throws Exception {
        server.clearDB();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void register() throws Exception {
        AuthData authData = facade.register("player1", "password", "p1@email.com");
        Assertions.assertTrue(authData.getAuthToken().length() > 10);
    }

    @Test
    public void registerFail() throws Exception {
        AuthData authData = facade.register("player1", "password", "p1@email.com");
        Assertions.assertThrows(Exception.class, () -> {
            AuthData authData2 = facade.register("player1", "password", "p2@email.com");
        });
    }

    @Test
    public void login() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        AuthData authData = facade.login("player1", "password");
        Assertions.assertTrue(authData.getAuthToken().length() > 10);
    }

    @Test
    public void loginFail() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        Assertions.assertThrows(Exception.class, () -> {
            AuthData authData = facade.login("player1", "INCORRECT PASSWORD");
        });
    }

}
