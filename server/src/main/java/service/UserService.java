package service;

import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

public class UserService {

    UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public AuthData register(UserData userData) {

    }
    public AuthData login(UserData userData) {
        return null;
    }
    public void logout(AuthData authData) {

    }
}
