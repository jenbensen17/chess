package dataaccess;

import model.UserData;

import java.util.HashSet;

public class MemoryUserDAO implements UserDAO {

    private HashSet<UserData> users;

    @Override
    public UserData getUserData(String username) throws DataAccessException {
        UserData user = null;
        for(UserData userData : users) {
            if(userData.getUsername().equals(username)) {
                user = userData;
            }
        }
        if(user!= null) {
            return user;
        }
        throw new DataAccessException("User not found");
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        users.add(user);
    }
}
