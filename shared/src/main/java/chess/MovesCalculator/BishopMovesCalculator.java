package chess.MovesCalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BishopMovesCalculator implements PieceMovesCalculator{

    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
       Collection<ChessMove> validMoves = new ArrayList();
       System.out.println(board.toString());
       validMoves = PieceMovesCalculator.diagonalMoves(board,myPosition, "northeast", validMoves);
       validMoves = PieceMovesCalculator.diagonalMoves(board,myPosition, "northwest", validMoves);
       validMoves = PieceMovesCalculator.diagonalMoves(board,myPosition, "southeast", validMoves);
       validMoves = PieceMovesCalculator.diagonalMoves(board,myPosition, "southwest", validMoves);
       return validMoves;

    }
}
