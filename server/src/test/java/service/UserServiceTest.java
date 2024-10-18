package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    static UserService userService;
    static UserDAO userDAO;
    static AuthDAO authDAO;
    static GameDAO gameDAO;
    UserData testUser;
    static ClearService clearService;


    @BeforeAll
    static void beforeAll() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();
        userService = new UserService(userDAO, authDAO);
        clearService = new ClearService(userDAO, gameDAO, authDAO);
    }

    @BeforeEach
    void setUp() throws DataAccessException {
        testUser = new UserData("User", "test123", "test@gmail.com");
        clearService.clearApp();
    }

    @Test
    void register() throws DataAccessException {
        AuthData authData = userService.register(testUser);
        assertNotNull(authData);
    }

    @Test
    void registerFail() throws DataAccessException {
        AuthData authData = userService.register(testUser);
        Assertions.assertThrows(DataAccessException.class, () -> {
            userService.register(testUser);
        });
    }

    @Test
    void login() throws DataAccessException {
        userService.register(testUser);
        AuthData authData = userService.login(testUser);
        assertNotNull(authData);
    }

    @Test
    void loginFail() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            userService.login(testUser);
        });
    }

    @Test
    void logout() throws DataAccessException {
        userService.register(testUser);
        AuthData authData = userService.login(testUser);
        userService.logout(authData);
        Assertions.assertThrows(DataAccessException.class, () -> {
            authDAO.getAuth(authData.getAuthToken());
        });
    }

    @Test
    void logoutFail() throws DataAccessException {
        AuthData authData = new AuthData("invalidAuthToken", "testUser");
        Assertions.assertThrows(DataAccessException.class, () -> {
            userService.logout(authData);
        });
    }
}