package engine.board;


import engine.piece.Pawn;
import engine.piece.Piece;
import engine.player.player.BlackPlayer;
import engine.player.player.Player;
import engine.player.player.WhitePlayer;

import java.util.*;
public class Board {
    private final List<Tile> gameBoard;
    private final Collection<Piece> whiteActivePieces;
    private final Collection<Piece> blackActivePieces;
    protected final WhitePlayer whitePlayer;
    protected final BlackPlayer blackPlayer;
    private final Pawn enPassantPawn;
    private final Player currentPlayer;
    private static boolean whiteKingFirstMoveCheck = true;
    private static boolean blackKingFirstMoveCheck = true;

    private Board(final Builder builder) {
        this.gameBoard = createGameBoard(builder);
        this.whiteActivePieces = getPieces(gameBoard,Alliance.WHITE);
        this.blackActivePieces = getPieces(gameBoard,Alliance.BLACK);
        this.enPassantPawn = builder.getEnPassantPawn();
        Collection<Move> whiteLegalMoves = getLegalMoves(this.whiteActivePieces);
        Collection<Move> blackLegalMoves = getLegalMoves(this.blackActivePieces);
        this.whitePlayer = new WhitePlayer(this,whiteLegalMoves,blackLegalMoves);
        this.blackPlayer = new BlackPlayer(this,whiteLegalMoves,blackLegalMoves);
        setKingFirstMoveCheck();
        this.currentPlayer = builder.nextMoveMaker().choosePlayer(this.whitePlayer,this.blackPlayer);
    }
    public void setKingFirstMoveCheck() {
        if (this.whitePlayer.getPlayerKing().getPiecePosition() != 60) {
            whiteKingFirstMoveCheck = false;
        }
        if (this.blackPlayer.getPlayerKing().getPiecePosition() != 4) {
            blackKingFirstMoveCheck = false;
        }
    }
    public boolean isWhiteKingFirstMove() {
        return whiteKingFirstMoveCheck;
    }
    public boolean isBlackKingFirstMoveCheck() {
        return blackKingFirstMoveCheck;
    }
    public void resetKingFirstMove() {
        whiteKingFirstMoveCheck = true;
        blackKingFirstMoveCheck = true;
    }

    public Collection<Piece> getAllPieces() {
        final Collection<Piece> allPieces = new ArrayList<>();
        allPieces.addAll(whiteActivePieces);
        allPieces.addAll(blackActivePieces);
        return Collections.unmodifiableCollection(allPieces);
    }
    public Collection<Piece> getWhiteActivePieces() {
        return whiteActivePieces;
    }

    public Collection<Piece> getBlackActivePieces() {
        return blackActivePieces;
    }

    public WhitePlayer getWhitePlayer() {
        return whitePlayer;
    }

    public BlackPlayer getBlackPlayer() {
        return blackPlayer;
    }
    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    public Pawn getEnPassantPawn() {
        return enPassantPawn;
    }

    public Collection<Move> getAllLegalMoves() {
        Collection<Move> allLegalMoves =(this.currentPlayer.getLegalMoves());
        allLegalMoves.addAll(this.currentPlayer.getOpponent().getLegalMoves());
        return allLegalMoves;
    }

    /*Get legal moves for each type of engine.piece(white or black)*/
    private Collection<Move> getLegalMoves(Collection<Piece> pieces) {
        List<Move> legalMove = new ArrayList<>();
        for (final Piece piece :pieces) {
            legalMove.addAll(piece.getLegalMoves(this));
        }
        return legalMove;
    }
    /*
    Get all pieces of the specific alliance
     */
    private Collection<Piece> getPieces(final List<Tile> gameBoard, final Alliance alliance) {
        List<Piece> pieces = new ArrayList<>();
        for (Tile tile: gameBoard) {
            if (tile.isOccupied()) {
                final Piece pieceOnTile = tile.getPiece();
                if (pieceOnTile.getPieceAlliance() == alliance) {
                    pieces.add(pieceOnTile);
                }
            }
        }
        return pieces;
    }
    public static List<Tile> createGameBoard(Builder builder) {
        final Tile[] tile = new Tile[BoardUtils.NUM_TILES];
        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            tile[i] = Tile.createTile(i,builder.boardConfig.get(i));
        }
        return List.of(tile);
    }
    public static Board initBoard() {
        Builder builder = FenUtility.loadPosition(FenUtility.startFen);
        builder.setMoveMaker(Alliance.WHITE);
        return builder.build();
    }
    public static Board initBoard(String fen) {
        Builder builder = FenUtility.loadPosition(fen);
        builder.setMoveMaker(Alliance.WHITE);
        return builder.build();
    }
    public boolean isGameOverScenario() {
        //TODO draw by repetition
        return this.getCurrentPlayer().getOpponent().isInCheckMate() || this.getCurrentPlayer().getOpponent().isInStaleMate();
    }

    public Tile getTile(int coordinate) {
        return gameBoard.get(coordinate);
    }
    @Override
    public String toString() {
        StringBuilder board = new StringBuilder();
        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            final String tileString = this.gameBoard.get(i).toString();
            board.append(String.format("%3s",tileString));
            if ((i+1) % BoardUtils.NUM_TILES_PER_COLUMN == 0) {
                board.append("\n");
            }
        }
        return board.toString();
    }
    /*
    Builder pattern for Board class
    The Builder class put the engine.piece on its position on the engine.board
    Also, it declares which engine.player turn it is
     */

    public static class Builder {
        Map<Integer,Piece> boardConfig;
        Alliance nextMove;
        Pawn enPassantPawn;

        public Pawn getEnPassantPawn() {
            return enPassantPawn;
        }

        public void setEnPassantPawn(Pawn enPassantPawn) {
            this.enPassantPawn = enPassantPawn;
        }

        public Builder() {
            boardConfig = new HashMap<>();
        }
        public Builder setPiece(final Piece piece) {
            this.boardConfig.put(piece.getPiecePosition(),piece);
            return this;
        }
        public Builder setMoveMaker(final Alliance alliance) {
            this.nextMove = alliance;
            return this;
        }
        public Alliance nextMoveMaker() {
            return this.nextMove;
        }
        public Board build() {
            return new Board(this);
        }
    }
    
}
