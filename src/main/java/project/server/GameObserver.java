package project.server;

import java.io.IOException;
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

    public GameObserver(Socket socket) throws IOException {
        this.socket = socket;
        out = new PrintWriter(socket.getOutputStream());
    }

    /*
     * update function simply output strings to the socket by now.
     */
    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof String){
            out.println((String)arg);
            out.flush();
        }
    }
}
