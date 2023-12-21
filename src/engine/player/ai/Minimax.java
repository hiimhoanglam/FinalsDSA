package engine.player.ai;

import engine.board.Board;
import engine.board.Move;
import engine.board.MoveTransition;
public class Minimax implements MoveStrategy{
    final BoardEvaluator boardEvaluator;
    final int searchDepth;
    int boardsEvaluated;

    public Minimax(final int searchDepth) {
        this.boardEvaluator = StandardBoardEvaluator.getInstance();
        this.boardsEvaluated = 0;
        this.searchDepth = searchDepth;
    }

    @Override
    public long getNumBoardsEvaluated() {
        return this.boardsEvaluated;
    }

    @Override
    public Move execute(Board board) {
        final long startTime = System.currentTimeMillis();
        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentEvaluation;
        System.out.println(board.getCurrentPlayer() + " ANALYZING with depth " + searchDepth);
        Move bestMove = null;
//        final int numMoves = board.getCurrentPlayer().getLegalMoves().size();
        for (final Move move: board.getCurrentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                currentEvaluation = board.getCurrentPlayer().getAlliance().isWhite()
                        ? min(moveTransition.getBoard(),this.searchDepth-1)
                        : max(moveTransition.getBoard(),this.searchDepth-1);
                if (board.getCurrentPlayer().getAlliance().isWhite() && currentEvaluation >= highestSeenValue) {
                    highestSeenValue = currentEvaluation;
                    bestMove = move;
                }
                else if (!board.getCurrentPlayer().getAlliance().isWhite() && currentEvaluation <= lowestSeenValue){
                    lowestSeenValue = currentEvaluation;
                    bestMove = move;
                }
            }
        }
        final long runTime = System.currentTimeMillis() - startTime;
        System.out.println("Evaluation took " + runTime + " ms");
        return bestMove;
    }
    public int min(final Board board, final int depth) {
        if (depth == 0 || board.isGameOverScenario()) {
            this.boardsEvaluated++;
            return this.boardEvaluator.evaluate(board,depth);
        }
        int currentSmallestValue = Integer.MAX_VALUE;
        for (final Move move: board.getCurrentPlayer().getLegalMoves()) {
            MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                final int currentValue = max(moveTransition.getBoard(),depth-1);
                if (currentValue <= currentSmallestValue) {
                    currentSmallestValue = currentValue;
                }
            }
        }
        return currentSmallestValue;
    }
    public int max(final Board board, final int depth) {
        if (depth == 0 || board.isGameOverScenario()) {
            this.boardsEvaluated++;
            return this.boardEvaluator.evaluate(board,depth);
        }
        int currentHighestValue = Integer.MIN_VALUE;
        for (final Move move: board.getCurrentPlayer().getLegalMoves()) {
            MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                final int currentValue = min(moveTransition.getBoard(),depth-1);
                if (currentValue >= currentHighestValue) {
                    currentHighestValue = currentValue;
                }
            }
        }
        return currentHighestValue;
    }


    @Override
    public String toString() {
        return "Minimax";
    }
}
