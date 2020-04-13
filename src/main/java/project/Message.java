package project;

import java.io.Serializable;

public class Message implements Serializable {
    int typeOfMessage;
    int dest;

    String data;
    String gameBoard[][];

    public Message(int typeOfMessage, int dest, String data){
        this.typeOfMessage = typeOfMessage;
        this.dest = dest;
        gameBoard = new String[5][5];
        this.data = data;
    }

    public void addBoard(String gameBoard[][]){
        this.gameBoard = gameBoard;
    }
}
