package chess;

import chess.MovementRules.KingMovementRule;

import java.util.ArrayList;
import java.util.Collection;

public class ChessRuleBook {

    public ChessRuleBook() {

    }

    public Collection<ChessMove> validMoves(ChessBoard board, ChessPosition startPosition){
        ChessPiece piece = board.getPiece(startPosition);
        switch(piece.getPieceType()) {
            case KING: return new KingMovementRule().validMoves(board, startPosition);
        }
        return new ArrayList<>();
    }

    public boolean isBoardValid(ChessBoard board) {
        return false;
    }

    public boolean isInCheck(ChessBoard board, ChessGame.TeamColor teamColor) {
        return false;
    }

    public boolean isInCheckmate(ChessGame.TeamColor teamColor) {
        return false;
    }

    public boolean isInStalemate(ChessGame.TeamColor teamColor) {
        return false;
    }
}
