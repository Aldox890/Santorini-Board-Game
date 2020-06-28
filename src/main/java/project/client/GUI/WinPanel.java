package project.client.GUI;

import javax.swing.*;
import java.awt.*;

/*
* Class for the panel that shows who has won the game.
* */
public class WinPanel extends JPanel {
    JPanel container;
    JPanel godCardContainer;
    JLabel text;

    WinPanel(String winner){
        this.setBounds(0,0,1280,720);
        this.setLayout(new BorderLayout());
        this.setOpaque(true);
        this.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.6f));

        JPanel filler = new JPanel();
        filler.setPreferredSize(new Dimension(300,720));
        filler.setOpaque(false);

        container = new JPanel();
        container.setLayout(new BorderLayout());
        container.setOpaque(false);
        container.setBounds(0,0,640,360);

        text = new JLabel(winner + "\n has won the game!!!");
        text.setFont(text.getFont().deriveFont(64.0f));
        text.setForeground(Color.WHITE);

        container.add(text, BorderLayout.CENTER);
        this.add(filler, BorderLayout.LINE_START);
        this.add(container, BorderLayout.CENTER);

        this.setVisible(true);
    }
}
