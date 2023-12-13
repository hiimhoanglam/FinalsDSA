package engine.piece;

import engine.board.Alliance;

public class CreatePiece {
    public static Piece createType(final char pieceType, final int coordinate) {
        switch (pieceType) {
            case 'q':
                return new Queen(coordinate,Alliance.BLACK);
            case 'p':
                return new Pawn(coordinate,Alliance.BLACK);
            case 'r':
                return new Rook(coordinate,Alliance.BLACK);
            case 'b':
                return new Bishop(coordinate,Alliance.BLACK);
            case 'n':
                return new Knight(coordinate,Alliance.BLACK);
            case 'k':
                return new King(coordinate,Alliance.BLACK);
            // Handling uppercase versions
            case 'Q':
                return new Queen(coordinate,Alliance.WHITE);
            case 'P':
                return new Pawn(coordinate,Alliance.WHITE);
            case 'R':
                return new Rook(coordinate,Alliance.WHITE);
            case 'B':
                return new Bishop(coordinate,Alliance.WHITE);
            case 'N':
                return new Knight(coordinate,Alliance.WHITE);
            case 'K':
                return new King(coordinate,Alliance.WHITE);
            default:
                throw new IllegalArgumentException("Invalid engine.piece type: " + pieceType);
        }
    }
}
