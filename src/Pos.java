public class Pos {
    int x;
    int y;

    public void setXY(int y, int x) {
        this.x = x;
        this.y = y;
    }

    public Pos getXY() {
        return this;
    }


    // Pos 클래스의 생성자
    Pos(int y, int x) {
        this.x = x;
        this.y = y;
    }
}