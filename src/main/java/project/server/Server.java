package project.server;

import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Scanner;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private int port;

    private int playerid;
    private ServerSocket serverSocket;

    public Server(int port){
        this.port = port;
        playerid = 0;
    }

    public void startServer() throws IOException{
        ExecutorService executor = Executors.newCachedThreadPool();
        try{
            serverSocket = new ServerSocket(port);
        }
        catch(IOException e){
            System.err.println(e.getMessage());
            return;
        }
        System.out.println("Server socket ready on port: " + port);

        Game game = new Game();
        GameController gameController = new GameController(game);

        while(playerid < 3){ // server waits for 3 players to connect to the game
            try {
                Socket socket = serverSocket.accept();
                GameObserver gameObserver = new GameObserver(socket,playerid);
                game.addObserver(gameObserver);
                executor.submit(new ClientObserver(gameController, socket, playerid));
                playerid++;
            }
            catch(IOException e){
                break;
            }
        }
        serverSocket.close();
        //executor.shutdown();
    }
}
