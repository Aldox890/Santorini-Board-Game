package project.server;

import java.io.IOException;
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
    private int socketId;

    public ClientObserver(GameController gameController, Socket socket, int socketId) throws IOException {
        this.gameController = gameController;
        this.socket = new Socket();
        this.socketId = socketId;

        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream());
    }

    @Override
    public void run() {
        initPlayer();
    }

    /*
    * Setup name and age of the player
    * */
    public void initPlayer(){

        //Saves initial player information
        String line = in.nextLine(); // the first message recived should be "username;age"
        String[] playerInfo = line.split(";");
        player = new Player(playerInfo[0],Integer.parseInt(playerInfo[1]));

        gameController.addPlayer(player,socketId);
    }

}
