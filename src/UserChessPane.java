import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class UserChessPane extends JLayeredPane {
    public static final int DIMENSION = 8;
    public static Square[][] grid = new Square[DIMENSION][DIMENSION];
    private static UserChessPane boardInstance = new UserChessPane();

    private ObjectOutputStream out;
    private Cor playerColor = Cor.white;

    private boolean myTurn = false;
    public static UserChessPane getInstance() {
        return boardInstance;
    }
    public UserChessPane(Cor cor, ObjectOutputStream out){
        this();
        this.playerColor = cor;
        this.out = out;
    }
    public UserChessPane() {
        setLayout(new GridLayout(DIMENSION, DIMENSION));
        initializeSquares();
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
                //만약 서버로부터 색을 부여받지 못할 경우
                //게임말을 선택하지 못하게 함.
                if(playerColor == null) continue;
                grid[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        Pos ps = ((Square) e.getComponent()).pos;
                        if(myTurn) {
                            //send(new Send(ps, this.playerColor));

                        }
                    }
                });
                grid[i][j].setVisible(true);
            }
        }
    }
    public void reprint(ChessPiece[][] cp, Color[][] colors){
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                grid[i][j].setPiece(cp[i][j]);
                grid[i][j].setBackground(colors[i][j]);
            }
        }
    }
    private void send(Send send) {
        try {
            out.writeObject(send);
            out.flush();
        } catch (IOException e) {
            System.out.println("클라이언트 일반 전송 오류"+e.getMessage());
        }
    }

    public Cor getPlayerColor() {
        return playerColor;
    }

}