package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;

import java.sql.SQLException;
import java.util.HashSet;

public class SQLGameDAO implements GameDAO {

    public SQLGameDAO() throws DataAccessException {
        configureDatabase();
    }

    private final String [] createStatements = {
        """
        CREATE TABLE IF NOT EXISTS games (
        game_id INT NOT NULL UNIQUE,
        white_username VARCHAR(255) NOT NULL,
        black_username VARCHAR(255) NOT NULL,
        game_name VARCHAR(255) NOT NULL,
        game_state longtext NOT NULL,
        PRIMARY KEY (game_id)
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
    public GameData getGame(int gameID) throws DataAccessException {
        int dbGameId;
        String dbWhiteUsername;
        String dbBlackUsername;
        String dbGameName;
        ChessGame dbGame;
        try(var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("SELECT game_id, white_username, black_username, game_name, game_state FROM games WHERE game_id = ?")) {
                statement.setInt(1, gameID);
                try (var results = statement.executeQuery()) {
                    results.next();
                    dbGameId = results.getInt("game_id");
                    dbWhiteUsername = results.getString("white_username");
                    dbBlackUsername = results.getString("black_username");
                    dbGameName = results.getString("game_name");

                    var json = results.getString("game_state");
                    dbGame = new Gson().fromJson(json, ChessGame.class);

                    return new GameData(dbGameId, dbWhiteUsername, dbBlackUsername, dbGameName, dbGame);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Invalid game ID");
        }
    }

    @Override
    public void createGame(GameData gameData) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()) {
            try(var preparedStatement = conn.prepareStatement("INSERT INTO games (game_id, white_username, black_username, game_name, game_state) VALUES (?, ?, ?, ?, ?)")) {
                preparedStatement.setInt(1, gameData.getGameID());
                preparedStatement.setString(2, gameData.getWhiteUsername());
                preparedStatement.setString(3, gameData.getBlackUsername());
                preparedStatement.setString(4, gameData.getGameName());

                var json = new Gson().toJson(gameData);
                preparedStatement.setString(5, json);
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException("Unable to create game");
        }
    }

    @Override
    public void updateGame(int gameID, ChessGame.TeamColor playerColor, AuthData authData) throws DataAccessException {

    }

    @Override
    public HashSet<GameData> listGames() throws DataAccessException {
        return null;
    }

    @Override
    public void removeGames() {

    }
}
