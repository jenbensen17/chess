package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;

import java.util.HashSet;
import java.util.Objects;

public class MemoryClearDAO implements ClearDAO {

    HashSet<AuthData> authDB;
    HashSet<UserData> userDB;
    HashSet<GameData> gameDB;

    public MemoryClearDAO() {
        authDB = new HashSet<>();
        userDB = new HashSet<>();
        gameDB = new HashSet<>();
    }

    public MemoryClearDAO(HashSet<AuthData> authDB, HashSet<UserData> userDB, HashSet<GameData> gameDB) {
        this.authDB = new HashSet<>();
        this.userDB = new HashSet<>();
        this.gameDB = new HashSet<>();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemoryClearDAO that = (MemoryClearDAO) o;
        return Objects.equals(authDB, that.authDB) && Objects.equals(userDB, that.userDB) && Objects.equals(gameDB, that.gameDB);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authDB, userDB, gameDB);
    }
}
