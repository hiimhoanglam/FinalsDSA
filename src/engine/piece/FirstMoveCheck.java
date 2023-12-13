package engine.piece;
/*
Composition in OOP
Only the king and the rook and the pawn need to check for the first move
In that case, only those pieces have to contain an instance of the FirstMoveCheck
 */
public class FirstMoveCheck {
    protected boolean isFirstMove = true;
    public boolean isFirstMove() {
        return isFirstMove;
    }

    public void setFirstMove(boolean firstMove) {
        isFirstMove = firstMove;
    }
}
