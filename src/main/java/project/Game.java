package project;

public class Game {
    private Player [] playersList;
    private Board gameBoard;

    public Game(){
        playersList = new Player[3];
        gameBoard = new Board();
    }
}
