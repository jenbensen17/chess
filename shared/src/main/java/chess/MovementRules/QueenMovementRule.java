package chess.MovementRules;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.MovesCalculator.QueenMovesCalculator;

import java.util.Collection;
import java.util.List;

public class QueenMovementRule implements MovementRule {
    @Override
    public Collection<ChessMove> validMoves(ChessBoard board, ChessPosition startPosition) {
        return new QueenMovesCalculator().pieceMoves(board, startPosition);
    }
}
