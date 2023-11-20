import javax.swing.*;


enum Cor{
    black, white
}
public abstract class ChessPiece {


    protected Pos pos;
    protected ImageIcon pieceImg;
    protected String name;
    protected Cor color;
    protected abstract void Move(JPanel chessPan);
    protected abstract Boolean hit();
    protected abstract Pos getPos();
    protected abstract Pos setPos();
}
