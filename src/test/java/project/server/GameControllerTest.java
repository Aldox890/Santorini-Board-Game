package project.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.server.model.Game;
import project.server.model.Player;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    Game g;
    GameController gc;
    Player p1,p2,p3;

    @BeforeEach
    void setUp() {
        g= new Game();
        gc= new GameController(g);
        p1 = new Player("Giovanni",22);
        p2 = new Player("Mattia",23);
        p3 = new Player("Aldo",35);
        g.addPlayer(p1,0);
        g.addPlayer(p2,1);
        g.addPlayer(p3,2);
    }

    @Test
    void setGods() {
        String[] gods = new String[]{"Apollo", "Pan","Atlas"};
        gc.setGods(gods,2);
        assertEquals(g.getGodList().get(0),"Apollo");
        assertEquals(g.getGodList().get(1),"Pan");
        assertEquals(g.getGodList().get(2),"Atlas");
    }


    @Test
    void addPlayer() {
        assertFalse(gc.addPlayer(p1,0));  //It already exists
    }

    @Test
    void addWorker() {
        gc.addWorker(p1, new String[]{"0","0"},0);
        gc.addWorker(p1,null,0);
    }

    @Test
    void moveWorker() {
        String[] gods = new String[]{"Apollo", "Pan","Atlas"};
        gc.setGods(gods,2);
        gc.setGod("Apollo",p1,0);
        gc.setGod("Pan",p2,1);
        gc.addWorker(p1, new String[]{"0","0"},0);
        gc.moveWorker(p1,new String[]{"0","0","1","1"},0);
        gc.build(p1,new String[]{"1","1","0","0"},1,0);
    }
}