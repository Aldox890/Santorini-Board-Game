package project.client.GUI;

import javax.swing.*;
import java.awt.*;

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
