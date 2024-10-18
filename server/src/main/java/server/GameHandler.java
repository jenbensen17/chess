package server;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.*;
import service.GameService;
import spark.Request;
import spark.Response;
import java.util.HashSet;

public class GameHandler {

    GameService gameService;

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public Object createGame(Request req, Response res) {
        var serializer = new Gson();
        CreateGameRequest createGameRequest = serializer.fromJson(req.body(), CreateGameRequest.class);
        String authToken = req.headers("authorization");
        int newGameID;
        try {
            newGameID = gameService.createGame(new GameData(0,null, null, createGameRequest.gameName(),new ChessGame()), new AuthData(authToken, null));
        } catch (DataAccessException e) {
            res.status(401);
            return "{ \"message\": \"Error: unauthorized\" }";
        }
        res.status(200);
        CreateGameResult createGameResult = new CreateGameResult(newGameID);
        return new Gson().toJson(createGameResult);
    }

    public Object joinGame(Request req, Response res) {
        var serializer = new Gson();
        JoinGameRequest joinGameRequest = serializer.fromJson(req.body(), JoinGameRequest.class);
        String authToken = req.headers("authorization");
        try {
            GameData gameToJoin = new GameData(joinGameRequest.gameID(), null, null, null, null);
            gameService.joinGame(gameToJoin, new AuthData(authToken, null), joinGameRequest.playerColor());
        } catch (DataAccessException e) {
            if(e.getMessage().equals("Error: Unauthorized")) {
                res.status(401);
                return "{ \"message\": \"Error: unauthorized\" }";
            } else if(e.getMessage().equals("Error: Already Taken")) {
                res.status(403);
                return "{ \"message\": \"Error: already taken\" }";
            } else {
                res.status(400);
                return "{ \"message\": \"Error: Bad request\" }";
            }
        }
        res.status(200);
        return "{}";
    }

    public Object listGames(Request req, Response res) {
        var serializer = new Gson();
        ListGamesRequest listGamesRequest = new ListGamesRequest(req.headers("authorization"));
        HashSet<GameData> gamesList;
        try {
            gamesList = gameService.listGames(new AuthData(listGamesRequest.authToken(),null));
        } catch (DataAccessException e) {
            res.status(401);
            return "{ \"message\": \"Error: unauthorized\" }";
        }
        ListGamesResult listGamesResult = new ListGamesResult(gamesList);
        var json = new Gson().toJson(listGamesResult);
        res.status(200);
        return json;
    }
}
