//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.*;
//import java.net.Socket;
//import java.util.Arrays;
//import java.util.Vector;
//
//import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
//
//public class RoomList implements Serializable {
//    JFrame frame;
//    int roomNum;
//    int roomSize;
//    String id;
//    String pw;
//
//    int select;
//
//
//    JTextField chatField;
//    JButton sendButton;
//    JPanel chatPanel;
//    JLabel[] label = new JLabel[21];
//    JPanel gridPanel;
//    String roomName;
//
//
//    private Socket socket;
//    private ObjectOutputStream out;
//    private Thread receiveThread;
//    private ObjectInputStream in;
//    static private Vector<String> roomList = new Vector<>();
//    static private Vector<Vector<String>> idv = new Vector<Vector<String>>();
//    DefaultListModel<String> listModel;
//    JList<String> roomLists;
//    JScrollPane scrollPane;
//    JLabel statusBar;
//
//    private void send(Send msg) {
//        try {
//            out.writeObject(msg);
//            out.flush();
//        } catch (IOException e) {
//            System.err.println("클라이언트 일반 전송 오류" + e.getMessage());
//        }
//    }
//    void connectToServer() {
//        // 서버에 연결하는 로직 구현
//        receiveThread = new Thread(new Runnable() {
//            void receiveMessage() {
//                try {
//                    Send inMsg = (Send) in.readObject();
//                    // 메시지를 계속 읽어 들임
//                    if(inMsg != null) {
//                        switch (inMsg.mode) {
//                            case Send.MODE_TX_IMAGE: //방입장
//                                System.out.println("선택 후 받기 성공");
//                                send(new Send(id, inMsg.roomNum, inMsg.roomName, Send.MODE_IN_ME));
//                                System.out.println("입장 알리기 전송");
//                                break;
//                            case Send.MODE_ENTER_HUMAN:// 동시성 제어
//                                System.out.println("입장 알리기 성공");
//                                System.out.println("받은 ID: " + inMsg.userID);
//                                System.out.println("받은 idv Size: " + inMsg.idv.size());
//                                if(id.equals(inMsg.userID)) {
//                                    idv = inMsg.idv;
//                                    System.out.println("방생성");
//                                    System.out.println("들어갈 방: "+inMsg.roomList.size());
//                                    System.out.println("inmsg.roomNum: "+ inMsg.roomNum+1);
//                                    createRoom(inMsg.userID, inMsg.roomNum);
//                                }
//                                    newLabel(inMsg.roomNum);
//                                break;
//                            case Send.MODE_TX_FILE:
//                                idv = inMsg.idv;
//                                System.out.println("rm사이즈:" + inMsg.roomNum);
//                                listModel.addElement(inMsg.roomName);
//                                roomList = inMsg.roomList;
//                                System.out.println("서버에게 받은 roomlist크기: " + Arrays.deepToString(inMsg.roomList.toArray()));
//                                System.out.println("방 생성 후 roomlist크기: " + Arrays.deepToString(roomList.toArray()));
//                                System.out.println(roomList.size());
//                                if (inMsg.userID.equals(id)) {
//                                    createRoom(inMsg.userID, inMsg.roomNum-1);
//                                    newLabel(inMsg.roomNum);
//                                    frame.setVisible(false);
//                                }
//                                break;
//                            case Send.MODE_DEL_ROOM:
//                                listModel.remove(inMsg.roomNum);
//                                System.out.println("방제거 메세지옴");
//                                break;
//                            case Send.MODE_ERROR:
//                                System.out.println("error");
//                                break;
//                            case Send.MODE_IN_ROOM:
//                                initializeLabels();
//                                idv = inMsg.idv;
//                                roomList = inMsg.roomList;
//                                if (roomList.size() > 0) {
//                                    for (String name : roomList) {
//                                        listModel.addElement(name);
//                                    }
//                                }
//                                System.out.println("방들어옴 메세지옴");
//                                break;
//                        }
//                    }
//                } catch (IOException e) {
//                    System.out.println(e.getMessage());
//                } catch (ClassNotFoundException e) {
//                    System.out.println("잘못된 객체가 전달되었습니다.");
//                }
//            }
//
//            @Override
//            public void run() {
//                while (receiveThread == Thread.currentThread()) {
//                    receiveMessage();
//                }
//            }
//        });
//        receiveThread.start();
//    }
//    RoomList(String id, String pw, Socket socket, ObjectInputStream in, ObjectOutputStream out) {
//
//
//        if (socket == null || in == null || out == null) {
//            throw new IllegalArgumentException("Socket and Streams must not be null");
//        }
//        this.out = out;
//        this.in = in;
//        this.socket = socket;
//        this.id = id;
//        this.pw = pw;
//
//        frame = new JFrame("Game Lobby");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(600, 400);
//        JPanel panel = new JPanel();
//        panel.setLayout(new BorderLayout());
//        JPanel topPanel = new JPanel();
//        JButton ok = new JButton("확인");
//        JButton add = new JButton("추가");
//        JButton del = new JButton("삭제");
//        topPanel.add(ok);
//        topPanel.add(add);
//        topPanel.add(del);
//        panel.add(topPanel, BorderLayout.NORTH);
//        listModel = new DefaultListModel<>();
//        roomLists = new JList<>(listModel);
//        scrollPane = new JScrollPane(roomLists);
//        panel.add(scrollPane, BorderLayout.CENTER);
//        statusBar = new JLabel("Status: Connected");
//        panel.add(statusBar, BorderLayout.SOUTH);
//
//        add.addActionListener(new ActionListener() { // 방추가
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                JPanel dialogPanel = new JPanel();
//                dialogPanel.add(new JLabel("Enter room name:"));
//                JTextField roomNameField = new JTextField(20);
//                dialogPanel.add(roomNameField);
//                int result = JOptionPane.showConfirmDialog(
//                        frame, // use the frame as the parent
//                        dialogPanel,
//                        "Add Room",
//                        JOptionPane.OK_CANCEL_OPTION,
//                        JOptionPane.PLAIN_MESSAGE);
//                if (result == JOptionPane.OK_OPTION) {
//                    String roomName = roomNameField.getText().trim();
//                    if (!roomName.isEmpty()) {
//                        System.out.println("방생성: " + roomList.size());
//                        send(new Send(id,roomList,roomName,idv, roomList.size(),Send.MODE_CT_ROOM)); //id,룸, 룸이름, 룸넘버, 룸생성코드
//                    }
//                }
//            }
//        });
//        ok.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                select = roomLists.getSelectedIndex();
//                if (select > -1) {
//                    System.out.println("선택전송");
//                    send(new Send(id, roomList, roomName, idv, select, Send.MODE_ENTER_ROOM));
//                    System.out.println("선택 전송 성공");
//                    System.out.println(listModel.get(select));
//                }
//            }
//        });
//        del.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                // Show an input dialog to get the room name to delete
//                String roomNameToDelete = JOptionPane.showInputDialog(
//                        frame,
//                        "Enter the room name to delete:",
//                        "Delete Room",
//                        JOptionPane.PLAIN_MESSAGE);
//                if (roomNameToDelete != null && !roomNameToDelete.trim().isEmpty()) {
//                    // Check if the room name exists in the list model
//                    boolean exists = false;
//                    for (int i = 0; i < listModel.size(); i++) {
//                        if (listModel.get(i).equals(roomNameToDelete.trim())) {
//                            String delRoom = roomNameToDelete.trim();
//                            JOptionPane.showMessageDialog(frame, "Room '" + roomNameToDelete + "' deleted.");
//                            send(new Send(id,i,delRoom,Send.MODE_DEL_ROOM));
//                            exists = true;
//                            break;
//                        }
//                    }
//                    // If the room name does not exist, show a message
//                    if (!exists) {
//                        JOptionPane.showMessageDialog(frame, "Room '" + roomNameToDelete + "' not found.", "Error", JOptionPane.ERROR_MESSAGE);
//                    }
//                } else if (roomNameToDelete != null) {
//                    // If the user entered a blank name, show a warning message
//                    JOptionPane.showMessageDialog(frame, "Room name cannot be blank.", "Error", JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        });
//        frame.setContentPane(panel);
//        frame.setVisible(true);
//        connectToServer();
//        send(new Send(id, pw, Send.MODE_IN_ROOM));
//    }
//
//
//
//
//
//
//
//
//
//
//    Vector<Vector<String>> getIdVec() {
//        return this.idv;
//    }
//
//
//    String getRoomName() {
//        return roomName;
//    }
//
//    void createRoom(String id,int select) {
//        System.out.println(Arrays.deepToString(idv.toArray()));
//        if(idv.get(select)!= null)
//            if (!idv.get(select).contains(id))
//                idv.get(select).add(id);
//
//
//
//        this.roomNum = select;
//
//
//        JFrame jf = new JFrame();
//        jf.setTitle("Room with Grid and Chat Panel");
//
//
//        jf.setLayout(new BorderLayout());
//        // 채팅 패널 생성 및 구성
//        chatPanel = new JPanel(new BorderLayout());
//        chatPanel.setPreferredSize(new Dimension(400, 75)); // 채팅 패널의 선호되는 크기를 조금 줄임
//        chatField = new JTextField();
//
//        sendButton = new JButton("Send");
//        chatPanel.add(chatField, BorderLayout.CENTER);
//        chatPanel.add(sendButton, BorderLayout.EAST);
//
//        // 패널들을 프레임에 추가
//        jf.add(gridPanel, BorderLayout.CENTER);
//
//        jf.add(chatPanel, BorderLayout.SOUTH);
//
//        // 창의 기본 크기 설정을 조금 줄임
//        jf.setSize(350, 400);
//
//        // 창을 화면 가운데에 위치시키기
//        jf.setLocationRelativeTo(null);
//
//        // 창을 보이게 설정
//        jf.setVisible(true);
//
//
//        connectToServer();
//
//        // 창을 닫았을 때 프로그램이 종료되도록 설정
//        jf.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//    }
//    private void configureLabelColor(JLabel label, int index) {
//        if (index == 1) {
//            label.setOpaque(true);
//            label.setBackground(Color.red);
//            label.setForeground(Color.yellow);
//        } else if (index == 2) {
//            label.setOpaque(true);
//            label.setBackground(Color.blue);
//            label.setForeground(Color.yellow);
//        }
//    }
//    private void initializeLabels() {
//        gridPanel = new JPanel(new GridLayout(10, 2)); // gridPanel을 초기화합니다.
//
//        for (int i = 1; i < label.length; i++) {
//            if (i <= idv.size()) {
//                label[i] = new JLabel(i + " " + idv.get(i - 1));
//            } else {
//                label[i] = new JLabel(String.valueOf(i));
//            }
//
//            // 색상 설정
//            configureLabelColor(label[i], i);
//
//            gridPanel.add(label[i]);
//        }
//    }
//    private void newLabel(int rm) {
//        Vector<String> in_user = new Vector<>();
//        if(idv.size()>0) {
//            in_user = idv.get(rm-1);
//            System.out.println("in_user: "+Arrays.deepToString(in_user.toArray()));
//        }
//        else{
//            return;
//        }
//        for (int i = 1; i < label.length; i++) {
//            if (in_user.size()>0) {
//                if (i <= in_user.size()) {
//                    label[i].setText(i + " " + in_user.get(i-1));
//                } else {
//                    label[i].setText(String.valueOf(i));
//                }
//            }
//            else{
//                System.out.println("rm이 없음!!!!");
//                break;
//            }
//        }
//        System.out.println("newLabel 끝");
//    }
//
//
//
//
//}