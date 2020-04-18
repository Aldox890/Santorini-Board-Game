package project.client;

import project.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ServerObserver extends Observable {
    private List<Observer> observers = new ArrayList<>();
    Socket socket;
    ObjectInputStream ois;

    public ServerObserver(Socket socket) throws IOException {
        ois = new ObjectInputStream(socket.getInputStream());
        this.socket = socket;
    }

    public void addObserver(Observer arg) {
        this.observers.add(arg);
    }

    public void notifyObserver(Object obj){
        for (Observer observer : this.observers) {
            observer.update(this,obj);
        }
    }

    public void waitFromServer() throws IOException, ClassNotFoundException {
        while(true){
            Message obj = (Message)ois.readObject();
            notifyObserver(obj);
        }
    }
}
