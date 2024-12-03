package ui;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

import static ui.EscapeSequences.*;

public class BoardPrinter {

    public static void printBoard(ChessBoard board, ChessGame.TeamColor color, Collection<ChessMove> legalMoves) {
        System.out.println();
        printColumn(color);

        Collection<ChessPosition> endPositions = new HashSet<ChessPosition>();
        ChessPosition startPosition = new ChessPosition(-1,-2);
        if (legalMoves != null) {
            for (ChessMove move : legalMoves) {
                endPositions.add(move.getEndPosition());
                startPosition = move.getStartPosition();
            }
        }



        boolean light = true;
        int limit = color == ChessGame.TeamColor.WHITE ? 8 : 1;
        int inc = color == ChessGame.TeamColor.WHITE ? 1 : -1;
        for (int i = color == ChessGame.TeamColor.WHITE ? 1 : 8; i != limit + inc; i += inc) {
            printRow(i);
            for (int j = color == ChessGame.TeamColor.WHITE ? 1 : 8; j != limit + inc; j += inc) {
                if (endPositions.contains(new ChessPosition(i, j))) {
                    bgLegal(light);
                } else if(startPosition.equals(new ChessPosition(i, j))) {
                  bgStart();
                } else {
                    bg(light);
                }
                if (board.getPiece(new ChessPosition(i, j)) != null) {
                    ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                    String rep = piece.getPieceType().toString();
                    if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                        printPiece(piece, WHITE_KING, WHITE_QUEEN, WHITE_KNIGHT, WHITE_BISHOP, WHITE_ROOK, WHITE_PAWN, light);
                    } else {
                        printPiece(piece, BLACK_KING, BLACK_QUEEN, BLACK_KNIGHT, BLACK_BISHOP, BLACK_ROOK, BLACK_PAWN, light);
                    }
                } else {
                    System.out.print(EMPTY);
                }
                light = !light;
            }
            printRow(i);
            System.out.print(RESET_BG_COLOR);
            System.out.print("\n");
            light = !light;
        }
        printColumn(color);
        System.out.print("\n");
    }

    private static void bgStart() {
        System.out.print(SET_BG_COLOR_YELLOW);
    }


    private static void printPiece(ChessPiece piece, String king, String queen, String knight, String bishop, String rook, String pawn, boolean light) {
        switch (piece.getPieceType()) {
//            case KING -> {
//                if (piece.getTeamColor() == ChessGame.TeamColor.WHITE && !light) {
//                    System.out.print(king);
//                } else {
//                    System.out.print(queen);
//                }
//            }
//            case QUEEN -> {
//                if (piece.getTeamColor() == ChessGame.TeamColor.WHITE && light) {
//                    System.out.print(queen);
//                } else {
//                    System.out.print(king);
//                }
//            }
            case KING -> System.out.print(king);
            case QUEEN -> System.out.print(queen);
            case KNIGHT -> System.out.print(knight);
            case BISHOP -> System.out.print(bishop);
            case ROOK -> System.out.print(rook);
            case PAWN -> System.out.print(pawn);
        }
    }

    private static void bg(boolean light) {
        if (light) {
            System.out.print(SET_BG_COLOR_BLUE);
        } else {
            System.out.print(SET_BG_COLOR_DARK_GREY);
        }
    }

    private static void bgLegal(boolean light) {
        if (light) {
            System.out.print(SET_BG_COLOR_GREEN);
        } else {
            System.out.print(SET_BG_COLOR_DARK_GREEN);
        }
    }

    private static void printColumn(ChessGame.TeamColor color) {
        System.out.print(SET_TEXT_COLOR_WHITE);
        System.out.print(SET_BG_COLOR_LIGHT_GREY);
        System.out.print(SET_TEXT_BOLD);
        System.out.print(EMPTY);
        if (color == ChessGame.TeamColor.BLACK) {
            for (char c = 97; c < 105; c++) {
                System.out.print(" " + c + " ");
            }
        } else {
            for (char c = 104; c > 96; c--) {
                System.out.print(" " + c + " ");
            }
        }
        System.out.print(EMPTY);
        System.out.println(RESET_BG_COLOR);
        System.out.print(RESET_TEXT_BOLD_FAINT);
    }

    private static void printRow(int i) {
        System.out.print(SET_TEXT_COLOR_WHITE);
        System.out.print(SET_BG_COLOR_LIGHT_GREY);
        System.out.print(SET_TEXT_BOLD);
        System.out.print(" " + i + " ");
        System.out.print(RESET_BG_COLOR);
        System.out.print(RESET_TEXT_BOLD_FAINT);
    }

}
