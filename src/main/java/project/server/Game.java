package project.server;

import project.Board;

public class Game {
    private Player[] playersList;
    private Board gameBoard;
    private String turnOf;

    public Game(){
        playersList = new Player[3];
        gameBoard = new Board();
    }

    public String getTurnOf() {
        return turnOf;
    }
}
