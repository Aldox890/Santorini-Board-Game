package project.server;

import project.Board;

import java.util.ArrayList;
import java.util.Collections;

public class Game {

    private boolean roomIsFull;
    private Board gameBoard;
    private String turnOf;
    private ArrayList playerlist;
    private ArrayList allowedGods;
    private int turnNumber;


    public Game(){
        gameBoard = new Board();
        playerlist = new ArrayList();
        roomIsFull = false;
    }

    public synchronized void addPlayer(Player p){ // adds a new player to the list and
        playerlist.add(p);
        System.out.println("added to playerlist: " + p.getName());
        if(playerlist.size()>2){
            roomIsFull = true;
        }
    }

    public void addGod(String s){
        allowedGods.add(s);
    }

    public int getTurnNumber(){
        return turnNumber;
    }

    public synchronized void setTurnOf (String p){
        turnOf = p;
    }

    public void init() throws InterruptedException { // orders playerlist by age and lets the first player talk
        while(!roomIsFull){ Thread.sleep(100); }
        Collections.sort(playerlist, (Player m1, Player m2) -> (int) (m1.getAge() - m2.getAge()));
        Player p = (Player) playerlist.get(2);
        turnOf = p.getName();
        turnNumber = 0;
        System.out.println("turnof " + p.getName());
    }

    public String getTurnOf() {
        return turnOf;
    }
}
