import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

public class Send implements Serializable {
    public static final int DIMENSION = 8;
    public static Square[][] grid = new Square[DIMENSION][DIMENSION];

    public final static int MODE_LOGIN = 0x1;
    public final static int MODE_LOGOUT = 0x2;
    public final static int MODE_TX_POS = 0x4;
    public final static int MODE_TX_ChessPiece = 0x8;
    public final static int MODE_TX_STRING= 0x10;
    public final static int MODE_TX_FILE = 0x20;
    public final static int MODE_TX_IMAGE = 0x40;
    public final static int MODE_CT_ROOM = 0x80;
    public final static int MODE_ENTER_ROOM = 0x16;
    public final static int MODE_REMOVE_ROOM = 0x32;
    public final static int MODE_IN_ROOM = 0x64;
    public final static int MODE_CLOSE_ROOM = 0x64; //룸에서 나올 경우 사용
    public final static int MODE_TX_BACKGROUND = 0x128;



    //체스말 관련 전송
    Pos pos;
    ChessPiece[][] cp = new ChessPiece[][]{};
    boolean isFirstClick = true;
    Cor cor;
    //
    private int sendType;

    Color[][] colors = new Color[][]{};
    String userID, userPW;
    int mode;
    String message;
    ImageIcon image;
    long size;
    String roomName;
    Vector<String> roomList = new Vector<String>();
    ArrayList<String> rooms = null;
    int roomNum;
    Room room;
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
    public Send(String id,Room room,String roomName, int roomNum, int code){
        this.userID =id;
        this.room = room;
        this.roomName = roomName;
        this.roomNum = roomNum;
        this.mode = code;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Send(Pos pos, Boolean a,Cor cor, int mode){
        this.pos = pos;
        this.isFirstClick = a;
        this.cor = cor;
        this.mode = mode;
    }
    //체스말 백그라운드 전송
    public Send(Color[][] colors, int mode){
        this.colors = colors;
        this.mode = mode;
    }
    //체스말 전달
    public Send(ChessPiece[][] cp, Color[][] colors, int mode){
        this(colors, mode);
        this.cp = cp;
    }
    public Send(Pos pos){
        this.pos = pos;
        this.sendType = 8;
    }

    public Send(int code){
        this.mode = code;
    }

    public Send(Square[][] set){
        this.grid = set;
        this.sendType = 2;

    }
    public Send(ArrayList<String> roomNames, int code){
        this.mode = code;
        this.rooms = roomNames;
    }
    public Send(Vector<String> roomNames, int code){
        this.mode = code;
        this.roomList = roomNames;
    }
    public Send(String id,int selectedIndex,int code){
        this.userID = id;
        this.selectIndex = selectedIndex;
        this.mode = code;
    }

    public int getSendType() {
        return sendType;
    }

    public Pos getPos() {
        return pos;
    }
}
