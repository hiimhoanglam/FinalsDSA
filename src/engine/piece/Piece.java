package engine.piece;

import engine.board.Alliance;
import engine.board.Board;
import engine.board.Move;

import java.util.Collection;
import java.util.Objects;

public abstract class Piece{
    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    public FirstMoveCheck firstMoveCheck;
    protected PieceType pieceType;
    private final int hashCode;

    Piece(final int piecePosition, final Alliance alliance) {
        this.piecePosition = piecePosition;
        this.pieceAlliance = alliance;
        this.hashCode = calculateHashCode();
    }
    
    public abstract Collection<Move> getLegalMoves(final Board board);

    public int getPiecePosition() {
        return piecePosition;
    }

    public Alliance getPieceAlliance() {
        return pieceAlliance;
    }
    public abstract Piece movePiece(Move move);

    public PieceType getPieceType() {
        return pieceType;
    }

    public void setPieceType(PieceType pieceType) {
        this.pieceType = pieceType;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Piece piece = (Piece) other;
        return piecePosition == piece.piecePosition && pieceAlliance == piece.pieceAlliance && Objects.equals(firstMoveCheck, piece.firstMoveCheck);
    }
    private int calculateHashCode() {
        return Objects.hash(piecePosition, pieceAlliance, firstMoveCheck);
    }
    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @Override
    public String toString() {
        return getPieceType().toString();
    }

    /*
            Method to check if a engine.piece is a King.
            Default value is false. Only the King class have to override this method and return true
             */
    public boolean isKing() {
        return false;
    }

    public enum PieceType {
        QUEEN("Q"),
        BISHOP("B"),
        ROOK("R"),
        KING("K"),
        PAWN("P"),
        KNIGHT("N");
        private final String pieceType;
        PieceType(final String pieceType) {
            this.pieceType = pieceType;
        }

        @Override
        public String toString() {
            return pieceType;
        }
    }
}
