import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class LoginFr extends JFrame {

    LoginFr(){
        super("Chess Game");
        setSize(800, 800); // 프레임 크기를 800x800으로 설정
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(createPane());
        setLocationRelativeTo(null); // 화면 가운데 정렬
        setVisible(true);
    }

    private ImageIcon resizeImage(ImageIcon originalImage, int targetWidth, int targetHeight) {
        Image img = originalImage.getImage();
        Image resizedImg = img.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }

    JPanel createPane (){
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    BufferedImage backgroundImage = ImageIO.read(getClass().getResource("chess.png")); // 이미지 로드
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        mainPanel.setLayout(new GridBagLayout()); // GridBagLayout 사용

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10); // 여백 추가
        gbc.anchor = GridBagConstraints.CENTER; // 중앙 정렬

        // ID 라벨 (굵게 설정)
        JLabel idLabel = new JLabel("ID : ");
        idLabel.setFont(new Font("Arial", Font.BOLD, 20)); // 굵게 설정
        mainPanel.add(idLabel, gbc);

        // ID 텍스트 필드
        gbc.gridx++;
        JTextField idTextField = new JTextField(20);
        idTextField.setPreferredSize(new Dimension(300, 40)); // 텍스트 필드 크기 설정
        mainPanel.add(idTextField, gbc);

        // PW 라벨 (굵게 설정)
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel passwordLabel = new JLabel("PW : ");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 20)); // 굵게 설정
        mainPanel.add(passwordLabel, gbc);

        // PW 텍스트 필드
        gbc.gridx++;
        JPasswordField pwTextField = new JPasswordField(20);
        pwTextField.setPreferredSize(new Dimension(300, 40)); // 텍스트 필드 크기 설정
        mainPanel.add(pwTextField, gbc);

        // "아이디가 없으세요?" 라벨 (가운데 정렬)
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2; // 라벨을 2열로 확장
        JLabel noIdLabel = new JLabel("아이디가 없으세요?");
        noIdLabel.setHorizontalAlignment(JLabel.CENTER); // 가운데 정렬
        mainPanel.add(noIdLabel, gbc);

        // "회원가입" 라벨 (가운데 정렬)
        gbc.gridy++;
        JButton signUpLabel = new JButton("회원가입");
        signUpLabel.setPreferredSize(new Dimension(150, 30)); // 작은 크기로 설정
        signUpLabel.setOpaque(false); // 투명 설정
        signUpLabel.setContentAreaFilled(false); // 내용 영역 투명하게 설정
        signUpLabel.setBorderPainted(false); // 테두리 제거
        mainPanel.add(signUpLabel, gbc);

        // 입장하기 버튼 (작게 설정)
        gbc.gridy++;
        gbc.gridwidth = 2; // 버튼을 2열로 확장
        JButton enterButton = new JButton("입장하기");
        enterButton.setPreferredSize(new Dimension(150, 30)); // 작은 크기로 설정
        enterButton.setOpaque(false); // 투명 설정
        enterButton.setContentAreaFilled(false); // 내용 영역 투명하게 설정
        enterButton.setBorderPainted(false); // 테두리 제거
        mainPanel.add(enterButton, gbc);

        return mainPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginFr();
        });
    }
}
