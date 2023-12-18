package engine.player.ai;

import engine.board.Board;

public interface BoardEvaluator {
    int evaluate(final Board board, final int depth);
}
