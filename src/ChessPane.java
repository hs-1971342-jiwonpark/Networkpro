import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;


public class ChessPane extends JLayeredPane implements MouseListener {
    public static final int DIMENSION = 8;
    public static Square[][] grid = new Square[DIMENSION][DIMENSION];

    public Vector<ChessPiece[][]> turn = new Vector<ChessPiece[][]>();
    private static ChessPane boardInstance = new ChessPane();

    public Process process;
    public Cor playerColor = Cor.white;

    private boolean firstClick = true;
    private Square first = null;
    private Square second;
    public static ChessPane getInstance() {
        return boardInstance;
    }

    public Cor getPlayerColor() {
        return playerColor;
    }

    public ChessPane(Cor cor){
        this.playerColor = cor;
    }
    private void initializeSquares() {
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                grid[i][j] = new Square(new Pos(i,j));
                grid[i][j].setOpaque(true);
                if ((i + j) % 2 == 0)
                    grid[i][j].setBackground(Color.WHITE);
                else
                    grid[i][j].setBackground(new Color(0xCCA63D));
                add(grid[i][j]);
                grid[i][j].addMouseListener(this);
                grid[i][j].setVisible(true);
            }
        }
    }

    public void reprint(){
        if(turn.size()-1 <0) return;
        firstClick =true;
        ChessPiece[][] cp = turn.lastElement();
        turn.remove(turn.size()-1);

        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                grid[i][j].setPiece(cp[i][j]);
                if ((i + j) % 2 == 0) {
                    grid[i][j].setBackground(Color.white);
                }else {
                    grid[i][j].setBackground(new Color(0xCCA63D));

                }
            }

        }

        setVisible(true);
    }

    public void saveTurn(){
        ChessPiece[][] newGrid = new ChessPiece[DIMENSION][DIMENSION];
        for(int i =0; i< DIMENSION; i++){
            for (int j=0; j< DIMENSION; j++){
                newGrid[i][j]= grid[i][j].havePiece;
            }
        }
        turn.add(newGrid);



    }
    public ChessPane() {
        setLayout(new GridLayout(DIMENSION, DIMENSION));
        initializeSquares();

        // Pawn 클래스의 초기화를 ChessPane 이후에 진행
        new Pawn(Cor.white, this).initPos();
        new Pawn(Cor.black, this).initPos();
        new Rook(Cor.white, this).initPos();
        new Rook(Cor.black, this).initPos();
        new Knight(Cor.white, this).initPos();
        new Knight(Cor.black, this).initPos();
        new Bishop(Cor.white, this).initPos();
        new Bishop(Cor.black, this).initPos();
        new King(Cor.white, this).initPos();
        new King(Cor.black, this).initPos();
        new Queen(Cor.white, this).initPos();
        new Queen(Cor.black, this).initPos();
        saveTurn();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Pos ps = ((Square) e.getComponent()).pos;
            if(firstClick){
                switch (process.Check_first_click(ps)){
                    case 1://정상
                        firstClick = false;
                        break;
                    case 2://다른곳 클릭

                        break;
                    case 3://자신의 말 클릭

                        break;
                }
        }
        else {
            switch (process.Check_second_click(ps)){
                case 1://정상
                    firstClick = true;
                    this.playerColor = (this.playerColor == Cor.white) ? Cor.black : Cor.white;
                    for(int i=0;i<8;i++) {
                        for (int j = 0; j < 8; j++) {
                        }
                    }
                    System.out.println(turn.size());
                    break;
                case 2://다른곳 클릭

                    break;
                case 3://자신의 말 클릭

                    break;
            }
        }
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

    public static void main(String[] args) {
        new ChatPanel();
    }

}