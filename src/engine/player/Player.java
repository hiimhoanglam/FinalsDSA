package engine.player;

import engine.board.Alliance;
import engine.board.Board;
import engine.board.Move;
import engine.piece.King;
import engine.piece.Piece;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class Player {
    protected final Board board;
    protected Collection<Move> legalMoves;
    protected Collection<Move> opponentMoves;
    protected final King playerKing;
    protected final boolean isInCheck;

    public Player(Board board, Collection<Move> playerLegalMoves, Collection<Move> opponentMoves) {
        this.board = board;
        this.playerKing = establishKing();
        playerLegalMoves.addAll(calculateKingCastle(playerLegalMoves,opponentMoves));
        this.legalMoves = playerLegalMoves;
        this.opponentMoves = opponentMoves;
        this.isInCheck = !calculateAttackMovesOnTile(this.playerKing.getPiecePosition(),opponentMoves).isEmpty();
    }

    protected static Collection<Move> calculateAttackMovesOnTile(int coordinate,Collection<Move> opponentMoves) {
        List<Move> attackMoves = new ArrayList<>();
        for (Move move: opponentMoves) {
            if (move.getTargetCoordinate() == coordinate) {
                attackMoves.add(move);
            }
        }
        return Collections.unmodifiableCollection(attackMoves);
    }
    public Collection<Move> getEscapeMoves() {
        Collection<Move> escapeMoves = new ArrayList<>();
        for (final Move move: legalMoves) {
            final MoveTransition moveTransition = makeMove(move);
            if (moveTransition.moveStatus == MoveStatus.DONE) {
                escapeMoves.add(move);
            }
        }
        return Collections.unmodifiableCollection(escapeMoves);
    }
    protected boolean hasEscapeMoves() {
        for (final Move move: legalMoves) {
            final MoveTransition moveTransition = makeMove(move);
            if (moveTransition.moveStatus == MoveStatus.DONE) {
                return true;
            }
        }
        return false;
    }
    private King establishKing() {
        for (final Piece piece: getActivePieces()) {
            if (piece.isKing()) {
                return (King)piece;
            }
        }
        throw new RuntimeException("Impossible to achieve");
    }
    public abstract Collection<Piece> getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();

    public King getPlayerKing() {
        return playerKing;
    }

    public Collection<Move> getLegalMoves() {
        return legalMoves;
    }
    protected abstract Collection<Move> calculateKingCastle(Collection<Move> playerLegalMoves, Collection<Move> opponentLegalMoves);

    /* TODO IMPLEMENT ABOVE METHODS */
    public boolean isInCheck() {
        return isInCheck;
    }
    public boolean isInCheckMate() {
        return isInCheck && !hasEscapeMoves();
    }
    public boolean isInStaleMate() {
        return false;
    }
    public boolean isCastled() {
        return false;
    }
    public boolean isLegalMove(Move move) {
        return legalMoves.contains(move);
    }
    public MoveTransition makeMove(final Move move) {
        if (!isLegalMove(move)) {
            return new MoveTransition(this.board,move,MoveStatus.ILLEGAL_MOVES);
        }
        /*
        Dealing with a move that reveals an attack on the King(discover attack) by the engine.player(pinned pieces)
         */
        Board transitionBoard = move.execute();
        final Collection<Move> kingAttacks = Player.calculateAttackMovesOnTile(
                transitionBoard.getCurrentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
                transitionBoard.getCurrentPlayer().getLegalMoves());

        if (!kingAttacks.isEmpty()) {
                return new MoveTransition(this.board,move,MoveStatus.PLAYER_IN_CHECK);
        }
        return new MoveTransition(transitionBoard,move,MoveStatus.DONE);
    }

}
