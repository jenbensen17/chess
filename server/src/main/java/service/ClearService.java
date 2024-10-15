package service;

import dataaccess.ClearDAO;
import dataaccess.DataAccessException;

public class ClearService {

    ClearDAO clearDAO;

    public ClearService(ClearDAO clearDAO) {
        this.clearDAO = clearDAO;
    }
    public void clearApp() throws DataAccessException {
        clearDAO.removeAuthTokens();
        clearDAO.removeGames();
        clearDAO.removeUsers();
    }
}
