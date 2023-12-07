import javax.swing.*;
import java.io.Serializable;

public class Send implements Serializable {
    public static final int DIMENSION = 8;
    public static Square[][] grid = new Square[DIMENSION][DIMENSION];


    public final static int MODE_LOGIN = 0x1;
    public final static int MODE_LOGOUT = 0x2;
    public final static int MODE_TX_STRING= 0x10;
    public final static int MODE_TX_POS = 0x20;
    public final static int MODE_TX_IMAGE = 0x40;
    private Pos pos;
    String userPW;
    private int sendType;
    String userID;
    int mode;
    String message;
    ImageIcon image;
    long size;

    private Cor cor;

    public Send(String userID,String userPW,int code){
        this.userPW = userPW;
        this.userID = userID;
        this.mode = code;
    }

    public Send(Pos pos, Cor cor){
        this.pos = pos;
        this.sendType = 8;
        this.cor = cor;
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

    public Cor getColor(){
        return this.cor;
    }
}
