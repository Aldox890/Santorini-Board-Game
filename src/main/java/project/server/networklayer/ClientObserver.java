package project.server.networklayer;

import project.Message;
import project.server.GameController;
import project.server.model.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
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

    public ClientObserver(GameController gameController, Socket socket,ObjectInputStream i, int socketId) throws IOException {
        this.gameController = gameController;
        this.socket = new Socket();
        this.socketId = socketId;
        String playerName;

        in = new Scanner(socket.getInputStream());
        ois = i;
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
            try {
                Message msg = (Message) ois.readObject();
                String parsedLine[];
                switch (msg.getTypeOfMessage()) {
                    case 0:
                        // Select 3 gods cards
                        parsedLine = msg.getData().split(";");
                        gameController.setGods(parsedLine,socketId);
                        break;
                    case 1:
                        // Select 1 god card
                        gameController.setGod(msg.getData(),player,socketId);
                        break;
                    case 2: //Client added a worker
                        parsedLine = msg.getData().split(";");
                        gameController.addWorker(player, parsedLine,socketId);
                        break;
                    case 3: //client wants to move a worker in a certain position
                        parsedLine = msg.getData().split(";");
                        gameController.moveWorker(player,parsedLine,socketId);
                        break;
                    case 4: // client wants to build in a certain position
                        parsedLine = msg.getData().split(";");
                        gameController.build(player,parsedLine,1,socketId);
                        break;
                    case 5:
                        //Move Artemis
                        break;
                    case 6:
                        parsedLine = msg.getData().split(";");
                        gameController.build(player,parsedLine,2,socketId);
                        break;
                    case 7:
                        parsedLine = msg.getData().split(";");
                        gameController.build(player,parsedLine,4,socketId);
                        break;
                    case 8:
                        //Build Prometheus
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
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
