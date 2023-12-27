package engine.board;

import engine.piece.CreatePiece;
import engine.piece.Piece;

/*
Utility class for loading a position from a FEN STRING
 */
public class FenUtility {
    public static final String startFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    public static Board.Builder loadPosition(String fen) {
        Board.Builder builder = new Board.Builder();
        String[] sections = fen.split(" ");
        int file = 0;
        int rank = 0;
        String boardRep = sections[0];
        for (int i = 0; i < boardRep.length(); i++) {
            char character = boardRep.charAt(i);
            if (character == '/') {
                file = 0;
                rank++;
            }
            else {
                if (Character.isDigit(character)) {
                    file += Character.getNumericValue(character);
                }
                else {
                    builder.setPiece(CreatePiece.createType(character,rank * 8 + file));
                    file++;
                }
            }
        }
        boolean whiteToMove = (sections[1].equals("w"));
        if (whiteToMove) {
            builder.setMoveMaker(Alliance.WHITE);
        }
        else {
            builder.setMoveMaker(Alliance.BLACK);
        }
//        String castlingRights = (sections.length > 2) ? sections[2] : "KQkq";
//        boolean whiteCastleKingSide = castlingRights.contains("K");
//        boolean whiteCastleQueenSide = castlingRights.contains("Q");
//        boolean blackCastleKingSide = castlingRights.contains("k");
//        boolean blackCastleQueenSide = castlingRights.contains("q");
        return builder;
    }
    public static String currentFen(Board board) {
        StringBuilder fen = new StringBuilder();
        for (int rank = 7; rank >= 0; rank--) {
            int numEmptyFiles = 0;
            for (int file = 0; file < 8; file++) {
                int i = rank * 8 + file;
                Piece piece = board.getTile(i).getPiece();
                if (piece != null) {
                    if (numEmptyFiles != 0) {
                        fen.append(numEmptyFiles);
                        numEmptyFiles = 0;
                    }
                    boolean isBlack = !piece.getPieceAlliance().isWhite();
                    Piece.PieceType pieceType = piece.getPieceType();
                    char pieceChar = ' ';
                    switch (pieceType) {
                        case ROOK:
                            pieceChar = 'R';
                            break;
                        case KNIGHT:
                            pieceChar = 'N';
                            break;
                        case BISHOP:
                            pieceChar = 'B';
                            break;
                        case QUEEN:
                            pieceChar = 'Q';
                            break;
                        case KING:
                            pieceChar = 'K';
                            break;
                        case PAWN:
                            pieceChar = 'P';
                            break;
                    }
                    fen.append((isBlack) ? String.valueOf(pieceChar).toLowerCase() : String.valueOf(pieceChar));
                } else {
                    numEmptyFiles++;
                }

            }
            if (numEmptyFiles != 0) {
                fen.append(numEmptyFiles);
            }
            if (rank != 0) {
                fen.append('/');
            }
        }

        // Side to move
        fen.append(" ");
        fen.append((board.getCurrentPlayer().getAlliance().isWhite()) ? 'w' : 'b');

        // Castling TODO
        // Fifty move counter TODO
        return fen.toString();
    }
}
