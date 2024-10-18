package chess.movementrules;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.movescalculator.PawnMovesCalculator;

import java.util.Collection;

public class PawnMovementRule implements MovementRule {
    @Override
    public Collection<ChessMove> validMoves(ChessBoard board, ChessPosition startPosition) {
        return new PawnMovesCalculator().pieceMoves(board, startPosition);
    }
}
