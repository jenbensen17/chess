package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.BeforeEach;

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
}
