package project.client.GUI;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NumPlayersInputDialog extends JDialog {
    JLabel question;
    JButton twoPlayersButton, threePlayersButton;

    public NumPlayersInputDialog() {
        question = new JLabel("Select the number of players that will play in the game:");
        twoPlayersButton = new JButton("2");
        threePlayersButton = new JButton("3");

    }
}
