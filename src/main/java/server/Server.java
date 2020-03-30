package server;

import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Scanner;
import java.io.PrintWriter;

public class Server {

    public int port;
    private ServerSocket serverSocket;

    public Server(int port){
        this.port = port;
    }

    public void startServer() throws IOException{
        serverSocket = new ServerSocket(port);
        System.out.println("Server socket ready on port: " + port);

        //TODO: while loop waiting for ALL players to connect and start PlayerInstance as thread to serve each player.
        Socket socket = serverSocket.accept(); //waiting for first client connection
        System.out.println("Received client connection");

        //TODO: this should be moved into PlayerInstance class.
        Scanner in = new Scanner(socket.getInputStream());
        PrintWriter out = new PrintWriter(socket.getOutputStream());

        String line = in.nextLine();
        while (!line.equals("quit")) {
            out.println(line);
            out.flush();
            line = in.nextLine();
        }

        // Close socket stream
        out.println("quit");
        out.flush();
        System.out.println("Closing sockets"); in.close();
        out.close();
        socket.close();
        serverSocket.close();
    }
}
