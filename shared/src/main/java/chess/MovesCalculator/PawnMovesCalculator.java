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
            //Pawns can only move forward
            if (pawnColor.equals(ChessGame.TeamColor.BLACK)) {
                //initial move
                if(myPosition.getRow()==7 && board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()))== null) {
                    pawnMoves(board, myPosition, validMoves, -2, 0, null);
                }
                //promotion
                if(myPosition.getRow()==2) {
                    if(board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()))== null) {
                        pawnMoves(board, myPosition, validMoves, -1, 0, ChessPiece.PieceType.ROOK);
                        pawnMoves(board, myPosition, validMoves, -1, 0, ChessPiece.PieceType.KNIGHT);
                        pawnMoves(board, myPosition, validMoves, -1, 0, ChessPiece.PieceType.BISHOP);
                        pawnMoves(board, myPosition, validMoves, -1, 0, ChessPiece.PieceType.QUEEN);
                    }
                } else {
                    pawnMoves(board, myPosition, validMoves, -1, 0, null);
                }

                //WHITE
            } else {
                //initial move
                if(myPosition.getRow()==2 && board.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()))== null) {
                    pawnMoves(board, myPosition, validMoves, 2, 0, null);
                }
                if(myPosition.getRow()==7) {
                    if(board.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()))== null) {
                        pawnMoves(board, myPosition, validMoves, 1, 0, ChessPiece.PieceType.ROOK);
                        pawnMoves(board, myPosition, validMoves, 1, 0, ChessPiece.PieceType.KNIGHT);
                        pawnMoves(board, myPosition, validMoves, 1, 0, ChessPiece.PieceType.BISHOP);
                        pawnMoves(board, myPosition, validMoves, 1, 0, ChessPiece.PieceType.QUEEN);
                    }
                }
                else {
                    pawnMoves(board, myPosition, validMoves, 1, 0, null);
                }
            }
            pawnCapture(board, myPosition, validMoves, pawnColor);
        return validMoves;
    }

    public void pawnMoves(ChessBoard board, ChessPosition myPosition,
                     Collection<ChessMove> possibleMoves,  int rowChange, int colChange, ChessPiece.PieceType promotionPiece) {
        int currentRow = myPosition.getRow()+rowChange;
        int currentCol = myPosition.getColumn()+colChange;
        if( currentRow > 0 && currentRow <= 8 && currentCol <= 8 && currentCol > 0) {
            ChessPiece piece = board.getPiece(new ChessPosition(currentRow, currentCol));
            if (piece == null) {
                possibleMoves.add(new ChessMove(myPosition, new ChessPosition(currentRow, currentCol), promotionPiece));
            }
        }
    }

    public void pawnCapture(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> possibleMoves, ChessGame.TeamColor teamColor) {
        int rowChange = 1;
        int colChange1 = -1;
        int colChange2 = 1;
        if(teamColor.equals(ChessGame.TeamColor.BLACK)) {
            rowChange = -1;
        }
        int row = myPosition.getRow()+rowChange;
        int colLeft = myPosition.getColumn()+colChange1;
        int colRight = myPosition.getColumn()+colChange2;
        ChessPiece leftPiece = board.getPiece(new ChessPosition(row, colLeft));
        ChessPiece rightPiece = board.getPiece(new ChessPosition(row, colRight));
        if (leftPiece != null) {
            if (leftPiece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                possibleMoves.add(new ChessMove(myPosition, new ChessPosition(row, colLeft), null));
            }
        }
        if (rightPiece != null) {
            if (rightPiece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                possibleMoves.add(new ChessMove(myPosition, new ChessPosition(row, colRight), null));
            }
        }
    }
}
