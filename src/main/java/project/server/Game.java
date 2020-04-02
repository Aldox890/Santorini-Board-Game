package project.server;

import project.Board;

public class Game {
    private Player[] playersList;
    private Board gameBoard;

    public Game(){
        playersList = new Player[3];
        gameBoard = new Board();
    }
}
