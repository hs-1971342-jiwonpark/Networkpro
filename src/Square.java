import javax.swing.*;
import java.awt.*;

    public class Square extends JLabel{
        public ChessPiece havePiece = null;
        public boolean move_p = false;

        protected Pos pos;

        protected boolean first_move = true;

        public void setImage() {
            ImageIcon originalIcon = new ImageIcon(this.havePiece.color+"_"+this.havePiece.name+".png");
            Image image = originalIcon.getImage();
            Image resizedImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            this.havePiece.pieceImg = new ImageIcon(resizedImage);

            setIcon(this.havePiece.pieceImg);
            setHorizontalAlignment(SwingConstants.CENTER);
            setVerticalAlignment(SwingConstants.CENTER);
        }

        public Square(int y, int x) {
            this.pos = new Pos(y,x);
            setLayout(null);
        }
        public Square(int y, int x, ChessPiece cp) {
            this(y,x);
            this.havePiece = cp;
        }

        //깊은 복사를 위해 만듬
        public Square(Pos pos, ChessPiece cp) {
            this.pos = pos;
            //setLayout(null);
            this.havePiece = cp;
        }

        public void setPiece(ChessPiece cp){
            this.havePiece = cp;
            if(this.havePiece != null)
                setImage();
            else setIcon(null);
        }
        public void synchronization(){

        }


        private void resizeImage(int targetWidth, int targetHeight) {
            Image img = this.havePiece.pieceImg.getImage();
            Image resizedImg = img.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
            this.havePiece.pieceImg = new ImageIcon(resizedImg);
        }

        public Square newSquare(){
            Square sq = new Square(this.pos, this.havePiece);
            if(sq.havePiece != null) {
                sq.setImage();
                sq.setIcon(sq.havePiece.pieceImg);
                this.havePiece.pos = sq.pos;
            }
            else sq.setIcon(null);
            return sq;
        }

    }
