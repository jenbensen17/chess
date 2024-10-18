package dataaccess;

import model.AuthData;

public interface AuthDAO {
    public void createAuth(AuthData authData) throws DataAccessException;
    public AuthData getAuth(String authToken) throws DataAccessException;
    public void deleteAuth(String authToken) throws DataAccessException;
    public void removeAuthTokens();
}
