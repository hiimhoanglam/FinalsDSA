package board;

public class BoardUtils {
    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] SECOND_COLUMN = initColumn(1);
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);
    public static final boolean[] EIGHTH_COLUMN = initColumn(7);
    public static final int NUM_TILES = 64;
    public static final int NUM_TILES_PER_COLUMN = 8;
    private BoardUtils() {
        throw new RuntimeException("Do not instantiate");
    }
    public static boolean isValidCoordinate(int coordinate) {
        return coordinate >= 0 && coordinate < 64;
    }
    /*
    An initialize method for the boolean arrays of column that can produce bugs when generating legal moves
     */
    private static boolean[] initColumn(int columnNumber) {
        boolean[] column = new boolean[NUM_TILES];
        for (int col = columnNumber; col < NUM_TILES; col += NUM_TILES_PER_COLUMN) {
            column[col] = true;
        }
        return column;
    }
}
