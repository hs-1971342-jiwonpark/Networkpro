import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

// 폰(Pawn) 클래스는 ChessPiece 클래스를 상속합니다.
public class Rook extends ChessPiece {
    private StartFrame chessPane;
    ChessPane pane;
    Rook(){
        this.name = "rook";
    }

    Rook(Cor cor, StartFrame chessPane) {
        this();
        this.color = cor;
        this.pieceImg = new ImageIcon(this.color.toString()+"_"+this.name+".png");
        this.chessPane = chessPane;
    }
    Rook(Cor cor, ChessPane chessPane) {
        this();
        this.color = cor;
        this.pieceImg = new ImageIcon(this.color.toString()+"_"+this.name+".png");
        this.pane = chessPane;
    }

    Rook(Pos pos, Cor cor, StartFrame chessPane) {
        this(cor, chessPane);
        this.pos = pos;
        this.possble= new Pos[]{
                new Pos(0, this.pos.x), new Pos(7, this.pos.x),
                new Pos(this.pos.y, 0), new Pos(this.pos.y, 7)
        };
    }

    protected void initPos() {
        int j = (this.color == Cor.white) ? 7 : 0;
        this.pos = new Pos(j,0);
        pane.grid[j][0].setPiece((ChessPiece)this);
        this.pos = new Pos(j,7);
        pane.grid[j][7].setPiece((ChessPiece)this);
    }


    @Override
    protected void Move(Square first) {
        if(chessPane.grid[this.pos.y][this.pos.x].getBackground() == Color.red){
            chessPane.grid[this.pos.y][this.pos.x].havePiece = first.havePiece;
            chessPane.grid[first.pos.y][first.pos.x].havePiece = null;
            chessPane.grid[first.pos.y][first.pos.x].setIcon(null);
            chessPane.grid[this.pos.y][this.pos.x].setImage();

        }
    }

    //구현 실패
    protected void Move_possible() {
        for(int x = this.pos.x+1; x<chessPane.DIMENSION; x++){
            //이동할 방향의 바로 앞칸에 말이 없으면, 색 칠함
            if(chessPane.grid[this.pos.y][x].havePiece == null){
                chessPane.grid[this.pos.y][x].setBackground(Color.red);
            }
            else {
                if (chessPane.playerColor != chessPane.grid[this.pos.y][x].havePiece.color) {
                    chessPane.grid[this.pos.y][x].setBackground(Color.red);
                    break;
                }
                else break;
            }
            //  만약 말이있고 그게 플레이어의 색과 다르면

        }
        for(int x = this.pos.x -1; x >= 0; x--){
            if(chessPane.grid[this.pos.y][x].havePiece == null){
                chessPane.grid[this.pos.y][x].setBackground(Color.red);
            }
            else {
                if (chessPane.playerColor != chessPane.grid[this.pos.y][x].havePiece.color) {
                    chessPane.grid[this.pos.y][x].setBackground(Color.red);
                    break;
                }
                else break;
            }
        }
        for(int y = this.pos.y +1; y<chessPane.DIMENSION; y++){
            if(chessPane.grid[y][this.pos.x].havePiece == null){
                chessPane.grid[y][this.pos.x].setBackground(Color.red);
            }
            else {
                if (chessPane.playerColor != chessPane.grid[y][this.pos.x].havePiece.color) {
                    chessPane.grid[y][this.pos.x].setBackground(Color.red);
                    break;
                }
                else break;
            }
        }
        for(int y = this.pos.y -1; y >= 0; y--){
            if(chessPane.grid[y][this.pos.x].havePiece == null){
                chessPane.grid[y][this.pos.x].setBackground(Color.red);
            }
            else {
                if (chessPane.playerColor != chessPane.grid[y][this.pos.x].havePiece.color) {
                    chessPane.grid[y][this.pos.x].setBackground(Color.red);
                    break;
                }
                else break;
            }
        }
    }


    protected void Clear_Move_possible() {

        for(int x = this.pos.x+1; x<chessPane.DIMENSION; x++){
            //이동할 방향의 바로 앞칸에 말이 없으면, 색 칠함
            if ((this.pos.y + x) % 2 == 0)
                chessPane.grid[this.pos.y][x].setBackground(Color.WHITE);
            else
                chessPane.grid[this.pos.y][x].setBackground(new Color(0xCCA63D));
        }
        //  만약 말이있고 그게 플레이어의 색과 다르면
        for(int x = this.pos.x -1; x >= 0; x--){
            if ((this.pos.y + x) % 2 == 0)
                chessPane.grid[this.pos.y][x].setBackground(Color.WHITE);
            else
                chessPane.grid[this.pos.y][x].setBackground(new Color(0xCCA63D));
        }

        for(int y = this.pos.y +1; y<chessPane.DIMENSION; y++){
            if ((y + this.pos.x) % 2 == 0)
                chessPane.grid[y][this.pos.x].setBackground(Color.WHITE);
            else
                chessPane.grid[y][this.pos.x].setBackground(new Color(0xCCA63D));
        }

        for(int y = this.pos.y -1; y >= 0; y--){
            if ((y + this.pos.x) % 2 == 0)
                chessPane.grid[y][this.pos.x].setBackground(Color.WHITE);
            else
                chessPane.grid[y][this.pos.x].setBackground(new Color(0xCCA63D));
        }
    }

}