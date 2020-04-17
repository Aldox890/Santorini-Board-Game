package project.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ServerObserver extends Observable {
    private List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer arg) {
        this.observers.add(arg);
    }

    public void notifyObserver(Object obj){
        for (Observer observer : this.observers) {
            observer.update(this,obj);
        }
    }

    public void waitFromServer(){

    }
}
