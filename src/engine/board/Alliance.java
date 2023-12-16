package engine.board;

import engine.player.BlackPlayer;
import engine.player.Player;
import engine.player.WhitePlayer;

public enum Alliance {
    WHITE {
        @Override
        public int getDirection() {
            return -1;
        }

        @Override
        public boolean isWhite() {
            return true;
        }

        @Override
        public int getOppositeDirection() {
            return 1;
        }

        @Override
        public boolean getPromotionSquare(int position) {
            return BoardUtils.EIGHTH_RANK[position];
        }

        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer) {
            return whitePlayer;
        }
    },
    BLACK {
        @Override
        public int getDirection() {
            return 1;
        }

        @Override
        public boolean isWhite() {
            return false;
        }

        @Override
        public int getOppositeDirection() {
            return -1;
        }

        @Override
        public boolean getPromotionSquare(int position) {
            return BoardUtils.FIRST_RANK[position];
        }

        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer) {
            return blackPlayer;
        }
    };

    public abstract int getDirection();
    public abstract boolean isWhite();
    public abstract int getOppositeDirection();
    public abstract boolean getPromotionSquare(int position);

    public abstract Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer);
}
