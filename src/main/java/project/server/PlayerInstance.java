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

    public PlayerInstance(Socket socket, Game game, int playerid) throws IOException {
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
            System.out.println("Closing sockets");
            in.close();
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
                if (game.getTurnNumber() == 0){
                    // Manage 3 gods chosing phase
                    setGods();
                }
                else if(game.getTurnNumber() == 1){
                    // Manage god card pick
                }
                else if(game.getTurnNumber() == 2){
                    // Manage set inital workers position
                }
                else if (game.getTurnNumber() >= 3){
                    // Manage standard game
                }
            }
            Thread.sleep(100);
        }
    }

    public void setGods(){
        out.println(game.getTurnNumber() + ";" + game.getTurnOf());
        out.flush();

        String line = in.nextLine();
        String[] godsList = line.split(";");

        if(game.setGods(godsList)){
            out.println("true");
        }
        else{
            out.println("false");
        }
        out.flush();
    }

    public void initPlayer(){

        //Saves initial player information
        String line = in.nextLine(); // the first message recived should be "username;age"
        String[] playerInfo = line.split(";");
        player = new Player(playerInfo[0],Integer.parseInt(playerInfo[1]));

        System.out.println("Player "+ playerInfo[0] + " connected with age: " + playerInfo[1]);
        game.addPlayer(player,playerid);
        out.println("true");
        out.flush();

    }
}
