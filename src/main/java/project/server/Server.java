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
    private Game game;
    private int playerid;
    private ServerSocket serverSocket;

    public Server(int port){
        this.game = new Game(); // game obj works as shared data;
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

        while(playerid < 3){
            try {
                Socket socket = serverSocket.accept();
                executor.submit(new PlayerInstance(socket,game,playerid));
                playerid++;
            }
            catch(IOException e){
                break;
            }
        }
        executor.shutdown();
    }
}
