package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;

public interface GameDAO {
    public GameData getGame(int gameID) throws DataAccessException;
    public void createGame(GameData gameData) throws DataAccessException;
    public void updateGame(int gameID, ChessGame.TeamColor playerColor, AuthData authData) throws DataAccessException;
}
