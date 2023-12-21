package test;
import engine.board.*;
import engine.piece.Piece;
import engine.player.ai.AlphaBeta;
import engine.player.ai.MoveStrategy;
import org.junit.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
public class TestAlphaBeta {
    @Test
    public void testQualityDepth6() {
        final Board board = Board.initBoard("4k2r/1R3R2/p3p1pp/4b3/1BnNr3/8/P1P5/5K2 w - - 1 0");
        final MoveStrategy alphaBeta = new AlphaBeta(7);
        final Move bestMove = alphaBeta.execute(board);
        assertEquals(bestMove, MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("f7"), BoardUtils.getCoordinateAtPosition("e7")));
    }

    @Test
    public void testQualityTwoDepth6() {
        final Board board = Board.initBoard("6k1/3b3r/1p1p4/p1n2p2/1PPNpP1q/P3Q1p1/1R1RB1P1/5K2 b - - 0-1");
        final MoveStrategy alphaBeta = new AlphaBeta(6);
        final Move bestMove = alphaBeta.execute(board);
        assertEquals(bestMove, MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("h4"), BoardUtils.getCoordinateAtPosition("f4")));
    }

    @Test
    public void testQualityThreeDepth6() {
        final Board board = Board.initBoard("r2r1n2/pp2bk2/2p1p2p/3q4/3PN1QP/2P3R1/P4PP1/5RK1 w - - 0 1");
        final MoveStrategy alphaBeta = new AlphaBeta(7);
        final Move bestMove = alphaBeta.execute(board);
        assertEquals(bestMove, MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("g4"), BoardUtils.getCoordinateAtPosition("g7")));
    }

    @Test
    public void testQualityFourDepth6() {
        final Board board = Board.initBoard("r1b1k2r/pp3pbp/1qn1p1p1/2pnP3/3p1PP1/1P1P1NBP/P1P5/RN1QKB1R b KQkq - 2 11");
        final MoveStrategy alphaBeta = new AlphaBeta(6);
        final Move bestMove = alphaBeta.execute(board);
        assertEquals(bestMove, MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e8"), BoardUtils.getCoordinateAtPosition("g8")));
    }
}
