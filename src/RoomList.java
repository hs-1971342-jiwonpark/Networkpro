import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.Vector;


public class RoomList {


    int roomNum;
    String id;
    String pw;
    int select;
    private static String serverAddress;
    private static int serverPort;
    private Socket socket;
    private ObjectOutputStream out;
    private Thread receiveThread;
    private ObjectInputStream in;
    static private Vector<String> roomList = null;

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
                        if (inMsg != null) {  // 메시지를 계속 읽어 들임
                            switch (inMsg.mode) {
                                case Send.MODE_CT_ROOM:
                                    listModel.addElement(inMsg.roomName);
                                    break;
                                case Send.MODE_ENTER_ROOM:
                                    new Room(inMsg.room,socket);
                                    break;
                                case Send.MODE_REMOVE_ROOM:
                                    if(inMsg.dodelete == true) {
                                        listModel.remove(listModel.size()-inMsg.selectIndex);
                                    }

                                    break;
                                case Send.MODE_IN_ROOM:
                                    if(inMsg.roomList.size() > 0)
                                        for (int i=0; i < inMsg.roomList.size(); i++) {
                                            String name = inMsg.roomList.get(i);
                                            listModel.addElement(name);
                                        }
                                    break;
                            }
                        }
                    } catch (IOException e) {
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




    RoomList(Socket socket,ObjectInputStream in,ObjectOutputStream out, String id, String pw) {
        this.out = out;
        this.in = in;
        this.socket = socket;
        this.id = id;
        this.pw = pw;
        System.out.println(socket);
        RoomlistGUI();

        connectToServer();
        send(new Send(id,pw,Send.MODE_IN_ROOM));

    }

    private void RoomlistGUI(){
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
        listModel = new DefaultListModel<>();
        roomLists = new JList<>(listModel);
        scrollPane = new JScrollPane(roomLists);
        panel.add(scrollPane, BorderLayout.CENTER);
        statusBar = new JLabel("Status: Connected");
        panel.add(statusBar, BorderLayout.SOUTH);

        //룸 생성
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

                        Room room = new Room(roomName, id);
                        System.out.println(Send.MODE_CT_ROOM);
                        send(new Send(id, room,roomName, listModel.size(), Send.MODE_CT_ROOM)); //id,룸, 룸이름, 룸넘버, 룸생성코드
                    }
                }
            }
        });

        //룸 참가
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                System.out.println(select);
                if (select > -1) {
                    select = listModel.size()-select;
                    send(new Send(id, select, Send.MODE_ENTER_ROOM)); // 사용자아이디, 선택, 들어가기모드코드

                    //System.out.println(listModel.get(select));
                }
            }
        });
        //룸 삭제?
        del.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (select > -1) {
                    send(new Send(id, select, Send.MODE_REMOVE_ROOM));
                    System.out.println(listModel.get(select));
                }
            }
        });

        frame.setContentPane(panel);
        frame.setVisible(true);

    }
}

