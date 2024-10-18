package chess.MovementRules;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.MovesCalculator.RookMovesCalculator;

import java.util.Collection;
import java.util.List;

public class RookMovementRule implements MovementRule {
    @Override
    public Collection<ChessMove> validMoves(ChessBoard board, ChessPosition startPosition) {
        return new RookMovesCalculator().pieceMoves(board, startPosition);
    }
}
