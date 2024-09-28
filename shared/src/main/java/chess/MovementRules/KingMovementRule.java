package chess.MovementRules;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.MovesCalculator.KingMovesCalculator;

import java.util.Collection;
import java.util.List;

public class KingMovementRule implements MovementRule{
    @Override
    public Collection<ChessMove> validMoves(ChessBoard board, ChessPosition startPosition) {
        return new KingMovesCalculator().pieceMoves(board, startPosition);
    }
}
