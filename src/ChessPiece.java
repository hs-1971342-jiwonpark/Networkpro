import javax.swing.*;

public abstract class ChessPiece {
    protected abstract String getName();
    protected abstract void Move(JPanel chessPan);
    protected ImageIcon pieceImg;
}
