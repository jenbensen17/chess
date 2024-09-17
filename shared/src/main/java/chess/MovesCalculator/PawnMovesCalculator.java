package chess.MovesCalculator;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class PawnMovesCalculator extends BaseMovesCalculator{
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        System.out.println(board);
        Collection<ChessMove> validMoves = new HashSet<ChessMove>();
        boolean initialMove = false;
        ChessGame.TeamColor pawnColor = board.getPiece(myPosition).getTeamColor();

            if (pawnColor.equals(ChessGame.TeamColor.BLACK)) {
                if(myPosition.getRow()==7) {
                    pawnMoves(board, myPosition, validMoves, -2, 0);
                }
                pawnMoves(board, myPosition, validMoves, -1, 0);
            } else {
                if(myPosition.getRow()==2) {
                    pawnMoves(board, myPosition, validMoves, 2, 0);
                }
                pawnMoves(board, myPosition, validMoves, 1, 0);
            }
        return validMoves;
    }

    public void pawnMoves(ChessBoard board, ChessPosition myPosition,
                     Collection<ChessMove> possibleMoves,  int rowChange, int colChange) {
        int currentRow = myPosition.getRow()+rowChange;
        int currentCol = myPosition.getColumn()+colChange;
        if( currentRow > 0 && currentRow <= 8 && currentCol <= 8 && currentCol > 0) {
            ChessPiece piece = board.getPiece(new ChessPosition(currentRow, currentCol));
            if (piece == null) {
                possibleMoves.add(new ChessMove(myPosition, new ChessPosition(currentRow, currentCol), null));
            }
        }
    }
}
