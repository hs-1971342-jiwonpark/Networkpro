import javax.swing.*;
import java.awt.*;

public class StartFrame extends JFrame {
    ChessPane chessPane;
    JLabel pan[][];
    StartFrame(){
        super("온라인 체스 게임");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500,500);
        chessPane = new ChessPane();
        add(chessPane, BorderLayout.CENTER);
        setVisible(true);
    }
    class ChessPane extends JLayeredPane{
        private ChessPane(){
            pan = new JLabel[8][8];
            setLayout(new GridLayout(8,8));
            for(int i=0;i<8;i++){
                for(int j=0;j<8;j++){
                    pan[i][j] = new JLabel();
                    pan[i][j].setOpaque(true);
                    if((i+j)%2==0)
                        pan[i][j].setBackground(Color.WHITE);
                    else
                        pan[i][j].setBackground(Color.BLACK);
                    add(pan[i][j]);
                }
            }
            setVisible(true);
        }
    }

}
