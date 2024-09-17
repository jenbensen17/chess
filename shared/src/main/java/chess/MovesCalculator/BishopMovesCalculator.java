package chess.MovesCalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BishopMovesCalculator implements PieceMovesCalculator{

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
       Collection<ChessMove> validMoves = new ArrayList<>();
       PieceMovesCalculator.diagonalMoves(board,myPosition, "northeast", validMoves);
       PieceMovesCalculator.diagonalMoves(board,myPosition, "northwest", validMoves);
       PieceMovesCalculator.diagonalMoves(board,myPosition, "southeast", validMoves);
       PieceMovesCalculator.diagonalMoves(board,myPosition, "southwest", validMoves);
       return validMoves;

    }
}
