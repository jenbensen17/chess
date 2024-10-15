package server;

import dataaccess.ClearDAO;
import dataaccess.MemoryClearDAO;
import service.ClearService;
import spark.*;

public class Server {

    ClearDAO clearDAO;

    ClearService clearService;

    ClearHandler clearHandler;

    public Server() {
        clearDAO = new MemoryClearDAO();
        clearService = new ClearService(clearDAO);
        clearHandler = new ClearHandler(clearService);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", clearHandler::clearApp);
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
