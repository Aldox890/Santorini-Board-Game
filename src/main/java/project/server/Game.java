package project.server;

import project.Board;
import project.Message;
import project.Worker;

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
        this.turnOf = new Player("",0);
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
                notifyObserver(new Message(socketId,0,"false", turnOf.getName()));
                return false;
            }
        }
        playerList.add(p);
        System.out.println("added to playerlist: " + p.getName());

        notifyObserver(new Message(socketId,0,"true", turnOf.getName()));
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

        String response = playerList.get(0).getName() + ";" + playerList.get(1).getName() + ";" + playerList.get(2).getName();
        notifyObserver(new Message(-1,3,response, turnOf.getName()));
    }

    /*
     * TRUE if the worker is added inside the game board
     * FALSE if the worker isn't added inside the game board
     */
    public boolean addWorker(Player p,String[] parsedLine, int socketId){
        if(p!= null && p.getNumberOfWorker()<2) {
            if (parsedLine != null && parsedLine[0] != null && parsedLine[1] != null) {
                if (gameBoard.createWorker(p, Integer.parseInt(parsedLine[0]),Integer.parseInt(parsedLine[1]))) {
                    if (p.getNumberOfWorker() == 2) {           //If a player has two worker change turn and print the board
                        passTurn();
                        notifyObserver(new Message(-1, 4, "", turnOf.getName()));
                        return true;
                    }
                    notifyObserver(new Message(-1, 4, "", turnOf.getName()));      //If a player add a worker print the board
                    return true;
                } else {                                                                                   //ERROR : if a player choose a wrong position
                    notifyObserver(new Message(socketId, 4, "false", turnOf.getName()));
                    return false;
                }
            }
            else{
                notifyObserver(new Message(socketId, 4, "false", turnOf.getName()));
            }
        }
        else{                                                                                       //ERROR : if a player has more than two worker
            notifyObserver(new Message(socketId,4,"false",turnOf.getName()));
            return false;
        }
        return false;
    }

    /*
     * Sets the 3 allowed gods to allowedGods list. sends false if fails.
     * Broadcast list to all players if it works.
     */
    public boolean setGods( String[] gList, int socketId) {
        if (gList!= null && gList[0] != null && gList[1] != null && gList[2] != null) {
            if (godsList.contains(gList[0]) && godsList.contains(gList[1]) && godsList.contains(gList[2])) {
                allowedGods.add(gList[0]);
                allowedGods.add(gList[1]);
                allowedGods.add(gList[2]);
                turnNumber = 1;
                turnOf = getPlayerList().get(0);
                notifyObserver(new Message(-1,1,gList[0] + ";" + gList[1] + ";" + gList[2], turnOf.getName()));
                //notifyObserver("-1;1;" + gList[0] + ";" + gList[1] + ";" + gList[2]); OLD VERSION
                return true;
            }
        }
        notifyObserver(new Message(socketId,1,"false", turnOf.getName()));
        return false;
    }

    public void addGod(String god, Player player, int socketId){

        if(god != null && allowedGods.contains(god)){
            player.selectGod(god);
            allowedGods.remove(god);
            turnNumber ++;
            if (turnNumber <=3) { turnOf = playerList.get(turnNumber - 1);}
            else { turnOf = playerList.get(0);}
            notifyObserver(new Message(-1,2,player.getName() + ";" + god, turnOf.getName()));
            //notifyObserver("-1;2;" + player.getName() + " picked " + god); OLD
            return;
        }
        notifyObserver(new Message(socketId,2,"false", turnOf.getName()));
    }

    private void passTurn(){
        int indexOfP = playerList.indexOf(turnOf);
        if (indexOfP < 2) { turnOf = playerList.get(indexOfP + 1); }
        else{ turnOf = playerList.get(0);}
    }

    public void move(Player player, int x, int y, int x1, int y1,int socketId){
        Worker worker;

        if(x<0 || x>4 || y<0 || y>4){
            notifyObserver(new Message(socketId,5,"false", turnOf.getName()));
        }

        worker=gameBoard.getBoard()[x][y].isOccupiedBy();

        if(worker.getOwner()!=player) {
            notifyObserver(new Message(socketId,5,"false", turnOf.getName()));
        }

        if(x1<0 || x1>4 || y1<0 || y1>4){
            notifyObserver(new Message(socketId,5,"false", turnOf.getName()));
        }

        if(gameBoard.getBoard()[x1][y1].isOccupiedBy()!=null){
            notifyObserver(new Message(socketId,5,"false", turnOf.getName()));
        }

        if(x==x1 && y==y1){
            notifyObserver(new Message(socketId,5,"false", turnOf.getName()));
        }

        if(gameBoard.getBoard()[x1][y1].getLevel()>gameBoard.getBoard()[x][y].getLevel()+1){
            notifyObserver(new Message(socketId,5,"false", turnOf.getName()));
        }

        if(gameBoard.getBoard()[x1][y1].getLevel()>3){
            notifyObserver(new Message(socketId,5,"false", turnOf.getName()));
        }


        int distanceX = Math.abs(x-x1);
        int distanceY = Math.abs(y-y1);

        if(distanceX > 1 || distanceY > 1){
            //ECCEZIONE
        }

        gameBoard.moveWorker(worker,x1,y1);
        if(gameBoard.getBoard()[x1][y1].getLevel()>3){
            notifyObserver(new Message(-1,7,"WIN", turnOf.getName()));
        }
        //passTurn();  TODO un giocatore dopo aver mosso deve poter costruire prima di passare il turno
        notifyObserver(new Message(-1,4,"",turnOf.getName()));

    }

    public void build(Player player,int x, int y, int x1, int y1,int socketId){
        Worker worker;

        if(x<0 || x>4 || y<0 || y>4){
            notifyObserver(new Message(socketId,6,"false", turnOf.getName()));
        }

        worker = gameBoard.getBoard()[x][y].isOccupiedBy();

        if(worker.getOwner()!=player){
            notifyObserver(new Message(socketId,6,"false", turnOf.getName()));
        }

        if(x1<0 || x1>4 || y1<0 || y1>4){
            notifyObserver(new Message(socketId,6,"false", turnOf.getName()));
        }

        if(gameBoard.getBoard()[x1][y1].getLevel()>4){
            notifyObserver(new Message(socketId,6,"false", turnOf.getName()));
        }

        if(gameBoard.getBoard()[x1][y1].isOccupiedBy()!=null){
            notifyObserver(new Message(socketId,6,"false", turnOf.getName()));
        }

        int distanceX = Math.abs(x-x1);
        int distanceY = Math.abs(y-y1);

        if(distanceX > 1 || distanceY > 1){
            notifyObserver(new Message(socketId,6,"false", turnOf.getName()));
        }

        gameBoard.buildInPos(worker,x1,y1);
        passTurn();
        notifyObserver(new Message(socketId,4,"", turnOf.getName()));
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
