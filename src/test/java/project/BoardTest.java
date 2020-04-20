package project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.server.Player;
import project.Board;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    Player p,p1;
    Board b;
    Worker w;

    @BeforeEach
    void setUp() {
        p = new Player("Giovanni",22);
        p1= new Player("Mattia",23);
        b = new Board();
        b.createWorker(p,0,1);
        b.createWorker(p1,1,2);
    }

    @Test
    void moveWorker() {
        assertTrue(b.move(p,0,1,0,2));    //Corretto
        assertFalse(b.move(p,1,1,0,2));   //Posizione senza worker
        assertFalse(b.move(p,0,2,2,2));   //Troppo distante
        assertFalse(b.move(p,1,5,0,2));   //Fuori dai bordi
        assertFalse(b.move(p,0,2,5,2));   //Fuori dai bordi
        assertFalse(b.move(p,0,2,0,2));   //Posizione uguale
        b.board[0][3].setLevel(4);
        assertFalse(b.move(p,0,2,0,3));   //Troppo alto
        assertFalse(b.move(p,1,2,0,2));   //Spostare un worker non tuo
        assertFalse(b.move(p,0,2,1,2));   //Posizione occupata

        assertNotNull(b.board[0][2].isOccupiedBy());
        assertNull(b.board[0][1].isOccupiedBy());
    }

    @Test
    void build(){
        assertTrue(b.build(p,0,1,0,2));
        assertEquals(b.board[0][2].getLevel(),1);
        assertFalse(b.build(p,0,5,0,2));  //Fuori dai bordi
        assertFalse(b.build(p,1,2,0,2));  //Worker non tuo
        assertFalse(b.build(p,0,1,0,5));  //Fuori dai bordi
        b.board[0][2].setLevel(4);
        assertFalse(b.build(p,0,1,0,2));  //Cupola
        assertFalse(b.build(p,0,1,1,2));  //Posizione occupata
        assertFalse(b.build(p,0,1,0,4));  //Troppo distante

    }

    @Test
    void createWorker(){
        b.createWorker(p,0,3);
        assertTrue(b.board[0][3].isOccupiedBy()!=null);

    }


}