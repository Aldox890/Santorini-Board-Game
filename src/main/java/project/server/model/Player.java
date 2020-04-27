package project.server.model;

import project.Worker;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {

    private String name;
    private int age;
    private ArrayList<Worker> workers = new ArrayList<>();


    private String god;

    public Player(String name, int age){

        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void selectGod(String god){
        this.god = god;
    }

    public String getGod() {
        return god;
    }

    public void addWorker(Worker w){
        workers.add(w);
    }

    /*
    * Removes from the Player's array list of workers the removed worker.
    * */
    public void  removeWorker(Worker w){
            workers.remove(w);
    }

    public int getNumberOfWorker(){
        return workers.size();
    }

    public int getAge(){
        return age;
    }

}
