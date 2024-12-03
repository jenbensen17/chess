package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import client.ServerFacade;
import client.State;
import client.websocket.WebSocketFacade;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class GameplayUI extends UI{

    private final ServerFacade server;
    private final WebSocketFacade websocket;
    private static ChessGame game;
    private static ChessGame.TeamColor teamColor;

    public GameplayUI(ServerFacade server, WebSocketFacade websocket) {
        this.server = server;
        this.websocket = websocket;
    }


    @Override
    public String eval(String input) {
        try {
            var cmd = input;
            return switch (cmd) {
                case "help" -> help();
                case "redraw chess board" -> redraw();
                case "leave" -> leave();
                case "make move" -> makeMove();
                default -> help();
            };
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private String makeMove() {


        ChessPosition startPosition = null;
        ChessPosition endPosition = null;
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Start Position: (ex: a7)");
            String startPositionString = scanner.nextLine();
            System.out.print("End Position: (ex: a5)");
            String endPositionString = scanner.nextLine();

            int startRow = Integer.parseInt(startPositionString.substring(1));
            char startCol = startPositionString.charAt(0);



            char endCol = endPositionString.charAt(0);
            int endRow = Integer.parseInt(endPositionString.substring(1));


            startCol = getCol(startCol);

            endCol = getCol(endCol);


            ChessPiece promotionPiece = new ChessPiece(teamColor, null);
            startPosition = new ChessPosition(startRow, startCol);
            endPosition = new ChessPosition(endRow, endCol);

            ChessPiece piece = game.getBoard().getPiece(startPosition);
            if (piece.getPieceType().equals(ChessPiece.PieceType.PAWN)) {
                if(endRow == 1 || endRow == 8) {
                    System.out.print("Promotion Piece: (example: queen)");
                    String promotionPieceString = scanner.nextLine();
                    switch (promotionPieceString) {
                        case "queen" -> promotionPiece = new ChessPiece(teamColor, ChessPiece.PieceType.QUEEN);
                        case "bishop" -> promotionPiece = new ChessPiece(teamColor, ChessPiece.PieceType.BISHOP);
                        case "rook" -> promotionPiece = new ChessPiece(teamColor, ChessPiece.PieceType.ROOK);
                        case "knight" -> promotionPiece = new ChessPiece(teamColor, ChessPiece.PieceType.KNIGHT);
                        default -> throw new IllegalStateException("Unexpected value: " + promotionPieceString);

                    }
                }
            }


            ChessMove move = new ChessMove(startPosition, endPosition, promotionPiece.getPieceType());

            websocket.makeMove(getAuthData().getAuthToken(), getGameID(), move, startPositionString, endPositionString);

        } catch (IllegalStateException e) {
            return "Please enter a valid promotion piece";
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            return "Invalid Chess Move.";
        }
        return "";
    }

    private char getCol(char endCol) {
        switch(endCol) {
            case 'a' -> endCol = 8;
            case 'b' -> endCol = 7;
            case 'c' -> endCol = 6;
            case 'd' -> endCol = 5;
            case 'e' -> endCol = 4;
            case 'f' -> endCol = 3;
            case 'g' -> endCol = 2;
            case 'h' -> endCol = 1;
        }
        return endCol;
    }

    private String leave() throws IOException {
        websocket.leave(getAuthData().getAuthToken(), getGameID());
        Repl.setState(State.SIGNEDIN);
        return "";
    }

    private String redraw() {
        BoardPrinter.printBoard(game.getBoard(), teamColor);
        return "";
    }

    private String help() {
        return """
                redraw chess board - redraws chessboard
                leave - leave game
                make move - input move
                resign - forfeit game
                highlight legal moves - show legal moves
                help - with possible commands
                """;
    }

    public static void setGame(ChessGame game) {
        GameplayUI.game = game;
    }

    public static void setColor(ChessGame.TeamColor teamColor) {
        GameplayUI.teamColor = teamColor;
    }

}
