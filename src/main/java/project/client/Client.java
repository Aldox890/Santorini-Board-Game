package project.client;

import java.io.IOException;
import java.io.PrintWriter;
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


public class Client {
    private String ip;
    private int port;
    static final String[] GODS= {"Apollo", "Artemis","Athena", "Atlas","Demeter", "Hephaestus","Minotaur", "Pan", "Prometheus"};    //<--- per test
    ArrayList<String> players;
    ArrayList<String> availableGods;

    public Client(String ip, int port){
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
                socketOut.println((inputLineUsername+";"+inputLineAge));    //send to server client's data
                socketOut.flush();

                //wait for project.server response
                //socketLine = socketIn.nextLine();
                //System.out.println(socketLine);
                String[] serverResponse = socketIn.nextLine().split(";");
                if(serverResponse[1].equals("true")){
                    System.out.println("registrazione avvenuta");//registrazione avvenuta       // <---------

                    /*-- "in attesa di altri player...." --*/

                    /*RECEIVE LIST OF PLAYERS */
                    serverResponse = socketIn.nextLine().split(";");


                    if(serverResponse[0].equals("-1")){
                        System.out.println("Giocatori connessi: "+ "1st-"+serverResponse[1]+" 2nd-"+serverResponse[2]+" 3rd-"+serverResponse[3]);

                        for(int i=1;i<=serverResponse.length-1;i++){
                            players.add(serverResponse[i]);
                        }
                    }
                    /*-------------------------*/


                    /*--SCELTA 3 DEI--*/

                    if(serverResponse[3].equals(inputLineUsername)){
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
                        socketOut.println(("0;"+inputGodsSelected));
                        socketOut.flush();
                    }

                    String[] serverGodList = socketIn.nextLine().split(";");
                    System.out.println("LISTA DEI SCELTI: "+ serverGodList[2]+ " " + serverGodList[3]+ " " +serverGodList[4]);

                    for(int i=1;i<=serverGodList.length-1;i++){
                        availableGods.add(serverGodList[i]);
                    }

                    /*-------------------------*/

                    /*
                    * Chosing of 3 gods by each player
                    * */
                    int turn=0;
                    while(turn<=2){
                        if(players.get(turn).equals(inputLineUsername)) {
                            System.out.print("Seleziona il Dio: ");
                            String input = stdin.nextLine();

                            /*- aggiungere controlli -*/
                            socketOut.println("1;" + input);
                            socketOut.flush();
                        }
                            turn++;
                            System.out.println("Server: "+ socketIn.nextLine());
                    }
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
