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
        return null;
    }

    public int createGame(GameData gameData, AuthData authData) throws DataAccessException {
        return 0;
    }

    public void joinGame(GameData gameData, AuthData authData, ChessGame.TeamColor playerColor) throws DataAccessException {

    }
}
