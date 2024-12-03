package websocket.commands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand {

    ChessMove move;
    String startPosition;
    String endPosition;

    public MakeMove(CommandType commandType, String authToken, Integer gameID, ChessMove move, String startPosition, String endPosition) {
        super(commandType, authToken, gameID);
        this.move = move;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public ChessMove getMove() {
        return move;
    }

    public String getStartPosition() {
        return startPosition;
    }

    public String getEndPosition() {
        return endPosition;
    }
}
