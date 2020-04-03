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

        System.out.print("Insert username: ");
        try{
            String socketLine = "";
            while(!socketLine.equals("true")){

                String inputLineUsername = stdin.nextLine();
                System.out.println();
                while((inputLineUsername.length()<3) || inputLineUsername.contains(";")){
                    System.out.print("Input error re-insert username: ");
                    inputLineUsername = stdin.nextLine();
                    System.out.println();
                }

                System.out.print("Insert your age: ");
                String inputLineAge = stdin.nextLine();
                int age = Integer.parseInt(inputLineAge);

                while((age<5 || age>120) || (inputLineAge.contains(";"))){
                    System.out.print("Input error re-insert your age: ");
                    inputLineAge = stdin.nextLine();
                    age = Integer.parseInt(inputLineAge);
                    System.out.println();
                }

                //send message to the project.server
                socketOut.println((inputLineUsername+";"+inputLineAge));    //send to server client's data
                socketOut.flush();

                //wait for project.server response
                socketLine = socketIn.nextLine();
                System.out.println(socketLine);
                if(socketLine.equals("true")){
                    //registrazione avvenuta
                }
                else{
                    //registrazione fallita
                }
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
