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
        } catch(DataAccessException e) {
            userDAO.createUser(user);
            String authToken = UUID.randomUUID().toString();
            AuthData authData = new AuthData(authToken, user.getUsername());
            authDAO.createAuth(authData);
            return authData;
        }
        throw new DataAccessException("User already exists");
    }

    public AuthData login(UserData userData) throws DataAccessException {
        try{
            UserData findUser = userDAO.getUserData(userData.getUsername());
            if(!findUser.getPassword().equals(userData.getPassword())){
                throw new DataAccessException("Incorrect password");
            }
        } catch (DataAccessException e) {
            throw new DataAccessException("User does not exist");
        }
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, userData.getUsername());
        authDAO.createAuth(authData);
        return authData;
    }

    public void logout(AuthData authData) throws DataAccessException {
        try {
            authDAO.getAuth(authData.getAuthToken());
        } catch (DataAccessException e) {
            throw new DataAccessException("Error: Unauthorized");
        }
        authDAO.deleteAuth(authData.getAuthToken());
    }
}
