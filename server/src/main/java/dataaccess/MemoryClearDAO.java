package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;

import java.util.HashSet;

public class MemoryClearDAO implements ClearDAO {

    HashSet<AuthData> authDB;
    HashSet<UserData> userDB;
    HashSet<GameData> gameDB;

    public MemoryClearDAO() {
        authDB = new HashSet<>();
        userDB = new HashSet<>();
        gameDB = new HashSet<>();
    }

    @Override
    public void removeUsers() throws DataAccessException {
        userDB = new HashSet<>();
    }

    @Override
    public void removeGames() throws DataAccessException {
        gameDB = new HashSet<>();
    }

    @Override
    public void removeAuthTokens() throws DataAccessException {
        authDB = new HashSet<>();
    }
}
