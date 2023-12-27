package engine.board;
import java.util.Random;

public class ZobristHashing {
    public final int ZOBRIST_SEED = 1;
    private long[] pieceKey = new long[768];
    private long[] enpassantKey = new long[64];
    private long[] castleKey = new long[16];
    private long sideToMove;
    private static volatile ZobristHashing instance;

    private ZobristHashing() {
        for (int i = 0; i < 768; i++) {
            pieceKey[i] = randomNumberGenerator();
        }
        for (int i = 0; i < 64; i++) {
            enpassantKey[i] = randomNumberGenerator();
        }
        for (int i = 0; i < 16; i++) {
            castleKey[i] = randomNumberGenerator();
        }
        sideToMove = randomNumberGenerator();
    }
    public static ZobristHashing getInstance() {
        ZobristHashing result = instance;
        if (result != null) {
            return result;
        }
        synchronized(ZobristHashing.class) {
            if (instance == null) {
                instance = new ZobristHashing();
            }
            return instance;
        }
    }

    public long randomNumberGenerator() {
        long min = 0L;
        long max = (long) Math.pow(2, 64);
        Random rnd = new Random();
        return rnd.nextLong() * (max - min) + min;
    }
}
