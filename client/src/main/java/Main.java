import chess.*;
import client.ServerFacade;
import ui.Repl;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        var serverUrl = "http://localhost:8080";
        ServerFacade server = new ServerFacade(8080);
        new Repl(server).run();
    }
}