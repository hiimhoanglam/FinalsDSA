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


    private Move(final Board board, final Piece movedPiece, final int targetCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.targetCoordinate = targetCoordinate;
    }

    public Board getBoard() {
        return board;
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
    public Board undo() {
        final Builder builder = new Builder();
        this.board.getAllPieces().forEach(builder::setPiece);
        builder.setMoveMaker(this.board.getCurrentPlayer().getAlliance());
        return builder.build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Move move = (Move) o;
        return this.targetCoordinate == move.targetCoordinate && this.getCurrentCoordinate() == move.getCurrentCoordinate()
                && this.movedPiece.equals(move.getMovedPiece());
    }

    @Override
    public int hashCode() {
        return Objects.hash(movedPiece, targetCoordinate,movedPiece.getPiecePosition());
    }

    /*
            Major Move means that the move is not attacking any engine.piece, it just moves the high value piece to the target position
            Attack Move means that the move is attacking a piece of the opponent. That means we have to have a variable for the
            attacked engine.piece in the class
             */
    public static class MajorMove extends Move{

        public MajorMove(final Board board, final Piece movedPiece, final int targetCoordinate) {
            super(board, movedPiece, targetCoordinate);
        }

        @Override
        public boolean equals(final Object o) {
            return this == o || o instanceof MajorMove && super.equals(o);
        }

        @Override
        public String toString() {
            return movedPiece.toString() + BoardUtils.getPositionAtCoordinate(targetCoordinate);
        }
    }
    public static class MajorAttackMove extends AttackMove {

        public MajorAttackMove(final Board board, final Piece movedPiece, final int targetCoordinate, final Piece attackedPiece) {
            super(board, movedPiece, targetCoordinate, attackedPiece);
        }

        @Override
        public String toString() {
            return movedPiece.getPieceType() + "x" + BoardUtils.getPositionAtCoordinate(targetCoordinate);
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
    public static final class PawnPromotion extends Move {
        final Move decoratedMove;
        final Pawn promotedPawn;
        //Decorator pattern
        public PawnPromotion(final Move decoratedMove) {
            super(decoratedMove.getBoard(), decoratedMove.getMovedPiece(), decoratedMove.getTargetCoordinate());
            this.decoratedMove = decoratedMove;
            this.promotedPawn = (Pawn) decoratedMove.getMovedPiece();
        }

        @Override
        public Board execute() {
            final Board movedBoard = this.decoratedMove.execute();
            final Builder builder = new Builder();
            for (final Piece piece: movedBoard.getCurrentPlayer().getActivePieces()) {
                if (!piece.equals(promotedPawn)) {
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece: movedBoard.getCurrentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            Piece piece = this.promotedPawn.getPromotionPiece().movePiece(this);
            builder.setPiece(piece);
            builder.setMoveMaker(movedBoard.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
        }

        @Override
        public boolean isAttack() {
            return decoratedMove.isAttack();
        }

        @Override
        public Piece getAttackedPiece() {
            return decoratedMove.getAttackedPiece();
        }

        public Move getDecoratedMove() {
            return decoratedMove;
        }

        public Pawn getPromotedPawn() {
            return promotedPawn;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            final PawnPromotion that = (PawnPromotion) o;
            return Objects.equals(decoratedMove, that.decoratedMove) && Objects.equals(promotedPawn, that.promotedPawn);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), promotedPawn);
        }

        @Override
        public String toString() {
            StringBuilder string = new StringBuilder();
            string.append(BoardUtils.getPositionAtCoordinate(targetCoordinate));
            string.append("=Q");
            if (execute().getCurrentPlayer().getOpponent().isInCheckMate()) {
                string.append("#");
            }
            if (execute().getCurrentPlayer().getOpponent().isInCheck()) {
                string.append("+");
            }
            return string.toString();
        }
    }
    public static final class PawnMove extends Move {

        public PawnMove(Board board, Piece movedPiece, int targetCoordinate) {
            super(board, movedPiece, targetCoordinate);
        }

        @Override
        public boolean equals(Object o) {
            return this == o || o instanceof PawnMove && super.equals(o);
        }

        @Override
        public String toString() {
            return BoardUtils.getPositionAtCoordinate(targetCoordinate);
        }
    }
    public static final class PawnJump extends Move {

        public PawnJump(final Board board, final Piece movedPiece, final int targetCoordinate) {
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
            final Pawn newPawn = (Pawn) this.movedPiece.movePiece(this);
            builder.setEnPassantPawn(newPawn);
            builder.setPiece(newPawn);
            for (final Piece opponentPiece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(opponentPiece);
            }
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
        }

        @Override
        public String toString() {
            return BoardUtils.getPositionAtCoordinate(targetCoordinate);
        }
    }
    public static class PawnAttackMove extends AttackMove {

        public PawnAttackMove(Board board, Piece movedPiece, int targetCoordinate, Piece attackedPiece) {
            super(board, movedPiece, targetCoordinate, attackedPiece);
        }
        @Override
        public boolean equals(Object o) {
            return this == o || o instanceof PawnAttackMove && super.equals(o);
        }

        @Override
        public String toString() {
            return BoardUtils.getPositionAtCoordinate(movedPiece.getPiecePosition()).substring(0,1) + "x" +
                    BoardUtils.getPositionAtCoordinate(targetCoordinate);
        }
    }
    public static final class PawnEnPassantMove extends PawnAttackMove {
        public PawnEnPassantMove(Board board, Piece movedPiece, int targetCoordinate, Piece attackedPiece) {
            super(board, movedPiece, targetCoordinate, attackedPiece);
        }
        @Override
        public boolean equals(Object o) {
            return o == this || o instanceof PawnEnPassantMove && super.equals(o);
        }

        @Override
        public Board execute() {
            final Builder builder = new Builder();
            for (final Piece currentPlayerPiece: this.board.getCurrentPlayer().getActivePieces()) {
                if (!currentPlayerPiece.equals(movedPiece)) {
                    builder.setPiece(currentPlayerPiece);
                }
            }
            for (final Piece opponentPiece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
                if (!opponentPiece.equals(this.getAttackedPiece())) {
                    builder.setPiece(opponentPiece);
                }
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
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
                if (!piece.equals(this.movedPiece) && !piece.equals(getCastleRook())) {
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece: board.getCurrentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            King newKing = (King) this.movedPiece.movePiece(this);
            newKing.firstMoveCheck.setFirstMove(false);
            builder.setPiece(newKing);
            int rookDestination = 0;
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

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof CastleMove other)) {
                return false;
            }
            return super.equals(other) && this.castleRook.equals(other.getCastleRook());
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), castleRook);
        }
    }
    public static final class CastleKingSide extends CastleMove {

        public CastleKingSide(Board board, Piece movedPiece, int targetCoordinate,Rook castleRook) {
            super(board, movedPiece, targetCoordinate,castleRook);
        }
        @Override
        public boolean equals(Object o) {
            return o == this || o instanceof CastleKingSide && super.equals(o);
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
        public boolean equals(Object o) {
            return o == this || o instanceof CastleQueenSide && super.equals(o);
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
