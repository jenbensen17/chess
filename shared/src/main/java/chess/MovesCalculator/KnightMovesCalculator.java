package chess.MovesCalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class KnightMovesCalculator extends BaseMovesCalculator{
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new HashSet<ChessMove>();
        calculateMoves(board, myPosition, 2, 1, validMoves, false);
        calculateMoves(board, myPosition, 2, -1, validMoves, false);
        calculateMoves(board, myPosition, -2, 1, validMoves, false);
        calculateMoves(board, myPosition, -2, -1, validMoves, false);
        calculateMoves(board, myPosition, 1, 2, validMoves, false);
        calculateMoves(board, myPosition, 1, -2, validMoves, false);
        calculateMoves(board, myPosition, -1, 2, validMoves, false);
        calculateMoves(board, myPosition, -1, -2, validMoves, false);
        return validMoves;
    }
}
