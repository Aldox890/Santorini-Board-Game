package project;

import java.io.Serializable;

public class Message implements Serializable {
    int typeOfMessage;
    int dest;

    String turnOf;
    String data;
    String gameBoard[][];

    public Message(int dest, int typeOfMessage, String data, String turnOf){
        this.typeOfMessage = typeOfMessage;
        this.dest = dest;
        this.turnOf = turnOf;
        gameBoard = new String[5][5];
        this.data = data;
    }

    public void addBoard(String gameBoard[][]){
        this.gameBoard = gameBoard;
    }

    public int getDest(){
        return dest;
    }

    public int getTypeOfMessage(){return typeOfMessage;}

    public String getData(){
        return data;
    }

    public String getTurnOf(){return turnOf;}

}
