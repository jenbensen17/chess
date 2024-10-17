package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.utils.Assert;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    static GameService gameService;
    static GameDAO gameDAO;
    static AuthDAO authDAO;
    static GameData testGame;
    static AuthData testAuth;


    @BeforeAll
    static void beforeAll() throws DataAccessException {
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();
        gameService = new GameService(gameDAO, authDAO);
        testAuth = new AuthData("testToken", "testUser");
        authDAO.createAuth(testAuth);
    }

    @BeforeEach
    void setup() {
        testGame = new GameData(1234, "whitePlayer", "blackPlayer", "testGame", new ChessGame());
    }

    @Test
    void listGames() throws DataAccessException {
        GameData testGame2 = new GameData(4321, "whitePlayer", "blackPlayer", "testGame", new ChessGame());
        gameService.createGame(testGame, testAuth);
        gameService.createGame(testGame2, testAuth);
        HashSet<GameData> expected = new HashSet<>();
        expected.add(testGame);
        expected.add(testGame2);
        HashSet<GameData> games = gameService.listGames(testAuth);
        Assertions.assertEquals(expected, games);
    }

    @Test
    void listGames_fail() throws DataAccessException {
       Assertions.assertThrows(DataAccessException.class, () -> gameService.listGames(new AuthData("invalidToken", "testUser")));
    }

    @Test
    void createGame() throws DataAccessException {
        int gameID = gameService.createGame(testGame, testAuth);
        Assertions.assertEquals(1234, gameID);
    }

    @Test
    void createGame_fail() throws DataAccessException {
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.createGame(testGame, new AuthData("invalidToken", "testUser"));
        });
    }

    @Test
    void joinGame() throws DataAccessException {
        testGame = new GameData(1234, "whitePlayer", null, "testGame", new ChessGame());
        gameService.createGame(testGame, testAuth);
        gameService.joinGame(testGame,testAuth, ChessGame.TeamColor.BLACK);
        GameData expected = new GameData(1234, "whitePlayer", testAuth.getUsername(), "testGame", testGame.getGame());
        Assertions.assertEquals(expected, gameDAO.getGame(testGame.getGameID()));
    }

    @Test
    void joinGame_fail() throws DataAccessException {
        gameService.createGame(testGame, testAuth);
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.joinGame(testGame,testAuth, ChessGame.TeamColor.BLACK);
        });
    }
}