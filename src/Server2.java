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
import java.util.Vector;
public class Server2 extends JFrame {
    private int port;
    private ServerSocket serverSocket;

    private JTextField t_input = new JTextField();
    private JTextArea t_display = new JTextArea();
    private JButton b_connect = new JButton("서버 시작");
    private JButton b_disconnect = new JButton("서버 종료");
    private JButton b_send = new JButton("보내기");
    private JButton b_exit = new JButton("종료하기");

    private Thread acceptThread = null;
    private Vector<RoomHandler> users = new Vector<RoomHandler>();
    public Server2(int port) {
        super("체스 서버");
        this.port = port;
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
                //ClientHandler cHandler = new ClientHandler(serverSocket.accept());
                //cHandler.start();
                //users.add(cHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void printDisplay(String msg) {
        t_display.append(msg+"\n");
        t_display.setCaretPosition(t_display.getDocument().getLength());
    }




    private class RoomHandler extends Thread{
        private ObjectOutputStream out;
        private Socket clientSocket;
        private String uid;
        private String upw;
        private ChessPane cp = ChessPane.getInstance();
        private Process process = new Process(this.cp);

        void sendMessage(String msg) throws IOException {
            send(new Send(uid,upw,Send.MODE_LOGIN));
        }

        //room에 참여하면 그때, 실행.
        public RoomHandler(Socket clientSocket) {
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


                    if(msg.mode == Send.MODE_LOGIN) {
                        uid = msg.userID;
                        upw = msg.userPW;
                        printDisplay("새 참가자 : "+uid);
                        printDisplay("현재 참가자 수 : "+ users.size());
                        broadcasting(msg);
                        continue;
                    }
                    else if(msg.mode == Send.MODE_LOGOUT) {
                        break;
                    }
                    else if(msg.mode == Send.MODE_TX_STRING) {
                        message = uid + ": "+ msg.message;
                        printDisplay(message);
                        broadcasting(msg);

                    }
                    else if (msg.mode == Send.MODE_TX_IMAGE) {
                        printDisplay(uid+": "+msg.message);
                        broadcasting(msg);
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
        private void send(Send msg) {
            try {
                out.writeObject(msg);
                out.flush();
            } catch (IOException e) {
                System.err.println("클라이언트 일반 전송 오류"+e.getMessage());
            }
        }

        private void broadcasting(Send msg) {
            for(RoomHandler c : users)
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