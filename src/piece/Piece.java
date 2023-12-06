package piece;

import java.util.List;
/*
Abstract class cho từng quân cờ
 */
public abstract class Piece {
    public Piece(Point current, int whichPlayer) {
        this.previous = null;
        this.current = current;
        this.whichPlayer = whichPlayer;
        this.isAlive = true;
    }
    /*
    Gồm tọa độ trước và hiện tại
     */
    protected Point current;
    protected Point previous;
    /*
    Gồm biến whichPlayer cho việc chỉ rõ quân cờ đấy của người chơi nào
     */
    private int whichPlayer;
    /*
    biến isAlive xem quân cờ đó còn ở trên bàn cờ k
     */
    private boolean isAlive;
    /*
    Phương thức getLegalMoves() là phương thức abstract, sẽ được khai triển bởi tùng quân cờ,
    sẽ generate các bước đi hợp lệ
     */
    public abstract List<Point> getLegalMoves();
    /*
    Phương thức move -> di chuyển đến một tọa độ mới
    Phương thức undo -> di chuyển đến vị trí trước
     */
    public void move(Point newPoint) {
        if (getLegalMoves().contains(newPoint)) {
            previous = current;
            current = newPoint;
        }
//        System.out.println("Illegal moves");
    }
    public void undo() {
        if (previous != null && getLegalMoves().contains(previous)) {
            current = previous;
        }
    }
    public abstract String getType();

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Piece) {
            Piece piece = (Piece) obj;
            return piece.current.equals(this.current);
        }
        return false;
    }
}
