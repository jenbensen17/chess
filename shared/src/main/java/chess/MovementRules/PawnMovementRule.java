package chess.MovementRules;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.MovesCalculator.PawnMovesCalculator;

import java.util.Collection;
import java.util.List;

public class PawnMovementRule implements MovementRule {
    @Override
    public Collection<ChessMove> validMoves(ChessBoard board, ChessPosition startPosition) {
        return new PawnMovesCalculator().pieceMoves(board, startPosition);
    }
}
