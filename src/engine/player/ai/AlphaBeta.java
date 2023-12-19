package engine.player.ai;

import engine.board.Board;
import engine.board.Move;

public class AlphaBeta implements MoveStrategy{
    @Override
    public long getNumBoardsEvaluated() {
        return 0;
    }

    @Override
    public Move execute(Board board) {
        return null;
    }
    //TODO
    public int min(final Board board, final int depth, final int alpha, final int beta) {
        return 0;
    }
    public int max(final Board board, final int depth, final int alpha, final int beta) {
        return 0;
    }
}
