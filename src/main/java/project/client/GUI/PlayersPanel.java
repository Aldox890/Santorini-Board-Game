package project.client.GUI;

import javax.swing.*;
import java.awt.*;

public class PlayersPanel extends JPanel {
    String graphicsPath = "src\\main\\java\\project\\client\\graphics\\";
    JPanel playerSlot;
    PlayersPanel(){
        playerSlot = new JPanel();
        playerSlot.setOpaque(false);
        playerSlot.setLayout(new GridLayout(4,1));
        this.setLayout(null);
        this.setBounds(0,0,300,720);

        ImagePanel background = new ImagePanel(graphicsPath + "DecoratedPanel.png",300,720);
        background.setBounds(-26,0,300,720);
        this.setOpaque(false);
        this.add(background);

        background.add(playerSlot);
        JPanel fillPanel = new JPanel();
        fillPanel.setPreferredSize(new Dimension(300,100));
        fillPanel.setOpaque(false);
        playerSlot.add(fillPanel);
    }

    JPanel createLabel(String name){
        JPanel panel = new JPanel();
        JLabel userLabel = new JLabel(name);
        panel.setPreferredSize(new Dimension(300,40));
        panel.setOpaque(false);
        panel.add(userLabel);
        return panel;
    }

    void addPlayers(String[] players){
        playerSlot.add(createLabel(players[0]));
        playerSlot.add(createLabel(players[1]));
        playerSlot.add(createLabel(players[2]));
    }
}
