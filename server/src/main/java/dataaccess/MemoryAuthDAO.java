package dataaccess;

import model.AuthData;

import java.util.HashSet;

public class MemoryAuthDAO implements AuthDAO {

    private final HashSet<AuthData> authDB;

    public MemoryAuthDAO() {
        authDB = new HashSet<>();
    }

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

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        AuthData authData = null;
        for (AuthData auth : authDB) {
            if (auth.getAuthToken().equals(authToken)) {
                authData = auth;
            }
        }
        if (authData == null) {
            throw new DataAccessException("Auth token not found");
        } else {
            authDB.remove(authData);
        }
    }

    @Override
    public void removeAuthTokens() {
        authDB.clear();
    }
}
