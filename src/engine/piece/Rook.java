package engine.piece;

import engine.board.*;

import java.util.*;

public class Rook extends Piece{
    private static final int[] CANDIDATE_OFFSETS = {-8,-1,8,1};
    public Rook(final int piecePosition, final Alliance alliance) {
        super(piecePosition, alliance);
        this.firstMoveCheck = new FirstMoveCheck();
        setPieceType(PieceType.ROOK);
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
    public Rook movePiece(Move move) {
        return new Rook(move.getTargetCoordinate(),move.getMovedPiece().getPieceAlliance());
    }

    protected static boolean firstColumnExclusions(final int targetCoordinate, final int candidateOffset) {
        return BoardUtils.FIRST_FILE[targetCoordinate] && (candidateOffset == -1);
    }
    protected static boolean eighthColumnExclusions(final int targetCoordinate, final int candidateOffset) {
        return BoardUtils.EIGHTH_FILE[targetCoordinate] && (candidateOffset == 1);
    }
}
