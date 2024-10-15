package dataaccess;

public interface ClearDAO {
    public void removeUsers() throws DataAccessException;
    public void removeGames() throws DataAccessException;
    public void removeAuthTokens() throws DataAccessException;
}
