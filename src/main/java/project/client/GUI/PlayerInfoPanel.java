package project.client.GUI;

import javax.swing.*;
import java.awt.*;

/**
* Class of the panel that contains the chosen god by a player, the player name and the color of its workers
* */
public class PlayerInfoPanel extends JPanel {

    private static final String pathGodsIcon = "graphics//gods//icons//";
    JLabel imgGod;
    JLabel username;
    JLabel color;


    public PlayerInfoPanel() {
        super();
        this.setPreferredSize(new Dimension(230,50));
        this.setLayout(new FlowLayout());
        this.setOpaque(false);

        imgGod = new JLabel();
        imgGod.setPreferredSize(new Dimension(35,35));
        imgGod.setOpaque(false);

        username = new JLabel();
        username.setMaximumSize(new Dimension(70,20));


        color = new JLabel();
        color.setPreferredSize(new Dimension(15,15));
        color.setOpaque(true);

        this.add(imgGod);
        this.add(username);
        this.add(color);
    }

    public Icon getImgGod() {
        return imgGod.getIcon();
    }

    /**
     * Sets the image based on the gods name
     * @param godname
     */
    public void setImgGod(String godname) {
        String path = pathGodsIcon + godname +"_c.png";
        ImageIcon img = new ImageIcon(path);
        this.imgGod.setIcon(img);
    }

    public String getUsername() {
        return username.getText();
    }

    public void setUsername(String name) {
        this.username.setText(name);
    }

    public void setColor(Color color) {
        this.color.setBackground(color);
    }

    /**
     * Sets the border of the panel if the boolean flag is true otherwise it resets it
      * @param active
     */
    public void setBorder(boolean active){
        if(active){
            this.setBorder(BorderFactory.createLineBorder(Color.BLUE,2));
        }else{
            this.setBorder(null);
        }
    }


}
