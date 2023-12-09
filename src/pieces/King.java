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

public class King extends Piece {
    private final static int[] CANDIDATE_MOVE_COORDINATES = {-9, -8, -7, -1, 1, 7, 8, 9}; // Possible move directions for the king

    public King(int piecePosition, Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;

            // Check exclusion conditions for the first and eighth columns
            if (isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                    isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)) {
                continue;
            }

            if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

                if (!candidateDestinationTile.isTiledOccupied()) {
                    // If the destination tile is valid and unoccupied, add to the list of legal moves
                    legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                } else {
                    // If the destination tile has a piece
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

                    if (this.pieceAlliance != pieceAlliance) {
                        // If the piece at the destination belongs to the opponent, add to the list of legal moves as an attack move
                        legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                    }
                }
            }
        }

        return Collections.unmodifiableList(legalMoves);
    }

    private static boolean isFirstColumnExclusion(int currentPosition, int candidateOffset) {
        // Check if the king is in the first column and moving in a direction that would move it out of bounds
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == -1 || candidateOffset == 7);
    }

    private static boolean isEighthColumnExclusion(int currentPosition, int candidateOffset) {
        // Check if the king is in the eighth column and moving in a direction that would move it out of bounds
        return BoardUtils.EIGHT_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset == 1 || candidateOffset == 9);
    }
}
