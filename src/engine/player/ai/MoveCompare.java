package engine.player.ai;

import engine.board.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
/*
Compare moves
First, compare if the move is an immediate threat to the player
Second, compare if the move is an attack move(of the opponent piece)
Third, compare if the move is a castling move
 */
public class MoveCompare implements Comparator<Move> {
//    private final List<Comparator<Move>> comparatorList;
//    public MoveCompare() {
//        comparatorList = new ArrayList<>(Arrays.asList(
//                immediateThreatComparator(),
//                isAttack(),
//                isCastling()
//        ));;
//    }
//
//    private Comparator<Move> immediateThreatComparator() {
//        return (o1, o2) -> {
//            if (o1.getBoard().getCurrentPlayer().getOpponent().isInCheck()) {
//                return 1;
//            }
//            else if (o2.getBoard().getCurrentPlayer().getOpponent().isInCheck()) {
//                return -1;
//            }
//            return 0;
//        };
//    }
//    private Comparator<Move> isAttack() {
//        return (o1, o2) -> {
//            if (o1.isAttack()) {
//                return 1;
//            }
//            else if (o2.isAttack()) {
//                return -1;
//            }
//            return 0;
//        };
//    }
//    private Comparator<Move> isCastling() {
//        return (o1, o2) -> {
//            if (o1.isCastlingMove()) {
//                return 1;
//            }
//            else if (o2.isCastlingMove()) {
//                return -1;
//            }
//            return 0;
//        };
//    }
    @Override
    public int compare(Move o1, Move o2) {
        if (o1.getBoard().getCurrentPlayer().isInCheck() != o2.getBoard().getCurrentPlayer().isInCheck()) {
            return Boolean.compare(o1.getBoard().getCurrentPlayer().isInCheck(), o2.getBoard().getCurrentPlayer().isInCheck());
        } else if (o1.isAttack() != o2.isAttack()) {
            return Boolean.compare(o1.isAttack(), o2.isAttack()); }
//        } else if (o1.isCastlingMove() != o2.isCastlingMove()) {
            return Boolean.compare(o1.isCastlingMove(), o2.isCastlingMove());
//        } else {
//            return Integer.compare(o2.getMovedPiece().getPieceValue(), o1.getMovedPiece().getPieceValue());
//        }
    }
}
