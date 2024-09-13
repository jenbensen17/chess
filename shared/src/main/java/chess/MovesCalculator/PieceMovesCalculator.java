package chess.MovesCalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.*;

public interface PieceMovesCalculator {

    static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return new ArrayList<ChessMove>();
    }
}
