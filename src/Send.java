import javax.swing.*;
import java.io.Serializable;
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
    public final static int MODE_DEL_ROOM = 0x128;
    public final static int MODE_ERROR = 0x256;
    public final static int MODE_ENTER_HUMAN = 0x512;
    public final static int MODE_IN_ME = 0x1024;
    public final static int MODE_RETURN = 0x2048;
    public final static int MODE_RETURN2 = 0x4096;
    private Pos pos;
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

    void setName(String name){
        this.userID = name;
    }
    public Send(String userID,String userPW,int code){
        this.userPW = userPW;
        this.userID = userID;
        this.mode = code;
    }
    public Send(String id, Vector<String> users, int mode){
        this.userID = id;
        this.users = users;
        this.mode = mode;
    }

   /* public Send(String id, RoomList.Room room, String roomName, int roomNum, int code){
        this.userID =id;
        this.room = room;
        this.roomName = roomName;
        this.roomNum = roomNum;
        this.mode = code;
    }*/
    public Send(String id,Vector<String>roomList,String roomName,Vector<Vector<String>> idArr, int roomNum, int code){
        this.userID =id;
        this.roomList = roomList;
        this.roomName = roomName;
        this.roomNum = roomNum;
        this.mode = code;
        this.idv = idArr;
    }
   /* public Send(String id, Vector<String>idv,int mode){
        this.mode = mode;
        this.idv = idv;
        this.userID = id;
    }*/
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
   /* public void setRoom(RoomList.Room room) {
        this.room = room;
    }*/

    public Send(Pos pos){
        this.pos = pos;
        this.sendType = 8;
    }

    public Send(Square[][] set){
        this.grid = set;
        this.sendType = 2;

    }
    public Send(Vector<String> roomNames, int code){
        this.mode = code;
        this.users.addAll(roomNames);
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
