package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class SQLGameTest {

    static GameDAO gameDAO;
    static GameData testGame;

    @BeforeEach
    void setUp() throws DataAccessException {
        DatabaseManager.createDatabase();
        gameDAO = new SQLGameDAO();
        try(var conn = DatabaseManager.getConnection()) {
            try(var statement = conn.prepareStatement("TRUNCATE games")) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        testGame = new GameData(1,"white", "black", "testGame", new ChessGame());
    }

    @Test
    void createGame() throws DataAccessException {
        gameDAO.createGame(testGame);
        int dbGameId;
        String dbWhiteUsername;
        String dbBlackUsername;
        String dbGameName;
        ChessGame dbGame;
        try(var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("SELECT game_id, white_username, black_username, game_name, game_state FROM games WHERE game_id = ?")) {
                statement.setInt(1, testGame.getGameID());
                try (var results = statement.executeQuery()) {
                    results.next();
                    dbGameId = results.getInt("game_id");
                    dbWhiteUsername = results.getString("white_username");
                    dbBlackUsername = results.getString("black_username");
                    dbGameName = results.getString("game_name");

                    var json = results.getString("game_state");
                    dbGame = new Gson().fromJson(json, ChessGame.class);

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(testGame.getGameID(), dbGameId);
        Assertions.assertEquals(testGame.getWhiteUsername(), dbWhiteUsername);
        Assertions.assertEquals(testGame.getBlackUsername(), dbBlackUsername);
        Assertions.assertEquals(testGame.getGameName(), dbGameName);
        Assertions.assertEquals(testGame.getGame(), dbGame);
    }

    @Test
    void createGameFail() throws DataAccessException {
        gameDAO.createGame(testGame);
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameDAO.createGame(testGame);
        });
    }
}
