import javax.swing.*;
import java.awt.*;

// 폰(Pawn) 클래스는 ChessPiece 클래스를 상속합니다.
public class King extends ChessPiece {
    private StartFrame chessPane;
    ChessPane pane;

    King() {
        this.name = "king";
    }

    King(Cor cor, StartFrame chessPane) {
        this();
        this.color = cor;
        this.pieceImg = new ImageIcon(this.color.toString() + "_" + this.name + ".png");
        this.chessPane = chessPane;
    }
    King(Cor cor, ChessPane chessPane) {
        this();
        this.color = cor;
        this.pieceImg = new ImageIcon(this.color.toString() + "_" + this.name + ".png");
        this.pane = chessPane;
    }
    King(Pos pos, Cor cor, StartFrame chessPane) {
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
        pane.grid[j][4].setPiece((ChessPiece) this);
    }


    @Override
    protected void Move(Square first) {
        if (chessPane.grid[this.pos.y][this.pos.x].getBackground() == Color.red) {
            int castling = Castling(first.pos);
            if(castling == 1){
                chessPane.grid[this.pos.y][3].havePiece = chessPane.grid[this.pos.y][0].havePiece;
                chessPane.grid[this.pos.y][3].setImage();
                chessPane.grid[this.pos.y][0].havePiece = null;
                chessPane.grid[this.pos.y][0].setIcon(null);
            }
            else if(castling ==2){
                chessPane.grid[this.pos.y][5].havePiece = chessPane.grid[this.pos.y][7].havePiece;
                chessPane.grid[this.pos.y][5].setImage();
                chessPane.grid[this.pos.y][7].havePiece = null;
                chessPane.grid[this.pos.y][7].setIcon(null);
            }



            chessPane.grid[this.pos.y][this.pos.x].havePiece = first.havePiece;
            chessPane.grid[first.pos.y][first.pos.x].havePiece = null;
            chessPane.grid[first.pos.y][first.pos.x].setIcon(null);
            chessPane.grid[this.pos.y][this.pos.x].setImage();

        }

    }

    protected void Move_possible() {
        int first = (this.color == Cor.white) ? -1 : 1;
        for (Pos i : possble) {
            if ((i.y < chessPane.DIMENSION && i.y >= 0)
                    && (i.x < chessPane.DIMENSION && i.x >= 0))
                if ((chessPane.grid[i.y][i.x].havePiece == null)) {
                    chessPane.grid[i.y][i.x].setBackground(Color.red);
                } else if (chessPane.grid[i.y][i.x].havePiece.color != this.color) {
                    chessPane.grid[i.y][i.x].setBackground(Color.red);
                }
        }
        int castling = Castling(this.pos);
        if(castling == 1){
            chessPane.grid[this.pos.y][2].setBackground(Color.red);
        }
        else if(castling ==2){
            chessPane.grid[this.pos.y][6].setBackground(Color.red);
        }
    }

    protected void Clear_Move_possible() {
        for (int i = 0; i < StartFrame.DIMENSION; i++) {
            for (int j = 0; j < StartFrame.DIMENSION; j++) {
                if ((i + j) % 2 == 0)
                    StartFrame.grid[i][j].setBackground(Color.WHITE);
                else {
                    StartFrame.grid[i][j].setBackground(new Color(0xCCA63D));
                }
            }
        }
    }

    public int Castling(Pos ps) {
        //킹의 위치
        if (ps.x != 4) return 0;
        //플레이어 색에 따라 다름
        int NonY = (this.color == Cor.white) ? 7 : 0;
        //킹의 y좌표가 양 끝
        if (ps.y != NonY) return 0;
        int leftCnt=0;
        int rightCnt=0;
        //모든 턴 중에 룩과 킹이 움직이면 실패
        //왼쪽 룩과 킹
        for (int i = 0; i < chessPane.turn.size() - 1; i++) {
            ChessPiece[][] cp = chessPane.turn.elementAt(i);
            if (cp[NonY][0] != null || chessPane.grid[NonY][0].havePiece != cp[NonY][0]) {
                leftCnt++;
                break;
            }
            if (cp[NonY][7] == null || chessPane.grid[NonY][4].havePiece != cp[NonY][4]) {
                leftCnt++;
                break;
            }
        }
        if(leftCnt!=0) {
            System.out.println(leftCnt + "왼쪽 움직임 해결");
        }

        //오른쪽 룩과 킹
        for (int i = 0; i < chessPane.turn.size() - 1; i++) {
            ChessPiece[][] cp = chessPane.turn.elementAt(i);
            if (cp[NonY][7] == null || chessPane.grid[NonY][7].havePiece != cp[NonY][7]) {
                rightCnt++;
                break;
            }
            if (cp[NonY][4] == null || chessPane.grid[NonY][4].havePiece != cp[NonY][4]) {
                rightCnt++;
                break;
            }
        }
        if(rightCnt!=0) {
            System.out.println(rightCnt + "오른쪽 움직임 해결");
        }
        if(rightCnt !=0 && leftCnt !=0)
            return 0;
        //킹과 룩 사이에 아무것도 없으면
        if (chessPane.grid[NonY][1].havePiece == null || chessPane.grid[NonY][2].havePiece == null || chessPane.grid[NonY][3].havePiece == null) {
            return 1;
        }
        if (chessPane.grid[NonY][5].havePiece == null || chessPane.grid[NonY][6].havePiece == null)
            return 2;


        return 0;
    }

    public int Check(Pos ps) {
        for (int x = this.pos.x + 1; x < chessPane.DIMENSION; x++) {
            if (chessPane.grid[this.pos.y][x].havePiece != null) {
                if (chessPane.grid[this.pos.y][x].havePiece.color != this.color) {
                    if (chessPane.grid[this.pos.y][x].havePiece.name.equals("rook") ||
                            chessPane.grid[this.pos.y][x].havePiece.name.equals("queen")) {
                        return 1;
                    }
                } else break;
            }
        }

        for (int x = this.pos.x - 1; x >= 0; x--) {
            if (chessPane.grid[this.pos.y][x].havePiece != null) {
                if (chessPane.grid[this.pos.y][x].havePiece.color != this.color) {
                    if (chessPane.grid[this.pos.y][x].havePiece.name.equals("rook") ||
                            chessPane.grid[this.pos.y][x].havePiece.name.equals("queen")) {
                        return 1;
                    }
                } else break;
            }
        }


        for (int y = this.pos.y + 1; y < chessPane.DIMENSION; y++) {
            if (chessPane.grid[y][this.pos.x].havePiece != null) {
                if (chessPane.grid[y][this.pos.x].havePiece.color != this.color) {
                    if (chessPane.grid[y][this.pos.x].havePiece.name.equals("rook") ||
                            chessPane.grid[y][this.pos.x].havePiece.name.equals("queen")) {
                        return 1;
                    }
                } else break;
            }
        }
        for (int y = this.pos.y - 1; y >= 0; y--) {
            if (chessPane.grid[y][this.pos.x].havePiece != null) {
                if (chessPane.grid[y][this.pos.x].havePiece.color != this.color) {
                    if (chessPane.grid[y][this.pos.x].havePiece.name.equals("rook") ||
                            chessPane.grid[y][this.pos.x].havePiece.name.equals("queen")) {
                        return 1;
                    }
                } else break;
            }
        }

        int x;
        int y = this.pos.y;
        for (x = this.pos.x + 1; x < chessPane.DIMENSION; x++) {
            y++;
            if (chessPane.grid[y][this.pos.x].havePiece != null) {
                if (chessPane.grid[y][this.pos.x].havePiece.color != this.color) {
                    if (chessPane.grid[y][this.pos.x].havePiece.name.equals("bishop") ||
                            chessPane.grid[y][this.pos.x].havePiece.name.equals("queen")) {
                        return 1;
                    }
                } else break;
            }
        }


        y = this.pos.y;
        for (x = this.pos.x + 1; x < chessPane.DIMENSION; x++) {
            y--;
            if (chessPane.grid[y][this.pos.x].havePiece != null) {
                if (chessPane.grid[y][this.pos.x].havePiece.color != this.color) {
                    if (chessPane.grid[y][this.pos.x].havePiece.name.equals("bishop") ||
                            chessPane.grid[y][this.pos.x].havePiece.name.equals("queen")) {
                        return 1;
                    }
                } else break;
            }
        }

        y = this.pos.y;
        for (x = this.pos.x - 1; x >= 0; x--) {
            y++;
            if (chessPane.grid[y][this.pos.x].havePiece != null) {
                if (chessPane.grid[y][this.pos.x].havePiece.color != this.color) {
                    if (chessPane.grid[y][this.pos.x].havePiece.name.equals("bishop") ||
                            chessPane.grid[y][this.pos.x].havePiece.name.equals("queen")) {
                        return 1;
                    }
                } else break;
            }
        }

        y = this.pos.y;
        for (x = this.pos.x - 1; x >= 0; x--) {
            y--;
            if (chessPane.grid[y][this.pos.x].havePiece != null) {
                if (chessPane.grid[y][this.pos.x].havePiece.color != this.color) {
                    if (chessPane.grid[y][this.pos.x].havePiece.name.equals("bishop") ||
                            chessPane.grid[y][this.pos.x].havePiece.name.equals("queen")) {
                        return 1;
                    }
                } else break;
            }
        }

        Pos[] knightpossble = new Pos[]
                {new Pos(this.pos.y + 2, this.pos.x -1), new Pos(this.pos.y + 2, this.pos.x + 1),
                        new Pos(this.pos.y - 2, this.pos.x - 1), new Pos(this.pos.y - 2, this.pos.x + 1),
                        new Pos(this.pos.y + 1, this.pos.x + 2), new Pos(this.pos.y - 1, this.pos.x + 2),
                        new Pos(this.pos.y + 1, this.pos.x - 2), new Pos(this.pos.y - 1, this.pos.x - 2)
                };
        for(Pos i:knightpossble){
            if((i.y < chessPane.DIMENSION && i.y >= 0)
                    && (i.x < chessPane.DIMENSION && i.x >= 0))

                //대각선에 상대말이 잇을 경우 움직임
                if (((chessPane.grid[i.y][i.x].havePiece != null) &&
                        chessPane.playerColor != chessPane.grid[i.y][i.x].havePiece.color)
                        && chessPane.grid[i.y][i.x].havePiece.name.equals("knight"))
                    return 1;
        }

        int other = (this.color== Cor.white) ? 6 : 1;
        if(chessPane.grid[other][ps.x].havePiece.name.equals("king"))
            return 1;
        if(chessPane.grid[other][ps.x-1].havePiece.name.equals("pawn"))
            return 1;
        if(chessPane.grid[other][ps.x+1].havePiece.name.equals("pawn"))
            return 1;




        return 0;
    }
}
