import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.Vector;

public class Room extends JFrame implements Serializable {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Thread receiveThread;
    Vector<String> id = new Vector<>();
    int roomNum;
    JTextField chatField;
    JButton sendButton;
    JPanel chatPanel;
    JLabel[] label;
    JPanel gridPanel;
    String roomName;

    Vector<String> getIdVec() {
        return this.id;
    }

    void addUserId(String uid) {
        id.add(uid);
    }

    void disconnect(String id, String pw) {
        send(new Send(id, pw, Send.MODE_LOGOUT));
        try {
            receiveThread = null;
            socket.close();

        } catch (IOException e) {
            System.err.println("클라이언트 닫기 오류 " + e.getMessage());
            System.exit(-1);
        }
    }
    private void send(Send send) {
        try {
            out.writeObject(send);
            out.flush();
        } catch (IOException e) {
            System.out.println("클라이언트 일반 전송 오류" + e.getMessage());
        }
    }
    void connectToServer() {
        receiveThread = new Thread(new Runnable() {
            void receiveMessage() throws IOException {
                try {
                    Send inMsg = (Send) in.readObject();
                    switch (inMsg.mode) {
                        case Send.MODE_ENTER_HUMAN:// 동시성 제어
                                if (!id.contains(inMsg.userID)) {
                                    id.add(inMsg.userID);
                                } else {
                                    System.out.println("이미 존재하는 사용자 ID: " + inMsg.userID);
                                }
                            break;
                        case Send.MODE_IN_ME:
                            id = inMsg.idv;
                            newLabel();
                            break;
                    }
                } catch (IOException e) {
                    System.out.println("d");
                } catch (ClassNotFoundException e) {
                    System.out.println("잘못된 객체가 전달되었습니다.");
                }
            }

            @Override
            public void run() {
                while (receiveThread == Thread.currentThread()) {
                    try {
                        receiveMessage();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        receiveThread.start();
    }

    private ImageIcon resizeImage(ImageIcon originalImage, int targetWidth, int targetHeight) {
        Image img = originalImage.getImage();
        Image resizedImg = img.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }


    String getId() {
        return id.get(0);
    }

    String getRoomName() {
        return roomName;
    }

    JPanel createRoom(String id,int select, Socket socket, ObjectInputStream in, ObjectOutputStream out) throws IOException {
        if (!this.id.contains(id))
            this.id.add(id);
        this.in = in;
        this.out = out;
        this.socket = socket;
        this.roomNum = select;
        System.out.println(socket);
        System.out.println(in);
        System.out.println(out);
        System.out.println(id);
        send(new Send(id,roomName,roomNum,Send.MODE_IN_ME));

        setTitle("Room with Grid and Chat Panel");


        setLayout(new BorderLayout());
        initializeLabels();

        // 채팅 패널 생성 및 구성
        chatPanel = new JPanel(new BorderLayout());
        chatPanel.setPreferredSize(new Dimension(400, 75)); // 채팅 패널의 선호되는 크기를 조금 줄임
        chatField = new JTextField();

        sendButton = new JButton("Send");
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


        connectToServer();

        // 창을 닫았을 때 프로그램이 종료되도록 설정
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        return gridPanel;
    }
    private void configureLabelColor(JLabel label, int index) {
        if (index == 1) {
            label.setOpaque(true);
            label.setBackground(Color.red);
            label.setForeground(Color.yellow);
        } else if (index == 2) {
            label.setOpaque(true);
            label.setBackground(Color.blue);
            label.setForeground(Color.yellow);
        }
    }
    private void initializeLabels() {
        label = new JLabel[21];
        gridPanel = new JPanel(new GridLayout(10, 2)); // gridPanel을 초기화합니다.

        for (int i = 1; i < label.length; i++) {
            if (i <= id.size()) {
                label[i] = new JLabel(i + " " + id.get(i - 1));
            } else {
                label[i] = new JLabel(String.valueOf(i));
            }

            // 색상 설정
            configureLabelColor(label[i], i);

            gridPanel.add(label[i]);
        }
    }
    private void newLabel() {
        for (int i = 1; i < label.length; i++) {
            if (i <= id.size()) {
                label[i].setText(i + " " + id.get(i - 1));
            } else {
                label[i].setText(String.valueOf(i));
            }
        }
    }

    public Room(String roomName, String id) {
        this.roomName = roomName;
        if (!this.id.contains(id))
            this.id.add(id);

    }
}
