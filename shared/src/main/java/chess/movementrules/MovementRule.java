package chess.movementrules;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public interface MovementRule {
    Collection<ChessMove> validMoves(ChessBoard board, ChessPosition startPosition);
}
