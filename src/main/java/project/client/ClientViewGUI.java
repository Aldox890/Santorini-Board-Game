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
        JFrame santoriniFrame = new JFrame("Santorini");
        GameBoard gameBoard = new GameBoard();

        santoriniFrame.getContentPane().add(gameBoard);

        gameBoard.setSize(new Dimension(1024,615));
        santoriniFrame.setSize(1024, 615);

        santoriniFrame.setVisible(true);


        santoriniFrame.addMouseListener(new MouseAdapter() {
                                            @Override
                                            public void mousePressed(MouseEvent e) {
                                                System.out.println("X: " + e.getX() + "Y: " +e.getY());
                                            }
                                        });
    }

    public static void main(String[] args) {
        ClientViewGUI();
    }
}
