import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Vector;


public class ChessPane extends JFrame {
    public static final int DIMENSION = 8;
    public static Square[][] grid = new Square[DIMENSION][DIMENSION];
    ObjectOutputStream out;
    Socket socket;
    public Vector<ChessPiece[][]> turn = new Vector<ChessPiece[][]>();
    private static ChessPane boardInstance = new ChessPane();

    public Cor playerColor = Cor.white;

    public static ChessPane getInstance() {
        return boardInstance;
    }

    public ChessPane(StartFrame sf){
        this();
        setVisible(true);
        add(this);
    }
    private void initializeSquares() {
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                grid[i][j] = new Square(new Pos(i,j));
                grid[i][j].setOpaque(true);
                grid[i][j].setVisible(true);
            }
        }
    }

    public void rollback(){
        if(turn.size()-1 <0) return;
        //자신의 턴을 계산하기 위해 나머지 연산
        int num = (this.playerColor == Cor.white)? 0:1;
        ChessPiece[][] cp;
        //일단 무르기 한번하고
        cp = turn.lastElement();
        turn.remove(turn.size() - 1);
        //한번 헀을 때 현제 턴이 자신의 턴과 같지 않다면 다시
        if((turn.size()-1)%2==num) {
            cp = turn.lastElement();
            turn.remove(turn.size() - 1);
        }
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


    public ChessPiece[][] saveTurn(){
        ChessPiece[][] newGrid = new ChessPiece[DIMENSION][DIMENSION];
        for(int i =0; i< DIMENSION; i++){
            for (int j=0; j< DIMENSION; j++){
                newGrid[i][j]= grid[i][j].havePiece;
            }
        }
        return newGrid;



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
    public ChessPane(Socket socket, ObjectInputStream in, ObjectOutputStream out, Cor cor) {
        this();
        this.socket = socket;
        this.out = out;
        this.playerColor = cor;

    }
    public void send(Send send) {
        try {
            if(this.socket !=null) {
                out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                out.writeObject(send);
                out.flush();
            }

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

}