import javax.swing.*;
import java.awt.*;
public class ChessPane extends JLayeredPane {
    public static final int DIMENSION = 8;
    private static Square[][] grid = new Square[DIMENSION][DIMENSION];
    private static ChessPane boardInstance = new ChessPane();

    public static ChessPane getInstance() {
        return boardInstance;
    }

    public Square getSquareAt(int row, int col) {
        return grid[row][col];
    }

    public void setSquareAt(int row, int col, ImageIcon imgcon) {
        grid[row][col].setImage(imgcon);
    }

    private static void initializeSquares() {
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                grid[i][j] = new Square(i, j);
                grid[i][j].setOpaque(true);
                if ((i + j) % 2 == 0)
                    grid[i][j].setBackground(Color.WHITE);
                else
                    grid[i][j].setBackground(new Color(0xCCA63D));
                grid[i][j].setVisible(true);
            }
        }
    }

    public ChessPane() {
        setLayout(new GridLayout(DIMENSION, DIMENSION));
        initializeSquares();

        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                add(grid[i][j]);
            }
        }

        // Pawn 클래스의 초기화를 ChessPane 이후에 진행
        Pawn p = new Pawn(Cor.black, this);
        p.initPos();
    }
}