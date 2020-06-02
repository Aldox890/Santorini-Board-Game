package project.server;

import project.Message;
import project.server.model.Game;
import project.server.model.Player;

import java.io.*;
import java.util.ArrayList;

/*
* This class plays the role of the controller in the MVC Pattern.
* */
public class GameController {

    private Game game;
    public GameController(Game game){
        this.game = game;
    }

    /**
     * This method sets the Gods chosen by the eldest player, that will be used in the Game.
     * @param gods arraylist that contains gods chosen by eldest player
     * @param socketId id of the eldest player
     */
    public void setGods(ArrayList<String> gods, int socketId){
        if (gods!= null  && (gods.size() == game.getNPlayers())) {
            if(gods.get(0)!=gods.get(1)) {
                if(game.getNPlayers() == 2 || (gods.get(1)!=gods.get(2))) {
                    game.setGods(gods, socketId);
                    return;
                }
            }
        }
        game.badInputException(socketId,3,"false", "Error: Bad inputs while inserting the gods.");
    }

    /**
     * Method called when a player end his turn
     * @throws IOException when the game can't be saved correctly
     */
    public void passTurn() throws IOException {
        game.nextTurn();
    }


    /**
     * This method adds a new player inside the game if there are less than 3 player already in
     * @param player Player object
     * @param socketId id of the player
     * @return
     */
    public boolean addPlayer(Player player, int socketId){
        return game.addPlayer(player,socketId);
    }

    /**
     * This method adds a new player's worker in a certain position on the board .
     * @param p player
     * @param x x coordinate where a worker is placed
     * @param y y coordinate where a worker is placed
     * @param socketId id of the player
     * @throws IOException
     */
    public void addWorker(Player p, int x,int y,int socketId) throws IOException {
        if(p!= null && p.getNumberOfWorker()<2) {
            if (x >= 0 && y >= 0) {
                game.addWorker(p, x, y,socketId);
                return;
            }
        }
        game.badInputException(socketId,4,"false", "Error: Bad inputs of adding a worker.");
        return;
    }

    /**
     * This method moves a player's worker in a certain position.
     * @param player
     * @param xStart starting x coordinate of the worker
     * @param yStart starting y coordinate of the worker
     * @param xDest final x coordinate of the worker
     * @param yDest final y coordinate of the worker
     * @param socketId id of the player
     */
    public void moveWorker(Player player, int xStart,int yStart,int xDest, int yDest,int socketId){
        if (xStart >=0 && yStart >=0 && xDest >= 0 && yDest >= 0) {
            game.moveWorker(player, xStart,yStart,xDest,yDest, socketId);
            return;
        }
        game.badInputException(socketId,5,"false","Error: Bad Move inputs.");
    }

    /**
     * This method builds a new level inside a certain cell of the game board
     * @param player
     * @param xStart x coordinate of the worker that builds
     * @param yStart y coordinate of the worker that builds
     * @param xDest x coordinate where a worker builds
     * @param yDest y coordinate where a worker builds
     * @param level 1 if standard build, 2 if hephaestus build, 4 if atlas build
     * @param socketId id of the player
     */
    public void build(Player player, int xStart,int yStart,int xDest, int yDest,int level,int socketId){
        if (xStart >= 0 && yStart >= 0 && xDest >= 0 && yDest >= 0) {
            game.build(player, xStart, yStart, xDest, yDest, level, socketId);
            return;
        }
        game.badInputException(socketId,6,"false","Error: Bad Build inputs.");

    }

    /**
     * lets the player chose his god card.
     * @param god god chosen by the player
     * @param player
     * @param socketId id of the player
     */
    public void setGod(String god,Player player,int socketId) {
        if(god != null){
            game.addGod(god,player,socketId);
            return;
        }
        game.badInputException(socketId,2,"false", "Error: Bad inputs while selecting a god.");
        return;
    }

    /**
     * Remove a player in case of disconnection
     * @param player player to remove
     * @throws IOException
     */
    public void removePlayer(Player player) throws IOException {
            game.removePlayer(player);
    }

    /**
     * Method called when a player decides to continue an existing game
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void loadGame() throws IOException, ClassNotFoundException {
        game.loadGame();
    }

    /**
     * Method used if a player decides to not continue an existing game
     */
    public void callGod(){
        game.callGod();
    }

}
