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

    private final String[] createStatements = {
            """
        CREATE TABLE IF NOT EXISTS gameTable (
        game_id INT NOT NULL UNIQUE AUTO_INCREMENT,
        white_username VARCHAR(255),
        black_username VARCHAR(255),
        game_name VARCHAR(255) NOT NULL,
        game_state longtext NOT NULL,
        PRIMARY KEY (game_id)
        );
        """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseConfigurer.configureDatabase(createStatements);
    }



    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        int dbGameId;
        String dbWhiteUsername;
        String dbBlackUsername;
        String dbGameName;
        ChessGame dbGame;
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("SELECT game_id, white_username, " +
                    "black_username, game_name, game_state FROM gameTable WHERE game_id = ?")) {
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
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO gameTable " +
                    "(white_username, black_username, game_name, game_state) " +
                    "VALUES (?, ?, ?, ?)")) {
                if (gameData.getWhiteUsername() != null) {
                    preparedStatement.setString(1, gameData.getWhiteUsername());
                } else {
                    preparedStatement.setNull(1, java.sql.Types.VARCHAR);
                }
                if (gameData.getBlackUsername() != null) {
                    preparedStatement.setString(2, gameData.getBlackUsername());
                } else {
                    preparedStatement.setNull(2, java.sql.Types.VARCHAR);
                }
                preparedStatement.setString(3, gameData.getGameName());

                var json = new Gson().toJson(gameData.getGame());
                preparedStatement.setString(4, json);
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException("Unable to create game");
        }
    }

    @Override
    public void updateGame(int gameID, ChessGame.TeamColor playerColor, AuthData authData) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var getColorStatement = conn.prepareStatement("SELECT white_username, black_username FROM gameTable WHERE game_id = ?")) {
                getColorStatement.setInt(1, gameID);
                try (var results = getColorStatement.executeQuery()) {
                    results.next();
                    var whiteUsername = results.getString("white_username");
                    var blackUsername = results.getString("black_username");
                    String colorToUpdate;
                    if (playerColor == null) {
                        throw new DataAccessException("Invalid player color");
                    } else if (playerColor.equals(ChessGame.TeamColor.BLACK) && blackUsername == null) {
                        colorToUpdate = "black_username=?";
                    } else if (playerColor.equals(ChessGame.TeamColor.WHITE) && whiteUsername == null) {
                        colorToUpdate = "white_username=?";
                    } else {
                        throw new DataAccessException("Error: Already Taken");
                    }
                    try (var preparedStatement = conn.prepareStatement("UPDATE gameTable SET " + colorToUpdate + " WHERE game_id = ?")) {
                        preparedStatement.setString(1, authData.getUsername());
                        preparedStatement.setInt(2, gameID);
                        preparedStatement.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to update game");
        }
    }

    @Override
    public HashSet<GameData> listGames() throws DataAccessException {
        HashSet<GameData> games = new HashSet<>();
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT game_id, " +
                    "white_username, black_username, game_name, game_state FROM gameTable")) {
                try (var results = preparedStatement.executeQuery()) {
                    boolean gamesInList = false;
                    while (results.next()) {
                        gamesInList = true;
                        var gameID = results.getInt("game_id");
                        var whiteUsername = results.getString("white_username");
                        var blackUsername = results.getString("black_username");
                        var gameName = results.getString("game_name");
                        var json = results.getString("game_state");
                        var chessGame = new Gson().fromJson(json, ChessGame.class);
                        games.add(new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame));
                    }
                    if (!gamesInList) {
                        return games;
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
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE gameTable")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException ignored) {
        }
    }
}
