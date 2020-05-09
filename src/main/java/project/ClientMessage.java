package project;

import java.util.ArrayList;

public class ClientMessage {
    int typeOfMessage;
    String god;
    ArrayList<String> gameGods;
    int xStart;
    int yStart;
    int xDest;
    int yDest;
    String data;

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
