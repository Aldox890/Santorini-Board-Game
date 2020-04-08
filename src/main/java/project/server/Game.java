package project.server;

import project.Board;

import java.util.*;

public class Game extends Observable {

    private List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer arg) {
        this.observers.add(arg);
    }

    public void notifyObserver(Object obj){
        for (Observer observer : this.observers) {
            observer.update(this,obj);
        }
    }


    /** OLD DATA ***/
    private boolean roomIsFull;
    private Board gameBoard;
    private String turnOf;
    private ArrayList<Player> playerList;
    private ArrayList<String> allowedGods;
    private int turnNumber;
    private ArrayList<String> godsList;

    public Game(){
        gameBoard = new Board();
        playerList = new ArrayList();
        roomIsFull = false;
        godsList = new ArrayList<String>();
        allowedGods = new ArrayList<String>();
        godsList.add("Apollo");
        godsList.add("Artemis");
        godsList.add("Athena");
        godsList.add("Atlas");
        godsList.add("Demeter");
        godsList.add("Hephaestus");
        godsList.add("Minotaur");
        godsList.add("Pan");
        godsList.add("Prometheus");

    }

    public synchronized void addPlayer(Player p){ // adds a new player to the list and
        playerList.add(p);
        System.out.println("added to playerlist: " + p.getName());
        if(playerList.size()>2){
            roomIsFull = true;
        }
    }

    public boolean setGods( String[] gList) {
        if (gList!= null && gList[0] != null && gList[1] != null && gList[2] != null) {
            if (godsList.contains(gList[0]) && godsList.contains(gList[0]) && godsList.contains(gList[0])) {
                allowedGods.add(gList[0]);
                allowedGods.add(gList[1]);
                allowedGods.add(gList[2]);
                turnNumber = 1;
                turnOf = playerList.get(0).getName();
                return true;
            }
        }
        return false;
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
        Collections.sort(playerList, (Player m1, Player m2) -> (int) (m1.getAge() - m2.getAge()));
        Player p = (Player) playerList.get(2);
        turnOf = p.getName();
        turnNumber = 0;
        System.out.println("turnof " + p.getName());
    }

    public String getTurnOf() {
        return turnOf;
    }
}
