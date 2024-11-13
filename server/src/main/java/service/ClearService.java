package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

public class ClearService {

    UserDAO userDAO;
    GameDAO gameDAO;
    AuthDAO authDAO;

    public ClearService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public void clearApp() throws DataAccessException {
        userDAO.removeUsers();
        gameDAO.removeGames();
        authDAO.removeAuthTokens();
    }
}
