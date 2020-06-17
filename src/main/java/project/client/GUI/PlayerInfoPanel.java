package project.client.GUI;

import javax.swing.*;
import java.awt.*;

public class PlayerInfoPanel extends JPanel {

    private static final String pathGodsIcon = "graphics//gods//icons//";
    JLabel imgGod;
    JLabel username;
    JLabel color;


    public PlayerInfoPanel() {
        super();
        this.setPreferredSize(new Dimension(250,40));
        this.setLayout(new FlowLayout());
        this.setOpaque(false);

        imgGod = new JLabel();
        imgGod.setPreferredSize(new Dimension(35,35));
        imgGod.setOpaque(false);

        username = new JLabel();


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

    /*sets the border of the panel if the boolean flag is true otherwise it resets it*/
    public void setBorder(boolean active){
        if(active){
            this.setBorder(BorderFactory.createLineBorder(Color.BLUE,1));
        }else{
            this.setBorder(null);
        }
    }


}
