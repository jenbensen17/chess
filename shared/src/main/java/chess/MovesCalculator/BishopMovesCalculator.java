package chess.MovesCalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class BishopMovesCalculator extends BaseMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new HashSet<ChessMove>();
        calculateMoves(board, myPosition, -1, -1, validMoves, true);
        calculateMoves(board, myPosition, 1, -1, validMoves, true);
        calculateMoves(board, myPosition, -1, 1, validMoves, true);
        calculateMoves(board, myPosition, 1, 1, validMoves, true);
        return validMoves;
    }
//    @Override
//    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
//       Collection<ChessMove> validMoves = new ArrayList<>();
//       PieceMovesCalculator.diagonalMoves(board,myPosition, "northeast", validMoves);
//       PieceMovesCalculator.diagonalMoves(board,myPosition, "northwest", validMoves);
//       PieceMovesCalculator.diagonalMoves(board,myPosition, "southeast", validMoves);
//       PieceMovesCalculator.diagonalMoves(board,myPosition, "southwest", validMoves);
//       return validMoves;
//
//    }
}
