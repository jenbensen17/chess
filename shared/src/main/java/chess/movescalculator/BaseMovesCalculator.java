package chess.movescalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;

public abstract class BaseMovesCalculator implements MovesCalculator {

    protected void calculateMoves(ChessBoard board, ChessPosition myPosition, int rowChange, int colChange,
                                  Collection<ChessMove> possibleMoves, boolean allowDistance) {
        int currentRow = myPosition.getRow() + rowChange;
        int currentCol = myPosition.getColumn() + colChange;
        do {
            if (currentRow > 0 && currentRow <= 8 && currentCol <= 8 && currentCol > 0) {
                ChessPiece piece = board.getPiece(new ChessPosition(currentRow, currentCol));
                if (piece != null) {
                    if (piece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                        possibleMoves.add(new ChessMove(myPosition, new ChessPosition(currentRow, currentCol), null));
                    }
                    break;
                } else {
                    possibleMoves.add(new ChessMove(myPosition, new ChessPosition(currentRow, currentCol), null));
                }
                currentCol += colChange;
                currentRow += rowChange;
            }
        } while (allowDistance && currentRow > 0 && currentRow <= 8 && currentCol <= 8 && currentCol > 0);
    }

    public abstract Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);
}
