import javax.swing.*;


enum Cor{
    black,
    white,

    none
}
public abstract class ChessPiece {

    protected Pos[] possble;

    //위치좌표
    protected Pos pos;

    //이미지
    protected ImageIcon pieceImg;

    //말의 종류
    protected String name;

    protected ChessPane chessPane;
    //말의 색
    protected Cor color;
    protected abstract void Move(Square sq);
//    protected abstract Boolean hit();
//    protected abstract Pos getPos();
//    protected abstract Pos setPos();
}
