package project.server.model;

import project.Worker;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {

    private String name;
    private int age;
    private ArrayList<Worker> workers = new ArrayList<>();
    private Color color;



    private String god;

    /**
     * Constructor of a Player object
     * @param name his username
     * @param age his age
     */
    public Player(String name, int age){

        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    /**
     * Set god chosen by the player
     */
    public void selectGod(String god){
        this.god = god;
    }

    /**
     * @return actual god of the player
     */
    public String getGod() {
        return god;
    }

    public void addWorker(Worker w){
        workers.add(w);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    /**
    * Removes from the Player's arraylist of workers the removed worker.
    * */
    public void  removeWorker(Worker w){
            workers.remove(w);
    }

    public ArrayList<Worker> getWorkers() {
        return workers;
    }

    public int getNumberOfWorker(){
        return workers.size();
    }

    public int getAge(){
        return age;
    }

}
