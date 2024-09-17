package chess.MovesCalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class KingMovesCalculator extends BaseMovesCalculator{


    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new HashSet<ChessMove>();
        calculateMoves(board, myPosition, -1, -1, validMoves, false);
        calculateMoves(board, myPosition, 1, -1, validMoves, false);
        calculateMoves(board, myPosition, -1, 1, validMoves, false);
        calculateMoves(board, myPosition, 1, 1, validMoves, false);
        calculateMoves(board, myPosition, 1, 0, validMoves, false);
        calculateMoves(board, myPosition, -1, 0, validMoves, false);
        calculateMoves(board, myPosition, 0, 1, validMoves, false);
        calculateMoves(board, myPosition, 0, -1, validMoves, false);
        return validMoves;
    }
}
