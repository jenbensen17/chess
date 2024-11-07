package client;

import com.google.gson.Gson;
import model.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.*;
import java.net.*;
import java.util.Map;

public class ServerFacade {

    private String serverUrl = "http://localhost:";

    public ServerFacade(int serverUrl) {
        this.serverUrl+=serverUrl;
    }

    public AuthData register(String username, String password, String email) {
        var req = Map.of("username", username, "password", password, "email", email);
        Map resp = makeRequest("POST", "/user", req, Map.class);
        var authToken = (String)resp.get("authToken");
        AuthData authData = new AuthData(authToken, username);
        return authData;
    }

    public AuthData login(String username, String password) {
        var req = Map.of("username", username, "password", password);
        Map resp = makeRequest("POST", "/session", req, Map.class);
        var authToken = (String)resp.get("authToken");
        AuthData authData = new AuthData(authToken, username);
        return authData;
    }

    public void logout(String authToken) {
        var req = Map.of("authToken", authToken);
        makeRequest("DELETE", "/session", req, Map.class);
    }

    public int createGame(String authToken, String gameName) {
        var req = Map.of("authToken", authToken, "gameName", gameName);
        Map resp = makeRequest("POST", "/game", req, Map.class);
        int gameId =  (int)Math.round((Double)resp.get("gameID"));
        return gameId;
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) {
        try{
            URI url = (new URI(serverUrl+path));
            HttpURLConnection http = (HttpURLConnection)url.toURL().openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            if(request != null){
                if(((Map<?, ?>) request).containsKey("authToken")){
                    writeHeader((String) ((Map<?, ?>) request).get("authToken"), http);
                }
            }
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if(request!= null){
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try(OutputStream reqBody = http.getOutputStream()) {
               reqBody.write(reqData.getBytes());
            }
        }
    }

    private static void writeHeader(String authToken, HttpURLConnection http) throws IOException {
        http.addRequestProperty("authorization", authToken);
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new IOException(http.getResponseMessage());
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
