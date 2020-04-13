package project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.server.Player;
import project.Board;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    Player p;
    Board b;
    Worker w;

    @BeforeEach
    void setUp() {
        p = new Player("Giovanni",22);
        b = new Board();
        w = new Worker(p, b.board[0][1]);
    }

    @Test
    void moveWorker() {
        b.moveWorker(w,0,2);
        assertNotNull(b.board[0][2].isOccupiedBy());
        assertNull(b.board[0][1].isOccupiedBy());
    }

    @Test
    void buildInPos(){
        b.buildInPos(w,0,2);
        assertEquals(b.board[0][2].getLevel(),1);
    }

    @Test
    void createWorker(){
        b.createWorker(p,0,3);
        assertTrue(b.board[0][3].isOccupiedBy()!=null);

    }
}