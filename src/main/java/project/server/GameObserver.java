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
    private int socketId;

    public GameObserver(Socket socket,int socketId) throws IOException {
        this.socket = socket;
        this.socketId = socketId;
        out = new PrintWriter(socket.getOutputStream());
    }

    /*
     * update function simply output strings to the socket by now.
     */
    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof String){
            String parsedArg[] = ((String) arg).split(";");
            if(Integer.parseInt(parsedArg[0]) != socketId && Integer.parseInt(parsedArg[0]) >=0) {
                return;
            }
            out.println((String)arg);
            out.flush();
        }
    }
}
