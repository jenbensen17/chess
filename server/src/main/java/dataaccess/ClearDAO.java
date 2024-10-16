package dataaccess;

import model.AuthData;

import java.util.HashSet;

public interface ClearDAO {
    public void removeUsers() throws DataAccessException;
    public void removeGames() throws DataAccessException;
    public void removeAuthTokens() throws DataAccessException;
}
