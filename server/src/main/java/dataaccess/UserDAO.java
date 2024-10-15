package dataaccess;

import model.UserData;

public interface UserDAO {
    public UserData getUserData(String username) throws DataAccessException;
    public void createUser(UserData user) throws DataAccessException;

}
