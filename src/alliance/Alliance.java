package alliance;

public enum Alliance {


    BLACK() {
        public boolean isBlack() {
            return true;
        }
        public boolean isWhite() {
            return false;
        }
        public int getDirection() {
            return UP_Direction;
        }
    },
    WHITE() {
        public boolean isBlack() {
            return false;
        }
        public boolean isWhite() {
            return true;
        }
        public int getDirection() {
            return DOWN_Direction;
        }

    };

    public abstract boolean isBlack();

    public abstract boolean isWhite();

    public abstract int getDirection();

    private static final int UP_Direction = 1;

    private static final int DOWN_Direction = -1;
}
