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
    private Scanner in;
    private PrintWriter out;

    public PlayerInstance(Socket socket, Game game,int playerid) throws IOException {
        this.game = game;
        this.socket = socket;
        this.playerid = playerid;
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            initPlayer();
            gameLoop();

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

    public void gameLoop() throws InterruptedException {

        String line = "";

        while (!line.equals("quit")) { //
            if (game.getTurnOf() != null && game.getTurnOf().equals(player.getName())) { // keeps reading game to check if it's my turn
                out.println(game.getTurnNumber() + game.getTurnOf());
                out.flush();
                System.out.println("A");
            }
            Thread.sleep(100);
        }
    }

    public void initPlayer(){

        //Saves initial player information
        String line = in.nextLine(); // the first message recived should be "username;age"
        String[] playerInfo = line.split(";");
        player = new Player(playerInfo[0],Integer.parseInt(playerInfo[1]));

        System.out.println("Player "+ playerInfo[0] + " connected with age: " + playerInfo[1]);
        game.addPlayer(player);
        out.println("true");
        out.flush();

    }
}
