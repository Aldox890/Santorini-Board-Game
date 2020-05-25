package project;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.server.model.Player;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {

    Cell c;

    @BeforeEach
    void setUp(){
        c = new Cell(1,1);
    }


    @Test
    void getLevel() {
        assertEquals(c.getLevel(),0);
    }

    @Test
    void setLevel() {
        c.setLevel(1);
        assertEquals(c.getLevel(),1);
    }

    @Test
    void isOccupiedBy() {
        assertEquals(c.isOccupiedBy(),null);
    }

    @Test
    void setOccupiedBy() {
        c.setOccupiedBy(new Worker(new Player("Pippo",22),this.c));
        assertEquals(c.isOccupiedBy().getOwner().getName(),"Pippo");
    }

    @Test
    void getX() {
        assertEquals(c.getX(),1);
    }

    @Test
    void getY() {
        assertEquals(c.getY(),1);
    }
}