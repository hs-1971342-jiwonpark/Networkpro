import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.Vector;
public class RoomList {
    JFrame frame;
    int roomNum;
    String id;
    String pw;
    int select;
    private Socket socket;
    private ObjectOutputStream out;
    private Thread receiveThread;
    private ObjectInputStream in;
    static private Vector<String> roomList = new Vector<>();
    DefaultListModel<String> listModel;
    JList<String> roomLists;
    JScrollPane scrollPane;
    JLabel statusBar;

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
            void receiveMessage() {
                try {
                    Send inMsg = (Send) in.readObject();
                    // 메시지를 계속 읽어 들임
                    switch (inMsg.mode) {
                        case Send.MODE_CT_ROOM:
                            listModel.addElement(inMsg.roomName);
                            if(inMsg.userID.equals(id)) {
                                inMsg.room.createRoom(id,select, socket, in, out);
                                frame.dispose();
                            }
                            System.out.println("방생성 메세지옴");
                            break;
                        case Send.MODE_ENTER_ROOM:
                           // 동시성 제어
                                if (inMsg.userID.equals(id)) {
                                    Room room = inMsg.room;
                                    room.createRoom(id,select, socket, in, out);
                                    frame.dispose();
                                }
                            break;
                        case Send.MODE_DEL_ROOM:
                                listModel.remove(inMsg.roomNum);
                                System.out.println("방제거 메세지옴");
                            break;
                        case Send.MODE_ERROR:
                            System.out.println("error");
                            break;
                        case Send.MODE_IN_ROOM:
                            roomList = inMsg.roomList;
                            if (roomList.size() > 0) {
                                roomNum = roomList.size();
                                for (String name : roomList) {
                                    listModel.addElement(name);
                                }
                            }
                            System.out.println("방들어옴 메세지옴");
                            break;
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                } catch (ClassNotFoundException e) {
                    System.out.println("잘못된 객체가 전달되었습니다.");
                }
            }

            @Override
            public void run() {
                while (receiveThread == Thread.currentThread()) {
                    receiveMessage();
                }
            }
        });
        receiveThread.start();
    }
    RoomList(String id, String pw, Socket socket, ObjectInputStream in, ObjectOutputStream out) {

        if (socket == null || in == null || out == null) {
            throw new IllegalArgumentException("Socket and Streams must not be null");
        }
        this.out = out;
        this.in = in;
        this.socket = socket;
        this.id = id;
        this.pw = pw;

        frame = new JFrame("Game Lobby");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JPanel topPanel = new JPanel();
        JButton ok = new JButton("확인");
        JButton add = new JButton("추가");
        JButton del = new JButton("삭제");
        topPanel.add(ok);
        topPanel.add(add);
        topPanel.add(del);
        panel.add(topPanel, BorderLayout.NORTH);
        listModel = new DefaultListModel<>();
        roomLists = new JList<>(listModel);
        scrollPane = new JScrollPane(roomLists);
        panel.add(scrollPane, BorderLayout.CENTER);
        statusBar = new JLabel("Status: Connected");
        panel.add(statusBar, BorderLayout.SOUTH);

        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel dialogPanel = new JPanel();
                dialogPanel.add(new JLabel("Enter room name:"));
                JTextField roomNameField = new JTextField(20);
                dialogPanel.add(roomNameField);
                int result = JOptionPane.showConfirmDialog(
                        frame, // use the frame as the parent
                        dialogPanel,
                        "Add Room",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    String roomName = roomNameField.getText().trim();
                    if (!roomName.isEmpty()) {
                        Room room = new Room(roomName,id);
                        send(new Send(id, room, roomName, roomNum, Send.MODE_CT_ROOM)); //id,룸, 룸이름, 룸넘버, 룸생성코드
                    }
                }
            }
        });
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                select = roomLists.getSelectedIndex();
                if (select > -1)
                    send(new Send(id, select, Send.MODE_ENTER_ROOM)); // 사용자아이디, 선택, 들어가기모드코드
                System.out.println(listModel.get(select));
            }
        });
        del.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show an input dialog to get the room name to delete
                String roomNameToDelete = JOptionPane.showInputDialog(
                        frame,
                        "Enter the room name to delete:",
                        "Delete Room",
                        JOptionPane.PLAIN_MESSAGE);
                if (roomNameToDelete != null && !roomNameToDelete.trim().isEmpty()) {
                    // Check if the room name exists in the list model
                    boolean exists = false;
                    for (int i = 0; i < listModel.size(); i++) {
                        if (listModel.get(i).equals(roomNameToDelete.trim())) {
                            String delRoom = roomNameToDelete.trim();
                            JOptionPane.showMessageDialog(frame, "Room '" + roomNameToDelete + "' deleted.");
                            send(new Send(id,i,delRoom,Send.MODE_DEL_ROOM));
                            exists = true;
                            break;
                        }
                    }
                    // If the room name does not exist, show a message
                    if (!exists) {
                        JOptionPane.showMessageDialog(frame, "Room '" + roomNameToDelete + "' not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (roomNameToDelete != null) {
                    // If the user entered a blank name, show a warning message
                    JOptionPane.showMessageDialog(frame, "Room name cannot be blank.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        frame.setContentPane(panel);
        frame.setVisible(true);
        connectToServer();
        send(new Send(id, pw, Send.MODE_IN_ROOM));
    }

}
