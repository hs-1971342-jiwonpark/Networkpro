import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

// 폰(Pawn) 클래스는 ChessPiece 클래스를 상속합니다.
public class Queen extends ChessPiece {
    private StartFrame chessPane;

    ChessPane pane;
    Queen(){
        this.name = "queen";
    }

    Queen(Cor cor, StartFrame chessPane) {
        this();
        this.color = cor;
        this.pieceImg = new ImageIcon(this.color.toString()+"_"+this.name+".png");
        this.chessPane = chessPane;
    }
    Queen(Cor cor, ChessPane chessPane) {
        this();
        this.color = cor;
        this.pieceImg = new ImageIcon(this.color.toString()+"_"+this.name+".png");
        this.pane = chessPane;
    }

    Queen(Pos pos, Cor cor, StartFrame chessPane) {
        this(cor, chessPane);
        this.pos = pos;
        this.possble= null;
    }

    protected void initPos() {
        int j = (this.color == Cor.white) ? 7 : 0;
        this.pos = new Pos(j,4);
        pane.grid[j][3].setPiece((ChessPiece)this);
    }


    @Override
    protected void Move(Square first) {
        if(chessPane.grid[this.pos.y][this.pos.x].getBackground() == Color.red){
            chessPane.grid[this.pos.y][this.pos.x].havePiece = first.havePiece;
            chessPane.grid[first.pos.y][first.pos.x].havePiece = null;
            chessPane.grid[first.pos.y][first.pos.x].setIcon(null);
            chessPane.grid[this.pos.y][this.pos.x].setImage();

            //프로모션
            if(this.pos.y == ((this.color == Cor.white)? 7 : 0)){
                //프로모션 함수 호출
                promotion();
            }
        }
    }
    protected void Move_possible() {
        // 방향에 대한 상대 좌표를 정의
        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

        for (int[] direction : directions) {
            int x = this.pos.x;
            int y = this.pos.y;

            while (true) {
                x += direction[0];
                y += direction[1];

                // 체스 판을 벗어나는 경우
                if (x < 0 || x >= chessPane.DIMENSION || y < 0 || y >= chessPane.DIMENSION) {
                    break;
                }

                // 이동할 방향의 바로 앞칸에 말이 없거나, 다른 팀의 말이 있는 경우
                if (chessPane.grid[y][x].havePiece == null || chessPane.playerColor != chessPane.grid[y][x].havePiece.color) {
                    chessPane.grid[y][x].setBackground(Color.red);
                }

                // 다른 팀의 말이 있는 경우, 더 이상 진행하지 않음
                if (chessPane.grid[y][x].havePiece != null) {
                    break;
                }
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

        int x;
        int y = this.pos.y;
        for(x = this.pos.x+1; x < chessPane.DIMENSION; x++){
            y++;
            if(y < chessPane.DIMENSION){
                if ((y + x) % 2 == 0)
                    chessPane.grid[y][x].setBackground(Color.WHITE);
                else
                    chessPane.grid[y][x].setBackground(new Color(0xCCA63D));
            }
            else break;
        }

        y = this.pos.y;
        for(x = this.pos.x+1; x < chessPane.DIMENSION; x++){
            y--;
            if(y >=0){
                if ((y + x) % 2 == 0)
                    chessPane.grid[y][x].setBackground(Color.WHITE);
                else
                    chessPane.grid[y][x].setBackground(new Color(0xCCA63D));
            }
            else break;
        }

        y = this.pos.y;
        for(x = this.pos.x-1; x >= 0; x--){
            y++;
            if(y < chessPane.DIMENSION){
                if ((y + x) % 2 == 0)
                    chessPane.grid[y][x].setBackground(Color.WHITE);
                else
                    chessPane.grid[y][x].setBackground(new Color(0xCCA63D));
            }
            else break;
        }

        y = this.pos.y;
        for(x = this.pos.x-1; x >= 0; x--){
            y--;
            if(y >=0){
                if ((y + x) % 2 == 0)
                    chessPane.grid[y][x].setBackground(Color.WHITE);
                else
                    chessPane.grid[y][x].setBackground(new Color(0xCCA63D));
            }
            else break;
        }
    }

    //앙파상 가능여부

    public void promotion(){
        
    }
}
