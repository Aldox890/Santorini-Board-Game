package project.server;

/*
* This class plays the role of the controller in the MVC Pattern.
* */
public class GameController {

    private Game game;
    public GameController(Game game){
        this.game = game;
    }

    /*
     * This method sets the game order of the players, based on players' age.
     * */
    public void init(){}

    /*
     * This method sets the Gods chosen by the eldest player, that will be used in the Game.
     * */
    public void setGods(){}

    /*
     * This method changes the turn.
     * */
    public void changeTurn(){}

    /*
     * This method adds a new player inside the game if there are less than 3 player already in.
     * */
    public void addPlayer(){}

    /*
     * This method adds a new player's worker in a certain position on the board .
     * */
    public void addWorker(){}

    /*
     * This method moves a player's worker in a certain position.
     * */
    public void moveWorker(){}

    /*
     * This method builds a new level inside a certain cell of the game board
     * */
    public void build(){}
}
