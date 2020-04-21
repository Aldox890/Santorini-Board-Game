package project.client;
import project.Message;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

/*
*  _____           _   _  _______  ____   _____   _____  _   _  _____
  / ____|   /\    | \ | ||__   __|/ __ \ |  __ \ |_   _|| \ | ||_   _|
 | (___    /  \   |  \| |   | |  | |  | || |__) |  | |  |  \| |  | |
  \___ \  / /\ \  | . ` |   | |  | |  | ||  _  /   | |  | . ` |  | |
  ____) |/ ____ \ | |\  |   | |  | |__| || | \ \  _| |_ | |\  | _| |_
 |_____//_/    \_\|_| \_|   |_|   \____/ |_|  \_\|_____||_| \_||_____|
 *
 * Client class
* */


public class old_Client {
    private String ip;
    private int port;
    static final String[] GODS= {"Apollo", "Artemis","Athena", "Atlas","Demeter", "Hephaestus","Minotaur", "Pan", "Prometheus"};    //<--- per test
    ArrayList<String> players;
    ArrayList<String> availableGods;

    public old_Client(String ip, int port){
        this.ip = ip;
        this.port = port;
        players = new ArrayList<String>();
        availableGods = new ArrayList<String>();
    }

    public void startClient() throws IOException{
        System.out.println("   _____           _   _  _______  ____   _____   _____  _   _  _____\n" +
                            "  / ____|   /\\    | \\ | ||__   __|/ __ \\ |  __ \\ |_   _|| \\ | ||_   _|\n" +
                            " | (___    /  \\   |  \\| |   | |  | |  | || |__) |  | |  |  \\| |  | |\n" +
                            "  \\___ \\  / /\\ \\  | . ` |   | |  | |  | ||  _  /   | |  | . ` |  | |\n" +
                            "  ____) |/ ____ \\ | |\\  |   | |  | |__| || | \\ \\  _| |_ | |\\  | _| |_\n" +
                            " |_____//_/    \\_\\|_| \\_|   |_|   \\____/ |_|  \\_\\|_____||_| \\_||_____|");
        Socket socket = new Socket(ip,port);
        System.out.println("Connection established");

        Scanner socketIn = new Scanner(socket.getInputStream());
        PrintWriter socketOut = new PrintWriter(socket.getOutputStream());
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());   //per ricevere oggetto serializzato da server
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());   //per inviare oggetto serializzato a server
        Scanner stdin = new Scanner(System.in);

        //System.out.print("Insert username: ");
        //String inputLineUsername = stdin.nextLine();
        try{
            String socketLine = "";
            while(!socketLine.equals("true")){

                System.out.print("Insert username: ");
                String inputLineUsername = stdin.nextLine();
                System.out.println();
                while((inputLineUsername.length()<3) || inputLineUsername.contains(";")){
                    System.out.print("Input error re-insert username: ");
                    inputLineUsername = stdin.nextLine();
                    System.out.println();
                }

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
                //socketOut.println((inputLineUsername+";"+inputLineAge));    //send to server client's data

                //wait for project.server response
                //socketLine = socketIn.nextLine();
                //System.out.println(socketLine);
                Message mex = (Message) objectInputStream.readObject();
                if(mex.getData().equals("true")){
                    System.out.println("registrazione avvenuta");//registrazione avvenuta       // <---------

                    /*-- "in attesa di altri player...." --*/

                    /*RECEIVE LIST OF PLAYERS */

                    mex = (Message) objectInputStream.readObject();
                    String[] serverResponse = mex.getData().split(";");


                    if(mex.getTypeOfMessage() == 3){
                        System.out.println("Giocatori connessi: "+ "1st-"+serverResponse[0]+" 2nd-"+serverResponse[1]+" 3rd-"+serverResponse[2]);

                        for(int i=0;i<=serverResponse.length-1;i++){
                            players.add(serverResponse[i]);
                        }
                    }
                    /*-------------------------*/


                    /*--SCELTA 3 DEI--*/

                    if(serverResponse[2].equals(inputLineUsername)){
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

                    mex = (Message) objectInputStream.readObject();
                    String[] serverGodList = mex.getData().split(";");
                   System.out.println("LISTA DEI SCELTI: "+ serverGodList[0]+ " " + serverGodList[1]+ " " +serverGodList[2]);

                    for(int i=0;i<=serverGodList.length-1;i++){
                        availableGods.add(serverGodList[i]);
                    }

                    /*-------------------------*/

                    /*
                    * Chosing of 3 gods by each player
                    * */

                    String input="";
                    do{
                        if(mex.getTypeOfMessage()==2){
                            String[] selectedGod = mex.getData().split(";");
                            availableGods.remove(selectedGod[1]);
                        }
                        if(mex.getTypeOfMessage()==0 || mex.getTypeOfMessage()==1 || mex.getTypeOfMessage()==2){    //typeOfMessage: 0 == "true/false"
                            if(mex.getTurnOf().equals(inputLineUsername) && !availableGods.isEmpty()){  //tocca a me
                                System.out.print("Seleziona il Dio: ");
                                input = stdin.nextLine();
                                //inserire controllo input
                                objectOutputStream.writeObject(new Message(0,1,input,null));
                                objectOutputStream.flush();
                            }

                        }
                        if ( (mex.getTypeOfMessage()==4 || mex.getTypeOfMessage()==0 || mex.getTypeOfMessage()==2) && mex.getTurnOf().equals(inputLineUsername) && availableGods.isEmpty()){ //ricevuto board
                            System.out.println("Inserisci X e Y (tra 0 e 4): ");
                            System.out.print("Inserisci X: ");
                            String x = stdin.nextLine();
                            System.out.print("Inserisci Y: ");
                            String y = stdin.nextLine();
                            //inserire controllo input
                            objectOutputStream.writeObject(new Message(0,2,(x +";"+ y),null));
                            objectOutputStream.flush();
                        }

                        mex = (Message) objectInputStream.readObject();
                        System.out.println("Server response: "+ mex.getData());

                    }while(true);

                   
                }
                else{
                    System.out.println("registrazione fallita");//registrazione fallita
                }
            }



            //gameloop
        }
        catch(NoSuchElementException e){
            System.out.println("Connection closed");
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally { stdin.close();
            socketIn.close();
            socketOut.close();
            socket.close();
        }
    }

    /* strategy method to control if the input of the age is correct*/
    boolean checkAge(String inputAge){
        int age = Integer.parseInt(inputAge);
        return ( (age<5 || age>120) || (inputAge.contains(";")) );
    }

}
