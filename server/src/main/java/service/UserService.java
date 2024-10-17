package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {

    UserDAO userDAO;
    AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData register(UserData user) throws DataAccessException {
        try {
            UserData findUser = userDAO.getUserData(user.getUsername());
        }
        //user does not exist
        catch(DataAccessException e) {
            userDAO.createUser(user);
            String authToken = UUID.randomUUID().toString();
            AuthData authData = new AuthData(authToken, user.getUsername());
            authDAO.createAuth(authData);
            return authData;
        }
        //user already exists
        throw new DataAccessException("User already exists");
    }

    public AuthData login(UserData userData) {
        return null;
    }

    public void logout(AuthData authData) {

    }
}
