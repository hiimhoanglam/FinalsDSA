package piece;

import Board.Board;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece{
    public Bishop(Point current, int whichPlayer) {
        super(current, whichPlayer);
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
        return Board.BISHOP;
    }
}
