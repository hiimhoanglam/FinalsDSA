package piece;

import board.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Knight extends Piece{
    private final static int[] MOVE_CANDIDATES = {-17,-15,-10,-6,17,15,10,6};
    public Knight(int piecePosition, Alliance alliance) {
        super(piecePosition, alliance);
    }

    @Override
    public Collection<Move> getLegalMoves(Board board) {
        List<Move> legalMoves = new ArrayList<>();
        for (int candidateOffset: MOVE_CANDIDATES) {
            int targetCoordinate = piecePosition + candidateOffset;
            //There is a legal tile to move to
            if (BoardUtils.isValidCoordinate(targetCoordinate)) {
                //Checking for bug cases when generating legal moves of a knight
                if (firstColumnExclusions(targetCoordinate,candidateOffset) || secondColumnExclusions(targetCoordinate,candidateOffset)
                || seventhColumnExclusions(targetCoordinate,candidateOffset) || eighthColumnExclusions(targetCoordinate,candidateOffset)) {
                    continue;
                }
                final Tile targetDestination = board.getTile(targetCoordinate);
                if (!targetDestination.isOccupied()) {
                    legalMoves.add(new Move.MajorMove(board,this,targetCoordinate));
                }
                else {
                    //If there is an opponent piece on our path
                    final Piece targetPiece = targetDestination.getPiece();
                    final Alliance targetAlliance = targetPiece.getPieceAlliance();
                    if (pieceAlliance != targetAlliance) {
                        legalMoves.add(new Move.AttackMove(board,this,targetCoordinate,targetPiece));
                    }
                }
            }
        }
        return Collections.unmodifiableCollection(legalMoves);
    }
    //Odd cases for checking legal moves of knights
    public static boolean firstColumnExclusions(int coordinate, int offsets) {
        return BoardUtils.FIRST_COLUMN[coordinate] && (offsets == -17 || offsets == -10 || offsets == 6 || offsets == 15);
    }
    public static boolean secondColumnExclusions(int coordinate, int offsets) {
        return BoardUtils.SECOND_COLUMN[coordinate] && (offsets == -10 || offsets == 6);
    }
    public static boolean seventhColumnExclusions(int coordinate, int offsets) {
        return BoardUtils.SEVENTH_COLUMN[coordinate] && (offsets == -6 || offsets == 10);
    }
    public static boolean eighthColumnExclusions(int coordinate, int offsets) {
        return BoardUtils.EIGHTH_COLUMN[coordinate] && (offsets == -15 || offsets == -6 || offsets == 10 || offsets == 17);
    }

}
