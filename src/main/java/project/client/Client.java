package project.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client {
    private String ip;
    private int port;

    public Client(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    public void startClient() throws IOException{
        Socket socket = new Socket(ip,port);
        System.out.println("Connection established");

        Scanner socketIn = new Scanner(socket.getInputStream());
        PrintWriter socketOut = new PrintWriter(socket.getOutputStream());
        Scanner stdin = new Scanner(System.in);

        try{
            String socketLine = "";
            while(!socketLine.equals("quit")){
                String inputLine = stdin.nextLine();

                //send message to the project.server
                socketOut.println(inputLine);
                socketOut.flush();

                //wait for project.server response
                socketLine = socketIn.nextLine();
                System.out.println(socketLine);
            }
        }
        catch(NoSuchElementException e){
            System.out.println("Connection closed");
        }
        finally { stdin.close();
            socketIn.close();
            socketOut.close();
            socket.close();
        }
    }
}
