package project.server;

import java.util.ArrayList;

public class Player {

    private String name;
    private int age;
    private String god;

    public Player(String name, int age){

        this.name = name;
        this.age = age;
    }

    public void test (){
        System.out.println("CIAO SONO PLAYER");
    }

    public String getName() {
        return name;
    }

    public void selectGod(String god){
        this.god = god;
    }

    public void setInitWorkerPos(){}

    public void move(){}

    public void build(){}

    public void endTurn(){}

    public int getAge(){
        return age;
    }

}
