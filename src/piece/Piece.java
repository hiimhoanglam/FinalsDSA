package piece;

import board.Alliance;
import board.Board;
import board.Move;

import java.util.Collection;

public abstract class Piece {
    protected int piecePosition;
    protected Alliance pieceAlliance;

    Piece(int piecePosition, Alliance alliance) {
        this.piecePosition = piecePosition;
        this.pieceAlliance = alliance;
    }



    public abstract Collection<Move> getLegalMoves(final Board board);

    public int getPiecePosition() {
        return piecePosition;
    }

    public Alliance getPieceAlliance() {
        return pieceAlliance;
    }
}
