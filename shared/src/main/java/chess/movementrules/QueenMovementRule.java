package chess.movementrules;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.movescalculator.QueenMovesCalculator;

import java.util.Collection;

public class QueenMovementRule implements MovementRule {
    @Override
    public Collection<ChessMove> validMoves(ChessBoard board, ChessPosition startPosition) {
        return new QueenMovesCalculator().pieceMoves(board, startPosition);
    }
}
