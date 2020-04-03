package project.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class PlayerInstance implements Runnable {
    private Socket socket;
    private Game game;
    private int playerid;
    private Player player;

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

            //Saves initial player information
            String line = in.nextLine(); // the first message recived should be "username;age"
            String[] playerInfo = line.split(";");
            player = new Player(playerInfo[0],Integer.parseInt(playerInfo[1]));

            System.out.println("Player "+ playerInfo[0] + " connected with age: " + playerInfo[1]);
            game.addPlayer(player);
            out.println("true");
            out.flush();

            while (!line.equals("quit")) { //
                if (game.getTurnOf() != null && game.getTurnOf().equals(player.getName())) { // keeps reading game to check if it's my turn

                }
                Thread.sleep(100);
            }

            // Close socket stream
            out.println("quit");
            out.flush();
            System.out.println("Closing sockets"); in.close();
            out.close();
            socket.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
