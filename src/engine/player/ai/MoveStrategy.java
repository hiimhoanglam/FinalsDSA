package engine.player.ai;

import engine.board.Board;
import engine.board.Move;

public interface MoveStrategy {
    long getNumBoardsEvaluated();
    Move execute(final Board board);
}
