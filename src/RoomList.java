import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class RoomList {


    int roomNum = 0;
    String id;
    String pw;
    int select;
    private static String serverAddress;
    private static int serverPort;
    private Socket socket;
    private ObjectOutputStream out;
    private Thread receiveThread;
    private ObjectInputStream in;

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
        try {
            out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            receiveThread = new Thread(new Runnable() {
                private ObjectInputStream in;

                void receiveMessage() {
                    try {
                        Send inMsg = (Send) in.readObject();
                        while (inMsg == null) {  // 메시지를 계속 읽어 들임
                            return;
                        }
                        switch (inMsg.mode) {
                            case Send.MODE_CT_ROOM:

                                break;
                            case Send.MODE_ENTER_ROOM:

                                break;
                            case Send.MODE_REMOVE_ROOM:

                                break;
                        }
                    } catch (IOException e) {
                        System.out.println("연결을 종료했습니다.");
                    } catch (ClassNotFoundException e) {
                        System.out.println("잘못된 객체가 전달되었습니다.");
                    }
                }

                @Override
                public void run() {
                    try {
                        in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                    } catch (IOException e) {
                        System.out.println("입력 스트림이 열리지 않음");
                    }
                    while (receiveThread == Thread.currentThread()) {
                        receiveMessage();
                    }
                }
            });
            receiveThread.start();
        } catch (IOException e) {
            socket = new Socket();
            SocketAddress sa = new InetSocketAddress(serverAddress, serverPort);
            try {
                socket.connect(sa, 3000);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }


    RoomList(Socket socket) {
        this.socket = socket;
        JFrame frame = new JFrame("Game Lobby");
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
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> roomList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(roomList);
        panel.add(scrollPane, BorderLayout.CENTER);
        JLabel statusBar = new JLabel("Status: Connected");
        panel.add(statusBar, BorderLayout.SOUTH);

        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // JPanel to hold the label and text field

                JPanel dialogPanel = new JPanel();
                dialogPanel.add(new JLabel("Enter room name:"));

                // Create the text field for room name input
                JTextField roomNameField = new JTextField(20);
                dialogPanel.add(roomNameField);

                // Show the dialog on top of the frame
                int result = JOptionPane.showConfirmDialog(
                        frame, // use the frame as the parent
                        dialogPanel,
                        "Add Room",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE);

                // Handle the dialog result
                if (result == JOptionPane.OK_OPTION) {
                    String roomName = roomNameField.getText().trim();
                    if (!roomName.isEmpty()) {
                        listModel.addElement(roomName);
                        Room room = new Room(roomName);
                        send(new Send(room, roomName, roomNum, Send.MODE_CT_ROOM)); //룸, 룸이름, 룸넘버, 룸생성코드
                    }
                }
            }
        });
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                select = roomList.getSelectedIndex();
                if (select > -1)
                    send(new Send(id, select, Send.MODE_ENTER_ROOM)); // 사용자아이디, 선택, 들어가기모드코드
                new Room(listModel.get(select));
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
                            // If it exists, remove it
                            listModel.remove(i);
                            JOptionPane.showMessageDialog(frame, "Room '" + roomNameToDelete + "' deleted.");
                            //서버로 룸 없어진 정보 보내기
                            //서버에서 정보받기
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
    }

    public static void main(String[] args) {
        new RoomList(null);
    }
}
