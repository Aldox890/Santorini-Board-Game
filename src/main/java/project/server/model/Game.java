package project.server.model;

import project.Message;
import project.Worker;

import java.util.*;


/*
* "Subject" class that contains all of the Model status attributes,
* and to be observed by the Observers.
*  */

public class Game extends Observable {
    public static final String reset = "\u001B[0m";
    private List<Observer> observers = new ArrayList<>();
    private ArrayList<Player> playerList;
    private Board gameBoard;
    private Player turnOf;
    private Worker worker;
    private boolean roomIsFull;

    private int nPlayers;


    public Game(){
        gameBoard = new Board();
        playerList = new ArrayList();
        roomIsFull = false;
        this.turnOf = new Player("",0);
        godsList = new ArrayList<String>();
        allowedGods = new ArrayList<String>();
        godsList.add("apollo");
        godsList.add("artemis");
        godsList.add("athena");
        godsList.add("atlas");
        godsList.add("demeter");
        godsList.add("hephaestus");
        godsList.add("minotaur");
        godsList.add("pan");
        godsList.add("prometheus");
        worker = null;
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


    public void badInputException(int socketId,int typeOfMessage,String data){
        notifyObserver(new Message(socketId,typeOfMessage,data, turnOf.getName()));
    }

    public void setNPlayers(int nPlayers){
        this.nPlayers = nPlayers;
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

        notifyObserver(new Message(socketId,0,"registered", turnOf.getName()));
        if (playerList.size() > nPlayers - 1) {
            roomIsFull = true;
            init();
        }

        return true;
    }

    /*
     * Orders the playerList and sets turnOf. Sends playerList to the clients
     */
    public void init(){
        Collections.sort(playerList, (Player m1, Player m2) -> (int) (m1.getAge() - m2.getAge()));  //sorting of players
        Player p = playerList.get(nPlayers - 1);
        turnOf = p;
        turnNumber = 0;
        String response = "";
        if (nPlayers == 3) {
            response = playerList.get(0).getName() + ";" + playerList.get(1).getName() + ";" + playerList.get(2).getName();

            playerList.get(0).setColor(Color.RED);
            playerList.get(1).setColor(Color.YELLOW);
            playerList.get(2).setColor(Color.CYAN);
        }
        else if (nPlayers == 2){
            response = playerList.get(0).getName() + ";" + playerList.get(1).getName();

            playerList.get(0).setColor(Color.RED);
            playerList.get(1).setColor(Color.YELLOW);
        }
        notifyObserver(new Message(-1,3,response, turnOf.getName()));
    }

    /*
     * TRUE if the worker is added inside the game board
     * FALSE if the worker isn't added inside the game board
     */
    public boolean addWorker(Player p,String[] parsedLine, int socketId){
        if (gameBoard.createWorker(p, Integer.parseInt(parsedLine[0]),Integer.parseInt(parsedLine[1]))) {
            if (p.getNumberOfWorker() == 2) {           //If a player has two worker change turn and print the board
                passTurn();
                Message mex = new Message(-1, 4, "true", turnOf.getName());
                mex.addBoard(gameBoard.getBoard());
                printBoard(mex);
                notifyObserver(mex);
                return true;
            }
            notifyObserver(new Message(-1, 4, "", turnOf.getName()));      //If a player add a worker print the board
            return true;
        }
        else {                                                                                   //ERROR : if a player choose a wrong position
            notifyObserver(new Message(socketId, 4, "false", turnOf.getName()));
            return false;
        }
    }

    public void printBoard(Message mex){
        //Cell[][] board = mex.getBoard();

        for(int i = 0;i<5;i++){
            for(int j = 0;j<5;j++){
                if(mex.getCell(i,j).isOccupiedBy() != null) {
                    String color = mex.getCell(i,j).isOccupiedBy().getOwner().getColor().getColor();
                    System.out.print(color+" P "+reset);
                }
                else{
                    System.out.print(" " + mex.getCell(i,j).getLevel() + " ");
                }
            }
            System.out.println("");
        }
        System.out.println("");
    }

    /*
     * Sets the 3 allowed gods to allowedGods list. sends false if fails.
     * Broadcast list to all players if it works.
     */
    public boolean setGods( String[] gList, int socketId) {
        if(gList.length==3){
            if (godsList.contains(gList[0].toLowerCase()) && godsList.contains(gList[1].toLowerCase()) && godsList.contains(gList[2].toLowerCase())) {
                allowedGods.add(gList[0].toLowerCase());
                allowedGods.add(gList[1].toLowerCase());
                allowedGods.add(gList[2].toLowerCase());
                for(int i =0; i <=allowedGods.size()-1;i++){    //debug print of gods
                    System.out.println(allowedGods.get(i));
                }
                turnNumber = 1;
                turnOf = getPlayerList().get(0);
                notifyObserver(new Message(-1,1,gList[0].toLowerCase() + ";" + gList[1].toLowerCase() + ";" + gList[2].toLowerCase(), turnOf.getName()));
                //notifyObserver("-1;1;" + gList[0] + ";" + gList[1] + ";" + gList[2]); OLD VERSION
                return true;
            }
        }
        else if(gList.length==2){
            if (godsList.contains(gList[0].toLowerCase()) && godsList.contains(gList[1].toLowerCase())) {
                allowedGods.add(gList[0].toLowerCase());
                allowedGods.add(gList[1].toLowerCase());
                for(int i =0; i <=allowedGods.size()-1;i++){    //debug print of gods
                    System.out.println(allowedGods.get(i));
                }
                turnNumber = 1;
                turnOf = getPlayerList().get(0);
                notifyObserver(new Message(-1,1,gList[0].toLowerCase() + ";" + gList[1].toLowerCase(), turnOf.getName()));
                //notifyObserver("-1;1;" + gList[0] + ";" + gList[1] + ";" + gList[2]); OLD VERSION
                return true;
            }
        }

        notifyObserver(new Message(socketId,3,"false", turnOf.getName()));
        return false;
    }

    public void addGod(String god, Player player, int socketId){
        if(allowedGods.contains(god)){
            player.selectGod(god);
            allowedGods.remove(god);
            turnNumber ++;
            if (turnNumber <=nPlayers) { turnOf = playerList.get(turnNumber - 1);}
            else { turnOf = playerList.get(0);}

            notifyObserver(new Message(-1,2,player.getName() + ";" + god, turnOf.getName()));
            //notifyObserver("-1;2;" + player.getName() + " picked " + god); OLD
            return;
        }
        notifyObserver(new Message(socketId,2,"false", turnOf.getName()));
    }

    /*
    * method that ends the turn of the current player
    * */
    public void passTurn(){
        int indexOfP = playerList.indexOf(turnOf);
        if (indexOfP < nPlayers - 1) { turnOf = playerList.get(indexOfP + 1); }
        else{ turnOf = playerList.get(0);}
        if(turnOf.getGod().equals("Athena")){
            gameBoard.resetCanMoveUp();
        }
        gameBoard.resetCurrentWorker();
        gameBoard.resetState();
    }

    public void nextTurn(){
        passTurn();
        checkStuckPlayer(this.turnOf);
        notifyObserver(new Message(-1, 6, "true", turnOf.getName()));
    }

    public void moveWorker(Player p,String[] parsedLine, int socketId) {
        int ris = gameBoard.move(p, Integer.parseInt(parsedLine[0]), Integer.parseInt(parsedLine[1]), Integer.parseInt(parsedLine[2]), Integer.parseInt(parsedLine[3]));
        if (ris == 1) {
            //sends the board to the client
            Message mex = new Message(-1, 5, "true", turnOf.getName());
            mex.addBoard(gameBoard.getBoard());
            notifyObserver(mex);
            return;
        }
        else if (ris == -1) { //win condition
            Message mex = new Message(-1, 30, "true", turnOf.getName());
            mex.addBoard(gameBoard.getBoard());
            notifyObserver(mex);
            return;
        }
        notifyObserver(new Message(socketId, 5, "false", turnOf.getName()));
    }


    public void build(Player p,String[] parsedLine,int level, int socketId){   // <------ DA MODIFICARE
        if (gameBoard.build(p, level, Integer.parseInt(parsedLine[0]), Integer.parseInt(parsedLine[1]), Integer.parseInt(parsedLine[2]), Integer.parseInt(parsedLine[3]))) {
            //send the board to the clinet
            Message mex = new Message(-1, 6, "true", turnOf.getName());
            mex.addBoard(gameBoard.getBoard());
            notifyObserver(mex);
            return;
        }
        notifyObserver(new Message(socketId,6,"false", turnOf.getName()));
    }

    /*
    * checks if a player has both the workers stucked
    * */
    public void checkStuckPlayer(Player p){
        Worker w1 = p.getWorkers().get(0);
        Worker w2 = p.getWorkers().get(1);
        if(gameBoard.checkStuckWorker(w1.getCell().getX(),w1.getCell().getY())){
            if (gameBoard.checkStuckWorker(w2.getCell().getX(),w2.getCell().getY())){
                gameBoard.removeWorker(w1);
                gameBoard.removeWorker(w2);
                Message mex = new Message(-1, 40, "true", turnOf.getName());
                mex.addBoard(gameBoard.getBoard());
                notifyObserver(mex);
                this.playerList.remove(p);
            }
        }

        if(playerList.size()==1){
            Message mex = new Message(-1, 30, "true", playerList.get(0).getName());
            mex.addBoard(gameBoard.getBoard());
            notifyObserver(mex);
        }
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
