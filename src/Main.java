import engine.board.Board;
import gui.Table;

public class Main {
    public static void main(String[] args) {
        Board board = Board.initBoard();
        System.out.println(board);
        Table.getTable().show();
    }
}
