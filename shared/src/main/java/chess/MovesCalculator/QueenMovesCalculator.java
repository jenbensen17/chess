package chess.MovesCalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class QueenMovesCalculator extends BaseMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new HashSet<ChessMove>();
        calculateMoves(board, myPosition, -1, -1, validMoves, true);
        calculateMoves(board, myPosition, 1, -1, validMoves, true);
        calculateMoves(board, myPosition, -1, 1, validMoves, true);
        calculateMoves(board, myPosition, 1, 1, validMoves, true);
        calculateMoves(board, myPosition, 1, 0, validMoves, true);
        calculateMoves(board, myPosition, -1, 0, validMoves, true);
        calculateMoves(board, myPosition, 0, 1, validMoves, true);
        calculateMoves(board, myPosition, 0, -1, validMoves, true);
        return validMoves;
    }
}
