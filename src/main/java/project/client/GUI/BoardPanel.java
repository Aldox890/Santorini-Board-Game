package project.client.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class BoardPanel extends JPanel {
    ObjectOutputStream objectOutputStream;
    String graphicsPath = "graphics//";
    GridBagLayout gridbaglayout;
    GridBagConstraints lim;

    public BoardPanel(/*ObjectOutputStream obj*/) {
        //this.objectOutputStream=obj;


        this.setSize(510,510);
        this.setOpaque(false);
        gridbaglayout = new GridBagLayout();
        lim = new GridBagConstraints();
        this.setLayout(gridbaglayout);

        for(int r=0;r<=4;r++){
            for(int c=0;c<=4;c++){
                //ImageIcon img1 = new ImageIcon(graphicsPath+"levelNumber\\3.png");
                //ImageIcon img = new ImageIcon(graphicsPath+"levelNumber\\dome45.png");
                BufferedImage combinedImage = null;
                try {
                    final BufferedImage bg = ImageIO.read(new File(graphicsPath+"buildings//3.png"));
                    final BufferedImage dome = ImageIO.read(new File(graphicsPath+"buildings//dome45.png"));
                    combinedImage = new BufferedImage(bg.getWidth(), bg.getHeight(), BufferedImage.TYPE_INT_ARGB );
                    Graphics2D g = combinedImage.createGraphics();
                    g.drawImage(bg,0,0,null);
                    g.drawImage(dome,26,26,null);
                    g.dispose();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                JButton component = new JButton("B"+r+","+c,new ImageIcon(combinedImage));
                //component.setPreferredSize(new Dimension(102,102));
                component.setContentAreaFilled(false);
                component.setOpaque(false);
                component.setBorder(null);
                lim.gridx=c;
                lim.gridy=r;
                lim.weightx=1;
                lim.weighty=1;
                lim.fill = GridBagConstraints.BOTH;
                //lim.insets = new Insets(10, 10, 10, 10); //lim è un GridBagConstraints
                gridbaglayout.setConstraints(component,lim);
                this.add(component);
            }
        }


    }

}
