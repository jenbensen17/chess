package dataaccess;

import model.UserData;

public interface UserDAO {
    UserData getUserData(String username) throws DataAccessException;

    void createUser(UserData user) throws DataAccessException;

    void removeUsers();
}
