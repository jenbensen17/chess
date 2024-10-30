package dataaccess;

import model.UserData;
import com.google.gson.Gson;
import org.mindrot.jbcrypt.BCrypt;

import javax.xml.crypto.Data;
import java.sql.SQLException;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() throws DataAccessException {
        configureDatabase();
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS users (
            `user_id` INT NOT NULL AUTO_INCREMENT,
            `username` VARCHAR(256) NOT NULL UNIQUE,
            `password` VARCHAR(256) NOT NULL,
            `email` VARCHAR(256) NOT NULL,
            PRIMARY KEY (`user_id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to configure database");
        }
    }

    @Override
    public UserData getUserData(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement("SELECT username, password, email FROM users WHERE username = ?");
            preparedStatement.setString(1, username);
            try (var result = preparedStatement.executeQuery()) {
                result.next();
                var resultUserName = result.getString("username");
                var resultPassword = result.getString("password");
                var resultEmail = result.getString("email");
                return new UserData(resultUserName, resultPassword, resultEmail);
            }
        } catch (Exception e) {
            throw new DataAccessException("User not found");
        }
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO users (username, password, email) VALUES (?, ?, ?)")) {
                preparedStatement.setString(1, user.getUsername());
                String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
                preparedStatement.setString(2, hashedPassword);
                preparedStatement.setString(3, user.getEmail());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("User already exists");
        }

    }

    @Override
    public void removeUsers() {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE users")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException ignored) {
        }
    }
}
