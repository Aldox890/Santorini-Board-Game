package project.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    Game g;
    Player p1,p2,p3,p4;
    @BeforeEach
    void setUp() {
        g = new Game();
        p1 = new Player("Giovanni",22);
        p2 = new Player("Mattia",23);
        p3 = new Player("Aldo",35);
    }

    //Game.addPlayer() and Game.init() test
    @Test
    void addPlayer() {
        ArrayList<Player> playerList;
        assertTrue(g.addPlayer(p1,0));
        assertFalse(g.addPlayer(p1,1));
        assertTrue(g.addPlayer(p2,1));
        assertTrue(g.addPlayer(p3,2));
        playerList=g.getPlayerList();
        assertEquals(playerList.get(0).getName(),"Giovanni");
        assertEquals(playerList.get(1).getName(),"Mattia");
        assertEquals(playerList.get(2).getName(),"Aldo");
    }

    @Test
    void addWorker(){
        assertTrue(g.addWorker(p1,0,1));
        assertFalse(g.addWorker(p2,0,1));
    }

    @Test
    void setGods(){
        g.addPlayer(p1,0);
        g.addPlayer(p2,1);
        g.addPlayer(p3,2);
        String gList[]={"3","Apollo","Athena","Pan"};
        g.setGods(gList,3);
        assertEquals(g.getGodList().get(0),"Apollo");
        assertEquals(g.getGodList().get(1),"Athena");
        assertEquals(g.getGodList().get(2),"Pan");
        assertFalse(g.setGods(null,3));

    }

    @Test
    void addGod(){
        g.addPlayer(p1,0);
        g.addPlayer(p2,1);
        g.addPlayer(p3,2);
        String gList[]={"2","Apollo","Athena","Pan"};
        String gChoice[]={"1","Apollo"};
        g.setGods(gList,3);
        g.addGod(gChoice,p1,0);
        assertEquals(p1.getGod(),gChoice[1]);
        g.addGod(gChoice,p2,1);
        assertNotEquals(p2.getGod(),gChoice[1]);

    }
}