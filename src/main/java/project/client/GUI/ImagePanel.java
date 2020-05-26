package project.client.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImagePanel extends JPanel{

    private BufferedImage img=null;
    int width;
    int height;

    public ImagePanel(int width, int height){
        try {
            this.width=width;
            this.height=height;
            img = ImageIO.read(new File("graphics//SantoriniBoard.png"));//Toolkit.getDefaultToolkit().createImage("graphics/SantoriniBoard.png");
        } catch (IOException e) {
            System.out.println("LOOZAPALOOZA");
        }
        loadImage(img);
    }

    public ImagePanel(String imgPath, int width, int height){
        try {
            this.width=width;
            this.height=height;
            img = ImageIO.read(new File(imgPath));
        } catch (IOException e) {
            System.out.println("LOOZAPALOOZA");
        }
        loadImage(img);
    }

    protected void paintComponent(Graphics g) {
        setOpaque(false);
        Image newImg = img.getScaledInstance(width,height,Image.SCALE_DEFAULT);
        g.drawImage(newImg, 0, 0, null);
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

    /*public void setBackground(String imgpath, int width, int height){
        try {
            this.width=width;
            this.height=height;
            img = ImageIO.read(new File(imgpath));
        } catch (IOException e) {
            System.out.println("LOOZAPALOOZA");
        }
        loadImage(img);
    }*/
}
