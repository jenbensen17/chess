package service;

import dataaccess.ClearDAO;
import dataaccess.DataAccessException;
import dataaccess.MemoryClearDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
class ClearServiceTest {

    private ClearDAO memoryClearDAO;
    private ClearService clearService;

    @Test
    void clearApp() {
    }

    @BeforeEach
    void setUp() {
        memoryClearDAO = new MemoryClearDAO(new HashSet<AuthData>(16), new HashSet<UserData>(16)
        , new HashSet<GameData>(16));
        clearService = new ClearService(memoryClearDAO);

    }

    @Test
    void testClearApp() throws DataAccessException {
        clearService.clearApp();

        Assertions.assertTrue(memoryClearDAO.equals(new MemoryClearDAO()));

    }
}