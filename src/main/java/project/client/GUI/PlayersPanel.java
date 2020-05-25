package project.client.GUI;

import javax.swing.*;
import java.awt.*;

public class PlayersPanel extends JPanel {

    PlayersPanel(){
        this.setLayout(new GridLayout(3,1));
    }

    void addPlayers(String[] players){
        JLabel player1 = new JLabel(players[0]);
        JLabel player2 = new JLabel(players[1]);
        JLabel player3 = new JLabel(players[2]);

        this.add(player1);
        this.add(player2);
        this.add(player3);
    }
}
