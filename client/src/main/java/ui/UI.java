package ui;

import model.AuthData;

public abstract class UI {

    private static AuthData authData;

    public abstract String eval(String input);

    public AuthData getAuthData() {
        return authData;
    }

    public void setAuthData(AuthData a) {
        authData = a;
    }
}
