package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;

import java.util.HashSet;

public interface GameDAO {
    GameData getGame(int gameID) throws DataAccessException;

    void createGame(GameData gameData) throws DataAccessException;

    void updateGame(int gameID, ChessGame.TeamColor playerColor, AuthData authData) throws DataAccessException;

    HashSet<GameData> listGames() throws DataAccessException;

    void removeGames();

    void updateGameState(GameData gameData);

    void removePlayer(int gameID, ChessGame.TeamColor color);
}
