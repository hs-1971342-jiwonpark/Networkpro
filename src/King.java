import javax.swing.*;
import java.awt.*;

// 폰(Pawn) 클래스는 ChessPiece 클래스를 상속합니다.
public class King extends ChessPiece {
    private ChessPane chessPane;

    King() {
        this.name = "king";
    }

    King(Cor cor, ChessPane chessPane) {
        this();
        this.color = cor;
        this.pieceImg = new ImageIcon(this.color.toString() + "_" + this.name + ".png");
        this.chessPane = chessPane;
    }

    King(Pos pos, Cor cor, ChessPane chessPane) {
        this(cor, chessPane);
        this.pos = pos;
        this.possble = new Pos[]{
                new Pos(this.pos.y + 1, this.pos.x - 1), new Pos(this.pos.y + 1, this.pos.x),
                new Pos(this.pos.y + 1, this.pos.x + 1), new Pos(this.pos.y, this.pos.x - 1),
                new Pos(this.pos.y, this.pos.x + 1), new Pos(this.pos.y - 1, this.pos.x - 1),
                new Pos(this.pos.y - 1, this.pos.x), new Pos(this.pos.y - 1, this.pos.x + 1)
        };
    }

    protected void initPos() {
        int j = (this.color == Cor.white) ? 7 : 0;
        this.pos = new Pos(j, 3);
        chessPane.grid[j][3].setPiece((ChessPiece) this);
    }


    @Override
    protected void Move(Square first) {
        if (chessPane.grid[this.pos.y][this.pos.x].getBackground() == Color.red) {
            chessPane.grid[this.pos.y][this.pos.x].havePiece = first.havePiece;
            chessPane.grid[first.pos.y][first.pos.x].havePiece = null;
            chessPane.grid[first.pos.y][first.pos.x].setIcon(null);
            chessPane.grid[this.pos.y][this.pos.x].setImage();

            //프로모션
            if (this.pos.y == ((this.color == Cor.white) ? 7 : 0)) {
                //프로모션 함수 호출
                promotion();
            }
        }

    }

    protected void Move_possible() {
        int first = (this.color == Cor.white) ? -1 : 1;
        for (Pos i : possble) {
            if ((i.y < chessPane.DIMENSION && i.y >= 0)
                    && (i.x < chessPane.DIMENSION && i.x >= 0))
                if ((chessPane.grid[i.y][i.x].havePiece == null)) {
                    chessPane.grid[i.y][i.x].setBackground(Color.red);
                }
                else if(chessPane.grid[i.y][i.x].havePiece.color != this.color){
                    chessPane.grid[i.y][i.x].setBackground(Color.red);
                }
        }
    }
    protected void Clear_Move_possible() {
        for(Pos i:possble){
            if((i.y < chessPane.DIMENSION && i.y >= 0)
                    && (i.x < chessPane.DIMENSION && i.x >= 0)) {
                if ((i.y + i.x) % 2 == 0)
                    chessPane.grid[i.y][i.x].setBackground(Color.WHITE);
                else
                    chessPane.grid[i.y][i.x].setBackground(new Color(0xCCA63D));
            }
        }
    }

    //앙파상 가능여부
    protected boolean isEn_Passant(Pos ps){
        int start;
        int end;
        //색에 따라 기준선 달리정함
        if(this.color == Cor.white){
            end = 2;
            start = 1;
        }
        else {
            end = 5;
            start = 6;
        }

        if(end == ps.y){
            //그 전 상대의 움직임을 읽어와서 상대의 폰이 2칸 움직여
            // 내폰 옆에 왔을 경우 1리턴

            Square[][] sqend = (Square[][]) chessPane.turn.elementAt(0);
            Square[][] sqstart = (Square[][]) chessPane.turn.elementAt(1);
            if(sqend[this.pos.y][ps.x].havePiece == null || sqstart[start][ps.x].havePiece == null) {

                return false;
            }
            if(sqend[this.pos.y][ps.x].havePiece.equals(sqstart[start][ps.x].havePiece))
                return true;
            }
        return false;
    }


    public void promotion(){
        
    }
}
