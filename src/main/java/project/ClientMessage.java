package project;

import java.io.Serializable;
import java.util.ArrayList;

public class ClientMessage implements Serializable {
    int typeOfMessage;
    String god;
    ArrayList<String> gameGods;
    int xStart;
    int yStart;
    int xDest;
    int yDest;
    String data;

    /**
     * Constructor that initialize a message sent from a client to server
     * @param typeOfMessage see typeOfMessage codes table
     * @param god the god i can use
     * @param gameGods used to sent to clients the list of available gods
     * @param xStart starting x coordinate where a worker is
     * @param yStart starting y coordinate where a worker is
     * @param xDest x coordinate where a worker would move or build
     * @param yDest y coordinate where a worker would move or build
     * @param data
     */
    public ClientMessage(int typeOfMessage, String god, ArrayList<String> gameGods, int xStart, int yStart, int xDest, int yDest, String data) {
        this.typeOfMessage = typeOfMessage;
        this.god = god;
        this.gameGods = gameGods;
        this.xStart = xStart;
        this.yStart = yStart;
        this.xDest = xDest;
        this.yDest = yDest;
        this.data = data;
    }

    public int getTypeOfMessage() {
        return typeOfMessage;
    }

    public String getGod() {
        return god;
    }

    public ArrayList<String> getGameGods() {
        return gameGods;
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

    public String getData() {
        return data;
    }
}
