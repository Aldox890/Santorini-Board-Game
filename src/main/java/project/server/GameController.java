package project.server;

import project.Message;
import project.server.model.Game;
import project.server.model.Player;

import java.util.ArrayList;

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
    public void setGods(ArrayList<String> gods, int socketId){
        if (gods!= null /*&& parsedInput[0] != null && parsedInput[1] != null && parsedInput[2] != null*/) {
            game.setGods(gods, socketId);
        }
        else{
            game.badInputException(socketId,3,"false");
        }
    }

    public void passTurn(){
        game.nextTurn();
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
    public boolean addPlayer(Player player, int socketId){
        return game.addPlayer(player,socketId);
    }

    /*
     * This method adds a new player's worker in a certain position on the board .
     */
    public void addWorker(Player p, int x,int y,int socketId){
        if(p!= null && p.getNumberOfWorker()<2) {
            if (x >= 0 && y >= 0) {
                game.addWorker(p, x, y,socketId);
                return;
            }
        }
        game.badInputException(socketId,4,"false");
        return;
    }

    /*
     * This method moves a player's worker in a certain position.
     */
    public void moveWorker(Player player, int xStart,int yStart,int xDest, int yDest,int socketId){
        if (xStart >=0 && yStart >=0 && xDest >= 0 && yDest >= 0) {
            game.moveWorker(player, xStart,yStart,xDest,yDest, socketId);
            return;
        }
        game.badInputException(socketId,5,"false");
    }

    /*
     * This method builds a new level inside a certain cell of the game board
     */
    public void build(Player player, int xStart,int yStart,int xDest, int yDest,int level,int socketId){
        if (xStart >= 0 && yStart >= 0 && xDest >= 0 && yDest >= 0) {
            game.build(player, xStart, yStart, xDest, yDest, level, socketId);
            return;
        }
        game.badInputException(socketId,6,"false");

    }

    /*
     * lets the player chose his god card.
     */
    public void setGod(String god,Player player,int socketId) {
        if(god != null){
            game.addGod(god,player,socketId);
            return;
        }
        game.badInputException(socketId,2,"false");
        return;
    }
}
