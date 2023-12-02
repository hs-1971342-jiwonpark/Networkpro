import javax.swing.*;
import java.awt.*;

public class Room extends JFrame {

    public Room() {
        // 창의 제목 설정
        setTitle("Room with Grid and Chat Panel");

        // 레이아웃을 BorderLayout으로 설정
        setLayout(new BorderLayout());

        // 그리드 패널 생성 및 구성
        JPanel gridPanel = new JPanel(new GridLayout(10 , 2));
        // 첫 번째 라벨 설정
        JLabel jb1 = new JLabel("1");
        jb1.setOpaque(true);
        jb1.setBackground(Color.red);
        jb1.setForeground(Color.yellow);
        gridPanel.add(jb1); // 그리드 패널에 라벨 추가
        // 첫 번째 라벨 설정
        JLabel jb2 = new JLabel("2");
        jb2.setOpaque(true);
        jb2.setBackground(Color.blue);
        jb2.setForeground(Color.yellow);
        gridPanel.add(jb2); // 그리드 패널에 라벨 추가

        // 2부터 8까지의 라벨 추가 (배경색 없음)
        for (int i = 3; i <= 20; i++) {
            JLabel label = new JLabel(Integer.toString(i));
            gridPanel.add(label);
        }

        // 채팅 패널 생성 및 구성
        JPanel chatPanel = new JPanel(new BorderLayout());
        chatPanel.setPreferredSize(new Dimension(400, 75)); // 채팅 패널의 선호되는 크기를 조금 줄임
        JTextField chatField = new JTextField();
        JButton sendButton = new JButton("Send");
        chatPanel.add(chatField, BorderLayout.CENTER);
        chatPanel.add(sendButton, BorderLayout.EAST);

        // 패널들을 프레임에 추가
        add(gridPanel, BorderLayout.CENTER);
        add(chatPanel, BorderLayout.SOUTH);

        // 창의 기본 크기 설정을 조금 줄임
        setSize(350, 400);

        // 창을 화면 가운데에 위치시키기
        setLocationRelativeTo(null);

        // 창을 보이게 설정
        setVisible(true);

        // 창을 닫았을 때 프로그램이 종료되도록 설정
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        // Room 클래스의 인스턴스를 생성하여 창을 띄움
        new Room();
    }
}
