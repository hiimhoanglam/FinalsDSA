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
import static engine.board.Move.*;
public class WhitePlayer extends Player{
    public WhitePlayer(final Board board, final Collection<Move> whiteLegalMoves, final Collection<Move> blackLegalMoves) {
        super(board, whiteLegalMoves, blackLegalMoves);

    }
    /*
    To castle legally we need to check for:
    If the rook and the king are not already moved
    If the path between the rook and the king are not occupied
    If the path between the rook and the king are not attacked by an opponent engine.piece
     */
    @Override
    protected Collection<Move> calculateKingCastle(final Collection<Move> playerLegalMoves, final Collection<Move> opponentLegalMoves) {
        final int rookShort = BoardUtils.NUM_TILES - 1;
        final int rookLong = rookShort - 7;
        final Collection<Move> legalCastle = new ArrayList<>();
        if (getPlayerKing().firstMoveCheck.isFirstMove() && !this.isInCheck()) {
            /*King side castle*/
            if (this.board.getTile(rookShort).isOccupied() && this.board.getTile(rookShort).getPiece().getPieceType() == Piece.PieceType.ROOK
                    && this.board.getTile(rookShort).getPiece().firstMoveCheck.isFirstMove()) {
                if (!this.board.getTile(61).isOccupied()
                        && !this.board.getTile(62).isOccupied()) {
                    if (calculateAttackMovesOnTile(61, opponentLegalMoves).isEmpty()
                            && calculateAttackMovesOnTile(62, opponentLegalMoves).isEmpty()) {
                        legalCastle.add(new CastleKingSide(board,getPlayerKing(),62,(Rook)this.board.getTile(rookShort).getPiece()));
                    }
                }
            }
            /*Queen side castle*/
            if (this.board.getTile(rookLong).isOccupied() && this.board.getTile(rookShort).getPiece().getPieceType() == Piece.PieceType.ROOK
                    && this.board.getTile(rookShort).getPiece().firstMoveCheck.isFirstMove()) {
                if (this.board.getTile(57).isOccupied()
                        && this.board.getTile(58).isOccupied()
                        && this.board.getTile(59).isOccupied()) {
                    if (calculateAttackMovesOnTile(57,opponentLegalMoves).isEmpty()
                            && calculateAttackMovesOnTile(58,opponentLegalMoves).isEmpty()
                            && calculateAttackMovesOnTile(59,opponentLegalMoves).isEmpty()) {
                        legalCastle.add(new CastleQueenSide(board,getPlayerKing(),58,(Rook)this.board.getTile(rookLong).getPiece()));
                    }
                }
            }
        }
        return Collections.unmodifiableCollection(legalCastle);
    }
    @Override
    public Collection<Piece> getActivePieces() {
        return board.getWhiteActivePieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return board.getBlackPlayer();
    }



}
