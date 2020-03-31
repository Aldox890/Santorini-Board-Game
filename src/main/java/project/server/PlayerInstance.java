package project.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class PlayerInstance implements Runnable {
    Socket socket;
    public PlayerInstance(Socket s){
        this.socket = s;
    }

    @Override
    public void run() {
        try {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        //serverSocket.close();
    }
}
