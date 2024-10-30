package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ClearServiceTest {

    private UserDAO userDAO;
    private GameDAO gameDAO;
    private AuthDAO authDAO;
    private ClearService clearService;

    @BeforeEach
    void setUp() throws DataAccessException {
        userDAO = new MemoryUserDAO();
        userDAO.createUser(new UserData("testUser", "password", "test@gmail.com"));
        gameDAO = new MemoryGameDAO();
        gameDAO.createGame(new GameData(123, "white", "black", "test", new ChessGame()));
        authDAO = new MemoryAuthDAO();
        authDAO.createAuth(new AuthData("4567", "test"));
        clearService = new ClearService(userDAO, gameDAO, authDAO);
    }

    @Test
    void testClearApp() throws DataAccessException {
        clearService.clearApp();
        Assertions.assertThrows(DataAccessException.class, () -> {
            userDAO.getUserData("testUser");
        });
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameDAO.getGame(123);
        });
        Assertions.assertThrows(DataAccessException.class, () -> {
            authDAO.getAuth("4567");
        });
    }
}