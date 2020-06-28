package project.client.GUI;

import project.ClientMessage;
import project.server.model.Player;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class GodsPanel extends JPanel {
    private static final String graphicsPath = "graphics//gods//cards//";
    ArrayList<String> listOfGods;
    int nPlayers;
    ObjectOutputStream objectOutputStream;
    GodsPanel(ObjectOutputStream obj, int numOfPlayers) {
        this. nPlayers = numOfPlayers;
        this.objectOutputStream = obj;
        this.setBounds(0,0,1280,720);
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.setOpaque(true);
        this.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.6f));
        System.out.println("test");
        this.add(Box.createHorizontalStrut(300));

        addGods();

        this.add(Box.createHorizontalStrut(300));
    }

    /*
    *
    * */
    public void addGods(){
        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new GridLayout(3,3));
        innerPanel.setOpaque(false);
        innerPanel.setVisible(true);
        listOfGods = new ArrayList<>();
        File dir = new File(graphicsPath);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {

            for (File child : directoryListing) {
                String godName = child.getName();
                ImagePanel godImage = new ImagePanel(graphicsPath + godName, 138, 230);
                innerPanel.add(godImage);
                createListener(godImage, godName.substring(0,godName.length()-4));
                System.out.println(godName);
            }
        }

        this.add(innerPanel);
    }

    /*
    * adds the listener on the given god image when is pressed, that send the selected god card to the server.
    * */
    public void createListener(ImagePanel godImage, String name){
        GodsPanel gPanel = this;
        godImage.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(listOfGods.size()<nPlayers) {
                    if(!listOfGods.contains(name)) {
                        listOfGods.add(name);
                    }
                }
                if (listOfGods.size() >= nPlayers){
                    try {
                        objectOutputStream.writeObject(new ClientMessage(0,null, listOfGods, -1, -1,-1,-1,null));
                        objectOutputStream.flush();
                        gPanel.setEnabled(false);
                        gPanel.setVisible(false);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }
}
