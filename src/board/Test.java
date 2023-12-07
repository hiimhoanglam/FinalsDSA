package board;

public class Test {
    public static void main(String[] args) {
        Board board = Board.getInstance();
        board.loadPosition("r1bk3r/p2pBpNp/n4n2/1p1NP2P/6P1/3P4/P1P1K3/q5b1");
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(board.square[i * 8 + j] + " ");
            }
            System.out.println();
        }

    }
}
