package piece;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece{
    public Queen(Point current, int whichPlayer) {
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
        return "Queen";
    }

}
