import java.io.Serializable;

public class Send implements Serializable {
    public static final int DIMENSION = 8;
    public static Square[][] grid = new Square[DIMENSION][DIMENSION];

    private Pos pos;

    private int sendType;

    public Send(){

    }
    public Send(Pos pos){
        this.pos = pos;
        this.sendType = 1;
    }

    public Send(Square[][] set){
        this.grid = set;
        this.sendType = 2;

    }

    public int getSendType() {
        return sendType;
    }

    public Pos getPos() {
        return pos;
    }
}
