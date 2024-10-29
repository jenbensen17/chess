package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class SQLAuthTest {

    static AuthDAO authDAO;
    static AuthData testAuth;

    @BeforeEach
    void setUp() throws DataAccessException, SQLException {
        DatabaseManager.createDatabase();
        authDAO = new SQLAuthDAO();
        try(var conn = DatabaseManager.getConnection()) {
            try(var statement = conn.prepareStatement("TRUNCATE auth")) {
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        testAuth = new AuthData("test123", "testUser");
    }

    @Test
    void testAuth() throws DataAccessException, SQLException {
        authDAO.createAuth(testAuth);
        String dbUserName;
        String dbToken;

        try(var conn = DatabaseManager.getConnection()) {
            try(var preparedStatement = conn.prepareStatement("SELECT username, auth_token FROM auth WHERE username = ?")) {
                preparedStatement.setString(1, testAuth.getUsername());
                try (var resultSet = preparedStatement.executeQuery()) {
                    resultSet.next();
                    dbUserName = resultSet.getString("username");
                    dbToken = resultSet.getString("auth_token");
                }
            }
        }

        Assertions.assertEquals(testAuth.getUsername(), dbUserName);
        Assertions.assertEquals(testAuth.getAuthToken(), dbToken);
    }

    @Test
    void testAuthFail() throws DataAccessException, SQLException {
        authDAO.createAuth(testAuth);
        Assertions.assertThrows(DataAccessException.class, () -> {
            authDAO.createAuth(testAuth);
        });
    }

    @Test
    void getAuth() throws DataAccessException, SQLException {
        authDAO.createAuth(testAuth);
        AuthData authResult = authDAO.getAuth(testAuth.getAuthToken());
        Assertions.assertEquals(testAuth.getUsername(), authResult.getUsername());
        Assertions.assertEquals(testAuth.getAuthToken(), authResult.getAuthToken());
    }

    @Test
    void getAuthFail() throws DataAccessException, SQLException {
        authDAO.createAuth(testAuth);
        Assertions.assertThrows(DataAccessException.class, () -> {
            authDAO.getAuth("invalid auth token");
        });
    }
}
