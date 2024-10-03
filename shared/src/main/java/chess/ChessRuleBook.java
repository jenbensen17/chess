package chess;

import chess.MovementRules.*;
import chess.MovesCalculator.KnightMovesCalculator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class ChessRuleBook {

    public ChessRuleBook() {

    }

    public Collection<ChessMove> validMoves(ChessBoard board, ChessPosition startPosition) {
            System.out.println(board);
            ChessPiece piece = board.getPiece(startPosition);
            Collection<ChessMove> possibleMoves = new HashSet<>();
            Collection<ChessMove> validMoves = new HashSet<>();
            switch(piece.getPieceType()) {
                case KING: possibleMoves = new KingMovementRule().validMoves(board, startPosition);
                            break;
                case QUEEN: possibleMoves = new QueenMovementRule().validMoves(board, startPosition);
                            break;
                case ROOK: possibleMoves = new RookMovementRule().validMoves(board, startPosition);
                            break;
                case KNIGHT: possibleMoves = new KnightMovementRule().validMoves(board, startPosition);
                            break;
                case PAWN: possibleMoves = new PawnMovementRule().validMoves(board, startPosition);
                            break;
                case BISHOP: possibleMoves = new BishopMovementRule().validMoves(board, startPosition);
                            break;
            }
            for(ChessMove move : possibleMoves) {

                ChessBoard tempBoard = new ChessBoard();
                for (int i = 1; i <= 8; i++) {
                    for (int j = 1; j <= 8; j++) {
                        tempBoard.addPiece(new ChessPosition(i, j), board.getPiece(new ChessPosition(i, j)));
                    }
                }
                    tempBoard.movePiece(move.getStartPosition(), move.getEndPosition());
                    boolean check = isInCheck(tempBoard, piece.getTeamColor());
                    if (check) {
                        continue;
                    } else {
                        validMoves.add(move);
                    }

                }


            return validMoves;
    }

    public boolean isBoardValid(ChessBoard board) {
        return false;
    }

    public boolean isInCheck(ChessBoard board, ChessGame.TeamColor teamColor) {
        ChessPosition kingPosition = null;
        for(int i = 1; i<=8; i++) {
            for(int j = 1; j<=8; j++) {
                ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                if (piece !=null) {
                    if(piece.getPieceType() ==  ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor) {
                        kingPosition = new ChessPosition(i, j);
                        break;
                    }
                }
            } if(kingPosition != null) {
                break;
            }
        }
        for(int i = 1; i<=8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                if(piece == null || piece.getTeamColor() == teamColor){
                    continue;
                }
                Collection<ChessMove> enemyPieceMoves = piece.pieceMoves(board, new ChessPosition(i, j));
                for(ChessMove move : enemyPieceMoves) {
                    if(move.getEndPosition().equals(kingPosition)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isInCheckmate(ChessGame.TeamColor teamColor) {
        return false;
    }

    public boolean isInStalemate(ChessGame.TeamColor teamColor) {
        return false;
    }
}
