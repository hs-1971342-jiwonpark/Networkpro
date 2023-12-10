import javax.swing.*;
import java.awt.*;

    public class Square extends JLabel{
        public ChessPiece havePiece = null;

        protected Pos pos;

        public void setImage() {
            Image image = new ImageIcon(this.havePiece.color+"_"+this.havePiece.name+".png").getImage();
            Image resizedImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            this.havePiece.pieceImg = new ImageIcon(resizedImage);

            setIcon(this.havePiece.pieceImg);
            setHorizontalAlignment(SwingConstants.CENTER);
            setVerticalAlignment(SwingConstants.CENTER);
        }

        public Square(Pos pos) {
            this.pos = pos;
            setLayout(null);
        }
        public Square(Pos pos, ChessPiece cp) {
            this(pos);
            this.havePiece = cp;
        }

        public void setPiece(ChessPiece cp){
            this.havePiece = cp;
            if(this.havePiece != null) {
                setImage();
            }
            else setIcon(null);
        }




    }
