package client;

public class Game {
    Player [] playersList;
    Board gameBoard;

    public void Game(){
        playersList = new Player[3];
        gameBoard = new Board();
    }
}
