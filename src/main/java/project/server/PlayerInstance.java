package project.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class PlayerInstance implements Runnable {
    private Socket socket;
    private Game game;
    private int playerid;

    public PlayerInstance(Socket socket, Game game,int playerid){
        this.game = game;
        this.socket = socket;
        this.playerid = playerid;
    }

    @Override
    public void run() {
        try {
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            String line = in.nextLine();
            while (!line.equals("quit")) {
                out.println(line);
                System.out.println(playerid+": " + line); //print input from client "ID: line"
                out.flush();
                line = in.nextLine();
            }

            // Close socket stream
            out.println("quit");
            out.flush();
            System.out.println("Closing sockets"); in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
