package ui;

import model.AuthData;

public abstract class UI {

    private static AuthData authData;
    private static int gameID;

    public abstract String eval(String input);

    public AuthData getAuthData() {
        return authData;
    }

    public void setAuthData(AuthData a) {
        authData = a;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int i)  {
        gameID = i;
    }
}
