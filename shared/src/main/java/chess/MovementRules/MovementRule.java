package chess.MovementRules;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.ChessRuleBook;

import java.util.Collection;

public interface MovementRule {
    public Collection<ChessMove> validMoves(ChessBoard board, ChessPosition startPosition);
}
