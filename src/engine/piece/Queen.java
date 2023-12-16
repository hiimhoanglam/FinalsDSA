package engine.piece;

import engine.board.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Queen extends Piece{
    protected static final int[] CANDIDATE_OFFSETS = {-7,-9,7,9,-8,-1,8,1};
    public Queen(final int piecePosition, final Alliance alliance) {
        super(piecePosition, alliance);
        setPieceType(PieceType.QUEEN);
    }

    @Override
    public Collection<Move> getLegalMoves(final Board board) {
        List<Move> legalMoves = new ArrayList<>();
        for (int candidate: CANDIDATE_OFFSETS) {
            int currentPosition = this.piecePosition;
            while (BoardUtils.isValidCoordinate(currentPosition)) {
                if (firstColumnExclusions(currentPosition,candidate) || eighthColumnExclusions(currentPosition,candidate)) {
                    break;
                }
                currentPosition += candidate;
                if (BoardUtils.isValidCoordinate(currentPosition)) {
                    final Tile targetTile = board.getTile(currentPosition);
                    if (!targetTile.isOccupied()) {
                        legalMoves.add(new Move.MajorMove(board,this,currentPosition));
                    }
                    else {
                        final Piece targetPiece = targetTile.getPiece();
                        if (this.pieceAlliance != targetPiece.getPieceAlliance()) {
                            legalMoves.add(new Move.MajorAttackMove(board,this,currentPosition,targetPiece));
                        }
                        break;
                    }
                }
            }
        }
        return Collections.unmodifiableList(legalMoves);
    }
    @Override
    public Queen movePiece(Move move) {
        return new Queen(move.getTargetCoordinate(),move.getMovedPiece().getPieceAlliance());
    }
    protected static boolean firstColumnExclusions(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_FILE[currentPosition] && ((candidateOffset == -9)
                || (candidateOffset == -1) || (candidateOffset == 7));
    }
    protected static boolean eighthColumnExclusions(final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGHTH_FILE[currentPosition] && ((candidateOffset == -7)
                || (candidateOffset == 1) || (candidateOffset == 9));
    }

}
