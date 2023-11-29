import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ChatPanel extends JPanel {
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;

    private ChessPane cp = null;
    public ChatPanel() {
        createUI();
    }

    public ChatPanel(ChessPane cp) {
        this.cp = cp;
        createUI();
    }

    private void createUI() {

        setSize(350, 500);

        // 레이아웃 설정
        setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("굴림", Font.BOLD, 18)); // 채팅 영역 폰트 크기 설정
        chatArea.setBackground(Color.BLACK);
        chatArea.setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);

        // 입력 패널
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        inputField = new JTextField();
        inputField.setFont(new Font("굴림", Font.BOLD, 16)); // 폰트 크기 변경
        inputField.setPreferredSize(new Dimension(300, 30)); // 텍스트 필드 크기 조정
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendChatMessage();
            }
        });
        inputPanel.add(inputField, BorderLayout.CENTER);

        sendButton = new JButton("전송");
        sendButton.setFont(new Font("굴림",Font.BOLD,14));
        sendButton.setPreferredSize(new Dimension(70, 30)); // 버튼 크기 조정
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(int i=0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        System.out.println(cp.grid[i][j]);
                    }
                    System.out.println();
                }
                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println();
                //sendChatMessage();
                Square[][] a = new Square[8][8];
                a = cp.turn.get(cp.turn.size()-1);
                for(int i=0; i < 8; i++){
                    for(int j=0; j< 8; j++){

                        cp.grid[i][j] = a[i][j];
                        if(cp.grid[i][j].havePiece != null) {
                            System.out.println(cp.grid[i][j]);
                            cp.grid[i][j].setImage();
                            cp.grid[i][j].setIcon(cp.grid[i][j].havePiece.pieceImg);
                        }
                    }
                }
                System.out.println("성공");;
                for(int i=0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        System.out.println(cp.grid[i][j]);
                    }
                }
            }
        });
        inputPanel.add(sendButton, BorderLayout.EAST);


        add(inputPanel, BorderLayout.SOUTH);
    }

    private void sendChatMessage() {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            chatArea.append(message + "\n");
            inputField.setText("");
        }
    }


}
