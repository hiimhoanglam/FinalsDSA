package engine.player.ai;

import engine.board.Alliance;
import engine.board.Board;
import engine.board.Move;
import engine.board.MoveTransition;
import engine.player.player.Player;

import java.util.Collection;
import java.util.List;

public class AlphaBeta implements MoveStrategy{
    final BoardEvaluator boardEvaluator;
    final int searchDepth;
    int boardsEvaluated;

    public AlphaBeta(final int searchDepth) {
        this.boardEvaluator = StandardBoardEvaluator.getInstance();
        this.searchDepth = searchDepth;
    }
    private Collection<Move> sort(Collection<Move> moves) {
        List<Move> result = (List<Move>) moves;
        result.sort(new MoveCompare());
        return result;
    }

    @Override
    public long getNumBoardsEvaluated() {
        return this.boardsEvaluated;
    }
    /*
    Alliance: Type of player: White/Black
     */
    @Override
    public Move execute(final Board board) {
        final long startTime = System.currentTimeMillis();
        final Player currentPlayer = board.getCurrentPlayer();
        final Alliance alliance = currentPlayer.getAlliance();
        Move bestMove = null;
        /*
        Set the highestValue = - infinity
        Set the lowestValue = + infinity
         */
        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentValue;
        System.out.println(board.getCurrentPlayer() + " THINKING with depth = " + this.searchDepth);
        final Collection<Move> sortedMoves = sort(board.getCurrentPlayer().getLegalMoves());
        for (final Move move : sortedMoves) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                currentValue = alliance.isWhite() ?
                        min(moveTransition.getBoard(), this.searchDepth - 1, highestSeenValue, lowestSeenValue) :
                        max(moveTransition.getBoard(), this.searchDepth - 1, highestSeenValue, lowestSeenValue);
                if (alliance.isWhite() && currentValue > highestSeenValue) {
                    highestSeenValue = currentValue;
                    bestMove = move;
                }
                else if (!alliance.isWhite() && currentValue < lowestSeenValue) {
                    lowestSeenValue = currentValue;
                    bestMove = move;

                }

            }
        }
        final long runTime = System.currentTimeMillis() - startTime;
        System.out.println("Evaluation took " + runTime + " ms");
        return bestMove;
    }
    //Min call Max and Max call Min
    public int max(final Board board,
                   final int depth,
                   final int highest,
                   final int lowest) {
        if (depth == 0 || board.isGameOverScenario()) {
            this.boardsEvaluated++;
            return this.boardEvaluator.evaluate(board, depth);
        }
        int currentHighest = highest;
        final Collection<Move> sortedMoves = sort(board.getCurrentPlayer().getLegalMoves());
        for (final Move move : sortedMoves) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                currentHighest = Math.max(currentHighest, min(moveTransition.getBoard(), depth - 1, currentHighest, lowest));
                if (lowest <= currentHighest) {
                    break;
                }
            }
        }
        return currentHighest;
    }

    public int min(final Board board,
                   final int depth,
                   final int highest,
                   final int lowest) {
        if (depth == 0 || board.isGameOverScenario()) {
            this.boardsEvaluated++;
            return this.boardEvaluator.evaluate(board, depth);
        }
        int currentLowest = lowest;
        final Collection<Move> sortedMoves = sort(board.getCurrentPlayer().getLegalMoves());
        for (final Move move : sortedMoves) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                currentLowest = Math.min(currentLowest, max(moveTransition.getBoard(), depth -1 , highest, currentLowest));
                if (currentLowest <= highest) {
                    break;
                }
            }
        }
        return currentLowest;
    }

    @Override
    public String toString() {
        return "AB - MO";
    }
}
