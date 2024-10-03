package chess;

public class GameState {
    private ChessGame.TeamColor turn;
    private ChessBoard board;
    private final ChessRuleBook rules;

    public GameState() {
        turn = ChessGame.TeamColor.WHITE;
        board = new ChessBoard();
        board.resetBoard();
        rules = new ChessRuleBook();
    }

    public ChessGame.TeamColor turn() {
        return turn;
    }

    public void nextTurn() {
        switch (turn) {
            case WHITE:
                turn = ChessGame.TeamColor.BLACK;
                break;
            case BLACK:
                turn = ChessGame.TeamColor.WHITE;
                break;
        }
    }

    public ChessBoard board() {
        return board;
    }

    public ChessRuleBook rules() {
        return rules;
    }

    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    public void setTeamTurn(ChessGame.TeamColor team) {
        turn = team;
    }
}
