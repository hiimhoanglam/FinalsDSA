//package board;
//
//import pieces.Piece;
//
//import java.util.HashMap;
//
//public class FenUtility {
//    /* This is a class to load FEN strings into the board */
//    public static final String startFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
//    public static HashMap<Character,Integer> pieceType = new HashMap<>();
//    static {
//        Character[] pieceSymbol = {'k','q','b','r','n','p'};
//        int[] pieceValue = {Piece.KING,Piece.QUEEN,Piece.BISHOP,Piece.ROOK,Piece.KNIGHT,Piece.PAWN};
//        for (int i = 0; i < 6; i++) {
//            pieceType.put(pieceSymbol[i],pieceValue[i]);
//        }
//    }
//    public static void loadPosition(Board board, String fen) {
//        String[] sections = fen.split(" ");
//        int file = 0;
//        int rank = 7;
//        String boardRep = sections[0];
//        for (int i = 0; i < boardRep.length(); i++) {
//            char character = boardRep.charAt(i);
//            if (character == '/') {
//                file = 0;
//                rank--;
//            }
//            else {
//                if (Character.isDigit(character)) {
//                    file += Character.getNumericValue(character);
//                }
//                else {
//                    //If the character is uppercase => White. Otherwise => Black
//                    int color = Character.isUpperCase(character) ? Piece.WHITE: Piece.BLACK;
//                    int type = pieceType.get(Character.toLowerCase(character));
//                    board.square[rank * 8 + file] = type | color;
//                    file++;
//                }
//            }
//            board.whiteToMove = sections[1].equals("w");
//        }
//    }
//}
