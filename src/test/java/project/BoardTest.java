package project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.server.model.Player;
import project.server.model.Board;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    Player p,p1,p2;
    Board b;
    Worker w;

    @BeforeEach
    void setUp() {
        p = new Player("Giovanni",22);
        p1= new Player("Mattia",23);
        p2= new Player("Aldo",24);
        b = new Board();

    }

    @Test
    void moveWorkerApolloMinotauroPan() {
        p.selectGod("Apollo");
        p1.selectGod("Minotaur");
        p2.selectGod("Pan");
        b.createWorker(p,0,1);
        b.createWorker(p1,1,2);
        b.createWorker(p2,2,1);
        b.createWorker(p2,3,3);
        assertEquals(b.move(p,0,1,0,2),1);   //Corretto
        b.resetState();
        b.resetCurrentWorker();
        assertEquals(b.move(p,1,1,0,2),0);   //Posizione senza worker
        b.resetState();
        b.resetCurrentWorker();
        assertEquals(b.move(p,0,2,2,2),0);   //Troppo distante
        b.resetState();
        b.resetCurrentWorker();
        assertEquals(b.move(p,1,5,0,2),0);   //Fuori dai bordi
        b.resetState();
        b.resetCurrentWorker();
        assertEquals(b.move(p,0,2,5,2),0);   //Fuori dai bordi
        b.resetState();
        b.resetCurrentWorker();
        assertEquals(b.move(p,0,2,0,2),0);   //Posizione uguale
        b.board[0][3].setLevel(4);
        b.resetState();
        b.resetCurrentWorker();
        assertEquals(b.move(p,0,2,0,3),0);   //Troppo alto
        b.resetState();
        b.resetCurrentWorker();
        assertEquals(b.move(p,1,2,0,2),0);   //Spostare un worker non tuo
        b.resetState();
        b.resetCurrentWorker();
        assertEquals(b.move(p,0,2,1,2),1);    //MOVE DI APOLLO
        b.resetState();
        b.resetCurrentWorker();
        assertEquals(b.move(p1,0,2,1,2),1);   //MOVE DI MINOTAURO
        b.board[2][1].setLevel(2);
        b.resetState();
        b.resetCurrentWorker();
        assertEquals(b.move(p2,2,1,2,2),0);   //Posizione occupata
        b.resetState();
        b.resetCurrentWorker();
        assertEquals(b.move(p2,2,1,2,0),-1);   //MOVE DI PAN
        b.resetState();
        b.resetCurrentWorker();
        assertNotNull(b.board[1][2].isOccupiedBy());
        assertNull(b.board[0][2].isOccupiedBy());
        assertNull(b.board[0][1].isOccupiedBy());
    }


    @Test
    void moveWorkerArtemisAthena(){
        p.selectGod("Arthemis");
        p1.selectGod("Athena");
        p2.selectGod("Pan");
        b.createWorker(p,0,1);
        b.createWorker(p1,1,2);
        b.createWorker(p1,2,1);
        b.createWorker(p2,3,3);
        assertEquals(b.move(p,0,1,1,1),1);
        assertEquals(b.move(p,1,1,1,0),1);
        b.resetState();
        b.resetCurrentWorker();
        b.board[2][2].setLevel(1);
        assertEquals(b.move(p1,2,1,2,2),1);
        b.resetState();
        b.resetCurrentWorker();
        b.board[4][4].setLevel(1);
        assertEquals(b.move(p2,3,3,4,4),0);
    }



    @Test
    void build(){
        b.createWorker(p,0,1);
        b.createWorker(p1,1,2);
        b.createWorker(p2,2,1);
        b.createWorker(p2,3,3);
        b.setNumberOfMoves();
        assertTrue(b.build(p,1,0,1,0,2));
        b.resetState();
        assertEquals(b.board[0][2].getLevel(),1);
        b.setNumberOfMoves();
        assertFalse(b.build(p,1,0,5,0,2));  //Fuori dai bordi
        b.resetState();
        b.setNumberOfMoves();
        assertFalse(b.build(p,1,1,2,0,2));  //Worker non tuo
        b.resetState();
        b.setNumberOfMoves();
        assertFalse(b.build(p,1,0,1,0,5));  //Fuori dai bordi
        b.board[0][2].setLevel(4);
        b.resetState();
        b.setNumberOfMoves();
        assertFalse(b.build(p,1,0,1,0,2));  //Cupola
        b.resetState();
        b.setNumberOfMoves();
        assertFalse(b.build(p,1,0,1,1,2));  //Posizione occupata
        b.resetState();
        b.setNumberOfMoves();
        assertFalse(b.build(p,1,0,1,0,4));  //Troppo distante

    }

    @Test
    void buildAtlasDemeterHephaestus(){
        p.selectGod("Atlas");
        p1.selectGod("Demeter");
        p2.selectGod("Hephaestus");
        b.createWorker(p,0,1);
        b.createWorker(p1,1,2);
        b.createWorker(p2,3,3);
        b.setNumberOfMoves();
        assertTrue(b.build(p,4,0,1,0,0));
        b.resetState();
        b.resetCurrentWorker();
        b.setNumberOfMoves();
        assertTrue(b.build(p1,1,1,2,2,2));
        assertTrue(b.build(p1,1,1,2,2,1));
        b.resetState();
        b.resetCurrentWorker();
        b.setNumberOfMoves();
        b.board[4][4].setLevel(2);
        assertFalse(b.build(p2,2,3,3,4,4));
        assertTrue(b.build(p2,2,3,3,3,4));
    }

    @Test
    void Prometheus(){
        p.selectGod("Prometheus");
        p1.selectGod("Apollo");
        p2.selectGod("Pan");
        b.createWorker(p,0,1);
        b.createWorker(p1,1,2);
        b.createWorker(p1,2,1);
        b.createWorker(p2,3,3);
        assertTrue(b.build(p,1,0,1,1,1));
        assertEquals(b.move(p,0,1,1,1),0);
        assertEquals(b.move(p,0,1,1,0),1);
        assertTrue(b.build(p,1,1,0,1,1));
    }

    @Test
    void createWorker(){
        b.createWorker(p,0,3);
        assertTrue(b.board[0][3].isOccupiedBy()!=null);

    }

    @Test
    void checkStuck(){
        b.createWorker(p,0,1);
        b.createWorker(p1,1,2);
        b.createWorker(p2,2,1);
        b.createWorker(p2,3,3);
        b.board[2][2].setLevel(3);
        b.board[2][3].setLevel(3);
        b.board[2][4].setLevel(3);
        b.board[3][2].setLevel(3);
        b.board[3][4].setLevel(3);
        b.board[4][2].setLevel(3);
        b.board[4][3].setLevel(3);
        b.board[4][4].setLevel(3);
        assertTrue(b.checkStuck(3,3));
        assertFalse(b.checkStuck(0,0));
    }


}