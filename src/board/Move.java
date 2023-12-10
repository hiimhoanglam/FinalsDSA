package board;

import piece.Piece;

public abstract class Move {
    /*
    A board that we can operate on
    The piece that we just moved
    The destination that we legally can arrive
     */
    final Board board;
    final Piece movedPiece;
    final int targetCoordinate;

    private Move(final Board board, final Piece movedPiece, final int targetCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.targetCoordinate = targetCoordinate;
    }
    /*
    Major Move means that the move is not attacking any piece, it just moves the high value piece to the target position
    Attack Move means that the move is attacking a piece of the opponent. That means we have to have a variable for the
    attacked piece in the class
     */
    public static class MajorMove extends Move{

        public MajorMove(Board board, Piece movedPiece, int targetCoordinate) {
            super(board, movedPiece, targetCoordinate);
        }
    }
    public static class AttackMove extends Move{
        final Piece attackedPiece;

        public AttackMove(final Board board, final Piece movedPiece, final int targetCoordinate, final Piece attackedPiece) {
            super(board, movedPiece, targetCoordinate);
            this.attackedPiece = attackedPiece;
        }
    }
}
