
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Vector;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class Room {

    JFrame jf;
    JTextField chatField;
    JButton sendButton;
    JPanel chatPanel;
    JLabel[] label = new JLabel[2];
    JPanel gridPanel = new JPanel();
    String id;
    String pw;

    Cor cor;
    private Socket socket;
    private ObjectOutputStream out;
    private Thread receiveThread;
    private ObjectInputStream in;
    static private Vector<String> userList = new Vector<String>();

    private int turn;

    private void send(Send msg) {
        try {
            out.writeObject(msg);
            out.flush();
        } catch (IOException e) {
            System.err.println("클라이언트 일반 전송 오류" + e.getMessage());
        }
    }

    void connectToServer() {
        // 서버에 연결하는 로직 구현
        receiveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Send inMsg;
                    while ((inMsg = (Send) in.readObject()) != null) {
                        if (inMsg.mode == Send.MODE_RETURN) {
                            if(id.equals(inMsg.userID)) {
                                turn = inMsg.turn;
                            }
                            for (String a : inMsg.users) {
                                if (!userList.contains(a)) {
                                    userList.add(a);
                                }
                            }
                            System.out.println(inMsg.cor);
                            if (userList.size()%2==1) {
                                sendButton.setText("Ready");
                            } else {
                                sendButton.setText("Go");
                            }
                            initializeLabels();

                        } else if (inMsg.mode == Send.MODE_ENTER_HUMAN) {

                        }
                        else if(inMsg.mode==Send.RESULT_OK){
                            System.out.println("시작버튼 받음");
                            System.out.println(inMsg.cor);
                            if (inMsg.cor == Cor.black)
                                Thread.sleep(100);
                            new StartFrame(socket,in,out,inMsg.cor);
                            Thread.currentThread().interrupt();
                            //exit();
                            break;
                        }
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    System.out.println("잘못된 객체가 전달되었습니다.");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        receiveThread.start();
    }

    Room(String id, String pw, Socket socket, ObjectInputStream in, ObjectOutputStream out) {

        this.out = out;
        this.in = in;
        this.socket = socket;
        this.id = id;
        this.pw = pw;
        createRoom();
    }

    void createRoom() {
        jf = new JFrame();
        jf.setTitle("Room with Grid and Chat Panel");


        jf.setLayout(new BorderLayout());
        gridPanel.setLayout(new GridLayout(0, 2)); // 레이아웃 재설정
        initializeLabels();
        // 채팅 패널 생성 및 구성
        chatPanel = new JPanel(new BorderLayout());
        chatPanel.setPreferredSize(new Dimension(400, 75)); // 채팅 패널의 선호되는 크기를 조금 줄임
        chatField = new JTextField();

        sendButton = new JButton();
        chatPanel.add(chatField, BorderLayout.CENTER);
        chatPanel.add(sendButton, BorderLayout.EAST);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(sendButton.getText().equals("Go"))
                    send(new Send(turn,Send.RESULT_OK));
            }
        });


        // 패널들을 프레임에 추가
        jf.add(gridPanel, BorderLayout.CENTER);
        jf.add(chatPanel, BorderLayout.SOUTH);

        // 창의 기본 크기 설정을 조금 줄임
        jf.setSize(350, 400);

        // 창을 화면 가운데에 위치시키기
        jf.setLocationRelativeTo(null);
        // 창을 보이게 설정
        jf.setVisible(true);
        connectToServer();
        send(new Send(id, userList, Send.MODE_IN_ROOM));
        jf.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void configureLabelColor(JLabel label, int index) {
        if (index == 0) {
            label.setOpaque(true);
            label.setBackground(Color.white);
            label.setForeground(Color.black);
        } else if (index == 1) {
            label.setOpaque(true);
            label.setBackground(Color.black);
            label.setForeground(Color.white);
        }
    }

    public void initializeLabels() {

        gridPanel.removeAll(); // 기존 레이블 제거


        int labelCount = Math.max(2, userList.size()); // 최소 2개의 레이블을 유지
        label = new JLabel[labelCount]; // 레이블 배열을 labelCount 크기에 맞게 초기화

        for (int i = 0; i < labelCount; i++) {
            if (i < userList.size()) {
                label[i] = new JLabel("i: " + userList.get(i).toString()); // userList의 요소를 사용하여 레이블 생성
            } else {
                label[i] = new JLabel(String.valueOf(i + 1)); // 사용자가 없을 경우 순서 번호만 표시
            }
            configureLabelColor(label[i], i % 2); // 색상 설정
            gridPanel.add(label[i]); // gridPanel에 레이블 추가
        }

        gridPanel.revalidate(); // 레이아웃 업데이트
        gridPanel.repaint(); // 다시 그리기
    }

    public void exit() {
        jf.dispose();
    }

}