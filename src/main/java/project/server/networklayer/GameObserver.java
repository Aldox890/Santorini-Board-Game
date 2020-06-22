package project.server.networklayer;

import project.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

/*
* This class observes the status of the game and ....
* */
public class GameObserver implements Observer {
    Socket socket;
    PrintWriter out;
    private int socketId;
    ObjectOutputStream oos;


    public GameObserver(Socket socket,ObjectOutputStream o,int socketId) throws IOException {
        this.socket = socket;
        this.socketId = socketId;
        out = new PrintWriter(socket.getOutputStream());
        oos = o;
        oos.writeObject(new Message(socketId,0,"true",""));
        oos.flush();
    }

    /**
     * update function simply output strings to the socket by now.
     */
    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof Message){
            Message msg = (Message) arg;
            if(msg.getDest() != socketId && msg.getDest()  >=0) {
                return;
            }
            try {
                System.out.println("list arrived: " + msg.getData());
                oos.writeObject(msg);
                oos.flush();
                oos.reset(); // Reset will disregard the state of any objects already written to the stream.
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
    }
}
