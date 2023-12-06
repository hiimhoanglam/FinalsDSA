package piece;

import Board.Board;

import java.util.List;

public class Pawn extends Piece{
    public Pawn(Point current, int whichPlayer) {
        super(current, whichPlayer);
    }

    @Override
    public List<Point> getLegalMoves() {
        return null;
    }

    @Override
    public String getType() {
        return Board.PAWN;
    }

}
