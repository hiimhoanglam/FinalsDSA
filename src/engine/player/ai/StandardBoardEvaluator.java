package engine.player.ai;

import engine.board.Alliance;
import engine.board.Board;
import engine.board.BoardUtils;
import engine.board.Move;
import engine.piece.Piece;
import engine.player.player.Player;

import java.util.Objects;

public class StandardBoardEvaluator implements BoardEvaluator {
    private static volatile StandardBoardEvaluator instance;
    private static final int CASTLE_BONUS = 150;
    private static final int CHECKMATE_BONUS = 10000;
    private static final int CHECK_BONUS = 70;
    private static final int DEPTH_BONUS = 100;
    private final static int ATTACK_MULTIPLIER = 1;

    private StandardBoardEvaluator() {
    }

    public static StandardBoardEvaluator getInstance() {
        StandardBoardEvaluator result = instance;
        if (result != null) {
            return result;
        }
        synchronized(StandardBoardEvaluator.class) {
            if (instance == null) {
                instance = new StandardBoardEvaluator();
            }
            return instance;
        }
    }

    @Override
    public int evaluate(final Board board, final int depth) {
        return scorePlayer(board,board.getWhitePlayer(),depth) - scorePlayer(board,board.getBlackPlayer(),depth);
    }

    private int scorePlayer(final Board board, final Player player, final int depth) {
        return pieceScore(player) + check(player) + checkmate(player,depth) + castled(player) + mobility(player)
                + attacks(player) + kingThreats(player,depth);

    }
    //TODO pinned pieces
    //TODO pawn structure

    private int mobility(Player player) {
        return player.getLegalMoves().size();
    }
    private int kingThreats(final Player player,
                                   final int depth) {
        return player.getOpponent().isInCheckMate() ? CHECKMATE_BONUS  * depthBonus(depth) : check(player);
    }
    private int attacks(final Player player) {
        int attackScore = 1;
        for (final Move move : player.getLegalMoves()) {
            if (move.isAttack()) {
                final Piece movedPiece = move.getMovedPiece();
                final Piece attackedPiece = move.getAttackedPiece();
                if (movedPiece.getPieceValue() <= attackedPiece.getPieceValue()) {
                    attackScore++;
                }
            }
        }
        return attackScore * ATTACK_MULTIPLIER;
    }
    private static int depthBonus(int depth) {
        return depth == 0 ? 1 : DEPTH_BONUS * depth;
    }

    private int castled(Player player) {
        return player.isCastled() ? CASTLE_BONUS : 0;
    }

    private int checkmate(Player player, int depth) {
        return player.isInCheckMate() ? CHECKMATE_BONUS * depthBonus(depth) : 0;
    }

    private int check(Player player) {
        return player.isInCheck() ? CHECK_BONUS : 0;
    }

    private int pieceScore(final Player player) {
        int score = 0;
        for (final Piece piece : player.getActivePieces()) {
            score += piece.getPieceValue() + (piecePositionBonus(piece));
        }
        return score;
    }
    private int piecePositionBonus(final Piece piece) {
        int piecePosition = piece.getPiecePosition();
        if (!piece.getPieceAlliance().isWhite()) {
            piecePosition = (piecePosition + 56) - (piecePosition / 8) * 16;
        }
        switch (piece.getPieceType()) {
            case KING -> {
                return BoardUtils.kingPositionBonus[piecePosition];
            }
            case PAWN -> {
                return BoardUtils.pawnPositionBonus[piecePosition];
            }
            case ROOK -> {
                return BoardUtils.rookPositionBonus[piecePosition];
            }
            case BISHOP -> {
                return BoardUtils.bishopPositionBonus[piecePosition];
            }
            case KNIGHT -> {
                return BoardUtils.knightPositionBonus[piecePosition];
            }
            case QUEEN -> {
                return BoardUtils.queenPositionBonus[piecePosition];
            }
        }
        throw new RuntimeException("Unable to reach");
    }
    //TODO
    private int forceKingToCornerEndgameEval() {
        return -1;
    }
}
