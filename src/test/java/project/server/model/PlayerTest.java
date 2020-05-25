package project.server.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.Cell;
import project.Worker;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    Player p;

    @BeforeEach
    void setUp(){
        p=new Player("Pippo",22);
    }

    @Test
    void getName() {
        assertEquals(p.getName(),"Pippo");
    }

    @Test
    void selectGod() {
        p.selectGod("Apollo");
        assertEquals(p.getGod(),"Apollo");
    }

    @Test
    void getGod() {
        assertEquals(p.getGod(),null);
    }

    @Test
    void addWorker() {
        p.addWorker(new Worker(this.p,new Cell(0,0)));
        assertEquals(p.getNumberOfWorker(),1);
        assertEquals(p.getWorkers().get(0).getCell().getX(),0);
    }

    @Test
    void getColor() {
        assertEquals(p.getColor(),null);;
    }

    @Test
    void setColor() {
        p.setColor(Color.RED);
        assertEquals(p.getColor(),Color.RED);
    }

    @Test
    void removeWorker() {
        p.addWorker(new Worker(this.p,new Cell(0,0)));
        p.removeWorker(p.getWorkers().get(0));
        assertEquals(p.getNumberOfWorker(),0);
    }

    @Test
    void getWorkers() {
        assertEquals(p.getWorkers(),new ArrayList<Worker>());
    }

    @Test
    void getNumberOfWorker() {
        assertEquals(p.getNumberOfWorker(),0);
    }

    @Test
    void getAge() {
        assertEquals(p.getAge(),22);
    }
}