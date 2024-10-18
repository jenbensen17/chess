package chess.movementrules;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.movescalculator.KingMovesCalculator;

import java.util.Collection;

public class KingMovementRule implements MovementRule {
    @Override
    public Collection<ChessMove> validMoves(ChessBoard board, ChessPosition startPosition) {
        return new KingMovesCalculator().pieceMoves(board, startPosition);
    }
}
