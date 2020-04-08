package project.server;

import java.util.Observable;
import java.util.Observer;


/*
* This class observes the action taken by the Client, and manages the messages exchanged between the View and the Controller
* */
public class ClientObserver implements Observer {

    /*
    * on update of client status, makes the Controller act
    * */
    @Override
    public void update(Observable o, Object arg) {

    }
}
