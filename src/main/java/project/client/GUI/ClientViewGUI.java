package project.client.GUI;

import project.ClientMessage;
import project.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;


public class ClientViewGUI implements Observer {

    Socket socket;
    ObjectOutputStream objectOutputStream;

    public ClientViewGUI(Socket socket) throws IOException {
        this.socket = socket;
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

    }

    public void login() throws IOException {

        JFrame loginFrame = new JFrame("Santorini login");
        JPanel loginPanel = new JPanel();
        JLabel username = new JLabel("Username:");
        JLabel age = new JLabel("Age:");
        JTextField usernameArea = new JTextField(10);
        JTextField ageArea = new JTextField(2);
        JButton loginButton = new JButton("Login");

        loginPanel.setLayout(null);

        loginFrame.setSize(new Dimension(400,140));
        loginPanel.setSize(new Dimension(400,140));

        loginPanel.setBackground(new Color(135, 177, 182));

        username.setBounds(20,20,100,20);
        age.setBounds(20,50,100,20);

        usernameArea.setBounds(100,20,100,20);
        ageArea.setBounds(100,50,100,20);

        loginButton.setBounds(220,20,100,50);

        loginPanel.add(loginButton);
        loginPanel.add(age);
        loginPanel.add(username);
        loginPanel.add(usernameArea);
        loginPanel.add(ageArea);
        loginFrame.add(loginPanel);
        loginFrame.setVisible(true);

        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("Sent");
                String username = usernameArea.getText();
                String age = ageArea.getText();
                try {
                    objectOutputStream.writeObject(new ClientMessage(0,null, null, -1, -1,-1,-1,(username+";"+age) ));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        Message mex = (Message) arg;
        try {
            switch (mex.getTypeOfMessage()) {
                case (20): //con quanti giocatori vuoi giocare
                    objectOutputStream.writeObject(new ClientMessage(20, null, null, -1, -1, -1, -1, "3"));
                    break;

                case (0): // required player registration
                    if (mex.getData().equals("registered")) {
                        System.out.println("Successfully registered!");
                    } else if (mex.getErrorData() == null) {
                        login();

                    } else {
                        System.out.println(mex.getErrorData());
                        //login();
                    }
                    break;

            }
        }
        catch (IOException e){

        }
    }


    //public void ClientViewGUI(){
        /*
        JFrame santoriniFrame = new JFrame("Santorini");
        GameBoard gameBoard = new GameBoard();

        JFrame loginFrame = new JFrame("Santorini login");
        JPanel loginPanel = new JPanel();
        JLabel username = new JLabel("Username:");
        JLabel age = new JLabel("Age:");
        JTextField usernameArea = new JTextField(10);
        JTextField ageArea = new JTextField(2);
        JButton loginButton = new JButton("Login");

        loginPanel.setLayout(null);
        loginFrame.setSize(new Dimension(400,140));
        loginPanel.setSize(new Dimension(400,140));
        loginPanel.setBackground(new Color(135, 177, 182));

        username.setBounds(20,20,100,20);
        age.setBounds(20,50,100,20);

        usernameArea.setBounds(100,20,100,20);
        ageArea.setBounds(100,50,100,20);

        loginButton.setBounds(220,20,100,50);

        loginPanel.add(loginButton);
        loginPanel.add(age);
        loginPanel.add(username);
        loginPanel.add(usernameArea);
        loginPanel.add(ageArea);
        loginFrame.add(loginPanel);
        loginFrame.setVisible(true);

        loginButton.addMouseListener(new MouseAdapter() {
                                            @Override
                                            public void mousePressed(MouseEvent e) {
                                                loginFrame.setVisible(false);
                                                santoriniFrame.setVisible(true);
                                            }
                                        });

        santoriniFrame.getContentPane().add(gameBoard);

        gameBoard.setSize(new Dimension(1280,720));
        santoriniFrame.setSize(1280, 758);
        santoriniFrame.setResizable(false);

        //santoriniFrame.setVisible(true);


        santoriniFrame.addMouseListener(new MouseAdapter() {
                                            @Override
                                            public void mousePressed(MouseEvent e) {
                                                System.out.println("X: " + e.getX() + "Y: " +e.getY());
                                            }
                                        });

         */
}
