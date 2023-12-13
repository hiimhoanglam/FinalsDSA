package engine.board;

import engine.piece.*;

import java.util.Objects;

import static engine.board.Board.*;

public abstract class Move {
    /*
    An engine.board that we can operate on
    The engine.piece that we just moved
    The destination that we legally can arrive
     */
    final Board board;
    final Piece movedPiece;
    final int targetCoordinate;
    public static final Move NULL_MOVE = MoveFactory.createNullMove();
    Pawn enPassantPawn;

    public void setEnPassantPawn(Pawn enPassantPawn) {
        this.enPassantPawn = enPassantPawn;
    }

    private Move(final Board board, final Piece movedPiece, final int targetCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.targetCoordinate = targetCoordinate;
    }
    public int getCurrentCoordinate() {
        return this.movedPiece.getPiecePosition();
    }
    public int getTargetCoordinate() {
        return targetCoordinate;
    }
    public Piece getMovedPiece() {
        return movedPiece;
    }
    public boolean isCastlingMove() {
        return false;
    }
    public boolean isAttack() {
        return false;
    }
    public Piece getAttackedPiece() {
        return null;
    }
    /*
    execute is a function that return a new engine.board to reduce memory consumption and make the program faster
    */
    public Board execute() {
        final Builder builder = new Builder();
        for (final Piece currentPlayerPiece: this.board.getCurrentPlayer().getActivePieces()) {

            if (!currentPlayerPiece.equals(movedPiece)) {
                builder.setPiece(currentPlayerPiece);
            }
        }
        for (final Piece opponentPiece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
            builder.setPiece(opponentPiece);
        }
        //Piece newPiece = CreatePiece.createType(movedPiece.toString().charAt(0),targetCoordinate);
        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
        return builder.build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Move move = (Move) o;
        return this.targetCoordinate == move.targetCoordinate && this.getCurrentCoordinate() == move.getCurrentCoordinate();
    }

    @Override
    public int hashCode() {
        return Objects.hash(movedPiece, targetCoordinate);
    }

    /*
            Major Move means that the move is not attacking any engine.piece, it just moves the high value engine.piece to the target position
            Attack Move means that the move is attacking a engine.piece of the opponent. That means we have to have a variable for the
            attacked engine.piece in the class
             */
    public static class MajorMove extends Move{

        public MajorMove(final Board board, final Piece movedPiece, final int targetCoordinate) {
            super(board, movedPiece, targetCoordinate);
        }
    }
    public static class AttackMove extends Move{
        final Piece attackedPiece;

        public AttackMove(final Board board, final Piece movedPiece, final int targetCoordinate, final Piece attackedPiece) {
            super(board, movedPiece, targetCoordinate);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public boolean isAttack() {
            return true;
        }

        @Override
        public Piece getAttackedPiece() {
            return this.attackedPiece;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            AttackMove move = (AttackMove) o;
            return super.equals(move) && move.attackedPiece.equals(move.getAttackedPiece());
        }

        @Override
        public int hashCode() {
            return super.hashCode() + this.getAttackedPiece().hashCode();
        }
    }
    public static final class PawnMove extends Move {

        public PawnMove(Board board, Piece movedPiece, int targetCoordinate) {
            super(board, movedPiece, targetCoordinate);
        }
    }
    public static final class PawnJump extends Move {

        public PawnJump(Board board, Piece movedPiece, int targetCoordinate) {
            super(board, movedPiece, targetCoordinate);
        }

        @Override
        public Board execute() {
            final Builder builder = new Builder();
            for (final Piece currentPlayerPiece: this.board.getCurrentPlayer().getActivePieces()) {
                if (!currentPlayerPiece.equals(movedPiece)) {
                    builder.setPiece(currentPlayerPiece);
                }
            }
            Pawn newPawn = (Pawn) this.movedPiece.movePiece(this);
            this.setEnPassantPawn(newPawn);
            builder.setPiece(enPassantPawn);
            for (final Piece opponentPiece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(opponentPiece);
            }
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }
    public static class PawnAttackMove extends AttackMove {

        public PawnAttackMove(Board board, Piece movedPiece, int targetCoordinate, Piece attackedPiece) {
            super(board, movedPiece, targetCoordinate, attackedPiece);
        }
    }
    public static final class EnPassantMove extends PawnAttackMove {
        public EnPassantMove(Board board, Piece movedPiece, int targetCoordinate, Piece attackedPiece) {
            super(board, movedPiece, targetCoordinate, attackedPiece);
        }
    }
    public static abstract class CastleMove extends Move {
        Rook castleRook;

        public CastleMove(Board board, Piece movedPiece, int targetCoordinate,Rook castleRook) {
            super(board, movedPiece, targetCoordinate);
            this.castleRook = castleRook;
        }

        public Rook getCastleRook() {
            return castleRook;
        }

        @Override
        public boolean isCastlingMove() {
            return true;
        }

        @Override
        public Board execute() {
            Builder builder = new Builder();
            for (final Piece piece: board.getCurrentPlayer().getActivePieces()) {
                //Ignore current king and current rook
                if (!piece.equals(this.movedPiece) || !piece.equals(getCastleRook())) {
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece: board.getCurrentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            King newKing = (King) this.movedPiece.movePiece(this);
            newKing.firstMoveCheck.setFirstMove(false);
            builder.setPiece(this.movedPiece.movePiece(this));
            int rookDestination = 56;
            //Calculate the destination for the rook. If castled queen side: destination = newKingPosition + 1
            if (getCastleRook().getPiecePosition() == 0 || getCastleRook().getPiecePosition() == 56) {
                rookDestination = targetCoordinate + 1;
            }
            //Calculate the destination for the rook. If castled king side: destination = newKingPosition - 1
            if (getCastleRook().getPiecePosition() == 7 || getCastleRook().getPiecePosition() == 63) {
                rookDestination = targetCoordinate - 1;
            }
            Rook castledRook = new Rook(rookDestination,board.getCurrentPlayer().getAlliance());
            castledRook.firstMoveCheck.setFirstMove(false);
            builder.setPiece(castledRook);
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }
    public static final class CastleKingSide extends CastleMove {

        public CastleKingSide(Board board, Piece movedPiece, int targetCoordinate,Rook castleRook) {
            super(board, movedPiece, targetCoordinate,castleRook);
        }

        @Override
        public String toString() {
            return "O-O";
        }
    }
    public static final class CastleQueenSide extends CastleMove {
        public CastleQueenSide(Board board, Piece movedPiece, int targetCoordinate,Rook castleRook) {
            super(board, movedPiece, targetCoordinate,castleRook);
        }
        @Override
        public String toString() {
            return "O-O-O";
        }
    }
    public static final class NullMove extends Move {
        public NullMove() {
            super(null,null,-1);
        }

        @Override
        public Board execute() {
            throw new RuntimeException("Unable to execute");
        }
    }
}
