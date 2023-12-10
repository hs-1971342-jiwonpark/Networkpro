import javax.swing.*;
import java.io.Serializable;
import java.util.Vector;

public class Send implements Serializable {
    public static final int DIMENSION = 8;
    public static Square[][] grid = new Square[DIMENSION][DIMENSION];

    public final static int MODE_LOGIN = 0x1;
    public final static int MODE_LOGOUT = 0x2;
    public final static int MODE_TX_ChessPiece = 0x8;
    public final static int MODE_TX_STRING= 0x10;
    public final static int MODE_TX_FILE = 0x20;
    public final static int MODE_TX_IMAGE = 0x40;
    public final static int MODE_IN_ROOM = 0x64;
    public final static int MODE_MOVE_CANCEL = 0x128;
    public final static int MODE_GAMESTART = 0x256;
    public final static int MODE_GAME_OVER = 0x512;
    public final static int MODE_ENTER_HUMAN = 1026;
    public final static int MODE_RETURN = 0x2048;
    private Pos pos;

    Cor cor;
    String userPW;
    private int sendType;
    String userID;
    int mode;

    String message;
    Vector<String>users = new Vector<>();
    Vector<String> roomList = new Vector<>(10);
    Vector<Vector<String>> idv = new Vector<Vector<String>>(10);
    ImageIcon image;
    long size;
    String roomName;
    int roomNum;
    int selectIndex;




    //체스말 관련 전송

    ChessPiece[][] cp;
    int cpSize=0;

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
    public Send(ChessPiece[][] cp, int mode){
        this.mode = mode;
        this.cp = cp;
    }
    public Send(ChessPiece[][] cp, int cpSize, int mode){
        this(cp,mode);
        this.cpSize = cpSize;
    }


    public Send(String id, Vector<String> users, int mode){
        this.userID = id;
        this.users = users;
        this.mode = mode;
    }
    public Send(String id,Vector<String>roomList,String roomName,Vector<Vector<String>> idArr, int roomNum, int code){
        this.userID =id;
        this.roomList = roomList;
        this.roomName = roomName;
        this.roomNum = roomNum;
        this.mode = code;
        this.idv = idArr;
    }
    public Send(Cor cor,int mode){
         this.mode = mode;
         this.cor = cor;
     }
    public Send(String id,int roomNum,String roomName,int mode){
        this.userID = id;
        this.roomNum = roomNum;
        this.roomName = roomName;
        this.mode = mode;
    }

    public Send(String roomName,Vector<Vector<String>> idv,int roomNum,int mode){
        this.roomName = roomName;
        this.idv = idv;
        this.roomNum = roomNum;
        this.mode=mode;
    }
    public Send(int mode){
        this.mode = mode;
    }

    public Send(Pos pos){
        this.pos = pos;
    }
    public Send(Vector<String> roomNames, int code){
        this.mode = code;
        this.users.addAll(roomNames);
    }

    public Pos getPos() {
        return pos;
    }
}