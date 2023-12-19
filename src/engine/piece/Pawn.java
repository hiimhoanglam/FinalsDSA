package engine.piece;

import engine.board.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Pawn extends Piece{
    public Pawn(final int piecePosition, final Alliance alliance) {
        super(piecePosition, alliance);
        this.firstMoveCheck = new FirstMoveCheck();
        setPieceType(PieceType.PAWN);
    }
    protected static final int[] CANDIDATE_OFFSETS = {7,8,9,16};

    @Override
    public Collection<Move> getLegalMoves(final Board board) {
        List<Move> legalMoves = new ArrayList<>();
        for (int candidateOffsets: CANDIDATE_OFFSETS) {
            int candidatePosition = this.piecePosition + (this.pieceAlliance.getDirection() * candidateOffsets);
            if (BoardUtils.isValidCoordinate(candidatePosition)) {
                /*
                Move the pawn one tile up
                 */
                if (candidateOffsets == 8 && !board.getTile(candidatePosition).isOccupied()) {
                    if (this.pieceAlliance.getPromotionSquare(candidatePosition)) {
                        legalMoves.add(new Move.PawnPromotion(new Move.PawnMove(board,this,candidatePosition)));
                    }
                    else {
                        legalMoves.add(new Move.PawnMove(board,this,candidatePosition));
                    }

                }
                /*
                Move the pawn two tile up. The requirement is that this is the first move of the player and there is no
                piece blocking the path(both players' pieces)
                 */
                else if (candidateOffsets == 16 && this.firstMoveCheck.isFirstMove() &&
                      ((secondRowExclusions(this.getPiecePosition(),candidateOffsets) && this.pieceAlliance == Alliance.BLACK) ||
                      (seventhRowExclusions(this.getPiecePosition(),candidateOffsets) && this.pieceAlliance == Alliance.WHITE))){
                    int betweenDestination = this.piecePosition + (this.pieceAlliance.getDirection() * 8);
                    if (!board.getTile(betweenDestination).isOccupied() && !board.getTile(candidatePosition).isOccupied()) {
                        legalMoves.add(new Move.PawnJump(board,this,candidatePosition));
                    }
                }
                /*
                Attacking opponent engine.piece. The rule is that the pawn can only attack the piece diagonally forward one square
                to the left or right. However, we still have to handle exceptions located on the first and eighth column
                 */
                else if (candidateOffsets == 7) {
                    if (!((eighthColumnExclusions(this.piecePosition,candidateOffsets) && this.pieceAlliance == Alliance.WHITE)
                        || (firstColumnExclusions(this.piecePosition,candidateOffsets) && this.pieceAlliance == Alliance.BLACK))) {
                        final Tile candidateTile = board.getTile(candidatePosition);
                        if (candidateTile.isOccupied()) {
                            if (this.getPieceAlliance() != candidateTile.getPiece().pieceAlliance) {
                                boolean check = this.pieceAlliance.getPromotionSquare(candidatePosition);
                                if (check) {
                                    legalMoves.add(new Move.PawnPromotion(new Move.PawnAttackMove(board,this,candidatePosition,candidateTile.getPiece())));
                                }
                                else {
                                    legalMoves.add(new Move.PawnAttackMove(board,this,candidatePosition,candidateTile.getPiece()));
                                }
                            }
                        }
                        else if (!candidateTile.isOccupied()){
                            //Dealing with en passant attacking move
                            if (board.getEnPassantPawn() != null &&
                                board.getEnPassantPawn().getPiecePosition() == (this.getPiecePosition() + this.getPieceAlliance().getOppositeDirection())) {
                                final Piece candidatePiece = board.getEnPassantPawn();
                                if (this.getPieceAlliance() != candidatePiece.getPieceAlliance()) {
                                    legalMoves.add(new Move.PawnEnPassantMove(board,this,candidatePosition,candidatePiece));
                                }
                            }
                        }
                    }
                }
                else if (candidateOffsets == 9) {
                    if (!((firstColumnExclusions(this.piecePosition,candidateOffsets) && this.pieceAlliance == Alliance.WHITE)
                            || (eighthColumnExclusions(this.piecePosition,candidateOffsets) && this.pieceAlliance == Alliance.BLACK))) {

                        final Tile candidateTile = board.getTile(candidatePosition);
                        if (candidateTile.isOccupied()) {
                            if (this.getPieceAlliance() != candidateTile.getPiece().pieceAlliance) {
                                boolean check = this.pieceAlliance.getPromotionSquare(candidatePosition);
                                if (check) {
                                    legalMoves.add(new Move.PawnPromotion(new Move.PawnAttackMove(board,this,candidatePosition,candidateTile.getPiece())));
                                }
                                else {
                                    legalMoves.add(new Move.PawnAttackMove(board,this,candidatePosition,candidateTile.getPiece()));
                                }
                            }
                        }
                        else if (!candidateTile.isOccupied()){
                            //Dealing with en passant attacking move
                            if (board.getEnPassantPawn() != null &&
                                board.getEnPassantPawn().getPiecePosition() == (this.getPiecePosition() - this.getPieceAlliance().getOppositeDirection())) {
                                final Piece candidatePiece = board.getEnPassantPawn();
                                if (this.getPieceAlliance() != candidatePiece.getPieceAlliance()) {
                                    legalMoves.add(new Move.PawnEnPassantMove(board,this,candidatePosition,candidatePiece));
                                }
                            }
                        }
                    }
                }
            }
        }
        return Collections.unmodifiableCollection(legalMoves);
    }
    @Override
    public Pawn movePiece(Move move) {
        return new Pawn(move.getTargetCoordinate(),move.getMovedPiece().getPieceAlliance());
    }
    public Piece getPromotionPiece() {
        return new Queen(this.piecePosition,this.pieceAlliance);
    }

    protected static boolean firstColumnExclusions(final int coordinate, final int offsets) {
        return BoardUtils.FIRST_FILE[coordinate];
    }
    protected static boolean eighthColumnExclusions(final int coordinate, final int offsets) {
        return BoardUtils.EIGHTH_FILE[coordinate];
    }
    protected static boolean secondRowExclusions(final int coordinate, final int offsets) {
        return BoardUtils.SEVENTH_RANK[coordinate];
    }
    protected static boolean seventhRowExclusions(final int coordinate, final int offsets) {
        return BoardUtils.SECOND_RANK[coordinate];
    }
}
