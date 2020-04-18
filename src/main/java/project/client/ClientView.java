package project.client;

import project.Message;
import project.server.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
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
    String username;

    public ClientView(Socket s) throws IOException {
        objectOutputStream = new ObjectOutputStream(s.getOutputStream());
        stdin = new Scanner(System.in);
        players = new ArrayList<String>();
    }

    @Override
    public void update(Observable o, Object arg) {
        Message mex = (Message) arg;

        switch(mex.getTypeOfMessage()){
            case(0):
                try {
                    if(mex.getData().equals("registered")){ System.out.println("Successfully registered!"); }
                    else{ register(); }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case(3):
                printPlayerList(mex);
                try {
                    choseAllowedGods();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
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

    public void printPlayerList(Message mex){
        String[] serverResponse = mex.getData().split(";");
        System.out.println("Giocatori connessi: "+ "1st-"+serverResponse[0]+" 2nd-"+serverResponse[1]+" 3rd-"+serverResponse[2]);

        for(int i=0;i<=serverResponse.length-1;i++){
            players.add(serverResponse[i]);
        }
    }

    /* strategy method to control if the input of the age is correct*/
    boolean checkAge(String inputAge){
        int age = Integer.parseInt(inputAge);
        return ( (age<5 || age>120) || (inputAge.contains(";")) );
    }
}
