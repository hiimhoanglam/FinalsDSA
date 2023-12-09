package pieces;

import alliance.Alliance;
import board.Board;
import board.BoardUtils;
import board.Move;
import board.Tile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Pawn extends Piece {
    private final static int[] CANDIDATE_MOVE_COORDINATES = {8, 16, 7, 9}; // Possible move directions for the pawn

    private boolean isFirstMove;

    public Pawn(int piecePosition, Alliance pieceAlliance, boolean isFirst) {
        super(piecePosition, pieceAlliance);
        this.isFirstMove = isFirst;
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {
            final int candidateDestinationCoordinate = this.piecePosition +
                    (this.getPieceAlliance().getDirection() * currentCandidateOffset);

            // Check if the destination coordinate is valid
            if (!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                continue;
            }

            if (currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTiledOccupied()) {
                // Move one square forward
                legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
            } else if (currentCandidateOffset == 16 && this.isFirstMove() &&
                    ((BoardUtils.SECOND_ROW[this.piecePosition] && this.getPieceAlliance().isBlack()) ||
                            (BoardUtils.SEVENTH_ROW[this.piecePosition] && this.getPieceAlliance().isWhite()))) {
                // Move two squares forward (for the first move of the pawn)
                final int behindCandidateDestinationCoordinate = this.piecePosition +
                        (this.getPieceAlliance().getDirection() * 8);
                if (!board.getTile(behindCandidateDestinationCoordinate).isTiledOccupied() &&
                        !board.getTile(candidateDestinationCoordinate).isTiledOccupied()) {
                    legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                }
            } else if (currentCandidateOffset == 7 &&
                    !((BoardUtils.EIGHT_COLUMN[this.piecePosition] && this.getPieceAlliance().isWhite()) ||
                            (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.getPieceAlliance().isBlack()))) {
                // Move diagonally left to attack
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if (candidateDestinationTile.isTiledOccupied()) {
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    if (this.pieceAlliance != pieceAtDestination.getPieceAlliance()) {
                        legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                    }
                }
            } else if (currentCandidateOffset == 9 &&
                    !((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.getPieceAlliance().isWhite()) ||
                            (BoardUtils.EIGHT_COLUMN[this.piecePosition] && this.getPieceAlliance().isBlack()))) {
                // Move diagonally right to attack
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if (candidateDestinationTile.isTiledOccupied()) {
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    if (this.pieceAlliance != pieceAtDestination.getPieceAlliance()) {
                        legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                    }
                }
            }
        }

        return Collections.unmodifiableList(legalMoves);
    }

    private boolean isFirstMove() {
        return this.isFirstMove;
    }
}
