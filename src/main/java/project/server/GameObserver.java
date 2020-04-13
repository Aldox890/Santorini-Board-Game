package project.server;

import project.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

/*
* This class obeserves the status of the game and ....
* */
public class GameObserver implements Observer {
    Socket socket;
    PrintWriter out;
    private int socketId;
    ObjectOutputStream oos;

    public GameObserver(Socket socket,int socketId) throws IOException {
        this.socket = socket;
        this.socketId = socketId;
        out = new PrintWriter(socket.getOutputStream());
        oos = new ObjectOutputStream(socket.getOutputStream());
    }

    /*
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
                oos.writeObject(msg);
                oos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
