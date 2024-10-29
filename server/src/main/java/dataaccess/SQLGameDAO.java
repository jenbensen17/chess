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
        try(var conn = DatabaseManager.getConnection()) {
            String colorToUpdate;
            if(playerColor.equals(ChessGame.TeamColor.BLACK)) {
                colorToUpdate = "black_username=?";
            } else {
                colorToUpdate = "white_username=?";
            }
            try(var preparedStatement = conn.prepareStatement("UPDATE games SET "+colorToUpdate+" WHERE game_id = ?")) {
                preparedStatement.setString(1,authData.getUsername());
                preparedStatement.setInt(2, gameID);
                int status = preparedStatement.executeUpdate();
                if(status == 0) {
                    throw new DataAccessException("Unable to update game");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to update game");
        }
    }

    @Override
    public HashSet<GameData> listGames() throws DataAccessException {
        HashSet<GameData> games = new HashSet<>();
        try(var conn = DatabaseManager.getConnection()) {
            try(var preparedStatement = conn.prepareStatement("SELECT game_id, white_username, black_username, game_name, game_state FROM games")) {
                try(var results = preparedStatement.executeQuery()) {
                    boolean gamesInList = false;
                    while(results.next()) {
                        gamesInList = true;
                        var gameID = results.getInt("game_id");
                        var whiteUsername = results.getString("white_username");
                        var blackUsername = results.getString("black_username");
                        var gameName = results.getString("game_name");
                        var json = results.getString("game_state");
                        var chessGame = new Gson().fromJson(json, ChessGame.class);
                        games.add(new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame));
                    }
                    if(!gamesInList) {
                        throw new DataAccessException("Unable to list games");
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to list games");
        }
        return games;
    }

    @Override
    public void removeGames() {
        try(var conn = DatabaseManager.getConnection()) {
            try(var preparedStatement = conn.prepareStatement("TRUNCATE games")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException ignored) {
        }
    }
}
