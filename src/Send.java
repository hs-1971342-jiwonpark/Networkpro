import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;

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
    private Pos pos;
    String userPW;
    private int sendType;
    String userID;
    int mode;
    String message;
    ImageIcon image;
    long size;
    String roomName;
    int roomNum;
    Room room;
    int selectIndex;
    void setName(String name){
        this.userID = name;
    }
    public Send(String userID,String userPW,int code){
        this.userPW = userPW;
        this.userID = userID;
        this.mode = code;
    }
    public Send(Room room,String roomName, int roomNum, int code){
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
