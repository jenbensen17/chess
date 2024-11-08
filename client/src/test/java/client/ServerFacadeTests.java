package client;

import model.AuthData;
import org.junit.jupiter.api.*;
import server.Server;


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

    @Test
    public void logout() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        AuthData authData = facade.login("player1", "password");
        facade.logout(authData.getAuthToken());
        Assertions.assertThrows(Exception.class, () -> {
            facade.logout(authData.getAuthToken());
        });
    }

    @Test
    public void logoutFail() throws Exception {
        Assertions.assertThrows(Exception.class, () -> {
            facade.logout("invalid user auth token");
        });
    }

    @Test
    public void createGame() throws Exception {
        AuthData authData = facade.register("player1", "password", "p1@email.com");
        int gameID = facade.createGame(authData.getAuthToken(), "testGame");
        Assertions.assertEquals(gameID, 1);
    }

    @Test
    public void createGameFail() throws Exception {
        Assertions.assertThrows(Exception.class, () -> {
            facade.createGame("invalid", "testGame");
        });
    }

    @Test
    public void listGames() throws Exception {
        AuthData authData = facade.register("player1", "password", "p1@email.com");
        facade.createGame(authData.getAuthToken(), "testGame");
        facade.createGame(authData.getAuthToken(), "testGame2");
        Assertions.assertEquals(2, facade.listGames(authData.getAuthToken()).size());
    }

    @Test
    public void listGamesFail() throws Exception {
        Assertions.assertThrows(Exception.class, () -> {
            facade.listGames("invalid");
        });
    }

}
