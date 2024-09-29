package chess.MovementRules;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.MovesCalculator.KnightMovesCalculator;

import java.util.Collection;
import java.util.List;

public class KnightMovementRule implements MovementRule{
    @Override
    public Collection<ChessMove> validMoves(ChessBoard board, ChessPosition startPosition) {
        return new KnightMovesCalculator().pieceMoves(board, startPosition);
    }
}
