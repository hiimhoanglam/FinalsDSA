package engine.player.ai;

import engine.board.Board;
import engine.piece.Piece;
import engine.player.Player;

public class StandardBoardEvaluator implements BoardEvaluator {
    private static volatile StandardBoardEvaluator instance;
    private static final int CASTLE_BONUS = 60;
    private static final int CHECKMATE_BONUS = 10000;
    private static final int CHECK_BONUS = 70;
    private static final int DEPTH_BONUS = 100;

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
        return pieceScore(player) + check(player) + checkmate(player,depth) + castled(player) + mobility(player);
        //checkmate, check, castled, pawns structure, mobility
    }

    private int mobility(Player player) {
        return player.getLegalMoves().size();
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
            score += piece.getPieceValue();
        }
        return score;
    }
}
