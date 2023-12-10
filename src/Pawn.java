import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLOutput;
import java.util.Arrays;

// 폰(Pawn) 클래스는 ChessPiece 클래스를 상속합니다.
public class Pawn extends ChessPiece {

    StartFrame chessPane;

    ChessPane pane;
    Pawn(){
        this.name = "pawn";
    }

    Pawn(Cor cor, StartFrame chessPane) {
        this();
        this.color = cor;
        this.pieceImg = new ImageIcon(this.color.toString()+"_"+this.name+".png");
        this.chessPane = chessPane;
    }
    Pawn(Cor cor, ChessPane chessPane) {
        this();
        this.color = cor;
        this.pieceImg = new ImageIcon(this.color.toString()+"_"+this.name+".png");
        this.pane = chessPane;
    }
    Pawn(Pos pos, Cor cor) {
        this();
        this.color = cor;
        this.pos =pos;
    }

    Pawn(Pos pos, Cor cor, StartFrame chessPane) {
        this(cor, chessPane);
        this.pos = pos;
        this.possble= this.color.equals(Cor.black) ?
                new Pos[]{new Pos(this.pos.y + 1, this.pos.x), new Pos(this.pos.y + 1, this.pos.x + 1), new Pos(this.pos.y + 1, this.pos.x - 1), new Pos(this.pos.y + 2, this.pos.x)}
                : new Pos[]{new Pos(this.pos.y - 1, this.pos.x),new Pos(this.pos.y - 1, this.pos.x +1),new Pos(this.pos.y - 1, this.pos.x -1),new Pos(this.pos.y - 2, this.pos.x)};
        if(!(this.pos.y == 1 || this.pos.y == 6))
            this.possble = Arrays.copyOfRange(this.possble, 0, this.possble.length-1);
    }

    protected void initPos() {
        int j = (this.color == Cor.white) ? 6 : 1;
        for(int i=0;i<8;i++) {
            this.pos = new Pos(j,i);
            pane.grid[j][i].setPiece(new Pawn(this.pos,this.color));
        }
    }


    @Override
    protected void Move(Square first) {
        if(chessPane.grid[this.pos.y][this.pos.x].getBackground() == Color.red){
            int moveY;
            if(this.color == Cor.white) moveY = 3;
            else moveY = 4;
            System.out.println("지우기 코드 시작");
            int En_Passant_type = isEn_Passant(first.pos);
            System.out.println(En_Passant_type);
            if(En_Passant_type==1){
                System.out.println("왼쪽 코드 시작");
                chessPane.grid[first.pos.y][this.pos.x].havePiece = null;
                chessPane.grid[first.pos.y][this.pos.x].setIcon(null);
            }
            else if(En_Passant_type==2){
                System.out.println("왼쪽 코드 시작");
                chessPane.grid[first.pos.y][this.pos.x].havePiece = null;
                chessPane.grid[first.pos.y][this.pos.x].setIcon(null);
            }
            //프로모션

            chessPane.grid[this.pos.y][this.pos.x].havePiece = first.havePiece;
            chessPane.grid[first.pos.y][first.pos.x].havePiece = null;
            if(this.pos.y == ((chessPane.playerColor == Cor.white)? 0 : 7)){
                //프로모션 함수 호출
                System.out.println("프로모션 시작");
                promotion();
            }
            chessPane.grid[first.pos.y][first.pos.x].setIcon(null);
            chessPane.grid[this.pos.y][this.pos.x].setImage();
        }
    }
    protected void Move_possible() {
        int first = (this.color == Cor.white) ? 1 :  -1;
        for(Pos i:possble){
            if((i.y < chessPane.DIMENSION && i.y >= 0)
                    && (i.x < chessPane.DIMENSION && i.x >= 0))

                //대각선 이동 통제
                if(this.pos.x == i.x) {
                    //움직일 곳이 비여있으면
                    if ((chessPane.grid[i.y][i.x].havePiece == null)){
                        //만약 first값이 증감하면, 2칸 움직일 수 잇음
                        //다만 그 전칸에(1칸 이동한 위치)에 아무것도 없을 경우 움직여라
                        if(i.y == this.pos.y -2 || i.y == this.pos.y + 2){
                            if(chessPane.grid[(i.y + first)][i.x].havePiece == null) {
                                chessPane.grid[i.y][i.x].setBackground(Color.red);
                            }
                        }
                        else
                            chessPane.grid[i.y][i.x].setBackground(Color.red);
                    }

                }
                else {//대각선에 상대말이 잇을 경우 움직임
                    if ((chessPane.grid[i.y][i.x].havePiece != null) &&
                            chessPane.playerColor != chessPane.grid[i.y][i.x].havePiece.color)
                        chessPane.grid[i.y][i.x].setBackground(Color.red);
                }

                //앙파상 + 색칠항 위치의 y축 리턴
        }
        int moveY;
        if(this.color == Cor.white) moveY = 2;
        else moveY = 5;
        if(isEn_Passant(this.pos)==1){
            chessPane.grid[moveY][this.pos.x-1].setBackground(Color.red);
        }
        //오른쪽 앙파상
        else if(isEn_Passant(this.pos)==2){
            chessPane.grid[moveY][this.pos.x+1].setBackground(Color.red);
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
    protected int isEn_Passant(Pos ps){
        int start;
        int end;
        //색칠될 y축 계산
        if(this.color == Cor.white){
            end = 3;
            start = 1;
        }
        else {
            end = 4;
            start = 6;
        }
        if(ps.y != end) {
            System.out.println("ps.y 오류");
            return 0;
        }
        ChessPiece[][] lastcp;
        if(chessPane.turn.size()-1 ==0) {
            System.out.println("turn 사이즈 오류");
            return 0;
        }
        lastcp = (ChessPiece[][])chessPane.turn.elementAt(chessPane.turn.size()-1);

        //색에 따라 기준선 달리정함
        //내가 선택한 폰이 대각선 움직일때, 배열범위 벗어나지 못하게
        if(0<= ps.x-1){
            //내가 마지막으로 움직였을때 기준,상대 폰이 시작지점에 있는지 위치 체크
            if(chessPane.grid[ps.y][ps.x-1].havePiece!=null &&lastcp[start][ps.x-1]!=null ){
                if((chessPane.grid[ps.y][ps.x-1].havePiece.name).equals("pawn")&&(lastcp[start][ps.x-1].name).equals("pawn")) {
                    System.out.println("왼쪽1");
                    return 1;
                }
            }
        }

        //내가 선택한 폰이 대각선 움직일때, 배열범위 벗어나지 못하게
        if(ps.x+1 < 8){
            if(chessPane.grid[ps.y][ps.x+1].havePiece!=null &&lastcp[start][ps.x+1]!=null ){
                if((chessPane.grid[ps.y][ps.x+1].havePiece.name).equals("pawn")&&(lastcp[start][ps.x+1].name).equals("pawn")) {
                    System.out.println("오른쪽2");
                    return 2;
                }
            }
        }
        System.out.println("앙파상 끝");
        return 0;
    }


    public void promotion(){
        JButton pawnButton = new JButton("", new ImageIcon(chessPane.playerColor+"_pawn.png"));
        JButton rookButton = new JButton("", new ImageIcon(chessPane.playerColor+"_rook.png"));
        JButton bishopButton = new JButton("", new ImageIcon(chessPane.playerColor+"_bishop.png"));
        JButton queenButton = new JButton("", new ImageIcon(chessPane.playerColor+"_queen.png"));
        JButton knightButton = new JButton("", new ImageIcon(chessPane.playerColor+"_knight.png"));
        Object[] options = { pawnButton, rookButton, bishopButton, knightButton, queenButton };
        pawnButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chessPane.grid[pos.y][pos.x].havePiece.name = "pawn";
                JOptionPane.getRootFrame().dispose();
            }
        });

        rookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chessPane.grid[pos.y][pos.x].havePiece.name = "rook";
                JOptionPane.getRootFrame().dispose();
            }
        });

        bishopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chessPane.grid[pos.y][pos.x].havePiece.name = "bishop";
                JOptionPane.getRootFrame().dispose();
            }
        });

        knightButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chessPane.grid[pos.y][pos.x].havePiece.name = "knight";
                JOptionPane.getRootFrame().dispose();
            }
        });

        queenButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chessPane.grid[pos.y][pos.x].havePiece.name = "queen";
                JOptionPane.getRootFrame().dispose();
            }
        });

        int choice = JOptionPane.showOptionDialog(
                JOptionPane.getRootFrame(),
                "프로모션할 이미지 선택",
                "프로모션",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.DEFAULT_OPTION,
                null,
                options,
                null
        );




        System.out.println("프로모션 끝, 선택 값:" + choice);
    }
}
