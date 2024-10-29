package dataaccess;

import model.AuthData;

import java.sql.SQLException;
import java.sql.Statement;

public class SQLAuthDAO implements AuthDAO {

    public SQLAuthDAO() throws DataAccessException {
        configureDatabase();
    }

    private final String [] createStatements = {
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
        DatabaseManager.createDatabase();
        try(var conn = DatabaseManager.getConnection()) {
            for(var statement: createStatements) {
                try(var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch( SQLException e) {
            throw new DataAccessException("Unable to configure database");
        }
    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()) {
            try(var preparedStatement = conn.prepareStatement("INSERT INTO auth (auth_token, username) VALUES(?,?)")) {
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
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }

    @Override
    public void removeAuthTokens() {

    }
}
