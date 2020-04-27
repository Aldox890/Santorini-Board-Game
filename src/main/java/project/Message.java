package project;

import java.io.Serializable;

public class Message implements Serializable {
    int typeOfMessage;
    int dest;

    String turnOf;
    String data;
    Cell gameBoard[][];

    public Message(int dest, int typeOfMessage, String data, String turnOf){
        this.typeOfMessage = typeOfMessage;
        this.dest = dest;
        this.turnOf = turnOf;
        gameBoard = new Cell[5][5];
        this.data = data;
    }

    public void addBoard(Cell gameBoard[][]){
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

    public Cell getCell(int i, int j){
        return gameBoard[i][j];
    }

    public boolean boardIsEmpty(){
        if (gameBoard[0][0] == null) return true;
        return false;
    }

    public Cell[][] getBoard(){
        return gameBoard;
    }

}
