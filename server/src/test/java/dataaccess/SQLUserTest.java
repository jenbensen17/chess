package dataaccess;

import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class SQLUserTest {

    static UserDAO userDAO;
    static UserData testUser;

    @BeforeEach
    void setUp() throws DataAccessException {
        DatabaseManager.createDatabase();
        userDAO = new SQLUserDAO();
        try(var conn = DatabaseManager.getConnection()) {
            try(var statement = conn.prepareStatement("TRUNCATE users")) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        testUser = new UserData("testUser", "1234","test@gmail.com");
    }

    @Test
    void createUser() throws DataAccessException {
        userDAO.createUser(testUser);
        String dbUserName;
        String dbPassword;
        String dbEmail;

        try(var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("SELECT username, password, email FROM users WHERE username = ?")) {
                statement.setString(1, testUser.getUsername());
                try (var results = statement.executeQuery()) {
                    results.next();
                    dbUserName = results.getString("username");
                    dbPassword = results.getString("password");
                    dbEmail = results.getString("email");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(testUser.getUsername(), dbUserName);
        Assertions.assertTrue(BCrypt.checkpw(testUser.getPassword(), dbPassword));
        Assertions.assertEquals(testUser.getEmail(), dbEmail);
    }

    @Test
    void createUserFail() throws DataAccessException {
        userDAO.createUser(testUser);
        Assertions.assertThrows(DataAccessException.class, () -> {
            userDAO.createUser(testUser);
        });
    }

    @Test
    void getUser() throws DataAccessException {
        userDAO.createUser(testUser);
        UserData dbUserResult = userDAO.getUserData(testUser.getUsername());
        Assertions.assertEquals(testUser.getUsername(), dbUserResult.getUsername());
        Assertions.assertTrue(BCrypt.checkpw(testUser.getPassword(), dbUserResult.getPassword()));
        Assertions.assertEquals(testUser.getEmail(), dbUserResult.getEmail());
    }

    @Test
    void getUserFail() throws DataAccessException {
        userDAO.createUser(testUser);
        Assertions.assertThrows(DataAccessException.class, () -> {
            userDAO.getUserData("randomUsername");
        });
    }

    @Test
    void removeUsers() throws DataAccessException {
        userDAO.createUser(testUser);
        userDAO.removeUsers();
        Assertions.assertThrows(DataAccessException.class, () -> {
            userDAO.getUserData(testUser.getUsername());
        });
    }
}
