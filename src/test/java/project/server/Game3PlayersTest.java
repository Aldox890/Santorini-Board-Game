package project.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.server.model.Game;
import project.server.model.Player;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class Game3PlayersTest {

    Game g;
    Player p1,p2,p3,p4;
    ArrayList <String> gList;

    @BeforeEach
    //Starting a game with 3 players before each test
    void setUp() {
        g = new Game();
        g.setNPlayers(3);
        p1 = new Player("Giovanni",22);
        p2 = new Player("Mattia",23);
        p3 = new Player("Aldo",35);
        g.addPlayer(p1,0);
        g.addPlayer(p2,1);
        g.addPlayer(p3,2);
        gList = new ArrayList<>();
        gList.add("apollo");
        gList.add("athena");
        gList.add("pan");
    }

    @Test
    //Game.addPlayer() and Game.init() test
    void addPlayer() {
        ArrayList<Player> playerList;
        playerList=g.getPlayerList();
        assertEquals(playerList.get(0).getName(),"Giovanni");
        assertEquals(playerList.get(1).getName(),"Mattia");
        assertEquals(playerList.get(2).getName(),"Aldo");
    }



    @Test
    //Testing Game.setGods()
    void setGods(){
        g.setGods(gList,2);
        assertEquals(g.getGodList().get(0),"apollo");   //Correct
        assertEquals(g.getGodList().get(1),"athena");
        assertEquals(g.getGodList().get(2),"pan");
        //assertFalse(g.setGods(new String[]{"", ""},2)); //Error

    }

    @Test
    //Testing Game.addGod()
    void addGod(){
        String gChoice="apollo";
        g.setGods(gList,3);         //Creating the allowed god list in game
        g.addGod(gChoice,p1,0);     //p1 choose a god
        assertEquals(p1.getGod(),gChoice);
        g.addGod(gChoice,p2,1);     //p2 can't choose a god which is already taken
        assertNotEquals(p2.getGod(),gChoice);

    }


    @Test
    //Testing Game.addWorker()
    void addWorker(){
        String s1[] = {"0","1"};
        String s2[] = {"0","2"};
        g.setGods(gList,3);
        g.addGod("athena",p1,0);
        g.addGod("apollo",p2,1);
        g.addGod("pan",p3,2);
        assertTrue(g.addWorker(p1,0,1,0));
        assertTrue(g.addWorker(p1,0,2,0));
        assertFalse(g.addWorker(p1,0,2,0));              //Worker can't be added on a occupied cell
        assertFalse(g.addWorker(null,0,1,0));        //Function can't be used if there's not a player
        assertFalse(g.addWorker(p2,0,1,1));              //Function used on the wrong socket
    }


    @Test
    //Testing Game.move()
    void move(){
        g.setGods(gList,3);
        g.addGod("athena",p1,0);
        g.addGod("apollo",p2,1);
        g.addGod("pan",p3,2);
        g.addWorker(p1, 0,0,0);
        g.moveWorker(p1, 0,0,0,1,0);
        g.moveWorker(p1, 0,0,0,1,0);
    }


    @Test
    //Testing Game.build()
    void build(){
        g.setGods(gList,3);
        g.addGod("athena",p1,0);
        g.addGod("apollo",p2,1);
        g.addGod("pan",p3,2);
        g.addWorker(p1,0,0,0);
        g.build(p1,0,0,0,1,1,0);
    }

    @Test
    //Testing Game.nextTurn()
    void nextTurn(){
        g.setGods(gList,3);
        g.addGod("athena",p1,0);
        g.addGod("apollo",p2,1);
        g.addGod("pan",p3,2);
        g.addWorker(p1,0,0,0);
        g.addWorker(p1,4,4,0);
        g.addWorker(p2,2,1,0);
        g.addWorker(p2,1,2,0);
        g.moveWorker(p1,0,0,0,1,0);
        g.nextTurn();
        g.build(p1,0,1,1,1,1,0);
        g.nextTurn();

    }



}