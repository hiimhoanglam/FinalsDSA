package piece;

import Board.Board;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece{
    public boolean hasMoved;

    public Rook(Point current, int whichPlayer) {
        super(current, whichPlayer);
        this.hasMoved = true;
    }

    @Override
    public List<Point> getLegalMoves() {
        int x = current.getX();
        int y = current.getY();
        ArrayList<Point> legalMoves = new ArrayList<>();
        return legalMoves;
    }

    @Override
    public String getType() {
        return Board.ROOK;
    }
}
