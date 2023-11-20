import javax.swing.*;
import java.awt.*;

class ChessPane extends JLayeredPane {
    public static final int DIMENSION = 8;
    private static Square[][] grid = new Square[DIMENSION][DIMENSION];
    private static ChessPane boardInstance = new ChessPane();

    public static ChessPane getInstance() {
        return boardInstance;
    }

    private static void initializeSquares() {
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                grid[i][j] = new Square(i, j); // Move this line before setting properties
                grid[i][j].setOpaque(true);
                if ((i + j) % 2 == 0)
                    grid[i][j].setBackground(Color.WHITE);
                else
                    grid[i][j].setBackground(Color.BLACK);
                grid[i][j].setVisible(true);
            }
        }
    }

    public ChessPane() {
        setLayout(new GridLayout(DIMENSION, DIMENSION));
        initializeSquares();
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                add(grid[i][j]); // Add the squares to the ChessPane
            }
        }
    }
}