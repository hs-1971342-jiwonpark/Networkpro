import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class Send implements Serializable {
    public static final int DIMENSION = 8;
    public static Square[][] grid = new Square[DIMENSION][DIMENSION];

    public final static int MODE_LOGIN = 0x1;
    public final static int MODE_LOGOUT = 0x2;
    public final static int MODE_TX_ChessPiece = 0x8;
    public final static int MODE_TX_STRING= 0x10;
    public final static int MODE_CT_ROOM = 0x80;
    public final static int MODE_ENTER_ROOM = 0x16;
    public final static int MODE_REMOVE_ROOM = 0x32;
    public final static int MODE_IN_ROOM = 0x64;
    public final static int MODE_CLOSE_ROOM = 0x64; //룸에서 나올 경우 사용
    public final static int MODE_MOVE_CANCEL = 0x128;
    public final static int MODE_GAMESTART = 0x256;

    public final static int MODE_GAME_OVER = 0x512;


    //체스말 관련 전송
    Pos pos;
    ChessPiece[][] cp;
    int cpSize=0;
    Cor cor;
    private int sendType;

    String userID, userPW;
    int mode;
    String message;
    String roomName;
    Vector<String> roomList = new Vector<String>();
    ArrayList<String> rooms = null;
    int roomNum;
    int selectIndex;
    boolean dodelete = false;
    void setName(String name){
        this.userID = name;
    }
    public Send(String userID,String userPW,int code){
        this.userPW = userPW;
        this.userID = userID;
        this.mode = code;
    }
    public Send(String id,String roomName, int roomNum, int code){
        this.userID =id;
        this.roomName = roomName;
        this.roomNum = roomNum;
        this.mode = code;
    }
    public Send(String msg, int mode){
        this.message = msg;
        this.mode = mode;
    }
    //체스말 백그라운드 전송
    //체스말 전달
    public Send(Cor cor, int mode){
        this.cor = cor;
        this.mode = mode;
    }
    public Send(ChessPiece[][] cp, int mode){
        this.mode = mode;
        this.cp = cp;
    }
    public Send(ChessPiece[][] cp, int cpSize, int mode){
        this(cp,mode);
        this.cpSize = cpSize;
    }
    public Send(int code){
        this.mode = code;
    }
    public Send(String id,int selectedIndex,int code){
        this.userID = id;
        this.selectIndex = selectedIndex;
        this.mode = code;
    }

    public Pos getPos() {
        return pos;
    }
}
