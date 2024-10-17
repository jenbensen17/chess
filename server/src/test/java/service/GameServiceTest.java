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
    void listGames() {

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
}