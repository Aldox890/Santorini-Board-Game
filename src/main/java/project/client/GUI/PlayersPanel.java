package project.client.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class PlayersPanel extends JPanel {
    private static final String graphicsPath = "graphics//";
    private static final String pathGodsIcon = "graphics//gods//icons";
    JPanel playerSlot;
    List<PlayerInfoPanel> playersInfoList = new ArrayList<PlayerInfoPanel>();

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

    JPanel createLabel(String name, Color c){
        /*JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setPreferredSize(new Dimension(300,40));
        panel.setOpaque(false);

        JLabel godImg = new JLabel("god");
        godImg.setPreferredSize(new Dimension(35,35));
        godImg.setOpaque(false);
        //godImg.setBackground(Color.YELLOW);

        JLabel userLabel = new JLabel(name);

        JLabel color = new JLabel("colore");
        color.setPreferredSize(new Dimension(15,15));
        color.setOpaque(true);
        color.setBackground(c);*/


        //panel.add(godImg);
        //panel.add(userLabel);
        //panel.add(color);

        PlayerInfoPanel infoPanel = new PlayerInfoPanel();
        infoPanel.setImgGod("default");
        infoPanel.setUsername(name);
        infoPanel.setColor(c);

        playersInfoList.add(infoPanel);

        return infoPanel;
    }

    void addPlayers(String[] players){
        ArrayList<Color> colors = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.YELLOW);
        colors.add(Color.CYAN);
        for(int i=0; i<=players.length-1;i++){
            playerSlot.add(createLabel(players[i],colors.get(i)));
        }
    }

    /*adds the god image of each player */
    void addGodThumbnail(String player_name, String god_name){
        for(PlayerInfoPanel p : playersInfoList){
            if(p.getUsername().equals(player_name)){
                p.setImgGod(god_name);
            }
        }
    }


}
