import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Square extends JLabel{
    private int x;
    private int y;

    public Pos pos;
    private ImageIcon imgcon;
    public String Type = "Pawn";
    public ChessPiece cp;
    public boolean move_p = false;



    public void setImage(ImageIcon img) {
        this.imgcon = resizeImage(img,100,100);
        setIcon(imgcon);  // 추가된 부분
    }
    public Square(int y, int x) {
        this.pos = new Pos(y,x);
        setLayout(null);
        this.cp = cp;
    }
    public Square(int y, int x, ChessPiece cp) {
        this.pos = new Pos(y,x);
        setLayout(null);
        this.cp = cp;
        cp.color = Cor.black;
    }
    private ImageIcon resizeImage(ImageIcon originalImage, int targetWidth, int targetHeight) {
        Image img = originalImage.getImage();
        Image resizedImg = img.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }

}