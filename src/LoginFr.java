import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import javax.imageio.ImageIO;

public class LoginFr extends JFrame {
    String id; // id
    String pw; // pw
    JPasswordField pwTextField = new JPasswordField(20);
    JTextField idTextField = new JTextField(20);
    private static String serverAddress; //서버주소
    private static int serverPort; //포트번호
    private Socket socket = null; //소켓
    private ObjectOutputStream out =null;
    private Thread receiveThread;
    private ObjectInputStream in;
    LoginFr(String serverAddress, int serverPort){
        super("Chess Game");
        setSize(800, 800); // 프레임 크기를 800x800으로 설정
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(createPane());
        setLocationRelativeTo(null); // 화면 가운데 정렬
        setVisible(true);
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
            out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            out.writeObject(send);
            out.flush();
        } catch (IOException e) {
            System.out.println("클라이언트 일반 전송 오류"+e.getMessage());
        }
    }

    private void connectToServer() {
        try {
            serverAddress = "localhost";
            serverPort = 54321;
            socket = new Socket();
            SocketAddress sa = new InetSocketAddress(serverAddress, serverPort);
            socket.connect(sa, 3000);
        }catch (UnknownHostException e) {
            System.err.println("알 수 없는 서버> " + e.getMessage());
        } catch (IOException e) {
            this.socket =null;
            JOptionPane.showConfirmDialog(this, "서버가 열리지 않았습니다.", "서버 오류", JOptionPane.OK_CANCEL_OPTION);
        }
    }

    private void checkedLogin(){
        this.id = idTextField.getText();
        this.pw = pwTextField.getText();
        if (id.equals("") ||pw.equals("") ) {
            return;
        }
        connectToServer();
        if(this.socket !=null)
            send(new Send(id,pw,Send.MODE_LOGIN));
    }


    JPanel createPane(){
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    BufferedImage backgroundImage = ImageIO.read(getClass().getResource("chess.png")); // 이미지 로드
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        mainPanel.setLayout(new GridBagLayout()); // GridBagLayout 사용
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10); // 여백 추가
        gbc.anchor = GridBagConstraints.CENTER; // 중앙 정렬

        // ID 라벨 (굵게 설정)
        JLabel idLabel = new JLabel("ID : ");
        idLabel.setFont(new Font("Arial", Font.BOLD, 20)); // 굵게 설정
        mainPanel.add(idLabel, gbc);

        // ID 텍스트 필드
        gbc.gridx++;
        idTextField = new JTextField(20);
        idTextField.setPreferredSize(new Dimension(300, 40)); // 텍스트 필드 크기 설정
        mainPanel.add(idTextField, gbc);

        // PW 라벨 (굵게 설정)
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel passwordLabel = new JLabel("PW : ");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 20)); // 굵게 설정
        mainPanel.add(passwordLabel, gbc);

        // PW 텍스트 필드
        gbc.gridx++;
        pwTextField = new JPasswordField(20);
        pwTextField.setPreferredSize(new Dimension(300, 40)); // 텍스트 필드 크기 설정
        mainPanel.add(pwTextField, gbc);

        // "아이디가 없으세요?" 라벨 (가운데 정렬)
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2; // 라벨을 2열로 확장
        JLabel noIdLabel = new JLabel("아이디가 없으세요?");
        noIdLabel.setHorizontalAlignment(JLabel.CENTER); // 가운데 정렬
        mainPanel.add(noIdLabel, gbc);

        // "회원가입" 라벨 (가운데 정렬)
        gbc.gridy++;
        JButton signUpLabel = new JButton("회원가입");
        signUpLabel.setPreferredSize(new Dimension(150, 30)); // 작은 크기로 설정
        signUpLabel.setOpaque(false); // 투명 설정
        signUpLabel.setContentAreaFilled(false); // 내용 영역 투명하게 설정
        signUpLabel.setBorderPainted(false); // 테두리 제거
        mainPanel.add(signUpLabel, gbc);

        // 입장하기 버튼 (작게 설정)
        gbc.gridy++;
        gbc.gridwidth = 2; // 버튼을 2열로 확장
        JButton enterButton = new JButton("입장하기");
        enterButton.setPreferredSize(new Dimension(150, 30)); // 작은 크기로 설정
        enterButton.setOpaque(false); // 투명 설정
        enterButton.setContentAreaFilled(false); // 내용 영역 투명하게 설정
        enterButton.setBorderPainted(false); // 테두리 제거
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkedLogin();
                if(socket == null)
                    return;
                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        try {
                            in = new ObjectInputStream(socket.getInputStream());
                            Send inMsg;
                            while ((inMsg = (Send) in.readObject()) != null) {
                                if (inMsg.mode == Send.MODE_LOGIN) {
                                    System.out.println(inMsg.userID);
                                    System.out.println(inMsg.mode);
                                    //new StartFrame(socket,in,out,Cor.white);
                                    new StartFrame(socket,in,out,Cor.black);
                                    //new ChessPane(socket,in,out,Cor.white);
                                    dispose();
                                    break;
                                } else {
                                    System.out.println("r");
                                }
                            }
                            Thread.currentThread().interrupt();
                        } catch (IOException |
                                 ClassNotFoundException e1) {
                            System.out.println("e1.getMessage()");
                        }
                    }

                });
                thread.start();
            }
        });
        mainPanel.add(enterButton, gbc);
        return mainPanel;
    }



}