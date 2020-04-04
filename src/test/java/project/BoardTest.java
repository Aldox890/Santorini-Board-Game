package project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.server.Player;
import project.Board;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void testMoveWorker() {
        Player p = new Player("Giovanni",22);
        p.test();
        Board b = new Board();
        Worker w = new Worker(p, b.board[0][1]);
        b.moveWorker(w,0,2);
        assertNotNull(b.board[0][2].isOccupiedBy());
    }
}