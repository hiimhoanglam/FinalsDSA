package test;
import engine.board.*;
import engine.piece.Piece;
import engine.player.ai.Minimax;
import engine.player.ai.MoveStrategy;
import org.junit.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
public class TestMinimax {
    @Test
    public void testOpeningDepth1() {
        final Board board = Board.initBoard();
        final MoveStrategy minMax = new Minimax(1);
        minMax.execute(board);
        final long numBoardsEvaluated = minMax.getNumBoardsEvaluated();
        assertEquals(numBoardsEvaluated, 20L);
    }

    @Test
    public void testOpeningDepth2() {
        final Board board = Board.initBoard();
        final MoveStrategy minMax = new Minimax(2);
        minMax.execute(board);
        final long numBoardsEvaluated = minMax.getNumBoardsEvaluated();
        assertEquals(numBoardsEvaluated, 400L);
    }

    @Test
    public void testOpeningDepth3() {
        final Board board = Board.initBoard();
        final MoveStrategy minMax = new Minimax(3);
        minMax.execute(board);
        final long numBoardsEvaluated = minMax.getNumBoardsEvaluated();
        assertEquals(numBoardsEvaluated, 8902L);
    }

    @Test
    public void testOpeningDepth4() {
        final Board board = Board.initBoard();
        final MoveStrategy minMax = new Minimax(4);
        minMax.execute(board);
        final long numBoardsEvaluated = minMax.getNumBoardsEvaluated();
        assertEquals(numBoardsEvaluated, 197281L);
    }

    @Test
    public void testOpeningDepth5() {
        final Board board = Board.initBoard();
        final MoveStrategy minMax = new Minimax(5);
        minMax.execute(board);
        final long numBoardsEvaluated = minMax.getNumBoardsEvaluated();
        assertEquals(numBoardsEvaluated, 4865609L);
    }

    @Test
    public void testOpeningDepth6() {
        final Board board = Board.initBoard();
        final MoveStrategy minMax = new Minimax(6);
        minMax.execute(board);
        final long numBoardsEvaluated = minMax.getNumBoardsEvaluated();
        assertEquals(numBoardsEvaluated, 119060324L);
    }
    @Test
    public void testPosition5() {
        final Board board = Board.initBoard("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10");
        final MoveStrategy minMax = new Minimax(1);
        minMax.execute(board);
        final long numBoardsEvaluated = minMax.getNumBoardsEvaluated();
        assertEquals(numBoardsEvaluated, 46L);
    }
}
