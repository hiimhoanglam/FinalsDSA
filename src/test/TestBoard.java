package test;

import engine.board.*;
import engine.piece.Piece;
import engine.player.ai.AlphaBeta;
import engine.player.ai.Minimax;
import engine.player.ai.MoveStrategy;
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
    @Test
    public void testFoolsMate() {

        final Board board = Board.initBoard();
        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("f2"),
                        BoardUtils.getCoordinateAtPosition("f3")));

        assertTrue(t1.getMoveStatus().isDone());

        final MoveTransition t2 = t1.getBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t1.getBoard(), BoardUtils.getCoordinateAtPosition("e7"),
                        BoardUtils.getCoordinateAtPosition("e5")));

        assertTrue(t2.getMoveStatus().isDone());

        final MoveTransition t3 = t2.getBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t2.getBoard(), BoardUtils.getCoordinateAtPosition("g2"),
                        BoardUtils.getCoordinateAtPosition("g4")));

        assertTrue(t3.getMoveStatus().isDone());

        final MoveStrategy strategy = new AlphaBeta(4);
        final Move aiMove = strategy.execute(t3.getBoard());
        final Move bestMove = MoveFactory.createMove(t3.getBoard(),BoardUtils.getCoordinateAtPosition("d8"),
                BoardUtils.getCoordinateAtPosition("h4"));
        assertEquals(aiMove,bestMove);
    }
}