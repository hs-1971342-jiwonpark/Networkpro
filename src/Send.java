import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

public class Send implements Serializable {
    public static final int DIMENSION = 8;
    public static Square[][] grid = new Square[DIMENSION][DIMENSION];

    public final static int MODE_LOGIN = 0x1;
    public final static int MODE_LOGOUT = 0x2;
    public final static int MODE_TX_STRING= 0x10;
    public final static int MODE_TX_FILE = 0x20;
    public final static int MODE_TX_IMAGE = 0x40;
    public final static int MODE_CT_ROOM = 0x80;
    public final static int MODE_ENTER_ROOM = 0x16;
    public final static int MODE_REMOVE_ROOM = 0x32;
    public final static int MODE_IN_ROOM = 0x64;
    private Pos pos;
    String userPW;
    private int sendType;
    String userID;
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

    public Send(Pos pos){
        this.pos = pos;
        this.sendType = 8;
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
