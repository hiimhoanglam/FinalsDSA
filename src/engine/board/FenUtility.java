package engine.board;

import engine.piece.CreatePiece;

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
        return builder;
    }
}
