package engine.player;

public enum MoveStatus {
    DONE {
        @Override
        boolean isDone() {
            return true;
        }
    },
    ILLEGAL_MOVES {
        @Override
        boolean isDone() {
            return false;
        }
    },
    PLAYER_IN_CHECK {
        @Override
        boolean isDone() {
            return false;
        }
    };

    abstract boolean isDone();
}
