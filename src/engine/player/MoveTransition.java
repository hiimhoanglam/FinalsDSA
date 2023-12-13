package engine.player;

import engine.board.Board;
import engine.board.Move;

public class MoveTransition {
    /*
    A class to transition from one position to another position after making a move(whether it is legal or not)
     */
    final Board transitionBoard;
    final Move transitionMove;
    final MoveStatus moveStatus;

    public MoveTransition(Board transitionBoard, Move transitionMove, MoveStatus moveStatus) {
        this.transitionBoard = transitionBoard;
        this.transitionMove = transitionMove;
        this.moveStatus = moveStatus;
    }

    public MoveStatus getMoveStatus() {
        return this.moveStatus;
    }

    public Board getBoard() {
        return this.transitionBoard;
    }
}
