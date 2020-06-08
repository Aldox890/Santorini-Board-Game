package project.client.GUI;

import project.ClientMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ChooseGodPanel extends JPanel {

    private static final String graphicsPath = "graphics//gods//cards//";
    ArrayList<String> listOfAvailableGods;
    ObjectOutputStream objectOutputStream;
    private String chosenGod;

    public ChooseGodPanel(ObjectOutputStream obj, ArrayList<String> gList) {
        this.objectOutputStream = obj;
        listOfAvailableGods = gList;
        this.setLayout(null);
        this.setBounds(0,0,1280,720);
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.setOpaque(true);
        this.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.6f));
        System.out.println("test");
        this.add(Box.createHorizontalStrut(300));

        addGods();

        this.add(Box.createHorizontalStrut(300));
    }

    public void addGods(){
        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new GridLayout(3,3));
        innerPanel.setOpaque(false);
        innerPanel.setVisible(true);

        File dir = new File(graphicsPath);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {

            for (File child : directoryListing) {
                String godName = child.getName();
                if(listOfAvailableGods.contains(godName.substring(0,godName.length()-4))){
                    ImagePanel godImage = new ImagePanel(graphicsPath + godName, 138, 230);
                    innerPanel.add(godImage);
                    createListener(godImage, godName.substring(0,godName.length()-4));
                }

                System.out.println(godName);
            }
        }

        this.add(innerPanel);
    }

    public void createListener(ImagePanel godImage, String name){
        ChooseGodPanel cgPanel = this;
        godImage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                    try {
                        chosenGod = name;
                        objectOutputStream.writeObject(new ClientMessage(1,name, null, -1, -1,-1,-1,null));
                        objectOutputStream.flush();

                        cgPanel.setEnabled(false);
                        cgPanel.setVisible(false);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
            }
        });

    }

    public String getChosenGod() {
        return chosenGod;
    }

}
