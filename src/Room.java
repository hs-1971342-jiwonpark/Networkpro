import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class Room extends JFrame {
    private static String serverAddress = "localhost";
    private static int serverPort = 54321;
    private Socket socket = null;
    private ObjectOutputStream out =null;
    private Thread receiveThread;
    private ObjectInputStream in;
    JTextField chatField;
    JButton sendButton;
    JPanel chatPanel;
    JLabel jb2;
    JLabel jb1;
    JPanel gridPanel;
    JLabel label[];


    void sendMessage() {
    }
    void disconnect(String id, String pw){
        send(new Send(id,pw,Send.MODE_LOGOUT));
        try {
            receiveThread=null;
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
            System.out.println("클라이언트 일반 전송 오류"+e.getMessage());
        }
    }
    private void checkedLogin(){
        // 서버에 연결하는 로직 구현
        try {
            socket = new Socket();
            SocketAddress sa = new InetSocketAddress(serverAddress, serverPort);
            socket.connect(sa,3000);
            out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            receiveThread = new Thread(new Runnable(){
                private ObjectInputStream in;
                void receiveMessage() {
                    try {
                        Send inMsg = (Send)in.readObject();
                        while(inMsg == null) {  // 메시지를 계속 읽어 들임
                            System.out.println("클라이언트 일반 전송 오류");
                            return;
                        }
                        if(inMsg.mode == Send.MODE_LOGIN){
                            new StartFrame();
                        }
                        else{
                            System.out.println("dd");
                        }
                    } catch(IOException e) {
                        System.out.println(e.getMessage());
                    } catch(ClassNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                }
                @Override
                public void run() {
                    try {
                        in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                    }catch(IOException e) {
                        System.out.println("입력 스트림이 열리지 않음");
                    }
                    while(receiveThread == Thread.currentThread()) {
                        receiveMessage();
                    }
                }
            });
            receiveThread.start();
        }
        catch (IOException e) {
            socket = new Socket();
            SocketAddress sa = new InetSocketAddress(serverAddress,serverPort);
            try {
                socket.connect(sa,3000);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        sendMessage();
    }
    private ImageIcon resizeImage(ImageIcon originalImage, int targetWidth, int targetHeight) {
        Image img = originalImage.getImage();
        Image resizedImg = img.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }
    public Room() {

        // 창의 제목 설정
        setTitle("Room with Grid and Chat Panel");

        // 레이아웃을 BorderLayout으로 설정
        setLayout(new BorderLayout());

//        try {
//            Send inMsg = (Send)in.readObject();
//            while(inMsg == null) {  // 메시지를 계속 읽어 들임
//                System.out.println("클라이언트 일반 전송 오류");
//                return;
//            }
//            if(inMsg.mode == Send.MODE_LOGIN){
//                new StartFrame();
//            }
//            else{
//                System.out.println("dd");
//            }
//        } catch(IOException e) {
//            System.out.println(e.getMessage());
//        } catch(ClassNotFoundException e) {
//            System.out.println(e.getMessage());
//        }

        // 그리드 패널 생성 및 구성
        gridPanel = new JPanel(new GridLayout(10 , 2));
        // 첫 번째 라벨 설정
        jb1 = new JLabel("1");
        jb1.setOpaque(true);
        jb1.setBackground(Color.red);
        jb1.setForeground(Color.yellow);
        gridPanel.add(jb1); // 그리드 패널에 라벨 추가
        // 첫 번째 라벨 설정
        jb2 = new JLabel("2");
        jb2.setOpaque(true);
        jb2.setBackground(Color.blue);
        jb2.setForeground(Color.yellow);
        gridPanel.add(jb2); // 그리드 패널에 라벨 추가
        label = new JLabel[21];
        // 2부터 8까지의 라벨 추가 (배경색 없음)
        for (int i = 3; i <= 20; i++) {
            label[i] = new JLabel(Integer.toString(i));
            gridPanel.add(label[i]);
        }

        // 채팅 패널 생성 및 구성
        chatPanel = new JPanel(new BorderLayout());
        chatPanel.setPreferredSize(new Dimension(400, 75)); // 채팅 패널의 선호되는 크기를 조금 줄임
        chatField = new JTextField();
        sendButton = new JButton("Send");
        chatPanel.add(chatField, BorderLayout.CENTER);
        chatPanel.add(sendButton, BorderLayout.EAST);
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new StartFrame();

            }
        });
        // 패널들을 프레임에 추가
        add(gridPanel, BorderLayout.CENTER);
        add(chatPanel, BorderLayout.SOUTH);

        // 창의 기본 크기 설정을 조금 줄임
        setSize(350, 400);

        // 창을 화면 가운데에 위치시키기
        setLocationRelativeTo(null);

        // 창을 보이게 설정
        setVisible(true);

        // 창을 닫았을 때 프로그램이 종료되도록 설정
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        // Room 클래스의 인스턴스를 생성하여 창을 띄움
        new Room();
    }
}
