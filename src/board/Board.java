package board;

import java.util.Arrays;

public class Board {
    /*
    Singleton Design Pattern for Board class
     */
    private static Board instance;
    private Board() {
        square = new int[64];
        FenUtility.loadPosition(this,FenUtility.startFen);
    }
    public static Board getInstance() {
        if (instance == null)
        {
            //synchronized block to remove overhead
            synchronized (Board.class)
            {
                if(instance==null)
                {
                    // if instance is null, initialize
                    instance = new Board();
                }

            }
        }
        return instance;
    }
    public int[] square;
    public void loadPosition(String fen) {
        Arrays.fill(square,0);
        FenUtility.loadPosition(this,fen);
    }
}
