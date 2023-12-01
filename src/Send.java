import javax.swing.*;
import java.io.Serializable;

public class Send implements Serializable {
    public static final int DIMENSION = 8;
    public static Square[][] grid = new Square[DIMENSION][DIMENSION];


    public final static int MODE_LOGIN = 0x1;
    public final static int MODE_LOGOUT = 0x2;
    public final static int MODE_TX_STRING= 0x10;
    public final static int MODE_TX_FILE = 0x20;
    public final static int MODE_TX_IMAGE = 0x40;
    private Pos pos;
    private int sendType;
    String userID;
    int mode;
    String message;
    ImageIcon image;
    long size;
    public Send(String userID, int code, String message, ImageIcon image, long size,Pos pos){
        this.userID = userID;
        this.mode = code;
        this.message = message;
        this.image = image;
        this.size = size;
        this.pos = pos;
        this.sendType = 8;
    }
    public Send(String userID, int code, String message, ImageIcon image,Pos pos){
        this(userID,code,message,image,0,pos);
    }
    public Send(String userID, int code){
        this(userID,code,null,0);
    }
    public Send(String userID, int code,String message){
        this(userID,code,message,0);
    }
    public Send(String userID, int code, ImageIcon image){
        this(userID,code,null,null,0,null);
    }
    public Send(String userID, int code, String filename, long size) {
        this(userID, code, filename, null, size, null);
    }


    public Send(Pos pos){
        this.pos = pos;
        this.sendType = 8;
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
