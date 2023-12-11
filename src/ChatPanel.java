import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.Vector;
public class ChatPanel extends JPanel {
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private Thread acceptThread = null;
    private int port;
    private Vector<ClientHandler> users = new Vector<ClientHandler>();
    public ChatPanel() {
        createUI();
        this.port = port;
    }
    private class ClientHandler extends Thread {
        private ObjectOutputStream out;
        private Socket clientSocket;
        private String uid;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }
        void receiveMessages(Socket socket) {
            try {
                ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                String message;
                Send msg;
                while ((msg = (Send) in.readObject()) != null) {


                    if (msg.mode == Send.MODE_LOGIN) {
                        uid = msg.userID;
                        System.out.println("새 참가자 : " + uid);
                        System.out.println("현재 참가자 수 : " + users.size());
                        continue;
                    } else if (msg.mode == Send.MODE_LOGOUT) {
                        break;
                    } else if (msg.mode == Send.MODE_TX_STRING) {
                        message = uid + ": " + msg.message;
                        System.out.println(message);
                        broadcasting(msg);
                    } else if (msg.mode == Send.MODE_TX_IMAGE) {
                        System.out.println(uid + ": " + msg.message);
                        broadcasting(msg);
                    }
                }
                users.removeElement(this);
                System.out.println(uid + "퇴장. 현재 참가자 수: " + users.size());


            } catch (IOException e) {
                System.out.println(e.getMessage());
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                    users.remove(this);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        private void send(Send msg) {
            try {
                out.writeObject(msg);
                out.flush();
            } catch (IOException e) {
                System.err.println("클라이언트 일반 전송 오류" + e.getMessage());
            }
        }

        private void broadcasting(Send msg) {
            for (ClientHandler c : users)
                c.send(msg);
        }

        public void run() {
            receiveMessages(clientSocket);
        }
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
        sendButton.setFont(new Font("굴림", Font.BOLD, 14));
        sendButton.setPreferredSize(new Dimension(70, 30)); // 버튼 크기 조정
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendChatMessage();
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