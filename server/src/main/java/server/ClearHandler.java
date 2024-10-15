package server;

import service.ClearService;

public class ClearHandler {

    ClearService clearService;

    public ClearHandler(ClearService clearService) {
        this.clearService = clearService;
    }

    public Object clearApp() {
        return null;
    }
}
