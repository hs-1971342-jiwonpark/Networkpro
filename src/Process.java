import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

import static java.lang.System.exit;

public class Process {
    private static String serverAddress = "localhost";
    private static int serverPort = 54321;
    private Socket socket;
    ChessPane pane =null;
    Send send = null;
    private ObjectOutputStream out;
    private int port;
    private ServerSocket serverSocket;
    private Thread acceptThread = null;
    private Vector<ClientHandler> users = new Vector<ClientHandler>();


    private class ClientHandler extends Thread{
        private ObjectOutputStream out;
        private Socket clientSocket;
        private String uid;




        public ClientHandler(Socket clientSocket) {
            this.clientSocket  = clientSocket;
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
                        System.out.println("새 참가자 : "+uid);
                        System.out.println("현재 참가자 수 : "+ users.size());
                    }
                    else if(msg.mode == Send.MODE_LOGOUT) {
                        break;
                    }
                    else if(msg.mode == Send.MODE_TX_STRING) {
                        message = uid + ": "+ msg.message;

                        System.out.println(message);
                        broadcasting(msg);

                    }
                    else if (msg.mode == Send.MODE_TX_IMAGE) {
                        broadcasting(msg);
                    }
                }
                users.removeElement(this);
                System.out.println(uid + "퇴장. 현재 참가자 수: "+ users.size());


            } catch (IOException e) {
                System.out.println(e.getMessage());
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }finally {
                try {
                    socket.close();
                    users.remove(this);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
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
            for(ClientHandler c : users)
                c.send(msg);
        }

        public void run() {
            receiveMessages(clientSocket);
        }

    }

    Square first = null;
    Square second = null;
    public Process(){

    }
    public Process(ChessPane Pane){
        this.pane = Pane;
    }

    public int Check_first_click(Pos pos){
        //리턴 값에 따라서 클릭 다시 받아야함.

        //좌표가 문제 있으면 리턴
        if(!pos.check_Pos()) {
            return 2;
        }
        //받은 좌표의 Square와 sq 연동
        this.first = pane.grid[pos.y][pos.x];
        if(first.havePiece == null){
            return 2;
        }
        //플레이어의 색이랑 말의 색이 다르면 리턴;
        else if(pane.getPlayerColor() != first.havePiece.color) {
            System.out.println(first.havePiece.name);
            return 2;
        }
        //플레이어가 선택한 말의 종류에 따라 이동할 수 있는 말의 위치에 색을 바꿈.
        switch (first.havePiece.name){
            case "pawn":
                new Pawn(first.pos,first.havePiece.color, this.pane).Move_possible();
                break;
            case "rook":
                new Rook(first.pos,first.havePiece.color, this.pane).Move_possible();
                break;
            case "knight":
                new Knight(first.pos,first.havePiece.color, this.pane).Move_possible();
                break;
            case "bishop":
                new Bishop(first.pos,first.havePiece.color, this.pane).Move_possible();
                break;
            case "queen":
                new Queen(first.pos,first.havePiece.color, this.pane).Move_possible();
                break;
            case "king":
                new King(first.pos,first.havePiece.color, this.pane).Move_possible();
                break;
        }
        return 1;
        // 보내야함
    }
    public int Check_second_click(Pos pos){
        //리턴 값에 따라서 클릭 다시 받아야함.
        try {
            socket = new Socket(serverAddress,serverPort);
            out = new ObjectOutputStream(socket.getOutputStream());
        } catch (UnknownHostException e) {
            System.err.println("알 수 없는 서버" + e);
            exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Pos message = pos;
        Send sd = new Send(message);

        try {
            if(sd == null) {
                System.out.println("널");
            }
            out.writeObject(sd);
            System.out.println(sd.getPos().x);
            out.flush();
        } catch (NumberFormatException e) {
            System.out.println("ㅋ");
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        //좌표가 문제 있으면 리턴
        if(!pos.check_Pos()) return 2;
        //받은 좌표의 Square와 sq 연동
        second = pane.grid[pos.y][pos.x];

        //플레이어가 선택한 위치의 BG가 특정색이 아니면
        if( second.havePiece != null&&
            first.havePiece.color == second.havePiece.color){
            switch (this.first.havePiece.name){
            case "pawn":
                new Pawn(first.pos,second.havePiece.color, this.pane).Clear_Move_possible();
                break;
            case "rook":
                new Rook(first.pos,second.havePiece.color, this.pane).Clear_Move_possible();
                break;
            case "knight":
                new Knight(first.pos,second.havePiece.color, this.pane).Clear_Move_possible();
                break;
            case "bishop":
                new Bishop(first.pos,second.havePiece.color, this.pane).Clear_Move_possible();
                break;
            case "queen":
                new Queen(first.pos,second.havePiece.color, this.pane).Clear_Move_possible();
                break;
            case "king":
                new King(first.pos,second.havePiece.color, this.pane).Clear_Move_possible();
                break;
        }
            this.Check_first_click(pos);
            return 3;
        }
        else if((second.getBackground() != Color.red)) return 2;


        //플레이어가 선택한 말의 종류에 따라 이동할 수 있는 말의 위치에 색을 바꿈.
        switch (this.first.havePiece.name){
            case "pawn":
                new Pawn(second.pos,first.havePiece.color, this.pane).Move(this.first);
                new Pawn(first.pos,second.havePiece.color, this.pane).Clear_Move_possible();
                break;
            case "rook":
                new Rook(second.pos,first.havePiece.color, this.pane).Move(this.first);
                new Rook(first.pos,second.havePiece.color, this.pane).Clear_Move_possible();
                break;
            case "knight":
                new Knight(second.pos,first.havePiece.color, this.pane).Move(this.first);
                new Knight(first.pos,second.havePiece.color, this.pane).Clear_Move_possible();
                break;
            case "bishop":
                new Bishop(second.pos,first.havePiece.color, this.pane).Move(this.first);
                new Bishop(first.pos,second.havePiece.color, this.pane).Clear_Move_possible();
                break;
            case "queen":
                new Queen(second.pos,first.havePiece.color, this.pane).Move(this.first);
                new Queen(first.pos,second.havePiece.color, this.pane).Clear_Move_possible();
                break;
            case "king":
                new King(second.pos,first.havePiece.color, this.pane).Move(this.first);
                new King(first.pos,second.havePiece.color, this.pane).Clear_Move_possible();
                break;
        }
        return 1;
    }

}
