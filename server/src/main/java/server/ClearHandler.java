package server;

import dataaccess.DataAccessException;
import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler {

    ClearService clearService;

    public ClearHandler(ClearService clearService) {
        this.clearService = clearService;
    }

    public Object clearApp(Request req, Response res) throws DataAccessException {
        clearService.clearApp();
        res.status(200);
        return "{}";
    }
}
