package Board;

import piece.Piece;

public class Board {
    public static final String PAWN = "P";
    public static final String ROOK = "R";
    public static final String KNIGHT = "N";
    public static final String BISHOP = "B";
    public static final String QUEEN = "Q";
    public static final String KING = "K";
    public static final String EMPTY = "-";
    public static Piece[][] board = new Piece[8][8];

}
