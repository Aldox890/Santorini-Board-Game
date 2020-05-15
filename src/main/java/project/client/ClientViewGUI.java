package project.client;

import project.server.model.Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;


public class ClientViewGUI {

    private static  void ClientViewGUI(){
        //JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame santoriniFrame = new JFrame("Santorini");
        //santoriniFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //santoriniFrame.setLayout(new BorderLayout());

        GameBoard gameBoard = new GameBoard();

       /* JPanel containerPanel = new JPanel();
        JPanel gameBoardPanel = new JPanel();
        JPanel playerInfoPanel = new JPanel();
        JPanel controlsPanel = new JPanel();


        containerPanel.setLayout(new BorderLayout());
        playerInfoPanel.setLayout(new BorderLayout());
        controlsPanel.setLayout(new FlowLayout());
        gameBoardPanel.setLayout(new GridLayout(5,5));*/

        santoriniFrame.getContentPane().add(gameBoard);
        santoriniFrame.setSize(400, 287);
        santoriniFrame.setVisible(true);

        santoriniFrame.addMouseListener(new MouseAdapter() {
                                            @Override
                                            public void mousePressed(MouseEvent e) {
                                                System.out.println("X: " + e.getX() + "Y: " +e.getY());
                                            }
                                        });

        //containerPanel.add(,BorderLayout.NORTH);
        //containerPanel.add(controlsPanel,BorderLayout.SOUTH);
        //containerPanel.add(gameBoardPanel,BorderLayout.WEST);
        //containerPanel.add(playerInfoPanel,BorderLayout.EAST);
        //containerPanel.setBackground(new Color(0x6BFF13));
        //santoriniFrame.add(containerPanel);

        //JLabel label = new JLabel("Welcome to Santorini board game!");
        //santoriniFrame.getContentPane().add(label);

        //santoriniFrame.pack();
        //santoriniFrame.setSize(500,500);
        //santoriniFrame.setVisible(true);
    }

    public static void main(String[] args) {
        ClientViewGUI();
    }
}
