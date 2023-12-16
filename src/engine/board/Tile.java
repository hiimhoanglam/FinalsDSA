package engine.board;

import engine.piece.Piece;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import static engine.board.BoardUtils.NUM_TILES;

/*
Coordinate are protected so that only subclasses can access it
EMPTY_TILES are all the tiles available in the engine.board(indexed from 0-63). In addition, the list of tiles cannot be mutated
There is a factory for creating a tile based on the engine.piece available so that there is no dependencies on constructor
=> Better caching
=> Reduce garbage collection
=> Reduce memory footprint
=> => The use of immutable object(in this case - Tile class)
 */
public abstract class Tile {
    protected final int coordinate;
    private static final Map<Integer, Tile> EMPTY_TILES = createAllPossibleEmptyTiles();

    private static Map<Integer, Tile> createAllPossibleEmptyTiles() {
        final Map<Integer, Tile> emptyTileMap = new HashMap<>();
        for (int i = 0; i < NUM_TILES; i++) {
            emptyTileMap.put(i,new EmptyTile(i));
        }
        return Collections.unmodifiableMap(emptyTileMap);
    }
    public static Tile createTile(int coordinate, Piece piece) {
        return piece != null ? new OccupiedTile(coordinate,piece) : new EmptyTile(coordinate);
    }

    private Tile(int coordinate) {
        this.coordinate = coordinate;
    }
    public abstract boolean isOccupied();
    public abstract Piece getPiece();

    public int getCoordinate() {
        return this.coordinate;
    }

    public static final class EmptyTile extends Tile {
        public EmptyTile(final int coordinate) {
            super(coordinate);
        }

        @Override
        public boolean isOccupied() {
            return false;
        }

        @Override
        public Piece getPiece() {
            return null;
        }

        @Override
        public String toString() {
            return "-";
        }
    }
    public static final class OccupiedTile extends Tile {
        private final Piece pieceOnTile;
        private OccupiedTile(int coordinate, final Piece pieceOnTile) {
            super(coordinate);
            this.pieceOnTile = pieceOnTile;
        }
        @Override
        public boolean isOccupied() {
            return true;
        }
        @Override
        public Piece getPiece() {
            return pieceOnTile;
        }
        /*
        If the engine.piece is of the white engine.player, the string will be uppercase
        If the engine.piece is of the black engine.player, the string will be lowercase
         */
        @Override
        public String toString() {
            return !getPiece().getPieceAlliance().isWhite() ? getPiece().toString().toLowerCase() : getPiece().toString();
        }
    }
}
