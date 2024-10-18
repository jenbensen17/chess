package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.AuthData;
import model.RegisterRequest;
import model.RegisterResult;
import model.UserData;
import service.UserService;
import spark.Request;
import spark.Response;

public class UserHandler {

    UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    public Object register(Request req, Response res) throws DataAccessException {
        var serializer = new Gson();
        RegisterRequest registerRequest = serializer.fromJson(req.body(), RegisterRequest.class);
        if(registerRequest.username() == null || registerRequest.password() == null ||
                registerRequest.email() == null) {
            res.status(400);
            return "{ \"message\": \"Error: bad request\" }";
        }
        AuthData authData;
        try {
            authData = userService.register(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
        } catch (DataAccessException e) {
            res.status(403);
            return "{ \"message\": \"Error: already taken\" }";
        }
        res.status(200);
        RegisterResult registerResult = new RegisterResult(authData.getUsername(), authData.getAuthToken());
        var json = serializer.toJson(registerResult);
        return json;
    }
}
