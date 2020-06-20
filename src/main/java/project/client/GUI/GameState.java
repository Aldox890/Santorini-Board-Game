package project.client.GUI;

import project.Cell;

import java.util.ArrayList;

public class GameState {

    private ArrayList<String> availableGods;
    private ArrayList<String> players;
    private String playerName, personalGod;
    private int playerAge;
    private boolean isMyTurn;
    private int hasSetWorkers;
    private boolean moveFlag, buildFlag;
    private int boardClicks;
    private Cell[][] gameBoard;
    private int xStart, xDest, yStart, yDest;

    public GameState() {
        isMyTurn = false;
        hasSetWorkers = -1; // -1: means cannot set workers ; 0:means has to set workers; 1: means has set 1 worker, ...
        moveFlag = false;
        buildFlag = false;
        boardClicks = 0;
        gameBoard = null;
    }

    public int getHasSetWorkers() {
        return hasSetWorkers;
    }

    public void setHasSetWorkers(int hasSetWorkers) {
        this.hasSetWorkers = hasSetWorkers;
    }

    public boolean isMoveFlag() {
        return moveFlag;
    }

    public void setMoveFlag(boolean moveFlag) {
        this.moveFlag = moveFlag;
    }

    public boolean isBuildFlag() {
        return buildFlag;
    }

    public void setBuildFlag(boolean buildFlag) {
        this.buildFlag = buildFlag;
    }

    public int getBoardClicks() {
        return boardClicks;
    }

    public void setBoardClicks(int boardClicks) {
        this.boardClicks = boardClicks;
    }

    public Cell[][] getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(Cell[][] gameBoard) {
        this.gameBoard = gameBoard;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getPlayerAge() {
        return playerAge;
    }

    public void setPlayerAge(int playerAge) {
        this.playerAge = playerAge;
    }

    public boolean isMyTurn() {
        return isMyTurn;
    }

    public void setMyTurn(boolean myTurn) {
        isMyTurn = myTurn;
    }

    public int getxStart() {
        return xStart;
    }

    public int getyStart() {
        return yStart;
    }

    public int getxDest() {
        return xDest;
    }

    public int getyDest() {
        return yDest;
    }

    public void setxStart(int xStart) {
        this.xStart = xStart;
    }

    public void setyStart(int yStart) {
        this.yStart = yStart;
    }

    public void setxDest(int xDest) {
        this.xDest = xDest;
    }

    public void setyDest(int yDest) {
        this.yDest = yDest;
    }

    public ArrayList<String> getAvailableGods() {
        return availableGods;
    }

    public void setAvailableGods(ArrayList<String> availableGods) {
        this.availableGods = availableGods;
    }

    public ArrayList<String> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<String> players) {
        this.players = players;
    }

    public String getPersonalGod() {
        return personalGod;
    }

    public void setPersonalGod(String personalGod) {
        this.personalGod = personalGod;
    }
}
