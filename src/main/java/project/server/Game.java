package project.server;

import project.Board;

import java.util.*;


/*
* "Subject" class that contains all of the Model status attributes,
* and to be observed by the Observers.
*  */

public class Game extends Observable {

    private List<Observer> observers = new ArrayList<>();
    private ArrayList<Player> playerList;
    private Board gameBoard;
    private Player turnOf;
    private boolean roomIsFull;

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

    /*
     * Adds a new observer to the list.
     */
    public void addObserver(Observer arg) {
        this.observers.add(arg);
    }

    /*
     * Notifies the observers about changes.
     */
    public void notifyObserver(Object obj){
        for (Observer observer : this.observers) {
            observer.update(this,obj);
        }
    }

    /*
     * Adds a new player to the list and notifies the observer. false if player name already exists.
     * calls init() once 3 players are connected.
     */
    public synchronized boolean addPlayer(Player p,int socketId){
        for(int i = 0; i < playerList.size(); i++){
            if(playerList.get(i).getName().equals(p.getName())){
                notifyObserver(socketId + ";false");
                return false;
            }
        }
        playerList.add(p);
        System.out.println("added to playerlist: " + p.getName());

        notifyObserver(socketId + ";true");
        if (playerList.size() > 2) {
            roomIsFull = true;
            init();
        }

        return true;
    }

    /*
     * Orders the playerList and sets turnOf. Sends playerList to the clients
     */
    public void init(){
        Collections.sort(playerList, (Player m1, Player m2) -> (int) (m1.getAge() - m2.getAge()));
        Player p = playerList.get(2);
        turnOf = p;
        turnNumber = 0;
        System.out.println("turnof " + p.getName());
        String response = "-1;" + playerList.get(0).getName() + ";" + playerList.get(1).getName() + ";" + playerList.get(2).getName();
        notifyObserver(response);
    }

    /*
     * TRUE if the worker is added inside the game board
     * FALSE if the worker isn't added inside the game board
     */
    public boolean addWorker(Player p,int x, int y, int socketid){
        if  (gameBoard.createWorker(p,x,y)){
            notifyObserver("-1;3;ADDED NEW WORKER");
            return true;
        }
        notifyObserver(socketid + ";" + "false");
        return false;
    }

    /*
     * Sets the 3 allowed gods to allowedGods list. sends false if fails.
     * Broadcast list to all players if it works.
     */
    public boolean setGods( String[] gList, int socketId) {
        if (gList!= null && gList[1] != null && gList[2] != null && gList[3] != null) {
            if (godsList.contains(gList[1]) && godsList.contains(gList[2]) && godsList.contains(gList[3])) {
                allowedGods.add(gList[1]);
                allowedGods.add(gList[2]);
                allowedGods.add(gList[3]);
                turnNumber = 1;
                turnOf = getPlayerList().get(0);
                notifyObserver("-1;1;" + gList[1] + ";" + gList[2] + ";" + gList[3]);
                return true;
            }
        }
        notifyObserver(socketId + ";" + "false");
        return false;
    }

    public void addGod(String[] god, Player player, int socketId){
        if(god[1] != null && allowedGods.contains(god[1])){
            player.selectGod(god[1]);
            allowedGods.remove(god[1]);
            turnOf = playerList.get(turnNumber - 1);
            turnNumber ++;
            notifyObserver("-1;2;" + player.getName() + " picked " + god[1]);
            return;
        }
        notifyObserver(socketId + ";false");
    }

    public Player getTurnOf() {
        return turnOf;
    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    //PER TESTING
    public ArrayList<String> getGodList() {
        return allowedGods;
    }

    /** OLD DATA ***/
    private ArrayList<String> allowedGods;
    private int turnNumber;
    private ArrayList<String> godsList;

    public void addGod(String s){
        allowedGods.add(s);
    }

    public int getTurnNumber(){
        return turnNumber;
    }

    public synchronized void setTurnOf (Player p){
        turnOf = p;
    }

}
