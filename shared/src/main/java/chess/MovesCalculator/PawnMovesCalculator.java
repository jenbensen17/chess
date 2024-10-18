package chess.MovesCalculator;

import chess.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class PawnMovesCalculator extends BaseMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new HashSet<>();
        int currentRow = myPosition.getRow();
        int currentCol = myPosition.getColumn();
        ChessGame.TeamColor pawnColor = board.getPiece(myPosition).getTeamColor();
        int rowChange = pawnColor == ChessGame.TeamColor.WHITE ? 1 : -1;
        boolean initialMove = false;
        boolean promotion = false;
        if ((pawnColor == ChessGame.TeamColor.WHITE && currentRow == 2) || (pawnColor == ChessGame.TeamColor.BLACK && currentRow == 7)) {
            initialMove = true;
        } else if ((pawnColor == ChessGame.TeamColor.WHITE && currentRow == 7) || (pawnColor == ChessGame.TeamColor.BLACK && currentRow == 2)) {
            promotion = true;
        }

        ChessPosition forward = (new ChessPosition(currentRow + rowChange, currentCol));
        if (board.getPiece(forward) == null) {
            if (promotion) {
                moves.add(new ChessMove(myPosition, forward, ChessPiece.PieceType.ROOK));
                moves.add(new ChessMove(myPosition, forward, ChessPiece.PieceType.KNIGHT));
                moves.add(new ChessMove(myPosition, forward, ChessPiece.PieceType.BISHOP));
                moves.add(new ChessMove(myPosition, forward, ChessPiece.PieceType.QUEEN));
            } else {
                moves.add(new ChessMove(myPosition, forward, null));
            }
        }

        ChessPosition left = (new ChessPosition(currentRow + rowChange, currentCol - 1));
        if (currentCol - 1 > 0 && board.getPiece(left) != null && board.getPiece(left).getTeamColor() != pawnColor) {
            if (promotion) {
                moves.add(new ChessMove(myPosition, left, ChessPiece.PieceType.ROOK));
                moves.add(new ChessMove(myPosition, left, ChessPiece.PieceType.KNIGHT));
                moves.add(new ChessMove(myPosition, left, ChessPiece.PieceType.BISHOP));
                moves.add(new ChessMove(myPosition, left, ChessPiece.PieceType.QUEEN));
            } else {
                moves.add(new ChessMove(myPosition, left, null));
            }
        }

        ChessPosition right = (new ChessPosition(currentRow + rowChange, currentCol + 1));
        if (currentCol + 1 <= 8 && board.getPiece(right) != null && board.getPiece(right).getTeamColor() != pawnColor) {
            if (promotion) {
                moves.add(new ChessMove(myPosition, right, ChessPiece.PieceType.ROOK));
                moves.add(new ChessMove(myPosition, right, ChessPiece.PieceType.KNIGHT));
                moves.add(new ChessMove(myPosition, right, ChessPiece.PieceType.BISHOP));
                moves.add(new ChessMove(myPosition, right, ChessPiece.PieceType.QUEEN));
            } else {
                moves.add(new ChessMove(myPosition, right, null));
            }
        }

        if (initialMove) {
            ChessPosition initialForward = new ChessPosition(currentRow + rowChange + rowChange, currentCol);
            if (board.getPiece(forward) == null && board.getPiece(initialForward) == null) {
                moves.add(new ChessMove(myPosition, initialForward, null));
            }
        }


        return moves;
    }
}
