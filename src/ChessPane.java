import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class ChessPane extends JLayeredPane implements MouseListener {
    public static final int DIMENSION = 8;
    public static Square[][] grid = new Square[DIMENSION][DIMENSION];
    private static ChessPane boardInstance = new ChessPane();

    private boolean firstClick = true;
    private Square first = null;
    private Square second;
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
                grid[i][j].addMouseListener(this);
            }
        }

        // Pawn 클래스의 초기화를 ChessPane 이후에 진행
        Pawn p = new Pawn(Cor.black, this);
        p.initPos();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if(firstClick) {
            first = findSquare((Square) e.getComponent());
            move_possible(first);
            firstClick = false;
        }
        else {
             second = findSquare((Square) e.getComponent());
            move(first, second);
            firstClick = true;
        }
        //System.out.println(e.getComponent().getParent());
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    private void move_possible(Square sq){
        switch (sq.Type){
            case "Pawn":
                new Pawn(sq.pos.y,sq.pos.x,Cor.white, this).Move_possible();
                break;
        }
    }

    private void move(Square first, Square second){
        switch (first.Type){
            case "Pawn":
                new Pawn(second.pos.y,second.pos.x,Cor.white, this).Move(first);
                break;
        }
    }
    private Square findSquare(Square sq){
        for(int i = 0; i<DIMENSION; i++){
            for(int j = 0; j<DIMENSION; j++){
                if(grid[i][j] == sq){
                    grid[i][j].setImage(new ImageIcon("dra.png"));
                }
            }
        }
        return sq;
    }


}