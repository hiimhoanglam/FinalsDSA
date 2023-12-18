package engine.piece;

import engine.board.*;
import engine.player.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class King extends Piece{
    public King(final int piecePosition, final Alliance alliance) {
        super(piecePosition, alliance);
        this.firstMoveCheck = new FirstMoveCheck();
        setPieceType(PieceType.KING);
    }
    private static final int[] CANDIDATE_OFFSETS = Queen.CANDIDATE_OFFSETS;

    @Override
    public boolean isKing() {
        return true;
    }

    @Override
    public Collection<Move> getLegalMoves(Board board) {
        List<Move> legalMoves = new ArrayList<>();
        for (int candidateOffset: CANDIDATE_OFFSETS) {
            int targetCoordinate = piecePosition + candidateOffset;
            if (firstColumnExclusions(this.piecePosition,candidateOffset) || eighthColumnExclusions(this.piecePosition,candidateOffset)) {
                continue;
            }
            //There is a legal tile to move to
            if (BoardUtils.isValidCoordinate(targetCoordinate)) {
                //Checking for bug cases when generating legal moves of a king
                final Tile targetDestination = board.getTile(targetCoordinate);
                if (!targetDestination.isOccupied()) {
                    legalMoves.add(new Move.MajorMove(board,this,targetCoordinate));
                }
                else {
                    //If there is an opponent engine.piece on our path
                    final Piece targetPiece = targetDestination.getPiece();
                    final Alliance targetAlliance = targetPiece.getPieceAlliance();
                    if (pieceAlliance != targetAlliance) {
                        legalMoves.add(new Move.AttackMove(board,this,targetCoordinate,targetPiece));
                    }
                }
            }
        }
        return legalMoves;
    }
    @Override
    public King movePiece(Move move) {
        return new King(move.getTargetCoordinate(),move.getMovedPiece().getPieceAlliance());
    }
    protected static boolean firstColumnExclusions(final int coordinate, final int offsets) {
        return BoardUtils.FIRST_FILE[coordinate] && (offsets == -9 || offsets == 7 || offsets == -1);
    }
    protected static boolean eighthColumnExclusions(final int coordinate, final int offsets) {
        return BoardUtils.EIGHTH_FILE[coordinate] && (offsets == -7 || offsets == 1 || offsets == 9);
    }

}
