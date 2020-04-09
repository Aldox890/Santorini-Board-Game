package project.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
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

    public Client(String ip, int port){
        this.ip = ip;
        this.port = port;
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

        System.out.print("Insert username: ");
        try{
            String socketLine = "";
            while(!socketLine.equals("true")){

                String inputLineUsername = stdin.nextLine();
                System.out.println();
                while((inputLineUsername.length()<3) || inputLineUsername.contains(";")){
                    System.out.print("Input error re-insert username: ");
                    inputLineUsername = stdin.nextLine();
                    System.out.println();
                }

                System.out.print("Insert your age: ");
                String inputLineAge = stdin.nextLine();
                int age = Integer.parseInt(inputLineAge);

                while((age<5 || age>120) || (inputLineAge.contains(";"))){
                    System.out.print("Input error re-insert your age: ");
                    inputLineAge = stdin.nextLine();
                    age = Integer.parseInt(inputLineAge);
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

                    /*RECEIVE LIST OF PLAYERS */
                    serverResponse = socketIn.nextLine().split(";");
                    if(serverResponse[0].equals("-1")){
                        System.out.println("Giocatori connessi: "+ serverResponse[1]+" "+serverResponse[2]+" "+serverResponse[3]);
                    }
                    /*-------------------------*/

                 /*   String[] playerturn = socketIn.nextLine().split(";");
                    //System.out.println(playerturn[0]);
                    if(playerturn[1].equals(inputLineUsername)) {
                        if(Integer.parseInt(playerturn[0]) == 0){
                            System.out.println("Seleziona 3 divinità:");
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
                            System.out.println("Client: "+inputGodsSelected);
                            socketOut.println(inputGodsSelected);
                            socketOut.flush();                                                  // <---------
                        }
                    }

                    socketLine = socketIn.nextLine();
                    System.out.println("Server: "+socketLine);*/

                    /*
                    * Chosing of 3 gods
                    * */
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
}
