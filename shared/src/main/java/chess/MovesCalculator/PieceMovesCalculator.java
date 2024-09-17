package chess.MovesCalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.*;

public interface PieceMovesCalculator {

     Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);

    public static void diagonalMoves(ChessBoard board, ChessPosition myPosition, String direction, Collection<ChessMove> possibleMoves) {
        int startRow = myPosition.getRow();
        int startCol = myPosition.getColumn();
        int rowChange = 0;
        int colChange = 0;
        switch (direction) {
                case "northeast":
                    colChange = 1;
                    rowChange = 1;
                    break;
                case "southeast":
                    colChange = 1;
                    rowChange = -1;
                    break;
                case "southwest":
                    colChange = -1;
                    rowChange = -1;
                    break;
                case "northwest":
                    colChange = -1;
                    rowChange = 1;
                    break;

        }
        // {-1,1}, {1,1}, {-1,-1}, {1,-1}
        int currentRow = startRow+rowChange;
        int currentCol = startCol+colChange;
        while(currentRow > 0 && currentRow <= 8 && currentCol <= 8 && currentCol > 0) {
            ChessPiece piece = board.getPiece(new ChessPosition(currentRow, currentCol));
            if(piece != null) {
                if(piece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    possibleMoves.add(new ChessMove(myPosition, new ChessPosition(currentRow,currentCol), null));
                }
                break;
            } else {
                possibleMoves.add(new ChessMove(myPosition, new ChessPosition(currentRow,currentCol), null));
            }
            currentCol += colChange;
            currentRow += rowChange;
        }
    }
}
