package project.client;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameBoard extends JPanel{

    private BufferedImage img=null;
    public GameBoard(){
        try {
            img = ImageIO.read(new File("src\\main\\java\\project\\client\\graphics\\SantoriniBoard.png"));//Toolkit.getDefaultToolkit().createImage("graphics/SantoriniBoard.png");
        } catch (IOException e) {
            System.out.println("LOOZAPALOOZA");
        }
        loadImage(img);
    }

    protected void paintComponent(Graphics g) {
        setOpaque(false);
        g.drawImage(img, 0, 0, null);
        super.paintComponent(g);
    }


    private void loadImage(Image img) {
        try {
            MediaTracker track = new MediaTracker(this);
            track.addImage(img, 0);
            track.waitForID(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
