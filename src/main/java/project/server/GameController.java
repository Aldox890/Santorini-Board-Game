package project.server;

import project.Worker;

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
     */
    public void init(){}

    /*
     * This method sets the Gods chosen by the eldest player, that will be used in the Game.
     */
    public void setGods(String[] parsedInput,int socketId){
        game.setGods(parsedInput,socketId);
    }

    /*
     * This method changes the turn.
     * If the current turn is the turn of the eldest player, sets the next turn to the youngest player.
     */
    public void changeTurn(){
        if(this.game.getTurnOf().equals( this.game.getPlayerList().get(this.game.getPlayerList().size())) ){    //check if the current turn == eldest's turn -> next turn = youngest's turn
            this.game.setTurnOf(this.game.getPlayerList().get(0));
        }
        else{
            this.game.setTurnOf(
                    this.game.getPlayerList()
                            .get(this.game.getPlayerList().indexOf(this.game.getTurnOf())+1));
        }
    }

    /*
     * This method adds a new player inside the game if there are less than 3 player already in.
     */
    public boolean addPlayer(Player player,int socketId){
        return game.addPlayer(player,socketId);
    }

    /*
     * This method adds a new player's worker in a certain position on the board .
     */
    public boolean addWorker(Player p, String[] parsedLine,int socketid){
        return this.game.addWorker(p,parsedLine,socketid);
    }

    /*
     * This method moves a player's worker in a certain position.
     */
    public boolean moveWorker(Player player, String parsedLine[],int socketid){ // <------ DA MODIFICARE
        return game.moveWorker(player,parsedLine,socketid);
    }

    /*
     * This method builds a new level inside a certain cell of the game board
     */
    public void build(Player player, String parsedLine[],int level,int socketid){    // <------ DA MODIFICARE
        game.build(player,parsedLine,level,socketid);
    }

    /*
     * lets the player chose his god card.
     */
    public void setGod(String god,Player player,int socketid) {
        game.addGod(god,player,socketid);

    }
}
