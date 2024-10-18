package chess.movementrules;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.movescalculator.BishopMovesCalculator;

import java.util.Collection;

public class BishopMovementRule implements MovementRule {

    @Override
    public Collection<ChessMove> validMoves(ChessBoard board, ChessPosition startPosition) {
        return new BishopMovesCalculator().pieceMoves(board, startPosition);
    }
}
