package project;

import java.io.Serializable;

public class Message implements Serializable {
    int typeOfMessage;
    int dest;

    String turnOf;
    String data;
    String errorData;
    Cell gameBoard[][];

    /**
     * Constructor that initialize a message sent from server to client
     * @param dest -1 for broadcast , {1,2,3} for a particular client
     * @param typeOfMessage see typeOfMessage codes table
     * @param data  it can be several things according to typeOfMessage
     * @param turnOf username of the player which is in his turn
     */
    public Message(int dest, int typeOfMessage, String data, String turnOf){
        this.typeOfMessage = typeOfMessage;
        this.dest = dest;
        this.turnOf = turnOf;
        gameBoard = new Cell[5][5];
        this.data = data;
    }

    /**
     * In a message should be added the gameBoard if a game is already started
     * @param gameBoard last update of a gameboard to be sent to clients
     */
    public void addBoard(Cell gameBoard[][]){
        this.gameBoard = gameBoard;
    }

    /**
     * Method used by GameObserver to know if the message is for him
     * @return -1 if the message is broadcast, {0,1,2} if the message is for a particular client
     */
    public int getDest(){
        return dest;
    }

    public int getTypeOfMessage(){return typeOfMessage;}


    public String getData(){
        return data;
    }

    public String getErrorData() {
        return errorData;
    }

    /**
     * Method that adds a string used in case of error
     * @param errorData
     */
    public void setErrorData(String errorData) {
        this.errorData = errorData;
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
