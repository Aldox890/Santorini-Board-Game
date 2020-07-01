package project.client.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
* Class that specifies a JPanel with an image.
* */
public class ImagePanel extends JPanel{

    private BufferedImage img=null;
    int width;
    int height;

    public ImagePanel(int width, int height){
        try {
            this.width=width;
            this.height=height;
            img = ImageIO.read(new File("graphics//SantoriniBoard.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadImage(img);
    }

    /**
     * Takes as input the path of an image and the desirable width and height
     * @param imgPath
     * @param width
     * @param height
     */
    public ImagePanel(String imgPath, int width, int height){
        try {
            this.width=width;
            this.height=height;
            img = ImageIO.read(new File(imgPath));
        } catch (IOException e) {
            e.printStackTrace();
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
}
