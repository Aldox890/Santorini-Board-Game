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

public class AlertPanel extends JPanel {

    private static final String graphicsPath = "graphics//gods//cards//";
    private JLabel label;
    public AlertPanel() {
        this.setBounds(340,60,600,30);
        this.setOpaque(false);
        this.setBackground(Color.RED);
        label = new JLabel("Welcome to Santorini");
        label.setVisible(true);
        this.add(label);
        System.out.println("test");
    }

    public void setText(String s){
        label.setText((s!=null) ? s : "");
    }
}
