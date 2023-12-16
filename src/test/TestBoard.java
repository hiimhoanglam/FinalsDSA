package test;

import engine.board.Board;
import engine.board.Move;
import engine.piece.Piece;
import org.junit.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class TestBoard {
    @Test
    public void initialBoard() {
        final Board board = Board.initBoard();
        assertEquals(board.getCurrentPlayer().getLegalMoves().size(), 20);
        assertEquals(board.getCurrentPlayer().getOpponent().getLegalMoves().size(), 20);
        assertFalse(board.getCurrentPlayer().isInCheck());
        assertFalse(board.getCurrentPlayer().isInCheckMate());
        assertFalse(board.getCurrentPlayer().isCastled());
//        assertTrue(board.getCurrentPlayer().isKingSideCastleCapable());
//        assertTrue(board.getCurrentPlayer().isQueenSideCastleCapable());
        assertEquals(board.getCurrentPlayer(), board.getWhitePlayer());
        assertEquals(board.getCurrentPlayer().getOpponent(), board.getBlackPlayer());
        assertFalse(board.getCurrentPlayer().getOpponent().isInCheck());
        assertFalse(board.getCurrentPlayer().getOpponent().isInCheckMate());
        assertFalse(board.getCurrentPlayer().getOpponent().isCastled());
        final Collection<Piece> allPieces = board.getAllPieces();
        final Collection<Move> allMoves = board.getAllLegalMoves();
        for(final Move move : allMoves) {
            assertFalse(move.isAttack());
            assertFalse(move.isCastlingMove());
        }
        assertEquals(allMoves.size(), 40);
        assertEquals(allPieces.size(), 32);
//        assertTrue(board.getCurrentPlayer().getOpponent().isKingSideCastleCapable());
//        assertTrue(board.getCurrentPlayer().getOpponent().isQueenSideCastleCapable());
//        assertEquals("White", board.getWhitePlayer().toString());
//        assertEquals("Black", board.getBlackPlayer().toString());
    }
}