package project.server;

import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

/*
* This class obeserves the status of the game and ....
* */
public class GameObserver implements Observer {
    Socket socket;

    public GameObserver(Socket socket){
        this.socket = socket;
    }
    @Override
    public void update(Observable o, Object arg) {

    }
}
