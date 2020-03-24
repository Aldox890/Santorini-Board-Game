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
        Socket socket = serverSocket.accept();

        Scanner in = new Scanner(socket.getInputStream());
        PrintWriter out = new PrintWriter(socket.getOutputStream());
    }
}
