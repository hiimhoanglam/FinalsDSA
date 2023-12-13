package engine.piece;
import engine.board.Alliance;
import engine.board.*;

import java.util.*;

public class Bishop extends Piece{
    private static final int[] CANDIDATE_OFFSETS = Arrays.copyOfRange(Queen.CANDIDATE_OFFSETS,0,3);
    public Bishop(final int piecePosition, final Alliance alliance) {
        super(piecePosition, alliance);
        setPieceType(PieceType.BISHOP);
    }

    @Override
    public Collection<Move> getLegalMoves(final Board board) {
        List<Move> legalMoves = new ArrayList<>();
        for (int candidate: CANDIDATE_OFFSETS) {
            int currentPosition = getPiecePosition();
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
                            legalMoves.add(new Move.AttackMove(board,this,currentPosition,targetPiece));
                        }
                        break;
                    }
                }
            }
        }
        return Collections.unmodifiableList(legalMoves);
    }

    @Override
    public Bishop movePiece(Move move) {
        return new Bishop(move.getTargetCoordinate(),move.getMovedPiece().getPieceAlliance());
    }


    protected static boolean firstColumnExclusions(final int targetCoordinate, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[targetCoordinate] && (candidateOffset == 7 || candidateOffset == -9);
    }
    protected static boolean eighthColumnExclusions(final int targetCoordinate, final int candidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[targetCoordinate] && (candidateOffset == -7 || candidateOffset == 9);
    }

}
