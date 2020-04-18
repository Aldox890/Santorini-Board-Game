package project.client;

import project.Message;

import java.util.Observable;
import java.util.Observer;

public class ClientView implements Observer {


    @Override
    public void update(Observable o, Object arg) {
        Message mex = (Message) arg;
        System.out.println(mex.getData());
    }
}
