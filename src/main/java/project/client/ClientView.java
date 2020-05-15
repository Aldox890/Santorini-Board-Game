package project.client;

import project.Cell;
import project.ClientMessage;
import project.Message;
import project.server.model.Color;

import java.awt.*;      //<-- contrasto con lib SWING (?)
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;;

public class ClientView implements Observer {

    public static final String reset = "\u001B[0m";
    ObjectOutputStream objectOutputStream;
    Scanner stdin;
    ArrayList<String> players;
    ArrayList<String> availableGods;
    String username;
    String god;

    int hasSetWorkers=0;
    int hasMoved = 0;
    int hasBuild = 0;

    public ClientView(Socket s) throws IOException {
        objectOutputStream = new ObjectOutputStream(s.getOutputStream());
        stdin = new Scanner(System.in);
        players = new ArrayList<String>();
        availableGods = new ArrayList<String>();
    }

    @Override
    public void update(Observable o, Object arg) {
        Message mex = (Message) arg;
        try {
            switch(mex.getTypeOfMessage()){
                case(20): //con quanti giocatori vuoi giocare
                    System.out.println("Number of players of the game");
                    String numOfPlayers = stdin.nextLine();
                    objectOutputStream.writeObject(new ClientMessage(20,null, null, -1, -1,-1,-1,numOfPlayers));
                    objectOutputStream.flush();
                    break;
                case(25): //chiusura se crasha durante scelta dei
                    System.out.println(mex.getData());
                    System.exit(0);
                    break;

                case(30):   //WIN
                    printBoard(mex);
                    System.out.println(mex.getTurnOf()+" has won the game!");
                    System.exit(0);
                    break;

                case(40):
                    printBoard(mex);
                    System.out.println(mex.getTurnOf()+" is stuck and his workers has been removed from the board");
                    break;

                case (0): // required player registration
                    if (mex.getData().equals("registered")) {
                        System.out.println("Successfully registered!");
                    }
                    else if(mex.getErrorData()==null){
                        register();
                    }
                    else {
                        System.out.println(mex.getErrorData());
                        register();
                    }
                    break;

                case (3): // recived player list
                    if (!mex.getData().equals("false")) {
                        printPlayerList(mex);
                    }
                    if (mex.getData().equals("false")) {
                        System.out.println("Bad input");
                        System.out.println(mex.getErrorData());
                    }
                    choseAllowedGods();
                    break;

                case (1): // recived chosen gods list
                    addAllowedGods(mex);
                    choseGod(mex);
                    break;

                case (2): // recived god chosen by a player
                    removeAllowedGod(mex);
                    if(availableGods.isEmpty()){
                        createWorker(mex); // setup my workers position if it's my turn

                        break;
                    }
                    if (mex.getData().equals("false")) {
                        System.out.println("Bad input");
                        System.out.println(mex.getErrorData());
                    }
                    choseGod(mex);
                    break;
                case(4): //recived any player worker positions
                    if(!mex.boardIsEmpty()){ printBoard(mex); }
                    if(mex.getTurnOf().equals(username) && (hasSetWorkers<2 || mex.getData().equals("false"))){
                        if (mex.getData().equals("false")){
                            System.out.println(mex.getErrorData());
                            hasSetWorkers--;
                        }
                        createWorker(mex);
                    } // setup my workers position if it's my turn
                    else{turnMenu(mex);}
                    break;
                case(5): //if someone has moved and it's me, i build
                case(6):
                    if(!mex.boardIsEmpty()){ printBoard(mex); }
                    if (mex.getData().equals("false")) {
                        System.out.println("Bad input");
                        System.out.println(mex.getErrorData());
                    }
                    turnMenu(mex);
                    //checkTurnPhase(mex);
                    break;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    * method that checks if the player has to move or to build, based of what message receives from the server
    * */
    public void turnMenu(Message mex) throws IOException {
        if(mex.getTurnOf().equals(username)) {
            String in = "0";
            while (!in.equals("1") && !in.equals("2") && !in.equals("3")) {
                System.out.print("wha' will ye do next? ");
                System.out.print("1)Move ");
                System.out.print("2)Build ");
                System.out.println("3)End turn:");
                in = stdin.nextLine();
            }
            switch (in) {
                case ("1"):
                    moveWorker(mex);
                    break;
                case ("2"):     //controllo su
                    build(mex);
                    break;
                case ("3"):
                    objectOutputStream.writeObject(new ClientMessage(10,null, null, -1, -1,-1,-1,null));
                    objectOutputStream.flush();
                    break;
            }
        }
    }

    public void checkTurnPhase(Message mex) throws IOException {
        if(mex.getTurnOf().equals(username)){
            if(mex.getTypeOfMessage()==5){
                if(mex.getData().equals("false")){
                    moveWorker(mex);
                    System.out.println(mex.getErrorData());
                }
                else if(hasMoved==0) {
                    moveWorker(mex);
                    hasMoved--;
                }
                else if(hasMoved==1) {
                    build(mex);
                }
            }

            if(mex.getTypeOfMessage()==6){
                if(mex.getData().equals("false")){
                    System.out.println(mex.getErrorData());
                    build(mex);
                    hasBuild--;
                }
                else if(hasBuild==0) {
                    build(mex);
                }
                else if(hasBuild==1) {
                    System.out.println("Turn finished");
                }
            }
        }
    }

    public void register() throws IOException {
        System.out.print("Insert username: ");
        String inputLineUsername = stdin.nextLine();
        //System.out.println();
        while((inputLineUsername.length()<3) || inputLineUsername.contains(";")){
            System.out.print("Input error re-insert username: ");
            inputLineUsername = stdin.nextLine();
            System.out.println();
        }
        username = inputLineUsername;

        System.out.print("Insert your age: ");
        String inputLineAge = stdin.nextLine();

        while(checkAge(inputLineAge)){
            System.out.print("Input error re-insert your age: ");
            inputLineAge = stdin.nextLine();
            System.out.println();
        }

        //send message to the project.server
        objectOutputStream.writeObject(new ClientMessage(0,null, null, -1, -1,-1,-1,(inputLineUsername+";"+inputLineAge) ));
        objectOutputStream.flush();
    }

    public void choseAllowedGods() throws IOException {
        ArrayList<String> listOfGods = new ArrayList<>();
        if(players.get(players.size()-1).equals(username)){ //last player in list (eldest) choses the gods
            System.out.println("Sei il giocatore più anziano, scegli "+ players.size() +" dei:" + "Apollo " + "Artemis " +"Athena "+ "Atlas "+"Demeter "+ "Hephaestus "+"Minotaur "+ "Pan "+ "Prometheus");

            int gods_selection=1;   //index of selection
            while(gods_selection<=players.size()){
                System.out.print("Divinità "+gods_selection+": ");    //input gods (aggiungere controlli)
                String input= stdin.nextLine();
                if(!listOfGods.contains(input)){    //if the input isn't already in the list of gods, the input will be insert in; otherwise it wont.
                    listOfGods.add(input);
                    gods_selection++;
                }else{
                    System.out.println("Bad input, re-insert the current God");
                }
            }

            objectOutputStream.writeObject(new ClientMessage(0,null, listOfGods, -1, -1,-1,-1,null));
            objectOutputStream.flush();
        }
    }

    public void addAllowedGods(Message mex){
        String[] serverGodList = mex.getData().split(";");

        System.out.print("LISTA DEI SCELTI: ");
        for(int i=0;i<=serverGodList.length-1;i++){
            availableGods.add(serverGodList[i]);
            System.out.print(serverGodList[i]+" ");
        }
        System.out.println();
    }

    public void choseGod(Message mex) throws IOException {
        if(mex.getTurnOf().equals(username) && !availableGods.isEmpty()){  //tocca a me
            System.out.print("Seleziona il Dio: ");
            String input = stdin.nextLine();
            //inserire controllo input
            input = input.toLowerCase();
            god=input;
            objectOutputStream.writeObject(new ClientMessage(1,god, null, -1, -1,-1,-1,null));
                    //new Message(0,1,input,null));
            objectOutputStream.flush();
        }
    }

    public void removeAllowedGod(Message mex){
        if(!mex.getData().equals("false")){
            String[] selectedGod = mex.getData().split(";");
            System.out.println(selectedGod[0] + " has selected " + selectedGod[1]);
            availableGods.remove(selectedGod[1]);
        }
    }

    /*
    * prints the list of players with their respective color assaigned by the server.
    * */
    public void printPlayerList(Message mex){
        String[] serverResponse = mex.getData().split(";");
        if(serverResponse.length <2){   //if there is only 1 player left, because of disconnection=> WIN
            System.out.println(mex.getData());
            System.exit(0);
        }
        if(serverResponse.length == 3){
            System.out.println("Giocatori connessi: "+ Color.RED.getColor()+"1st-"+serverResponse[0]+Color.YELLOW.getColor()+" 2nd-"+serverResponse[1]+Color.CYAN.getColor()+" 3rd-"+serverResponse[2]+"\u001B[0m");
        }else if(serverResponse.length == 2){
            System.out.println("Giocatori connessi: "+ Color.RED.getColor()+"1st-"+serverResponse[0]+Color.YELLOW.getColor()+" 2nd-"+serverResponse[1]+"\u001B[0m");
        }
        for(int i=0;i<=serverResponse.length-1;i++){
            players.add(serverResponse[i]);
        }
    }

    /*
    function used to create a worker and set it in a cell.
    * */
    public void createWorker(Message mex) throws IOException {
        String coordinates;
        String coords[];
        if (mex.getTurnOf().equals(username)) {
            System.out.println("Insert workers' starting position:");
            coordinates = insertCoordinates();
            coords = coordinates.split(";");
            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);
            //inserire controllo input
            objectOutputStream.writeObject( new ClientMessage(2,null, null, x, y,-1,-1,null));
                    //new Message(0, 2, (x + ";" + y), null));
            objectOutputStream.flush();
            hasSetWorkers++;
        }
    }

    /*
    function used to print the game board on terminal
    * */
    public void printBoard(Message mex){
        Cell[][] board = mex.getBoard();
        for(int i = 0;i<5;i++){
            for(int j = 0;j<5;j++){
                if(board[i][j].isOccupiedBy() != null) {
                    String player = mex.getCell(i,j).isOccupiedBy().getOwner().getName().substring(0,1).toUpperCase();  //iniziale player
                    String color = mex.getCell(i,j).isOccupiedBy().getOwner().getColor().getColor();
                    String lvl = Integer.toString(mex.getCell(i,j).getLevel()); //level
                    System.out.print(color+player+lvl+" "+reset);
                }
                else{
                    System.out.print(" " + board[i][j].getLevel() + " ");
                }
            }
            System.out.println("");
        }
        System.out.println("turnof: "+ mex.getTurnOf());
        System.out.println("");

        mex.addBoard(null);
        mex = null;
    }

    /*
    * Function used to move one worker from a position [xStart;yStart] to the position [xdest;yDest]
    * */
    public void moveWorker(Message mex) throws IOException {  //   int x_start,int y_start,int x_dest,int y_dest
        if (mex.getTurnOf().equals(username)) {
            String[] coords = new String[2];
            System.out.println("Insert worker's MOVING starting point coordinates: ");
            String coordinates = insertCoordinates();

            coords = coordinates.split(";");
            int xStart = Integer.parseInt(coords[0]);
            int yStart = Integer.parseInt(coords[1]);

            System.out.println("Insert worker's MOVING destination point coordinates: ");
            coordinates=insertCoordinates();

            coords = coordinates.split(";");
            int xDest = Integer.parseInt(coords[0]);
            int yDest = Integer.parseInt(coords[1]);
            //inserire controllo input

            objectOutputStream.writeObject(new ClientMessage(3,null, null, xStart, yStart,xDest,yDest,null));
                    //new Message(0, 3, coordinates, null));
            objectOutputStream.flush();
            hasMoved++;
        }
        else{

        }
    }

    /*
    * Build function by the client.
    * Receives the message from the server, checks if it's the client's turn and asks for the building coordinates ( [xStart;yStart] and [xDest;yDest] ).
    * If the client who's building has the power of Hephaestus or Atlas, asks the client if wants to use its power.
    * Sends the building coordinates to the server.
    * */
    public void build(Message mex) throws IOException {  //   int x_start,int y_start,int x_dest,int y_dest
        int resp;
        if (mex.getTurnOf().equals(username)) {
            String[] coords = new String[2];
            System.out.println("Insert worker's starting BUILDING point coordinates: ");
            String coordinates = insertCoordinates();
            coords = coordinates.split(";");
            int xStart = Integer.parseInt(coords[0]);
            int yStart = Integer.parseInt(coords[1]);

            System.out.println("Insert worker's destination BUILDING point coordinates: ");
            coordinates = insertCoordinates();
            coords = coordinates.split(";");
            int xDest = Integer.parseInt(coords[0]);
            int yDest = Integer.parseInt(coords[1]);
            //inserire controllo input

            if(god.equals("hephaestus")){
                do {
                    System.out.println("Do you want to use Hephaestus power? 1-Y 2-N");
                    resp = Integer.parseInt(stdin.nextLine());
                }while(resp!=1 && resp!=2);
                if(resp==1){
                    objectOutputStream.writeObject( new ClientMessage(6,null, null, xStart, yStart,xDest,yDest,null));
                            //new Message(0, 6, coordinates, null));
                    objectOutputStream.flush();
                    hasBuild++;
                }
                else{
                    objectOutputStream.writeObject(new ClientMessage(4,null, null, xStart, yStart,xDest,yDest,null));
                            //new Message(0, 4, coordinates, null));
                    objectOutputStream.flush();
                    hasBuild++;
                }
            }
            else if(god.equals("atlas")){
                do {
                    System.out.println("Do you want to use atlas power? 1-Y 2-N");
                    resp = Integer.parseInt(stdin.nextLine());
                }while(resp!=1 && resp!=2);
                if(resp==1){
                    objectOutputStream.writeObject(new ClientMessage(7,null, null, xStart, yStart,xDest,yDest,null));
                            //new Message(0, 7, coordinates, null));
                    objectOutputStream.flush();
                    hasBuild++;
                }
                else{
                    objectOutputStream.writeObject(new ClientMessage(4,null, null, xStart, yStart,xDest,yDest,null));
                            //new Message(0, 4, coordinates, null));
                    objectOutputStream.flush();
                    hasBuild++;
                }
            }
            else{
                objectOutputStream.writeObject(new ClientMessage(4,null, null, xStart, yStart,xDest,yDest,null));
                        //new Message(0, 4, coordinates, null));
                objectOutputStream.flush();
                hasBuild++;
            }
        }

    }

    /* strategy method to control if the input of the age is correct*/
    boolean checkAge(String inputAge){
        int age = Integer.parseInt(inputAge);
        return ( (age<5 || age>120) || (inputAge.contains(";")) );
    }

    /*function that inserts the coordinates*/
    String insertCoordinates(){
        String x,y;
        do{
        System.out.print("Insert X: ");
        x = stdin.nextLine();
        System.out.print("Insert Y: ");
        y = stdin.nextLine();
        if(!isNumeric(x) || !isNumeric(y)){System.out.println("ERROR : You have not inserted a number");}
        }while(!isNumeric(x) || !isNumeric(y));
        return (x+";"+y);
    }

    /*function that checks if the input string is a number*/
    public boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}
