package board;

import pieces.Piece;

/**
 * The Move class represents a move made on the chessboard.
 */
public abstract class Move {
    final Board board;
    final Piece movedPiece;
    final int destinationCoordinate;

    /**
     * Constructs a Move with the given parameters.
     * @param board The chessboard.
     * @param movedPiece The piece being moved.
     * @param destinationCoordinate The destination coordinate of the move.
     */
    private Move(Board board, Piece movedPiece, int destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
    }

    /**
     * A subclass of Move representing a major move (non-attack move).
     */
    public static final class MajorMove extends Move {
        /**
         * Constructs a MajorMove with the given parameters.
         * @param board The chessboard.
         * @param movedPiece The piece being moved.
         * @param destinationCoordinate The destination coordinate of the move.
         */
        public MajorMove(Board board, Piece movedPiece, int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }

    /**
     * A subclass of Move representing an attack move.
     */
    public static final class AttackMove extends Move {
        final Piece attackedPiece;

        /**
         * Constructs an AttackMove with the given parameters.
         * @param board The chessboard.
         * @param movedPiece The piece being moved.
         * @param destinationCoordinate The destination coordinate of the move.
         * @param attackedPiece The piece being attacked.
         */
        public AttackMove(Board board, Piece movedPiece, int destinationCoordinate, Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }
    }
}
