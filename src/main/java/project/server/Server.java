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
    private ServerSocket serverSocket;

    public Server(int port){
        this.port = port;
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

        while(true){
            try {
                Socket socket = serverSocket.accept();
                executor.submit(new PlayerInstance(socket));
            }
            catch(IOException e){
                break;
            }
        }
        executor.shutdown();

        //TODO: while loop waiting for ALL players to connect and start PlayerInstance as thread to serve each player.
        //Socket socket = serverSocket.accept(); //waiting for first project.client connection
        //System.out.println("Received project.client connection");


    }
}
