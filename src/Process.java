import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

import static java.lang.System.exit;

public class Process {
    StartFrame pane =null;
    Square first = null;
    Square second = null;
    public Process(){

    }
    public Process(StartFrame Pane){
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
        else if(pane.playerColor != first.havePiece.color) {
            System.out.println("플레이어 색 문제");
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