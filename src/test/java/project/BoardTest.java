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
    //Creating the board and three player before each test case
    void setUp() {
        p = new Player("Giovanni",22);
        p1= new Player("Mattia",23);
        p2= new Player("Aldo",24);
        b = new Board();

    }

    @Test
    //Testing of standard move, Apollo and Minothaur move and Pan win condition.
    void moveWorkerApolloMinotauroPan() {
        p.selectGod("apollo");
        p1.selectGod("minotaur");
        p2.selectGod("pan");
        b.createWorker(p,0,1);
        b.createWorker(p1,1,2);
        b.createWorker(p2,2,1);
        b.createWorker(p2,3,3);
        assertEquals(b.move(p,0,1,0,2),1);   //Correct move
        b.resetState();
        b.resetCurrentWorker();
        assertEquals(b.move(p,1,1,0,2),0);   //No worker in that position
        b.resetState();
        b.resetCurrentWorker();
        assertEquals(b.move(p,0,2,2,2),0);   //Too far to move
        b.resetState();
        b.resetCurrentWorker();
        assertEquals(b.move(p,1,5,0,2),0);   //Start position out of the board
        b.resetState();
        b.resetCurrentWorker();
        assertEquals(b.move(p,0,2,5,2),0);   //Destination out of the board
        b.resetState();
        b.resetCurrentWorker();
        assertEquals(b.move(p,0,2,0,2),0);   //Start = Destination
        b.board[0][3].setLevel(4);
        b.resetState();
        b.resetCurrentWorker();
        assertEquals(b.move(p,0,2,0,3),0);   //Too high to move
        b.resetState();
        b.resetCurrentWorker();
        assertEquals(b.move(p,1,2,0,2),0);   //Trying to move a worker which isn't yours
        b.resetState();
        b.resetCurrentWorker();
        assertEquals(b.move(p,0,2,1,2),1);    //Apollo move
        b.resetState();
        b.resetCurrentWorker();
        assertEquals(b.move(p1,0,2,1,2),1);   //Minothaur move
        b.board[2][1].setLevel(2);
        b.resetState();
        b.resetCurrentWorker();
        assertEquals(b.move(p2,2,1,2,2),0);   //Trying to move into an occupied position
        b.resetState();
        b.resetCurrentWorker();
        assertEquals(b.move(p2,2,1,2,0),-1);   //Pan win condition
        b.resetState();
        b.resetCurrentWorker();
        assertNotNull(b.board[1][2].isOccupiedBy());
        assertNull(b.board[0][2].isOccupiedBy());
        assertNull(b.board[0][1].isOccupiedBy());
    }


    @Test
    //Testing Arthemis and Athena move
    void moveWorkerArtemisAthena(){
        p.selectGod("arthemis");
        p1.selectGod("athena");
        p2.selectGod("pan");
        b.createWorker(p,0,1);
        b.createWorker(p1,1,2);
        b.createWorker(p1,2,1);
        b.createWorker(p2,3,3);
        assertEquals(b.move(p,0,1,1,1),1);  //First Arthemis move
        assertEquals(b.move(p,1,1,1,0),1);  //Second Arthemis move
        b.resetState();
        b.resetCurrentWorker();
        b.board[2][2].setLevel(1);
        assertEquals(b.move(p1,2,1,2,2),1); //Athena moves up
        b.resetState();
        b.resetCurrentWorker();
        b.board[4][4].setLevel(1);
        assertEquals(b.move(p2,3,3,4,4),0); //Other players can't move up
    }


    //Testing standard buildings
    @Test
    void build(){
        b.createWorker(p,0,1);
        b.createWorker(p1,1,2);
        b.createWorker(p2,2,1);
        b.createWorker(p2,3,3);
        b.setNumberOfMoves();
        assertTrue(b.build(p,1,0,1,0,2));   //Correct Build
        b.resetState();
        assertEquals(b.board[0][2].getLevel(),1);
        b.setNumberOfMoves();
        assertFalse(b.build(p,1,0,5,0,2));  //Starting position outside of the board
        b.resetState();
        b.setNumberOfMoves();
        assertFalse(b.build(p,1,1,2,0,2));  //Worker which isn't yours
        b.resetState();
        b.setNumberOfMoves();
        assertFalse(b.build(p,1,0,1,0,5));  //Destination position outside of the board
        b.board[0][2].setLevel(4);
        b.resetState();
        b.setNumberOfMoves();
        assertFalse(b.build(p,1,0,1,0,2));  //Can't build on a dome
        b.resetState();
        b.setNumberOfMoves();
        assertFalse(b.build(p,1,0,1,1,2));  //Can't build on an occupied cell
        b.resetState();
        b.setNumberOfMoves();
        assertFalse(b.build(p,1,0,1,0,4));  //Too far to build

    }

    @Test
    //Testing Atlas,Demeter and Hephaestus build
    void buildAtlasDemeterHephaestus(){
        p.selectGod("atlas");
        p1.selectGod("demeter");
        p2.selectGod("hephaestus");
        b.createWorker(p,0,1);
        b.createWorker(p1,1,2);
        b.createWorker(p2,3,3);
        b.setNumberOfMoves();
        assertTrue(b.build(p,4,0,1,0,0));   //Atlas build
        b.resetState();
        b.resetCurrentWorker();
        b.setNumberOfMoves();
        assertTrue(b.build(p1,1,1,2,2,2));  //First Demeter build
        assertTrue(b.build(p1,1,1,2,2,1));  //Second Demeter build
        b.resetState();
        b.resetCurrentWorker();
        b.setNumberOfMoves();
        b.board[4][4].setLevel(2);
        assertFalse(b.build(p2,2,3,3,4,4)); //Hepheastus can't use his power to build a dome
        assertTrue(b.build(p2,2,3,3,3,4));  //Hepheastus build
    }

    @Test
    //Testing Prometheus build, move and build
    void Prometheus(){
        p.selectGod("prometheus");
        p1.selectGod("apollo");
        p2.selectGod("pan");
        b.createWorker(p,0,1);
        b.createWorker(p1,1,2);
        b.createWorker(p1,2,1);
        b.createWorker(p2,3,3);
        assertTrue(b.build(p,1,0,1,1,1));       //First build
        assertEquals(b.move(p,0,1,1,1),0);  //Prometheus can't move up if he had built
        assertEquals(b.move(p,0,1,1,0),1);  //Prometheus move
        assertTrue(b.build(p,1,1,0,1,1));       //Second build
    }

    @Test
    //Testing worker creations
    void createWorker(){
        b.createWorker(p,0,3);
        assertTrue(b.board[0][3].isOccupiedBy()!=null);

    }

    @Test
    //Testing if a worker is stuck
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
        assertTrue(b.checkStuckWorker(3,3));    //Worker in (3,3) is stuck
        assertFalse(b.checkStuckWorker(0,0));
    }


}