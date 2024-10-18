package server;

import dataaccess.*;
import service.ClearService;
import service.UserService;
import spark.*;

public class Server {

    ClearDAO clearDAO;
    ClearService clearService;
    ClearHandler clearHandler;

    UserDAO userDAO;
    AuthDAO authDAO;
    UserService userService;
    UserHandler userHandler;

    public Server() {
        clearDAO = new MemoryClearDAO();
        clearService = new ClearService(clearDAO);
        clearHandler = new ClearHandler(clearService);
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        userService = new UserService(userDAO, authDAO);
        userHandler = new UserHandler(userService);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", clearHandler::clearApp);
        Spark.post("/user", userHandler::register);
        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
