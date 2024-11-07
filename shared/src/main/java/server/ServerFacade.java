package server;

import com.google.gson.Gson;
import model.AuthData;
import model.LoginRequest;
import model.RegisterRequest;
import model.RegisterResult;

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
        RegisterRequest req = new RegisterRequest(username, password, email);
        Map resp = makeRequest("POST", "/user", req, Map.class);
        var authToken = (String)resp.get("authToken");
        AuthData authData = new AuthData(authToken, username);
        return authData;
    }

    public AuthData login(String username, String password) {
        LoginRequest req = new LoginRequest(username, password);
        Map resp = makeRequest("POST", "/session", req, Map.class);
        var authToken = (String)resp.get("authToken");
        AuthData authData = new AuthData(authToken, username);
        return authData;
    }

    private <T> T makeRequest(String method, String path, Object reuqest, Class<T> responseClass) {
        try{
            URI url = (new URI(serverUrl+path));
            HttpURLConnection http = (HttpURLConnection)url.toURL().openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            writeBody(reuqest, http);
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
