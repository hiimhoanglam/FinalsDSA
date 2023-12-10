package piece;

import board.*;

import java.util.*;

public class Rook extends Piece{
    private static final int[] CANDIDATE_OFFSETS = Arrays.copyOfRange(Queen.CANDIDATE_OFFSETS,4,7);
    public Rook(int piecePosition, Alliance alliance) {
        super(piecePosition, alliance);
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
    public static boolean firstColumnExclusions(final int targetCoordinate, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[targetCoordinate] && (candidateOffset == -1);
    }
    public static boolean eighthColumnExclusions(final int targetCoordinate, final int candidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[targetCoordinate] && (candidateOffset == 1);
    }
}
