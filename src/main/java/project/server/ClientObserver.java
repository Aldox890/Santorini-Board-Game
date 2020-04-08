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

    public ClientObserver(GameController gameController, Socket socket) throws IOException {
        this.gameController = gameController;
        this.socket = new Socket();

        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream());
    }

    @Override
    public void run() {
        String input = in.nextLine();
    }

    /*
    * on update of client status, makes the Controller act
    * */

}
