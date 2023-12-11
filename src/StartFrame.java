import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.TimerTask;
import java.util.Vector;
import java.util.Timer;
public class StartFrame extends JFrame {

    public static final int DIMENSION = 8;

    public static Square[][] grid = new Square[DIMENSION][DIMENSION];

    public Vector<ChessPiece[][]> turn = new Vector<>();
    Process process = new Process(this);
    ObjectOutputStream out;
    ObjectInputStream in;
    Socket socket;

    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;

    private JLayeredPane ChessPanel;

    private GridBagConstraints gbc;

    private boolean firstClick = true;

    private boolean myTurn = false;

    public Cor playerColor;
    JLabel time;

    Timer timer;
    private Thread receiveThread;
    StartFrame() {
        super("온라인 체스 게임");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 1000);
        // GridBagLayout 설정
        setLayout(new GridBagLayout());

    }

    StartFrame(Socket socket_, ObjectInputStream in_, ObjectOutputStream out_, Cor cor_) {
        this();
        in =in_;
        out = out_;
        playerColor = cor_;
        if(cor_ == Cor.white) myTurn = true;
        socket =socket_;
        GUI();

        receiveThread = new Thread(new Runnable() {
            void receiveMessage() {
                try {
                    Send inMsg = (Send) in.readObject();
                    if (inMsg != null) {  // 메시지를 계속 읽어 들임
                        switch (inMsg.mode) {
                            case Send.MODE_TX_STRING:
                                chatArea.append(inMsg.message + "\n");
                                break;
                            case Send.MODE_TX_ChessPiece:
                                if(inMsg.cp != null) {
                                    reprint(inMsg.cp);
                                    int i = (playerColor == Cor.white) ? 1 : 0;
                                    if (inMsg.cpSize % 2 == i) {
                                        myTurn = true;
                                        StartTimer();
                                    }
                                }
                                break;
                            case Send.MODE_MOVE_CANCEL:
                                if(turn.size()>1)
                                    turn.remove(turn.size()-1);
                                reprint(inMsg.cp);
                                int i = (playerColor == Cor.white) ? 1 : 0;
                                if (inMsg.cpSize % 2 == i) myTurn = true;
                                if(!ChessPanel.isEnabled()) {
                                    labelclickControl(true);
                                    if(inMsg.cpSize % 2 == i)
                                        StartTimer();
                                }
                                break;
                            case Send.MODE_GAME_OVER:
                                GameOver(inMsg.message);
                                labelclickControl(false);
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
        if(playerColor == Cor.black)
            send(new Send(Send.MODE_GAMESTART));
    }


    public void send(Send send) {
        try {
            out.writeObject(send);
            out.flush();
        } catch (IOException e) {
            e.getMessage();
        }
    }
    private void GUI(){
        this.gbc = new GridBagConstraints();
        // ChessPane 설정
        gbc.gridx = 0; // 첫 번째 열
        gbc.gridy = 0; // 첫 번째 행
        gbc.gridwidth = 2; // 두 열 차지
        gbc.gridheight = 1; // 한 행 차지
        gbc.weightx = 0.75; // 가로 공간의 75% 차지
        gbc.weighty = 1.0; // 세로 공간의 100% 차지
        gbc.fill = GridBagConstraints.BOTH; // 가로 세로 모두 채우기
        //add(cp, gbc);
        ChessPanel = Pane();
        add(ChessPanel,gbc);

        //채팅창
        gbc.gridx = 2; // 세 번째 열
        gbc.gridwidth = 1; // 한 열 차지
        gbc.weightx = 0.25; // 가로 공간의 25% 차지
        gbc.fill = GridBagConstraints.BOTH;
        add(createChatUI(), gbc);

        //무르기 버튼
        setVisible(true);
    }


    private void GameOver(String msg) {
        JOptionPane.showMessageDialog(null, msg, "게임 종료", JOptionPane.INFORMATION_MESSAGE);
        ChessPanel.setEnabled(false);
    }
    //체스판 관련 로직
    private JLayeredPane Pane(){
        JLayeredPane a = new JLayeredPane();
        a.setLayout(new GridLayout(DIMENSION, DIMENSION));
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                grid[i][j] = new Square(new Pos(i,j));
                grid[i][j].setOpaque(true);
                if ((i + j) % 2 == 0)
                    grid[i][j].setBackground(Color.WHITE);
                else
                    grid[i][j].setBackground(new Color(0xCCA63D));
                a.add(grid[i][j]);
                if(playerColor != null)
                    grid[i][j].addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent e) {
                            if(!myTurn) return;
                            Pos ps = ((Square) e.getComponent()).pos;

                            if(firstClick){
                                switch (process.Check_first_click(ps)){
                                    case 1://정상
                                        firstClick = false;

                                        break;
                                    case 2://다른곳 클릭

                                        break;
                                    case 3://자신의 말 클릭

                                        break;
                                }
                            }
                            else {
                                switch (process.Check_second_click(ps)){
                                    case 1://정상
                                        timer.cancel();
                                        time.setText("30");
                                        firstClick = true;
                                        ChessPiece[][] cp = saveTurn();
                                        System.out.println(Arrays.deepToString(cp));
                                        send(new Send(cp,Send.MODE_TX_ChessPiece));
                                        System.out.println("chessPane에서 send보냄");
                                        myTurn =false;
                                        break;
                                    case 2://다른곳 클릭

                                        break;
                                    case 3://자신의 말 클릭

                                        break;
                                }
                            }
                        }
                    });
                grid[i][j].setVisible(true);
            }
        }
        return a;
    }

    public ChessPiece[][] saveTurn(){
        ChessPiece[][] newGrid = new ChessPiece[DIMENSION][DIMENSION];
        for(int i =0; i< DIMENSION; i++){
            for (int j=0; j< DIMENSION; j++){
                newGrid[i][j]= grid[i][j].havePiece;
            }
        }
        turn.add(newGrid);
        return newGrid;
    }

    public void reprint(ChessPiece[][] cp){
        int kingCount= 0;
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                grid[i][j].setPiece(cp[i][j]);
                if ((i + j) % 2 == 0)
                    grid[i][j].setBackground(Color.WHITE);
                else
                    grid[i][j].setBackground(new Color(0xCCA63D));
                if(grid[i][j].havePiece != null)
                    if(grid[i][j].havePiece.color == playerColor&& grid[i][j].havePiece.name.equals("king"))
                        kingCount++;
            }
        }
        if(kingCount ==0 && turn.size()>1)
            send(new Send(playerColor,Send.MODE_GAME_OVER));
    }
    private void labelclickControl(boolean control){
        ChessPanel.setEnabled(control);
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                grid[i][j].setEnabled(control);
            }
        }

    }


    //채팅창 생성 함수
    private JPanel createChatUI() {
        JPanel returnjp = new JPanel();

        returnjp.setSize(350, 500);
        // 레이아웃 설정
        returnjp.setLayout(new BorderLayout());

        //채팅창 상단에 무르기 버튼 + 타이머
        JPanel ChessJP = new JPanel(new BorderLayout());
        ChessJP.setSize(returnjp.getWidth(), 60);
        JButton jb = new JButton("무르기");
        jb.setFont(new Font(null,Font.BOLD,20));
        jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                send(new Send(playerColor, Send.MODE_MOVE_CANCEL));

            }
        });
        time = new JLabel("30");
        time.setFont(new Font(null, Font.BOLD,50));
        time.setHorizontalTextPosition(SwingConstants.RIGHT);
        JLabel text = new JLabel("남은 시간: ");
        text.setFont(new Font(null,Font.BOLD,50));
        ChessJP.add(text,BorderLayout.WEST);
        ChessJP.add(time, BorderLayout.CENTER);
        ChessJP.add(jb, BorderLayout.EAST);
        returnjp.add(ChessJP,BorderLayout.NORTH);
        //

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("굴림", Font.BOLD, 18)); // 채팅 영역 폰트 크기 설정
        chatArea.setBackground(Color.BLACK);
        chatArea.setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        returnjp.add(scrollPane, BorderLayout.CENTER);

        // 입력 패널
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        inputField = new JTextField();
        inputField.setFont(new Font("굴림", Font.BOLD, 16)); // 폰트 크기 변경
        inputField.setPreferredSize(new Dimension(300, 30)); // 텍스트 필드 크기 조정
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendChatMessage();
            }
        });
        inputPanel.add(inputField, BorderLayout.CENTER);

        sendButton = new JButton("전송");
        sendButton.setFont(new Font("굴림", Font.BOLD, 14));
        sendButton.setPreferredSize(new Dimension(70, 30)); // 버튼 크기 조정
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendChatMessage();
            }
        });
        inputPanel.add(sendButton, BorderLayout.EAST);


        returnjp.add(inputPanel, BorderLayout.SOUTH);

        setVisible(true);

        return returnjp;
    }

    private void StartTimer(){
        timer= new Timer();
        TimerTask task= new TimerTask() {
            int count =30;
            @Override
            public void run() {
                if(count <=0){
                    time.setText(String.valueOf(count));
                    send(new Send(playerColor, Send.MODE_GAME_OVER));
                    timer.cancel();
                }
                else{
                    count--;
                    if(count < 10)
                        time.setText( 0 + String.valueOf(count));
                    else
                        time.setText(String.valueOf(count));
                }
            }
        };
        timer.schedule(task, 0,1000);
    }


    private void sendChatMessage() {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            send(new Send(message, Send.MODE_TX_STRING));
            inputField.setText("");
        }
    }
}