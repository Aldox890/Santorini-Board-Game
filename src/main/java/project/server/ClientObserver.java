package project.server;

import project.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;


/*
* This class observes the action taken by the Client, and manages the messages exchanged between the View and the Controller
* */
public class ClientObserver implements Runnable {
    GameController gameController;
    Socket socket;
    Scanner in;
    PrintWriter out;
    Player player;
    ObjectInputStream ois;
    private int socketId;

    public ClientObserver(GameController gameController, Socket socket, int socketId) throws IOException {
        this.gameController = gameController;
        this.socket = new Socket();
        this.socketId = socketId;
        String playerName;

        in = new Scanner(socket.getInputStream());
        ois = new ObjectInputStream(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            initPlayer();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        while(true) {
            String line = in.nextLine();
            String parsedLine[] = line.split(";");
            switch (Integer.parseInt(parsedLine[0])) {
                case 0:
                    // Select 3 gods cards
                    gameController.setGods(parsedLine,socketId);
                    break;
                case 1:
                    // Select 1 god card
                    gameController.setGod(parsedLine,player,socketId);
                    break;
                case 2:
                    gameController.addWorker(player, Integer.parseInt(parsedLine[1]), Integer.parseInt(parsedLine[2]),socketId);
                    break;
                case 3:
                    gameController.moveWorker();
                    break;
                case 4:
                    gameController.build();
                    break;
            }
        }
    }

    /*
    * Setup name and age of the player
    * */
    public void initPlayer() throws IOException, ClassNotFoundException {
        //Saves initial player information
        do {
            Message msg = (Message) ois.readObject(); // the first message recived should be "username;age"
            String[] playerInfo = msg.getData().split(";");
            player = new Player(playerInfo[0], Integer.parseInt(playerInfo[1]));
        } while (!gameController.addPlayer(player,socketId));
    }

}
