


//1971342 박지원



import javax.swing.*;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.Serializable;



public class Server extends JFrame {




    private int port;
    private ServerSocket serverSocket;
    private JTextArea t_display = new JTextArea();
    private JButton b_exit = new JButton("종료");
    private OutputStream out;
    private BufferedInputStream bin;
    public Server(int port) {
        super("1971342 박지원 객체의 전달 서버");
        this.port = port;
        buildGUI();
        setLocation(400,0);
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    void buildGUI() {
        setLayout(new BorderLayout());
        createDisplayPanel();
        createControlPanel();
    }

    void createDisplayPanel() {
        JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new BorderLayout());
        t_display.setEditable(false);
        displayPanel.add(new JScrollPane(t_display), BorderLayout.CENTER);
        add(displayPanel, BorderLayout.CENTER);
        displayPanel.setVisible(true);
    }


    void createControlPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(b_exit,BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
        b_exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printDisplay("종료");
                try {
                    if (serverSocket != null && !serverSocket.isClosed()) {
                        serverSocket.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    System.exit(0);
                }
            }
        });
        inputPanel.setVisible(true);
    }


    void startServer() {
        Socket clientSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            printDisplay("서버가 시작되었습니다.\n");
            while (true) {
                clientSocket = serverSocket.accept();
                printDisplay("클라이언트가 연결되었습니다.");
                ClientHandler cHandler = new ClientHandler(clientSocket);
                cHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void printDisplay(String msg) {
        t_display.append(msg+"\n");
        t_display.setCaretPosition(t_display.getDocument().getLength());
    }

    void receiveMessages(Socket socket) {
        ObjectInputStream in = null;
        try {
            BufferedInputStream bin = new BufferedInputStream(socket.getInputStream());
            in = new ObjectInputStream(bin);

            Send message;
            try {
                while ((message = (Send) in.readObject()) != null) {
                    printDisplay("클라이언트 메시지: " + message.getPos().x);
                }
            } catch (ClassNotFoundException e) {

                e.printStackTrace();
            }
            printDisplay("클라이언트가 연결을 종료했습니다.");

        } catch (EOFException e) {
            printDisplay("클라이언트가 연결을 종료했습니다.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) in.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    private class ClientHandler extends Thread{
        private Socket clientSocket;
        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        public void run() {
            receiveMessages(clientSocket);
        }
    }


    public static void main(String[] args) {
        Server sg =  new Server(54321);
        sg.startServer();
    }
}