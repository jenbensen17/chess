package chess.movementrules;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.movescalculator.KnightMovesCalculator;

import java.util.Collection;

public class KnightMovementRule implements MovementRule {
    @Override
    public Collection<ChessMove> validMoves(ChessBoard board, ChessPosition startPosition) {
        return new KnightMovesCalculator().pieceMoves(board, startPosition);
    }
}
