package engine.board;

public class BoardUtils {
    public static final boolean[] FIRST_FILE = initColumn(0);
    public static final boolean[] SECOND_FILE = initColumn(1);
    public static final boolean[] SEVENTH_FILE = initColumn(6);
    public static final boolean[] EIGHTH_FILE = initColumn(7);
    public static final int NUM_TILES = 64;
    public static final int NUM_TILES_PER_COLUMN = 8;
    public static final boolean[] EIGHTH_RANK = initRow(0);
    public static final boolean[] SEVENTH_RANK = initRow(1);
    public static final boolean[] SIXTH_RANK = initRow(2);
    public static final boolean[] FIFTH_RANK = initRow(3);
    public static final boolean[] FOURTH_RANK = initRow(4);
    public static final boolean[] THIRD_RANK = initRow(5);
    public static final boolean[] SECOND_RANK = initRow(6);
    public static final boolean[] FIRST_RANK = initRow(7);


    private BoardUtils() {
        throw new RuntimeException("Do not instantiate");
    }
    public static boolean isValidCoordinate(int coordinate) {
        return coordinate >= 0 && coordinate < 64;
    }
    /*
    An initialize method for the boolean arrays of columns that can produce bugs when generating legal moves
     */
    private static boolean[] initColumn(int columnNumber) {
        boolean[] column = new boolean[NUM_TILES];
        for (int col = columnNumber; col < NUM_TILES; col += NUM_TILES_PER_COLUMN) {
            column[col] = true;
        }
        return column;
    }
    /*
    An initialize method for the boolean arrays of rows that can produce bugs when generating legal moves
     */
    private static boolean[] initRow(int rowNumber) {
        boolean[] row = new boolean[NUM_TILES];
        final int startingIndex = rowNumber * NUM_TILES_PER_COLUMN;
        for (int rowIndex = startingIndex; rowIndex < startingIndex + NUM_TILES_PER_COLUMN; rowIndex++) {
            row[rowIndex] = true;
        }
        return row;
    }
}
