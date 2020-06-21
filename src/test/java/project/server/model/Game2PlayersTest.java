package project.server.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.server.model.Game;
import project.server.model.Player;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class Game2PlayersTest {

    Game g;
    Player p1,p2,p3,p4;
    ArrayList <String> gList;

    @BeforeEach
        //Starting a game with 3 players before each test
    void setUp() {
        g = new Game();
        g.setNPlayers(2);
        p1 = new Player("Giovanni",22);
        p2 = new Player("Mattia",23);
        g.addPlayer(p1,0);
        g.addPlayer(p2,1);
        gList = new ArrayList<>();
        gList.add("apollo");
        gList.add("athena");
    }

    @Test
        //Game.addPlayer() and Game.init() test
    void addPlayer() {
        ArrayList<Player> playerList;
        playerList=g.getPlayerList();
        assertEquals(playerList.get(0).getName(),"Giovanni");
        assertEquals(playerList.get(1).getName(),"Mattia");
    }



    @Test
        //Testing Game.setGods()
    void setGods(){
        g.setGods(gList,1);
        assertEquals(g.getGodList().get(0),"apollo");   //Correct
        assertEquals(g.getGodList().get(1),"athena");

    }

    @Test
        //Testing Game.addGod()
    void addGod(){
        String gChoice="apollo";
        g.setGods(gList,1);         //Creating the allowed god list in game
        g.addGod(gChoice,p1,0);     //p1 choose a god
        assertEquals(p1.getGod(),gChoice);
        g.addGod(gChoice,p2,1);     //p2 can't choose a god which is already taken
        assertNotEquals(p2.getGod(),gChoice);
    }


    @Test
        //Testing Game.addWorker()
    void addWorker() throws IOException {
        String s1[] = {"0","1"};
        String s2[] = {"0","2"};
        g.setGods(gList,1);
        g.addGod("athena",p1,0);
        g.addGod("apollo",p2,1);
        assertTrue(g.addWorker(p1,0,1,0));
        assertTrue(g.addWorker(p1,0,2,0));
        assertFalse(g.addWorker(p1,0,2,0));              //Worker can't be added on a occupied cell
        assertFalse(g.addWorker(null,0,1,0));        //Function can't be used if there's not a player
        assertFalse(g.addWorker(p2,0,1,1));              //Function used on the wrong socket
    }


    @Test
        //Testing Game.move()
    void move() throws IOException {
        g.setGods(gList,1);
        g.addGod("athena",p1,0);
        g.addGod("apollo",p2,1);
        g.addWorker(p1, 0,0,0);
        g.moveWorker(p1, 0,0,0,1,0);
    }


    @Test
        //Testing Game.build()
    void build() throws IOException {
        g.setGods(gList,1);
        g.addGod("athena",p1,0);
        g.addGod("apollo",p2,1);
        g.addWorker(p1,0,0,0);
        g.build(p1,0,0,0,1,1,0);
    }

    @Test
    //Testing when a player win
    void win() throws IOException {
        g.setGods(gList,2);
        g.addGod("athena",p1,0);
        g.addGod("apollo",p2,1);
        g.addWorker(p1, 0,0,0);
        g.addWorker(p1, 4,0,0);
        g.addWorker(p2, 0,2,1);
        g.addWorker(p2, 4,4,1);
        g.getGameBoard().getBoard()[0][0].setLevel(2);
        g.getGameBoard().getBoard()[1][0].setLevel(3);
        g.moveWorker(p1,0,0,1,0,0);
    }


    @Test
    //Testing when a player when stuck
    void getStuck() throws IOException {
        g.setGods(gList,2);
        g.addGod("athena",p1,0);
        g.addGod("apollo",p2,1);
        g.addWorker(p1, 0,2,0);
        g.addWorker(p1, 4,0,0);
        g.addWorker(p2, 0,0,1);
        g.addWorker(p2, 4,4,1);
        g.getGameBoard().getBoard()[0][1].setLevel(4);
        g.getGameBoard().getBoard()[1][0].setLevel(4);
        g.getGameBoard().getBoard()[1][1].setLevel(4);
        g.getGameBoard().getBoard()[3][4].setLevel(4);
        g.getGameBoard().getBoard()[4][3].setLevel(4);
        g.getGameBoard().getBoard()[3][3].setLevel(4);
        g.moveWorker(p1,0,0,1,0,0);
        g.nextTurn();
        g.moveWorker(p1,0,2,0,3,0);
        g.build(p1,0,3,0,4,1,0);
        g.nextTurn();
    }

}