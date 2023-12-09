package board;

import pieces.Piece;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

/**
 * The Tile class represents a square on the chessboard.
 */
public abstract class Tile {
    protected final int tileCoordinate;

    // A map to store all possible instances of EmptyTile to optimize memory usage
    private static final Map<Integer, EmptyTile> EMPTY_TILES = createAllPossibleEmptyTile();

    /**
     * Creates all possible instances of EmptyTile and stores them in a map.
     * @return An unmodifiable map of EmptyTile instances.
     */
    private static Map<Integer, EmptyTile> createAllPossibleEmptyTile() {
        Map<Integer, EmptyTile> emptyTiles = new HashMap<>();
        for (int i = 0; i < 64; i++) {
            emptyTiles.put(i, new EmptyTile(i));
        }
        return Collections.unmodifiableMap(emptyTiles);
    }

    /**
     * Creates either an EmptyTile or an OccupiedTile based on the presence of a Piece.
     * @param tileCoordinate The coordinate of the tile.
     * @param piece The piece on the tile (can be null if the tile is empty).
     * @return An instance of Tile representing an empty or occupied square.
     */
    public static Tile createEmptyTile(final int tileCoordinate, final Piece piece) {
        return piece != null ? new OccupiedTile(tileCoordinate, piece) : EMPTY_TILES.get(tileCoordinate);
    }

    /**
     * Constructs a Tile with a specific coordinate.
     * @param tileCoordinate The coordinate of the tile.
     */
    public Tile(int tileCoordinate) {
        this.tileCoordinate = tileCoordinate;
    }

    /**
     * Checks if the tile is occupied by a piece.
     * @return True if the tile is occupied, false otherwise.
     */
    public abstract boolean isTiledOccupied();

    /**
     * Gets the piece on the tile.
     * @return The piece on the tile, or null if the tile is empty.
     */
    public abstract Piece getPiece();

    /**
     * A subclass of Tile representing an empty square on the chessboard.
     */
    public static final class EmptyTile extends Tile {
        /**
         * Constructs an EmptyTile with a specific coordinate.
         * @param tileCoordinate The coordinate of the tile.
         */
        public EmptyTile(int tileCoordinate) {
            super(tileCoordinate);
        }

        @Override
        public boolean isTiledOccupied() {
            return false;
        }

        @Override
        public Piece getPiece() {
            return null;
        }
    }

    /**
     * A subclass of Tile representing an occupied square on the chessboard.
     */
    public static final class OccupiedTile extends Tile {
        private final Piece pieceOnTile;

        /**
         * Constructs an OccupiedTile with a specific coordinate and a piece.
         * @param tileCoordinate The coordinate of the tile.
         * @param pieceOnTile The piece on the tile.
         */
        public OccupiedTile(int tileCoordinate, Piece pieceOnTile) {
            super(tileCoordinate);
            this.pieceOnTile = pieceOnTile;
        }

        @Override
        public boolean isTiledOccupied() {
            return true;
        }

        @Override
        public Piece getPiece() {
            return this.pieceOnTile;
        }
    }
}
