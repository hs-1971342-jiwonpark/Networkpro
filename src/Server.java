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
import java.util.Arrays;
import java.util.Vector;
public class Server extends JFrame {
    private int port;
    private ServerSocket serverSocket;
    private JTextField t_input = new JTextField();
    private JTextArea t_display = new JTextArea();
    private JButton b_connect = new JButton("서버 시작");
    private JButton b_disconnect = new JButton("서버 종료");
    private JButton b_send = new JButton("보내기");
    private JButton b_exit = new JButton("종료하기");

    private Vector<ChessPiece[][]> chessPieces = new Vector<ChessPiece[][]>();
    private Vector<String> userList = new Vector<>();
    private Vector<Vector<ChessPiece[][]>> roomCP = new Vector<Vector<ChessPiece[][]>>();
    private ChessPane cp = ChessPane.getInstance();
    private int turn=0;
    private Thread acceptThread = null;
    private Vector<ClientHandler> users = new Vector<ClientHandler>();
    public Server(int port) {
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
            printDisplay("연결 종료");

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

        private Cor cor;
        private int team;

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
                    if(msg.mode == Send.MODE_LOGIN) {
                        uid = msg.userID;
                        upw = msg.userPW;
                        printDisplay("새 참가자 : "+uid);
                        printDisplay("현재 참가자 수 : "+ users.size());
                        sendAd(msg);
                        printDisplay("보낸 패킷" + msg.userID + "\n" + msg.userPW);
                    }
                    else if(msg.mode == Send.MODE_LOGOUT) {
                        break;
                    }
                    else if (msg.mode == Send.MODE_IN_ROOM){
                        printDisplay("유저 아이디 : "+msg.userID);
                        printDisplay("코드 "+msg.mode);
                        msg.mode = Send.MODE_RETURN;
                        System.out.println(userList.size());

                        userList.add(msg.userID);
                        msg.users = userList;
                        broadcasting(new Send(turn,msg.userID,userList,Send.MODE_RETURN));
                        printDisplay("보낸 패킷: "+turn +"\n"+msg.userID+"\n"+userList +"\n"+Send.MODE_RETURN);
                        if(users.size()%2==0){
                            users.get(users.size()-2).cor = Cor.white;
                            users.get(users.size()-1).cor =Cor.black;
                            users.get(users.size()-2).team = turn;
                            users.get(users.size()-1).team = turn;
                            turn++;
                        }
                    }else if(msg.mode == Send.MODE_LOGOUT) {
                        break;

                    }else if(msg.mode == Send.RESULT_OK){

                        printDisplay("버튼 누른 유저의 턴  : "+msg.turn);
                        printDisplay("코드 : "+ msg.mode);
                        for(ClientHandler c: users){
                            if(c.team == msg.turn) {
                                c.send(new Send(c.cor, Send.RESULT_OK));
                                printDisplay("보낸 패킷: "+ Send.RESULT_OK+"\n"+c.cor);
                            }
                        }


                    }else if(msg.mode == Send.MODE_TX_STRING) {
                        message = uid + ": " + msg.message;
                        printDisplay(message);
                        msg.message = message;
                        for(ClientHandler c: users) {
                            if(c.team == this.team) {
                                c.send(msg);
                            }
                        }
                        printDisplay("보낸 패킷: "+msg.mode+"\n"+msg.message);

                    }else if(msg.mode == Send.MODE_TX_ChessPiece){
                        printDisplay("체스판 : "+ Arrays.deepToString(msg.cp));
                        printDisplay("모드 : "+msg.mode);
                        roomCP.elementAt(team).add(msg.cp);
                        for(ClientHandler c: users) {
                            if (c.team == this.team) {
                                c.send(new Send(msg.cp, roomCP.elementAt(team).size(), Send.MODE_TX_ChessPiece));
                                printDisplay("보낸 패킷: "+ Arrays.deepToString(msg.cp) +"\n"+ roomCP.elementAt(team).size()+"\n"+ Send.MODE_TX_ChessPiece);
                            }
                        }
                    }else if(msg.mode == Send.MODE_GAMESTART){
                        printDisplay("코드 "+msg.mode);
                        Vector<ChessPiece[][]> a = new Vector<>();
                        ChessPiece[][] cc = cp.saveTurn();
                        a.add(cc);
                        roomCP.add(team,a);
                        for(ClientHandler c: users) {
                            if (c.team == this.team) {
                                c.send(new Send(roomCP.elementAt(team).lastElement(), roomCP.elementAt(team).size(), Send.MODE_TX_ChessPiece));

                                printDisplay("보낸 패킷: "+ Arrays.deepToString(roomCP.elementAt(team).lastElement())+"\n"+roomCP.elementAt(team).size()+"\n"+Send.MODE_TX_ChessPiece);
                            }
                        }
                    }else if(msg.mode == Send.MODE_MOVE_CANCEL){
                        //게임의 시작은 turn이 1부터임
                        //따라서 첫턴인 화이트는 홀수

                        printDisplay("플레이어의 색깔 : "+msg.cor);
                        printDisplay("코드 "+msg.mode);
                        int i= (msg.cor == Cor.white)?1:0;
                        ChessPiece[][] a =null;
                        //오직 자신의 턴에서만 무르기 가능
                        if( roomCP.elementAt(team).size()%2 == i)
                            if( roomCP.elementAt(team).size()-2>0){
                                roomCP.elementAt(team).remove( roomCP.elementAt(team).size()-1);
                                roomCP.elementAt(team).remove( roomCP.elementAt(team).size()-1);
                                a=  roomCP.elementAt(team).lastElement();
                                for(ClientHandler c: users) {
                                    if (c.team == this.team) {
                                        c.send(new Send(a,  roomCP.elementAt(team).size(), Send.MODE_MOVE_CANCEL));
                                        printDisplay("보낸 패킷: "+ Arrays.deepToString(a) +"\n"+  roomCP.elementAt(team).size()+"\n"+Send.MODE_MOVE_CANCEL);
                                    }
                                }
                            }
                            else if(roomCP.elementAt(team).size() >= 1){
                                a= roomCP.elementAt(team).lastElement();
                                for(ClientHandler c: users) {
                                    if (c.team == this.team) {
                                        c.send(new Send(a, roomCP.elementAt(team).size(), Send.MODE_MOVE_CANCEL));
                                        printDisplay("보낸 패킷: "+ Arrays.deepToString(a) +"\n"+  roomCP.elementAt(team).size()+"\n"+Send.MODE_MOVE_CANCEL);
                                    }
                                }
                            }
                    }else if(msg.mode == Send.MODE_GAME_OVER) {
                        printDisplay("플레이어의 색깔 : "+msg.cor);
                        printDisplay("코드 "+msg.mode);
                        message = msg.cor.toString()+"색인 "+uid+"님이 졌습니다!!!";
                        for(ClientHandler c: users) {
                            if (c.team == this.team) {
                                c.send(new Send(message, Send.MODE_GAME_OVER));
                                printDisplay("보낸 패킷: "+ message+"\n"+ Send.MODE_GAME_OVER);
                            }
                        }
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
                if(c.uid.equals(this.uid))
                    send(msg);
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