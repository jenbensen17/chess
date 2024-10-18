package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;

import java.util.HashSet;

public class GameService {

    GameDAO gameDAO;
    AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public HashSet<GameData> listGames(AuthData authData) throws DataAccessException {
        try {
            AuthData validAuth = authDAO.getAuth(authData.getAuthToken());
        } catch (DataAccessException e) {
            throw new DataAccessException("Error: Unauthorized");
        }
        return gameDAO.listGames();
    }

    public int createGame(GameData gameData, AuthData authData) throws DataAccessException {
        try {
            AuthData validAuth = authDAO.getAuth(authData.getAuthToken());
        } catch (DataAccessException e) {
            throw new DataAccessException("Error: Unauthorized");
        }
        gameDAO.createGame(gameData);
        gameData = gameDAO.getGame(gameDAO.listGames().size());
        return gameData.getGameID();
    }

    public void joinGame(GameData gameData, AuthData authData, ChessGame.TeamColor playerColor) throws DataAccessException {
        try{
            AuthData validAuth = authDAO.getAuth(authData.getAuthToken());
        } catch (DataAccessException e) {
            throw new DataAccessException("Error: Unauthorized");
        }
        GameData validGame = gameDAO.getGame(gameData.getGameID());
        gameDAO.updateGame(validGame.getGameID(), playerColor, authData);
    }
}
