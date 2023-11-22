import javax.swing.*;
import java.awt.*;

public class Square extends JLabel {
    private int x;
    private int y;
    private ImageIcon imgcon;

    public void setImage(ImageIcon img) {
        this.imgcon = resizeImage(img,100,100);
        setIcon(imgcon);  // 추가된 부분
    }

    public Square(int y, int x) {
        this.y = y;
        this.x = x;
        setLayout(null);
    }
    private ImageIcon resizeImage(ImageIcon originalImage, int targetWidth, int targetHeight) {
        Image img = originalImage.getImage();
        Image resizedImg = img.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }
}