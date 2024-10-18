package chess.MovementRules;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.MovesCalculator.BishopMovesCalculator;

import java.util.Collection;
import java.util.List;

public class BishopMovementRule implements MovementRule {

    @Override
    public Collection<ChessMove> validMoves(ChessBoard board, ChessPosition startPosition) {
        return new BishopMovesCalculator().pieceMoves(board, startPosition);
    }
}
