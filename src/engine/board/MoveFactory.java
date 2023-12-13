package engine.board;

public class MoveFactory {
    private MoveFactory() {
        throw new RuntimeException("Impossible to instantiate");
    }
    public static Move createMove(final Board board, final int currentCoordinate, final int targetCoordinate) {
        for (final Move move: board.getAllLegalMoves()) {
            if (move.getCurrentCoordinate() == currentCoordinate && move.getTargetCoordinate() == targetCoordinate) {
                return move;
            }
        }
        return Move.NULL_MOVE;
    }
    public static Move createNullMove() {
        return new Move.NullMove();
    }
}
