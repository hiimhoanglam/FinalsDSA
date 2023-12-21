package engine.player;

import engine.board.Alliance;
import engine.board.Board;
import engine.board.BoardUtils;
import engine.board.Move;
import engine.piece.Piece;
import engine.piece.Rook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class BlackPlayer extends Player{
    public BlackPlayer(final Board board, final Collection<Move> whiteLegalMoves, final Collection<Move> blackLegalMoves) {
        super(board, blackLegalMoves, whiteLegalMoves);
    }
    @Override
    public Collection<Move> calculateKingCastle(final Collection<Move> playerLegalMoves, final Collection<Move> opponentLegalMoves) {
        final int rookShort = 7;
        final int rookLong = 0;
        final Collection<Move> legalCastle = new ArrayList<>();
        if (this.kingFirstMove  && !this.isInCheck) {
            /*King side castle*/
            if (this.board.getTile(rookShort).isOccupied() && this.board.getTile(rookShort).getPiece().getPieceType() == Piece.PieceType.ROOK
                    && this.board.getTile(rookShort).getPiece().firstMoveCheck.isFirstMove()) {
                if (!this.board.getTile(5).isOccupied()
                        && !this.board.getTile(6).isOccupied()) {
                    if (calculateAttackMovesOnTile(5, opponentLegalMoves).isEmpty()
                            && calculateAttackMovesOnTile(6, opponentLegalMoves).isEmpty()) {
                        legalCastle.add(new Move.CastleKingSide(board,getPlayerKing(),6,(Rook)this.board.getTile(rookShort).getPiece()));
                    }
                }
            }
            /*Queen side castle*/
            if (this.board.getTile(rookLong).isOccupied() && this.board.getTile(rookLong).getPiece().getPieceType() == Piece.PieceType.ROOK
                    && this.board.getTile(rookLong).getPiece().firstMoveCheck.isFirstMove()) {
                if (!this.board.getTile(1).isOccupied()
                        && !this.board.getTile(2).isOccupied()
                        && !this.board.getTile(3).isOccupied()) {
                    if (calculateAttackMovesOnTile(1,opponentLegalMoves).isEmpty()
                            && calculateAttackMovesOnTile(2,opponentLegalMoves).isEmpty()
                            && calculateAttackMovesOnTile(3,opponentLegalMoves).isEmpty()) {
                        legalCastle.add(new Move.CastleQueenSide(board,getPlayerKing(),2,(Rook)this.board.getTile(rookLong).getPiece()));
                    }
                }
            }
        }
        return legalCastle;
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return board.getBlackActivePieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.BLACK;
    }

    @Override
    public Player getOpponent() {
        return board.getWhitePlayer();
    }

    @Override
    public String toString() {
        return Alliance.BLACK.toString();
    }
}
