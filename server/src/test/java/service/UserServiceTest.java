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
    UserData testUser;

    @BeforeAll
    static void beforeAll() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        userService = new UserService(userDAO, authDAO);
    }

    @BeforeEach
    void setUp() {
        testUser = new UserData("User", "test123", "test@gmail.com");
    }

    @Test
    void register() throws DataAccessException {
        AuthData authData = userService.register(testUser);
        assertNotNull(authData);
    }

    @Test
    void register_fail() throws DataAccessException {
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
    void login_fail() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            userService.login(testUser);
        });
    }
}