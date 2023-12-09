package board;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The BoardUtils class provides utility methods and constants related to the chessboard.
 */
public class BoardUtils {
    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] EIGHT_COLUMN = initColumn(7);
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);
    public static final boolean[] SECOND_COLUMN = initColumn(1);

    public static final boolean[] SECOND_ROW = null;

    public static final boolean[] SEVENTH_ROW = null;

    public static final int NUM_TILES_PER_ROW = 8;
    public static final int NUM_TILES = 64;

    /**
     * Private constructor to prevent instantiation of the class.
     */
    private BoardUtils() {
        throw new RuntimeException("You can't instantiate me");
    }

    /**
     * Initializes a boolean array representing a column on the chessboard.
     * @param columnNumber The number of the column to initialize.
     * @return A boolean array representing the specified column.
     */
    private static boolean[] initColumn(int columnNumber) {
        final boolean[] column = new boolean[NUM_TILES];
        do {
            column[columnNumber] = true;
            columnNumber += NUM_TILES_PER_ROW;
        } while (columnNumber < NUM_TILES);
        return column;
    }

    /**
     * Checks if a tile coordinate is valid on the chessboard.
     * @param coordinate The coordinate to check.
     * @return True if the coordinate is valid, false otherwise.
     */
    public static boolean isValidTileCoordinate(final int coordinate) {
        return coordinate >= 0 && coordinate < NUM_TILES;
    }
}
