package dataaccess;

import model.AuthData;

import java.sql.SQLException;
import java.sql.Statement;

public class SQLAuthDAO implements AuthDAO {

    public SQLAuthDAO() throws DataAccessException {
        configureDatabase();
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS auth (
            token_id INT NOT NULL AUTO_INCREMENT,
            auth_token VARCHAR(255) NOT NULL UNIQUE,
            username VARCHAR(255) NOT NULL,
            PRIMARY KEY (token_id)
            )
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseConfigurer.configureDatabase(createStatements);
    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO auth (auth_token, username) VALUES(?,?)")) {
                preparedStatement.setString(1, authData.getAuthToken());
                preparedStatement.setString(2, authData.getUsername());
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException("Error");
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT username, auth_token FROM auth WHERE auth_token = ?")) {
                preparedStatement.setString(1, authToken);
                try (var resultSet = preparedStatement.executeQuery()) {
                    resultSet.next();
                    var dbUsername = resultSet.getString("username");
                    var dbAuthToken = resultSet.getString("auth_token");
                    return new AuthData(dbAuthToken, dbUsername);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error");
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM auth WHERE auth_token=?", Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, authToken);
                int status = preparedStatement.executeUpdate();
                if (status != 1) {
                    throw new DataAccessException("Error");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Invalid authToken");
        }
    }

    @Override
    public void removeAuthTokens() {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE auth")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException ignored) {
        }
    }
}
