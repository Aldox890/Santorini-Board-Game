package project.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.server.model.Game;
import project.server.model.Player;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    Game g;
    Player p1,p2,p3,p4;
    @BeforeEach
    void setUp() {
        g = new Game();
        g.setNPlayers(3);
        p1 = new Player("Giovanni",22);
        p2 = new Player("Mattia",23);
        p3 = new Player("Aldo",35);
        g.addPlayer(p1,0);
        g.addPlayer(p2,1);
        g.addPlayer(p3,2);

    }

    //Game.addPlayer() and Game.init() test
    @Test
    void addPlayer() {
        ArrayList<Player> playerList;
        playerList=g.getPlayerList();
        assertEquals(playerList.get(0).getName(),"Giovanni");
        assertEquals(playerList.get(1).getName(),"Mattia");
        assertEquals(playerList.get(2).getName(),"Aldo");
    }



    @Test
    void setGods(){
        String gList[]={"Apollo","Athena","Pan"};
        g.setGods(gList,2);
        assertEquals(g.getGodList().get(0),"Apollo");
        assertEquals(g.getGodList().get(1),"Athena");
        assertEquals(g.getGodList().get(2),"Pan");
        assertFalse(g.setGods(new String[]{"", ""},2));

    }

    @Test
    void addGod(){
        String gList[]={"Apollo","Athena","Pan"};
        String gChoice="Apollo";
        g.setGods(gList,3);
        g.addGod(gChoice,p1,0);
        assertEquals(p1.getGod(),gChoice);
        g.addGod(gChoice,p2,1);
        assertNotEquals(p2.getGod(),gChoice);

    }


    @Test
    void addWorker(){
        String s1[] = {"0","1"};
        String s2[] = {"0","2"};
        String gList[]={"Apollo","Athena","Pan"};
        g.setGods(gList,3);
        g.addGod("Athena",p1,0);
        g.addGod("Apollo",p2,1);
        g.addGod("Pan",p3,2);
        assertTrue(g.addWorker(p1,s1,0));
        assertTrue(g.addWorker(p1,s2,0));
        assertFalse(g.addWorker(p1,s2,0));
        assertFalse(g.addWorker(null,s1,0));
        assertFalse(g.addWorker(p2,s1,1));
    }


    @Test
    void move(){
        String gList[]={"Apollo","Athena","Pan"};
        g.setGods(gList,3);
        g.addGod("Athena",p1,0);
        g.addGod("Apollo",p2,1);
        g.addGod("Pan",p3,2);
        g.addWorker(p1, new String[]{"0", "0"},0);
        g.moveWorker(p1, new String[]{"0", "0", "0", "1"},0);
    }


    @Test
    void build(){
        String gList[]={"Apollo","Athena","Pan"};
        g.setGods(gList,3);
        g.addGod("Athena",p1,0);
        g.addGod("Apollo",p2,1);
        g.addGod("Pan",p3,2);
        g.addWorker(p1, new String[]{"0", "0"},0);
        g.moveWorker(p1, new String[]{"0", "0", "0", "1"},0);
    }

}