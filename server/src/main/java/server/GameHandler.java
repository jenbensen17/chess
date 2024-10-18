package server;

import chess.ChessGame;
import chess.GameState;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.AuthData;
import model.CreateGameRequest;
import model.CreateGameResult;
import model.GameData;
import service.GameService;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.UUID;

public class GameHandler {

    GameService gameService;

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public Object createGame(Request req, Response res) {
        var serializer = new Gson();
        CreateGameRequest createGameRequest = new CreateGameRequest(req.headers("authorization"), req.body());
        int newGameID = UUID.randomUUID().hashCode();
        try {
            newGameID = gameService.createGame(new GameData(newGameID,null, null, createGameRequest.gameName(),new ChessGame()), new AuthData(createGameRequest.authToken(), null));
        } catch (DataAccessException e) {
            res.status(401);
            return "{ \"message\": \"Error: unauthorized\" }";
        }
        res.status(200);
        CreateGameResult createGameResult = new CreateGameResult(newGameID);
        return new Gson().toJson(createGameResult);
    }
}
