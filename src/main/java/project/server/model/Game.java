package project.server.model;

import project.Cell;
import project.Message;
import project.Worker;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;


public class Game extends Observable implements Serializable {
    private ArrayList <String> startingNames = new ArrayList<>();
    private int startingPlayers;
    private static final long serialVersionUID = 261752617;
    public static final String reset = "\033[0m";
    private List<Observer> observers = new ArrayList<>();
    private ArrayList<Player> playerList;
    private Board gameBoard;
    private Player turnOf;
    private Worker worker;
    private boolean roomIsFull;
    private String file="";
    private ArrayList<String> allowedGods;
    private int turnNumber;
    private ArrayList<String> godsList;
    private int nPlayers;

    /**
     *
     * @return number of players in game
     */
    public int getNPlayers(){
        return nPlayers;
    }

    /**
     * Initialize game object and add to godsList every god in Santorini
     */
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

    /**
     * Adds a new observer to the list.
     */
    public void addObserver(Observer arg) {
        this.observers.add(arg);
    }

    public void removeObserver(Observer arg) {
        this.observers.remove(arg);
    }

    /**
     * Notifies the observers about changes.
     * @param obj
     */
    public void notifyObserver(Object obj){
        for (Observer observer : this.observers) {
            observer.update(this,obj);
        }
    }

    /**
     * Notifies the client through the observer that has occoured an error in the input
     * @param socketId
     * @param typeOfMessage
     * @param data
     * @param errorData
     */
    public void badInputException(int socketId,int typeOfMessage,String data, String errorData){
        Message mex = new Message(socketId,typeOfMessage,data, turnOf.getName());
        mex.setErrorData(errorData);
        notifyObserver(mex);
    }

    /**
     * remove a player when he disconnects
     * @param p player to disconnect
     * @throws IOException
     */
    public void removePlayer(Player p) throws IOException {
        if ((p != null && allowedGods.isEmpty() && turnNumber>0)) {
            playerList.remove(p);
            if(p.getWorkers().size()>=2) {
                gameBoard.removeWorker(p.getWorkers().get(1));
                gameBoard.removeWorker(p.getWorkers().get(0));
            }
            subNPlayers();
            if (turnOf == p) {
                passTurn();
                notifyObserver(new Message(-1, 6, "true", turnOf.getName()));
            }
        }
        else{
            notifyObserver(new Message(-1, 25, "partita chiusa", turnOf.getName()));
        }
    }

    /**
     * Reduce nPlayers by 1
     */
    public void subNPlayers(){
        nPlayers = nPlayers -1;
    }

    public void setNPlayers(int nPlayers){
        this.nPlayers = nPlayers;
        startingPlayers = nPlayers;
    }

    /**
     * Adds a new player to the list and notifies the observer.
     * calls init() once 3 players are connected.
     * @return true if a player is added, false if player name already exists in actual game
     */
    public synchronized boolean addPlayer(Player p,int socketId){
        for(int i = 0; i < playerList.size(); i++){
            if(playerList.get(i).getName().equals(p.getName())){
                Message m = new Message(socketId,0,"false", turnOf.getName());
                m.setErrorData("Error: player already present in game, choose another username.");
                notifyObserver(m);
                return false;
            }
        }
        playerList.add(p);
        startingNames.add(p.getName());
        System.out.println("added to playerlist: " + p.getName());

        notifyObserver(new Message(socketId,0,"registered", turnOf.getName()));
        if (playerList.size() > nPlayers - 1) {
            roomIsFull = true;
            init();
        }

        return true;
    }

    /**
     * Orders the playerList and sets turnOf. Sends playerList to the clients
     * Checks if there is a saved game with same players of actual game
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
        else if (nPlayers == 1){
            notifyObserver(new Message(-1,3," YOU ARE THE ONLY PLAYER CONNECTED ", turnOf.getName()));
        }
        checkGame();
        if(file.equals("")){
            notifyObserver(new Message(-1,3,response, turnOf.getName()));
        }
    }

    /**
     * Adds a worker in board
     * @return TRUE if the worker is added inside the game board
     * @return FALSE if the worker isn't added inside the game board
     */
    public boolean addWorker(Player p,int x, int y, int socketId) throws IOException {
        if (gameBoard.createWorker(p,x,y)) {
            if (p.getNumberOfWorker() == 2) {           //If a player has two worker change turn and print the board
                try {
                    passTurn();
                    checkStuckPlayer(this.turnOf);
                }
                catch (IOException e){
                    e.printStackTrace();
                }
                Message mex = new Message(-1, 4, "true", turnOf.getName());
                mex.addBoard(gameBoard.getBoard());
                notifyObserver(mex);
                return true;
            }
            notifyObserver(new Message(-1, 4, "", turnOf.getName()));      //If a player add a worker print the board
            return true;
        }
        else {
            Message m = new Message(socketId, 4, "false", turnOf.getName());//ERROR : if a player choose a wrong position
            m.setErrorData("Error: Bad inputs while inserting workers");
            notifyObserver(m);
            return false;
        }
    }


    /**
     * Sets the 3 allowed gods to allowedGods list.
     * Broadcast list to all players if it works.
     * @param gods
     * @param socketId
     * @return true if gods are set, false if fails
     */
    public boolean setGods(ArrayList<String> gods, int socketId) {
        boolean correctGodsFlag = true;
        if(gods.size()==3){
            if (godsList.contains(gods.get(0).toLowerCase()) && godsList.contains(gods.get(1).toLowerCase()) && godsList.contains(gods.get(2).toLowerCase())) {
                for(int i =0; i <gods.size()-1;i++){
                    for(int j=i+1; j<gods.size();j++){
                        if(gods.get(i).equals(gods.get(j))){
                            correctGodsFlag = false;
                        }
                    }
                }
                if (correctGodsFlag) {
                allowedGods.add(gods.get(0).toLowerCase());
                allowedGods.add(gods.get(1).toLowerCase());
                allowedGods.add(gods.get(2).toLowerCase());

                turnNumber = 1;
                turnOf = getPlayerList().get(0);
                notifyObserver(new Message(-1,1,gods.get(0).toLowerCase() + ";" + gods.get(1).toLowerCase() + ";" + gods.get(2).toLowerCase(), turnOf.getName()));
                //notifyObserver("-1;1;" + gList[0] + ";" + gList[1] + ";" + gList[2]); OLD VERSION
                return true;
                }
            }
        }
        else if(gods.size()==2){
            if (godsList.contains(gods.get(0).toLowerCase()) && godsList.contains(gods.get(1).toLowerCase())) {
                for(int i =0; i<gods.size()-1;i++){
                    for(int j=i+1; j<gods.size();j++){
                        if(gods.get(i).equals(gods.get(j))){
                            correctGodsFlag = false;
                        }
                    }
                }
                if(correctGodsFlag){
                    allowedGods.add(gods.get(0).toLowerCase());
                    allowedGods.add(gods.get(1).toLowerCase());
                    turnNumber = 1;
                    turnOf = getPlayerList().get(0);
                    notifyObserver(new Message(-1,1,gods.get(0).toLowerCase() + ";" + gods.get(1).toLowerCase(), turnOf.getName()));
                    //notifyObserver("-1;1;" + gList[0] + ";" + gList[1] + ";" + gList[2]); OLD VERSION
                    return true;
                }
            }
        }

        Message m = new Message(socketId,3,"false", turnOf.getName());
        m.setErrorData("Error: unknown gods in the input.");
        notifyObserver(m);
        return false;
    }

    /**
     * Sets god chosen by a player and remove god from allowedGods list
     * @param god
     * @param player
     * @param socketId
     */
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

        Message m = new Message(socketId,2,"false", turnOf.getName());
        m.setErrorData("Error: God not in gods usage list.");
        notifyObserver(m);
    }

    /**
    * Method that ends the turn of the current player and saves the game
    * */
    public void passTurn() throws IOException {
        int indexOfP = playerList.indexOf(turnOf);
        /*if(indexOfP == -1){
            indexOfP = 0;
        }*/
        if (indexOfP < nPlayers - 1) { turnOf = playerList.get(indexOfP + 1); }
        else{ turnOf = playerList.get(0);}
        if(turnOf.getGod().equals("athena")){
            gameBoard.resetCanMoveUp();
        }
        gameBoard.resetCurrentWorker();
        gameBoard.resetState();
        if(playerList.size()>1){
            this.saveGame();
        }
    }

    /**
     * Method that calls passTurn() and checkStuckPlayer() if a player can end his turn
     * @throws IOException if game can't be saved
     */
    public void nextTurn() throws IOException {
        if(gameBoard.getNumberOfMoves()>=1 && gameBoard.getNumberOfBuild()>=1){
        passTurn();
        checkStuckPlayer(this.turnOf);
        notifyObserver(new Message(-1, 6, "true", turnOf.getName()));
        }
        else{
            notifyObserver(new Message(-1,6,"true",turnOf.getName()));
        }

    }

    /**
     * Invokes move() and according to results sends different messages to clients
     * @param p
     * @param xStart
     * @param yStart
     * @param xDest
     * @param yDest
     * @param socketId
     */
    public void moveWorker(Player p,int xStart,int yStart,int xDest, int yDest, int socketId) {
        int ris = gameBoard.move(p, xStart, yStart, xDest, yDest);
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
            deleteSavedGame();
            return;
        }
        Message m = new Message(socketId,5,"false", turnOf.getName());
        m.setErrorData("Error: Bad input for the move.");
        notifyObserver(m);

    }

    /**
     * Invokes Board.build() and according to results sends different messages to clients
     * @param p
     * @param xStart
     * @param yStart
     * @param xDest
     * @param yDest
     * @param level
     * @param socketId
     */
    public void build(Player p,int xStart,int yStart,int xDest, int yDest,int level, int socketId){   // <------ DA MODIFICARE
        if (gameBoard.build(p, level, xStart, yStart, xDest, yDest)) {
            //send the board to the clinet
            Message mex = new Message(-1, 6, "true", turnOf.getName());
            mex.addBoard(gameBoard.getBoard());
            notifyObserver(mex);
            return;
        }
        Message m = new Message(socketId,6,"false", turnOf.getName());
        m.setErrorData("Error: Bad input for the build.");
        notifyObserver(m);
    }

    /**
     * Checks if a player has both the workers stucked and removes the player from game and his workers from board when the condition is verified.
     * @param p
     * @throws IOException
     */
    public void checkStuckPlayer(Player p) throws IOException {

        if(p.getWorkers().size() < 2){
            return;
        }
        Worker w1 = p.getWorkers().get(0);
        Worker w2 = p.getWorkers().get(1);
        if(w1 != null && w2 != null){
            if(gameBoard.checkStuckWorker(w1.getCell().getX(),w1.getCell().getY())){
                if (gameBoard.checkStuckWorker(w2.getCell().getX(),w2.getCell().getY())){
                    gameBoard.removeWorker(w1);
                    gameBoard.removeWorker(w2);
                    Message mex = new Message(-1, 40, "true", turnOf.getName());
                    mex.addBoard(gameBoard.getBoard());
                    notifyObserver(mex);
                    passTurn();
                    this.playerList.remove(p);
                    nPlayers--;

                }
            }

            if(playerList.size()==1){
                Message mex = new Message(-1, 30, "true", playerList.get(0).getName());
                mex.addBoard(gameBoard.getBoard());
                notifyObserver(mex);

            }
        }

    }

    public Player getTurnOf() {
        return turnOf;
    }
    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    public ArrayList<String> getGodList() {
        return allowedGods;
    }

    public Board getGameBoard(){return gameBoard;}

    /**
     *Saves current state of the game in a file in "savedgames" directory
     */
     public void saveGame() {
        try {
            FileOutputStream f = new FileOutputStream(new File("savedgames//" + nPlayers + "-" + playersName()));
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(playerList);
            o.writeObject(gameBoard);
            o.writeObject(turnOf);
            o.close();
            f.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Check if there is a game in "savedgames" directory with same players of the actual game
     */
     public void checkGame(){
        File f = new File("savedgames");
        String[] fileList = f.list();
        for(String str : fileList){
            String names[] = str.split("-");
            int numPlayers = Integer.parseInt(names[0]);
            for(int i=1;i<numPlayers;i++){
                if(numPlayers == playerList.size()){
                    if(checkNames(names)){
                        file=str;
                        notifyObserver(new Message(0, 60, "true", turnOf.getName()));
                        return;
                    }
            }
            }
        }
    }

    /**
     * Loads an existing game
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void loadGame() throws IOException, ClassNotFoundException {
        file=".//savedgames//"+file;
        FileInputStream f=new FileInputStream(new File(this.file));
        ObjectInputStream o=new ObjectInputStream(f);
        ArrayList<Player> newPlayerList = (ArrayList<Player>)o.readObject();
        gameBoard = (Board)o.readObject();
        turnOf = (Player)o.readObject();
        for(int i=0; i<playerList.size();i++){
            if(turnOf.getName().equals(playerList.get(i).getName()))
            {
                turnOf=playerList.get(i);
            }
        }

        gameBoard.setCorrectPlayers(playerList);
        fixGods(newPlayerList);
        o.close();
        f.close();
        Message mex=new Message(-1,65,"true",turnOf.getName());
        mex.addBoard(gameBoard.getBoard());
        notifyObserver(mex);
        notifyObserver(new Message(-1,70,
                "You have to do "+(1-gameBoard.getNumberOfMoves())+" moves and "+(1-gameBoard.getNumberOfBuild())+" build",turnOf.getName()));
        notifyObserver(new Message(-1,5,"true",turnOf.getName()));
    }

    /**
     * @return all players name in this format name1-name2-name3-
     */
    public String playersName(){
        String s="";
        for(int i=0;i<playerList.size();i++) {
            s = s + playerList.get(i).getName() + "-";
        }
        return s;
    }

    /**
     * Checks if actual game players are the same of the saved games
     * @param names
     * @return true if there are a game with same players, else false
     */
    public boolean checkNames(String[] names){
        int cont=0;
        for(int i=0;i<playerList.size();i++){
            for(int j=1;j<playerList.size()+1;j++){
                if(playerList.get(i).getName().equals(names[j])){
                    cont++;
                }
            }
        }
        if(cont==playerList.size()){
            return true;
        }
        else return false;
    }

    /**
     * Players in actual game will have same gods they had in the previous saved game
     * @param plNew
     */
    public void fixGods(ArrayList<Player> plNew){
        for(int i=0;i<playerList.size();i++){
            for(int j=0;j<playerList.size();j++){
                if(playerList.get(i).getName().equals(plNew.get(j).getName())) {
                    playerList.get(i).selectGod(plNew.get(j).getGod());
                    notifyObserver(new Message(-1,420,playerList.get(i).getName()+";"+playerList.get(i).getGod()+";"+i,turnOf.getName()));
                }
            }
        }
    }



    /**
     * Used if a player doesn't want to load a previous game
     */
    public void callGod(){
        String response = "";
        if (playerList.size() == 3) {
            response = playerList.get(0).getName() + ";" + playerList.get(1).getName() + ";" + playerList.get(2).getName();

            playerList.get(0).setColor(Color.RED);
            playerList.get(1).setColor(Color.YELLOW);
            playerList.get(2).setColor(Color.CYAN);
        }
        else if (playerList.size() == 2){
            response = playerList.get(0).getName() + ";" + playerList.get(1).getName();

            playerList.get(0).setColor(Color.RED);
            playerList.get(1).setColor(Color.YELLOW);
        }
        notifyObserver(new Message(-1,3,response, turnOf.getName()));
    }

    /**
     * When a game gets to an end, its file is deleted
     */
    public void deleteSavedGame(){
        String filename="";
        filename=filename+startingPlayers+"-";
        for(int i=0;i<startingPlayers;i++) {
            filename=filename+startingNames.get(i)+"-";
        }
        try
        {
            Files.deleteIfExists(Paths.get("savedgames//"+filename));
        }
        catch(NoSuchFileException e)
        {
            System.out.println("No such file/directory exists");
        }
        catch(DirectoryNotEmptyException e)
        {
            System.out.println("Directory is not empty.");
        }
        catch(IOException e)
        {
            System.out.println("Invalid permissions.");
        }
        System.out.println("Deletion successful.");
    }

}

