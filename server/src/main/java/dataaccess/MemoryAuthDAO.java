package dataaccess;

import model.AuthData;

import java.util.HashSet;

public class MemoryAuthDAO implements AuthDAO {

    private HashSet<AuthData> authDB;

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        authDB.add(authData);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        AuthData authData = null;
        for (AuthData auth : authDB) {
            if (auth.getAuthToken().equals(authToken)) {
                authData = auth;
            }
        }
        if (authData == null) {
            throw new DataAccessException("Auth token not found");
        }
        return authData;
    }
}
