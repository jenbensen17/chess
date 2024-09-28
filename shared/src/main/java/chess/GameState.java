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
    public ChessBoard board(){
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
