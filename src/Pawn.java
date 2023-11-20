import javax.swing.*;

// 폰(Pawn) 클래스는 ChessPiece 클래스를 상속합니다.
public class Pawn extends ChessPiece {
    private String name;          // 말의 이름
    private boolean firstTurn;    // 첫 번째 움직임 여부
    private Pos position;         // 현재 위치
    private Boolean BW;            // 흑백 여부
    private ImageIcon pieceImg;   // 말의 이미지


    // 폰의 생성자
    public Pawn(int x, int y, Boolean bw) {
        this.name = "Pawn";
        this.firstTurn = true;
        this.position = new Pos(x, y);
        this.BW = bw;
        // 폰의 이미지 경로를 설정합니다.
        this.pieceImg = new ImageIcon("path_to_pawn_image.png");
    }

    // 말의 이름을 반환하는 메서드
    @Override
    protected String getName() {
        return this.name;
    }

    // 폰의 이동을 처리하는 메서드
    @Override
    protected void Move(JPanel chessPan) {
        if (this.firstTurn) {
            // 첫 번째 움직임인 경우 2칸 이동 가능
            if (isValidMove(position.x, position.y + (BW ? 2 : -2))) {
                position.y += (BW ? 2 : -2);
                this.firstTurn = false;
            }
        } else {
            // 그 이후에는 1칸씩 이동
            if (isValidMove(position.x, position.y + (BW ? 1 : -1))) {
                position.y += (BW ? 1 : -1);
            }
        }
        // GUI를 업데이트하는 코드 (필요 시 주석 해제)
        // UpdateChessboard(chessPan);
    }
    // 이동이 유효한지 확인하는 메서드
    private boolean isValidMove(int x, int y) {
        // 여기에 이동이 유효한지 확인하는 로직을 추가하세요.
        // 체스보드 경계 내에서의 이동만 허용하는 샘플 유효성 검사
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    // 추가적인 메서드 및 로직은 필요에 따라 추가할 수 있습니다.
}
