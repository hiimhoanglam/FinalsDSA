package pieces;

import alliance.Alliance;
import board.Board;
import board.Move;

import java.util.Collection;
import java.util.Map;


public abstract class Piece {
    protected final int piecePosition;
    protected final Alliance pieceAlliance;

    public Piece(int piecePosition, Alliance pieceAlliance) {
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
    }

    public abstract Collection<Move> calculateLegalMoves(Board board);

    public Alliance getPieceAlliance() {
        return pieceAlliance;
    }

    public int getPiecePosition() {
        return piecePosition;
    }

    public void setPiecePosition(int destinationCoordinate) {
        // Your implementation to set the piece's position on the board
    }
}

