package engine.board;

import java.util.*;

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
    /*
    Algebraic notation means a8 corresponds to 0,b8 corresponds to 1, etc...
    For each algebraic notation, there is a position that corresponds. For example, h1 corresponds to 63
     */
    public static final List<String> ALGEBRAIC_NOTATION = initializeAlgebraicNotation();
    public static final Map<String,Integer> POSITION_TO_COORDINATE = initializePositionToCoordinateMap();
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
    private static List<String> initializeAlgebraicNotation() {
        return Collections.unmodifiableList(Arrays.asList(
                "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
                "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
                "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
                "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
                "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"
        ));
    }
    private static Map<String, Integer> initializePositionToCoordinateMap() {
        final Map<String,Integer> map = new HashMap<>();
        for (int i = 0; i < NUM_TILES; i++) {
            map.put(ALGEBRAIC_NOTATION.get(i), i);
        }
        return Collections.unmodifiableMap(map);
    }
    public static String getPositionAtCoordinate(final int coordinate) {
        return ALGEBRAIC_NOTATION.get(coordinate);
    }
    public static int getCoordinateAtPosition(final String position) {
        return POSITION_TO_COORDINATE.get(position);
    }
}
