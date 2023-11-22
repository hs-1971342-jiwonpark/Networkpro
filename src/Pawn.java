import javax.swing.*;
import java.awt.*;

// 폰(Pawn) 클래스는 ChessPiece 클래스를 상속합니다.
public class Pawn extends ChessPiece {
    private ChessPane chessPane;

    Pawn(){}
    Pawn(Cor cor, ChessPane chessPane) {
        this.name = "Pawn";
        this.color = cor;
        this.pieceImg = new ImageIcon("image\\pawn.png");
        this.chessPane = chessPane;
    }
    Pawn(int y, int x, Cor cor, ChessPane chessPane) {
        this(cor, chessPane);
        this.name = "Pawn";
        this.pos = new Pos(y,x);
        this.possble= this.color.equals(Cor.black) ?
                new int[][]{{this.pos.y + 1, this.pos.x}, {this.pos.y + 2, this.pos.x}, {this.pos.y + 1, this.pos.x + 1}, {this.pos.y + 1, this.pos.x - 1}}
                : new int[][]{{this.pos.y - 1, this.pos.x}, {this.pos.y - 2, this.pos.x}, {this.pos.y - 1, this.pos.x + 1}, {this.pos.y - 1, this.pos.x - 1}};

        this.pieceImg = new ImageIcon("image\\pawn.png");
    }
    protected void initPos() {
        for(int i=0;i<8;i++) {
            chessPane.setSquareAt(1, i, pieceImg);
            chessPane.setSquareAt(6, i, pieceImg);
        }
    }


    @Override
    protected void Move(Square first) {
        if(chessPane.grid[this.pos.y][this.pos.x].getBackground() == Color.red){
            first.setImage(new ImageIcon("null"));
            chessPane.grid[this.pos.y][this.pos.x].setImage(new ImageIcon("dra.png"));
        }
    }
    protected void Move_possible() {
        for(int i[]:possble){
            if(i[0] < chessPane.DIMENSION || i[0] >= 0
                    &&i[1] < chessPane.DIMENSION || i[1] >= 0)
                chessPane.grid[i[0]][i[1]].setBackground(Color.red);
        }

    }


    @Override
    protected Boolean hit() {
        return null;
    }

    @Override
    protected Pos getPos() {
        return null;
    }

    @Override
    protected Pos setPos() {
        return null;
    }
}
