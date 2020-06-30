package project.client.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
* Class container of the infos of every player who's playing the game.
* */
public class PlayersPanel extends JPanel {
    private static final String graphicsPath = "graphics//";
    private static final String pathGodsIcon = "graphics//gods//icons";
    JPanel playerSlot;
    List<PlayerInfoPanel> playersInfoList = new ArrayList<PlayerInfoPanel>();

    PlayersPanel(){
        playerSlot = new JPanel();
        playerSlot.setOpaque(false);
        //playerSlot.setLayout(new GridLayout(4,1));
        playerSlot.setLayout(new BoxLayout(playerSlot,BoxLayout.PAGE_AXIS));
        this.setLayout(null);
        this.setBounds(0,0,300,720);

        ImagePanel background = new ImagePanel(graphicsPath + "DecoratedPanel.png",300,720);
        background.setBounds(-26,0,300,720);

        JPanel fillPanel = new JPanel();
        fillPanel.setPreferredSize(new Dimension(300,100));
        fillPanel.setOpaque(false);
        background.add(fillPanel);

        playerSlot.setBounds(49,130,230,200);
        background.add(playerSlot);

        this.setOpaque(false);
        this.add(background);

    }

    /**
     * Sets for a player its panel information panel
     * @param name
     * @param c
     * @return
     */
    JPanel createLabel(String name, Color c){
        PlayerInfoPanel infoPanel = new PlayerInfoPanel();
        infoPanel.setBounds(10,0,250,50);
        infoPanel.setImgGod("default");
        infoPanel.setUsername(name);
        infoPanel.setColor(c);

        playersInfoList.add(infoPanel);

        return infoPanel;
    }

    /**
     * Inserts a given player into the player slot with a color.
     * @param player
     * @param index
     */
    void addSinglePlayer(String player, String index){
        if(Integer.parseInt(index) == 0){
            playerSlot.add(createLabel(player,Color.RED));
        }
        if(Integer.parseInt(index) == 1){
            playerSlot.add(createLabel(player,Color.YELLOW));
        }
        if(Integer.parseInt(index) == 2){
            playerSlot.add(createLabel(player,Color.CYAN));
        }
    }

    /**
     * Sets the color for each player
     * @param players
     */
    void addPlayers(String[] players){
        ArrayList<Color> colors = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.YELLOW);
        colors.add(Color.CYAN);
        for(int i=0; i<=players.length-1;i++){
            playerSlot.add(createLabel(players[i],colors.get(i)));
        }
    }

    /**
     * Adds the god image of each player
     * @param player_name
     * @param god_name
     */
    void addGodThumbnail(String player_name, String god_name){
        for(PlayerInfoPanel p : playersInfoList){
            if(p.getUsername().equals(player_name)){
                p.setImgGod(god_name);
            }
        }
    }

    /**
     * Sets the border to blue of the container of the player who has the turn
     * @param player
     */
    public void setTurn(String player){
        for(PlayerInfoPanel p : playersInfoList){
            if(p.getUsername().equals(player)){
                p.setBorder(true);
            }else{
                p.setBorder(false);
            }
        }
    }


}
