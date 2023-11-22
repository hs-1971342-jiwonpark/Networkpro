import javax.swing.*;
import java.awt.*;

public class StartFrame extends JFrame {
    StartFrame(){
        super("온라인 체스 게임");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800,800);
        add(ChessPane.getInstance());
        setVisible(true);
    }


}
