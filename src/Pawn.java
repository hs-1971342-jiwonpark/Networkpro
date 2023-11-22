import javax.swing.*;

// 폰(Pawn) 클래스는 ChessPiece 클래스를 상속합니다.
public class Pawn extends ChessPiece {
    private ChessPane chessPane;

    Pawn(Cor cor, ChessPane chessPane) {
        this.name = "Pawn";
        this.color = cor;
        this.pieceImg = new ImageIcon("pawn.png");
        this.chessPane = chessPane;
    }

    protected void initPos() {
        for(int i=0;i<8;i++) {
            chessPane.setSquareAt(1, i, pieceImg);
            chessPane.setSquareAt(6, i, pieceImg);
        }
    }


    @Override
    protected void Move(JPanel chessPan) {

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
