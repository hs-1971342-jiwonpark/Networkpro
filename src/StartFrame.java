import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartFrame extends JFrame {
    StartFrame() {
        super("온라인 체스 게임");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 1000);

        // GridBagLayout 설정
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        ChessPane cp = ChessPane.getInstance();

        // ChessPane 설정
        gbc.gridx = 0; // 첫 번째 열
        gbc.gridy = 0; // 첫 번째 행
        gbc.gridwidth = 2; // 두 열 차지
        gbc.gridheight = 1; // 한 행 차지
        gbc.weightx = 0.75; // 가로 공간의 75% 차지
        gbc.weighty = 1.0; // 세로 공간의 100% 차지
        gbc.fill = GridBagConstraints.BOTH; // 가로 세로 모두 채우기
        add(cp, gbc);

        gbc.gridx = 2; // 세 번째 열
        gbc.gridwidth = 1; // 한 열 차지
        gbc.weightx = 0.25; // 가로 공간의 25% 차지
        gbc.fill = GridBagConstraints.BOTH;
        add(new ChatPanel(), gbc);

        gbc.gridx = 3; // 세 번째 열
        gbc.gridwidth = 1; // 한 열 차지
        gbc.weightx = 0.25; // 가로 공간의 25% 차지
        gbc.fill = GridBagConstraints.BOTH;
        JButton jb = new JButton("무르기");
        jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                cp.reprint();

            }
        });

        add(jb,gbc);


        setVisible(true);
    }
}
