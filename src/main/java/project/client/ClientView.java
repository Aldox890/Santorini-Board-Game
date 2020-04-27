package project.client;

import project.Cell;
import project.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

public class ClientView implements Observer {

    ObjectOutputStream objectOutputStream;
    Scanner stdin;
    ArrayList<String> players;
    ArrayList<String> availableGods;
    String username;

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
                case (0): // required player registration
                    if (mex.getData().equals("registered")) {
                        System.out.println("Successfully registered!");
                    }
                    else {
                        register();
                    }
                    break;

                case (3): // recived player list
                    if (!mex.getData().equals("false")) {
                        printPlayerList(mex);
                    }
                    if (mex.getData().equals("false")) {
                        System.out.println("Bad input");
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
                    }
                    choseGod(mex);
                    break;
                case(4): //recived any player worker positions
                    createWorker(mex); // setup my workers position if it's my turn
                    if(!mex.boardIsEmpty()){ printBoard(mex); }
                    break;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void register() throws IOException {
        System.out.print("Insert username: ");
        String inputLineUsername = stdin.nextLine();
        System.out.println();
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
        objectOutputStream.writeObject(new Message(0,0,(inputLineUsername+";"+inputLineAge), null));
        objectOutputStream.flush();
    }

    public void choseAllowedGods() throws IOException {
        if(players.get(2).equals(username)){
            System.out.println("Sei il giocatore più anziano, scegli 3 dei:" + "Apollo " + "Artemis " +"Athena "+ "Atlas "+"Demeter "+ "Hephaestus "+"Minotaur "+ "Pan "+ "Prometheus");

            //System.out.println("Seleziona 3 divinità:");
            String inputGodsSelected="";
            int gods_selection=1;
            while(gods_selection<=3){
                System.out.print("Divinità "+gods_selection+": ");    //input gods (aggiungere controlli)
                String input= stdin.nextLine();
                if(gods_selection!=3){
                    inputGodsSelected = (inputGodsSelected + (input+";"));
                }else{inputGodsSelected = (inputGodsSelected +input);}

                gods_selection++;
            }
            //System.out.println("Client: "+inputGodsSelected);
            objectOutputStream.writeObject(new Message(0,0,inputGodsSelected, null));
            objectOutputStream.flush();
        }
    }

    public void addAllowedGods(Message mex){
        String[] serverGodList = mex.getData().split(";");
        System.out.println("LISTA DEI SCELTI: "+ serverGodList[0]+ " " + serverGodList[1]+ " " +serverGodList[2]);

        for(int i=0;i<=serverGodList.length-1;i++){
            availableGods.add(serverGodList[i]);
        }
    }

    public void choseGod(Message mex) throws IOException {
        if(mex.getTurnOf().equals(username) && !availableGods.isEmpty()){  //tocca a me
            System.out.print("Seleziona il Dio: ");
            String input = stdin.nextLine();
            //inserire controllo input
            objectOutputStream.writeObject(new Message(0,1,input,null));
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

    public void printPlayerList(Message mex){
        String[] serverResponse = mex.getData().split(";");
        System.out.println("Giocatori connessi: "+ "1st-"+serverResponse[0]+" 2nd-"+serverResponse[1]+" 3rd-"+serverResponse[2]);

        for(int i=0;i<=serverResponse.length-1;i++){
            players.add(serverResponse[i]);
        }
    }

    public void createWorker(Message mex) throws IOException {
        if (mex.getTurnOf().equals(username)) {
            System.out.println("Inserisci X e Y (tra 0 e 4): ");
            System.out.print("Inserisci X: ");
            String x = stdin.nextLine();
            System.out.print("Inserisci Y: ");
            String y = stdin.nextLine();
            //inserire controllo input
            objectOutputStream.writeObject(new Message(0, 2, (x + ";" + y), null));
            objectOutputStream.flush();
        }
    }

    public void printBoard(Message mex){
        //Cell[][] board = mex.getBoard();

        for(int i = 0;i<5;i++){
            for(int j = 0;j<5;j++){
                if(mex.getCell(i,j).isOccupiedBy() != null) {
                    System.out.print("P");
                }
                else{
                    System.out.print(mex.getCell(i,j).getLevel());
                }
            }
            System.out.println("");
        }
    }

    /* strategy method to control if the input of the age is correct*/
    boolean checkAge(String inputAge){
        int age = Integer.parseInt(inputAge);
        return ( (age<5 || age>120) || (inputAge.contains(";")) );
    }
}
