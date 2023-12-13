package engine.board;

public class MoveFactory {
    private MoveFactory() {
        throw new RuntimeException("Impossible to instantiate");
    }
    public static Move moveFactory(final Board board, final int targetCoordinate, final int currentCoordinate) {
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
