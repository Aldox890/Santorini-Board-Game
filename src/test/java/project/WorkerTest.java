package project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.server.model.Player;

import static org.junit.jupiter.api.Assertions.*;

class WorkerTest {

    Worker w;
    Player p;
    Cell c;

    @BeforeEach
    void setUp(){
        p = new Player("Pippo",22);
        c = new Cell(0,0);
        w = new Worker(p,c);
    }

    @Test
    void getOwner() {
        assertEquals(w.getOwner(),p);
    }

    @Test
    void setOwner() {
        Player p1 = new Player("Pluto",23);
        w.setOwner(p1);
        assertEquals(w.getOwner(),p1);
    }

    @Test
    void getCell() {
        assertEquals(w.getCell(),c);
    }

    @Test
    void setCell() {
        Cell c1 = new Cell(1,1);
        w.setCell(c1);
        assertEquals(w.getCell(),c1);
    }
}