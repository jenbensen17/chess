package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

class GameServiceTest {

    static GameService gameService;
    static GameDAO gameDAO;
    static AuthDAO authDAO;
    static UserDAO userDAO;
    static GameData testGame;
    static AuthData testAuth;
    static ClearService clearService;


    @BeforeAll
    static void beforeAll() throws DataAccessException {
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();
        userDAO = new MemoryUserDAO();
        gameService = new GameService(gameDAO, authDAO);
        clearService = new ClearService(userDAO, gameDAO, authDAO);
    }

    @BeforeEach
    void setup() throws DataAccessException {
        clearService.clearApp();
        testGame = new GameData(1, "whitePlayer", "blackPlayer", "testGame", new ChessGame());
        testAuth = new AuthData("testToken", "testUser");
        authDAO.createAuth(testAuth);
    }

    @Test
    void listGames() throws DataAccessException {
        GameData testGame2 = new GameData(2, "whitePlayer", "blackPlayer", "testGame", new ChessGame());
        gameService.createGame(testGame, testAuth);
        gameService.createGame(testGame2, testAuth);
        HashSet<GameData> expected = new HashSet<>();
        expected.add(testGame);
        expected.add(testGame2);
        HashSet<GameData> games = gameService.listGames(testAuth);
        Assertions.assertEquals(expected, games);
    }

    @Test
    void listGamesFail() throws DataAccessException {
        Assertions.assertThrows(DataAccessException.class, () -> gameService.listGames(new AuthData("invalidToken", "testUser")));
    }

    @Test
    void createGame() throws DataAccessException {
        int gameID = gameService.createGame(testGame, testAuth);
        Assertions.assertEquals(1, gameID);
    }

    @Test
    void createGameFail() throws DataAccessException {
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.createGame(testGame, new AuthData("invalidToken", "testUser"));
        });
    }

    @Test
    void joinGame() throws DataAccessException {
        testGame = new GameData(1, "whitePlayer", null, "testGame", new ChessGame());
        gameService.createGame(testGame, testAuth);
        gameService.joinGame(testGame, testAuth, ChessGame.TeamColor.BLACK);
        GameData expected = new GameData(1, "whitePlayer", testAuth.getUsername(), "testGame", testGame.getGame());
        Assertions.assertEquals(expected, gameDAO.getGame(testGame.getGameID()));
    }

    @Test
    void joinGameFail() throws DataAccessException {
        gameService.createGame(testGame, testAuth);
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.joinGame(testGame, testAuth, ChessGame.TeamColor.BLACK);
        });
    }
}