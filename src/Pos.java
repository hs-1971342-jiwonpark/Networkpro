import java.io.Serializable;

public class Pos implements Serializable {
    int x;
    int y;
    // Pos 클래스의 생성자
    Pos(int y, int x) {
        this.x = x;
        this.y = y;
    }
    public Pos(Pos pos){
        this(pos.y, pos.x);
    }
    public void setXY(int y, int x) {
        this.x = x;
        this.y = y;
    }

    public Pos getXY() {
        return this;
    }


    public boolean check_Pos(){
        if((0 <= this.x && this.x < 8) && (0 <= this.y && this.y < 8)) {
            return true;
        }
        return false;
    }


}