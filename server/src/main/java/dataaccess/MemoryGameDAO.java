package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;

import java.util.HashSet;

public class MemoryGameDAO implements GameDAO {

    private final HashSet<GameData> games;

    public MemoryGameDAO() {
        games = new HashSet<>();
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        GameData game = null;
        for (GameData gameData : games) {
            if (gameData.getGameID() == gameID) {
                game = gameData;
            }
        }
        if (game == null) {
            throw new DataAccessException("Game not found");
        }
        return game;
    }

    @Override
    public void createGame(GameData gameData) throws DataAccessException {
        int gameID = games.size() + 1;
        gameData = new GameData(gameID, gameData.getWhiteUsername(), gameData.getBlackUsername(), gameData.getGameName(), gameData.getGame());
        games.add(gameData);
    }

    @Override
    public void updateGame(int gameID, ChessGame.TeamColor playerColor, AuthData authData) throws DataAccessException {
        if (playerColor == null) {
            throw new DataAccessException("Player color is null");
        }
        for (GameData gameData : games) {
            if (gameData.getGameID() == gameID) {
                if (playerColor.equals(ChessGame.TeamColor.BLACK)) {
                    if (gameData.getBlackUsername() != null) {
                        throw new DataAccessException("Error: Already Taken");
                    }
                    games.remove(gameData);
                    gameData = new GameData(gameID, gameData.getWhiteUsername(), authData.getUsername(), gameData.getGameName(), gameData.getGame());
                    games.add(gameData);
                } else {
                    if (gameData.getWhiteUsername() != null) {
                        throw new DataAccessException("Error: Already Taken");
                    }
                    games.remove(gameData);
                    gameData = new GameData(gameID, authData.getUsername(), gameData.getBlackUsername(), gameData.getGameName(), gameData.getGame());
                    games.add(gameData);
                }
                return;
            }
        }
        throw new DataAccessException("Game not found");
    }

    @Override
    public HashSet<GameData> listGames() throws DataAccessException {
        return games;
    }

    @Override
    public void removeGames() {
        games.clear();
    }
}
