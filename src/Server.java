import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;
public class Server extends JFrame {
    private int port;
    private ServerSocket serverSocket;

    private Process process;
    private JTextField t_input = new JTextField();
    private JTextArea t_display = new JTextArea();
    private JButton b_connect = new JButton("서버 시작");
    private JButton b_disconnect = new JButton("서버 종료");
    private JButton b_send = new JButton("보내기");
    private JButton b_exit = new JButton("종료하기");

    private ArrayList<Room> rooms = new ArrayList<Room>();
    private Vector<String> rooms1 = new Vector<String>();
    private Thread acceptThread = null;
    private Vector<ClientHandler> users = new Vector<ClientHandler>();
    public Server(int port) {
        super("체스 서버");
        this.port = port;
        this.process = new Process(ChessPane.getInstance());
        buildGUI();
        setLocation(400,0);
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    JPanel createInputPanel() {
        JPanel inputPanel = new JPanel();
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());
        southPanel.add(createControlPanel(), BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
        return inputPanel;
    }
    void buildGUI() {
        b_send.setEnabled(false);
        b_exit.setEnabled(true);
        b_connect.setEnabled(true);
        b_disconnect.setEnabled(false);
        setLayout(new BorderLayout());
        createDisplayPanel();
        createInputPanel();
    }

    void createDisplayPanel() {
        JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new BorderLayout());
        t_display.setEditable(false);
        displayPanel.add(new JScrollPane(t_display), BorderLayout.CENTER);
        add(displayPanel, BorderLayout.CENTER);
        displayPanel.setVisible(true);
    }


    JPanel createControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(1, 3));
        controlPanel.add(b_connect);
        controlPanel.add(b_disconnect);
        controlPanel.add(b_exit);
        b_connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                acceptThread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        startServer();

                    }

                });
                acceptThread.start();
                b_exit.setEnabled(false);
                b_connect.setEnabled(false);
                b_disconnect.setEnabled(true);
            }
        });
        b_disconnect.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                disconnect();

                b_send.setEnabled(false);
                b_exit.setEnabled(true);
                b_connect.setEnabled(true);
                b_disconnect.setEnabled(false);
            }
        });
        b_exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        return controlPanel;
    }
    void disconnect(){
        try {
            acceptThread = null;
            serverSocket.close();

        } catch (IOException e) {
            printDisplay("접속 종료 오류발생\n");
        }
    }


    void startServer() {
        try {
            serverSocket = new ServerSocket(port);
            printDisplay("서버가 시작되었습니다.\n");
            while (acceptThread == Thread.currentThread()) {
                printDisplay("클라이언트가 연결되었습니다.");
                ClientHandler cHandler = new ClientHandler(serverSocket.accept());
                cHandler.start();
                users.add(cHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void printDisplay(String msg) {
        t_display.append(msg+"\n");
        t_display.setCaretPosition(t_display.getDocument().getLength());
    }




    private class ClientHandler extends Thread{
        private ObjectOutputStream out;
        private Socket clientSocket;
        private String uid;
        private String upw;

        private Room room = null;

        String getUid(){
            return uid;
        }

        public ClientHandler(Socket clientSocket) {
            this.clientSocket  = clientSocket;
            b_send.setEnabled(true);
            t_input.setEnabled(true);
        }

        void receiveMessages(Socket socket) {
            try {
                ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                String message;
                Send msg;
                while((msg = (Send)in.readObject())!=null) {
                    System.out.println(msg.mode);
                    if(msg.mode == Send.MODE_LOGIN) {//로그인
                        uid = msg.userID;
                        upw = msg.userPW;
                        printDisplay("새 참가자 : "+uid);
                        printDisplay("현재 참가자 수 : "+ users.size());
                        send(msg);
                        continue;
                    }
                    else if(msg.mode == Send.MODE_LOGOUT) {
                        break;
                    }
                    else if(msg.mode == Send.MODE_CT_ROOM){
                        rooms.add(msg.roomNum,msg.room);
                        broadcasting(msg);
                    }
                    else if(msg.mode == Send.MODE_TX_STRING) {
                        message = uid + ": "+ msg.message;
                        printDisplay(message);
                        broadcasting(msg);

                    }
                    else if(msg.mode == Send.MODE_ENTER_ROOM){
                        //룸 참가?
                        msg.setRoom(rooms.get(msg.selectIndex));
                        this.room = msg.room;
                        sendAd(msg);
                    }
                    else if (msg.mode == Send.MODE_REMOVE_ROOM) {//룸 제거
                        //코드 문제 있음
                        if(msg.userID.equals(rooms.get(msg.selectIndex).host)) {
                            rooms.remove(msg.selectIndex);
                            msg.dodelete = true;
                            broadcasting(msg);
                        }
                        else {
                            send(msg);
                        }
                        printDisplay(uid+": "+msg.message);
                    }
                    else if (msg.mode == Send.MODE_IN_ROOM) {//로그인 한 후 룸리스트 들어가면.
                        for(int i=0; i< rooms.size(); i++){
                            rooms1.add(rooms.get(i).roomName);
                        }
                        send(new Send(rooms1, Send.MODE_IN_ROOM));

                    } else if (msg.mode == Send.MODE_TX_POS) {
                        if(msg.isFirstClick && msg.cor == process.pane.playerColor){
                            if(process.Check_first_click(msg.getPos())==1){
                                send((new Send(process.colors,Send.MODE_TX_BACKGROUND)));
                            }
                        }
                        else if(!msg.isFirstClick && msg.cor == process.pane.playerColor){
                            int returnValue = process.Check_second_click(msg.getPos());
                            if(returnValue ==1){
                                broadcasting((new Send(process.pane.turn.lastElement(),process.colors,Send.MODE_TX_ChessPiece)));
                                process.pane.playerColor = (process.pane.playerColor == Cor.white)? Cor.black: Cor.white;
                            } else if (returnValue==2) {
                                //잘못된 것을 누르면 아무것도 안함
                            }  else if (returnValue==3) {
                                //자기 자신의 말을 누르면
                                //여기 문제가 생길 수도 있음.\
                                System.out.println("두번째 클릭에서 같은 색 눌러서 return에 문제 ㄱㄴ");
                                send((new Send(process.colors,Send.MODE_TX_BACKGROUND)));
                            }
                        }

                    } else if (msg.mode == Send.MODE_CLOSE_ROOM) {
                        this.room = null;
                        printDisplay(this.room.toString());
                    }
                }
                users.removeElement(this);
                printDisplay(uid + "퇴장. 현재 참가자 수: "+ users.size());


            } catch (IOException e) {
                printDisplay(e.getMessage());
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }finally {
                try {
                    socket.close();
                    users.remove(this);
                } catch (IOException e) {
                    printDisplay(e.getMessage());
                }
            }
        }
        private void sendAd(Send msg){
            for(ClientHandler c : users)
                if(c.uid.equals(msg.userID)) {
                    send(msg);
                }
        }
        private void send(Send msg) {
            try {
                out.writeObject(msg);
                out.flush();
            } catch (IOException e) {
                System.err.println("클라이언트 일반 전송 오류"+e.getMessage());
            }
        }

        private void broadcasting(Send msg) {
            for(ClientHandler c : users)
                c.send(msg);
        }

        public void run() {
            receiveMessages(clientSocket);
        }

    }


    public static void main(String[] args) {
        Server sg =  new Server(54321);
        //sg.startServer();
    }
}