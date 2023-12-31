package engine.piece;

import engine.board.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Knight extends Piece{
    private final static int[] CANDIDATE_OFFSETS = {-17,-15,-10,-6,17,15,10,6};
    public Knight(final int piecePosition, final Alliance alliance) {
        super(piecePosition, alliance);
        setPieceType(PieceType.KNIGHT);
    }

    @Override
    public Collection<Move> getLegalMoves(Board board) {
        List<Move> legalMoves = new ArrayList<>();
        for (int candidateOffset: CANDIDATE_OFFSETS) {
            int targetCoordinate = piecePosition + candidateOffset;
            //There is a legal tile to move to
            if (BoardUtils.isValidCoordinate(targetCoordinate)) {
                //Checking for bug cases when generating legal moves of a knight
                if (firstColumnExclusions(piecePosition,candidateOffset) || secondColumnExclusions(piecePosition,candidateOffset)
                || seventhColumnExclusions(piecePosition,candidateOffset) || eighthColumnExclusions(piecePosition,candidateOffset)) {
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
                        legalMoves.add(new Move.MajorAttackMove(board,this,targetCoordinate,targetPiece));
                    }
                }
            }
        }
        return Collections.unmodifiableCollection(legalMoves);
    }
    @Override
    public Knight movePiece(Move move) {
        return new Knight(move.getTargetCoordinate(),move.getMovedPiece().getPieceAlliance());
    }
    //Odd cases for checking legal moves of knights
    protected static boolean firstColumnExclusions(int coordinate, int offsets) {
        return BoardUtils.FIRST_FILE[coordinate] && (offsets == -17 || offsets == -10 || offsets == 6 || offsets == 15);
    }
    protected static boolean secondColumnExclusions(int coordinate, int offsets) {
        return BoardUtils.SECOND_FILE[coordinate] && (offsets == -10 || offsets == 6);
    }
    protected static boolean seventhColumnExclusions(int coordinate, int offsets) {
        return BoardUtils.SEVENTH_FILE[coordinate] && (offsets == -6 || offsets == 10);
    }
    protected static boolean eighthColumnExclusions(int coordinate, int offsets) {
        return BoardUtils.EIGHTH_FILE[coordinate] && (offsets == -15 || offsets == -6 || offsets == 10 || offsets == 17);
    }

}
