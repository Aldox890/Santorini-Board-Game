package project.server;

import project.Worker;

import java.util.ArrayList;

public class Player {

    private String name;
    private int age;
    private ArrayList<Worker> workers = new ArrayList<>();


    private String god;

    public Player(String name, int age){

        this.name = name;
        this.age = age;
    }

    /*public void test (){
        System.out.println("CIAO SONO PLAYER");
    }*/

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

    public int getNumberOfWorker(){
        return workers.size();
    }

    public int getAge(){
        return age;
    }

}
