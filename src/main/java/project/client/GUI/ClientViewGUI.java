package project.client.GUI;

import project.ClientMessage;
import project.Message;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;


public class ClientViewGUI implements Observer {

    Socket socket;
    ObjectOutputStream objectOutputStream;
    String graphicsPath = "src\\main\\java\\project\\client\\graphics\\";
    JFrame loginFrame;
    JFrame santoriniFrame;
    LoginFrame login_frame;
    BoardPanel board_panel;
    PlayersPanel players_panel;

    public ClientViewGUI(Socket socket) throws IOException {
        this.socket = socket;
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
    }
    public void loginWithFrame(){
        try {
            login_frame=new LoginFrame(this.objectOutputStream);
            login_frame.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void login() throws IOException {

        ImageIcon logo = new ImageIcon(graphicsPath+"santorini-logo.png");
        ImageIcon loginBtnImg = new ImageIcon(graphicsPath+"button-play-normal.png");
        loginFrame = new JFrame("Santorini login");
        //JPanel loginPanel = new JPanel();
        ImagePanel loginPanel = new ImagePanel(graphicsPath+"bg_modeselect.png",600,300);


        Image i = logo.getImage();
        Image newImg = i.getScaledInstance(200,70,Image.SCALE_DEFAULT);
        logo = new ImageIcon(newImg);

        i = loginBtnImg.getImage();
        newImg = i.getScaledInstance(90,90,Image.SCALE_DEFAULT);
        loginBtnImg = new ImageIcon(newImg);



        JLabel logoLabel = new JLabel(logo);
        JLabel username = new JLabel("Username:");
        JLabel age = new JLabel("Age:");
        JLabel errorMexLogin = new JLabel("ERROR LABEL");
        JTextField usernameArea = new JTextField(10);
        JTextField ageArea = new JTextField(2);
        JButton loginButton = new JButton("Login", loginBtnImg);

        loginPanel.setLayout(null);

        loginFrame.setSize(new Dimension(600,300));
        //loginPanel.setSize(new Dimension(600,300));
        //loginPanel.setBackground(new Color(135, 177, 182));

        logoLabel.setBounds(200,5,200,70);
        username.setBounds(20,105,100,20);
        age.setBounds(20,135,100,20);

        usernameArea.setBounds(100,105,150,20);
        ageArea.setBounds(100,135,30,20);
        usernameArea.setOpaque(false);
        usernameArea.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));   //show just bottom border of JTextField
        ageArea.setOpaque(false);
        ageArea.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));

        loginButton.setBounds(450,150,90,90);
        loginButton.setBorder(null);
        loginButton.setOpaque(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setBorderPainted(false);

        errorMexLogin.setBounds(150,200,300,20);
        errorMexLogin.setForeground(Color.RED);
        errorMexLogin.setFont(errorMexLogin.getFont().deriveFont(14f)); //increase font of error label
        errorMexLogin.setVisible(false);



        loginPanel.add(loginButton);
        loginPanel.add(age);
        loginPanel.add(username);
        loginPanel.add(logoLabel);
        loginPanel.add(usernameArea);
        loginPanel.add(ageArea);
        loginPanel.add(errorMexLogin);
        loginFrame.add(loginPanel);
        loginFrame.setResizable(false);
        loginFrame.setVisible(true);

        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                String username = usernameArea.getText();
                String age = ageArea.getText();
                if( username.replace(" ", "").length()>=3 && age.replace(" ", "").length()>=1){
                    System.out.println("Sent");
                    try {
                        objectOutputStream.writeObject(new ClientMessage(0,null, null, -1, -1,-1,-1,(username+";"+age) ));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }else if(username.equals("") || age.equals("")){
                    //errorMexLogin.setEnabled(true);
                    errorMexLogin.setText("Error: username and age field are empty!");
                    errorMexLogin.setVisible(true);
                }else if(username.length()<3 || age.length()>3){
                    //errorMexLogin.setEnabled(true);
                    errorMexLogin.setText("Error: wrong format input in username and/or age field!");
                    errorMexLogin.setVisible(true);
                }
            }
        });
    }

    public void createGameFrame() {
        //loginFrame.setVisible(false);
        login_frame.setVisible(false);
        santoriniFrame = new JFrame("Santorini");
        ImagePanel imagePanel = new ImagePanel(1280,720);
        board_panel = new BoardPanel();
        players_panel = new PlayersPanel();
        board_panel.setBounds(380,105,525,525);
        santoriniFrame.setLayout(null);



        imagePanel.setBounds(0,0,1280,720);
        santoriniFrame.getContentPane().add(players_panel);
        santoriniFrame.getContentPane().add(board_panel);
        santoriniFrame.getContentPane().add(imagePanel);


        //gameBoard.setSize(new Dimension(1280, 720));
        santoriniFrame.setSize(1280, 758);
        santoriniFrame.setResizable(false);

        santoriniFrame.setVisible(true);

        santoriniFrame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("X: " + e.getX() + "Y: " + e.getY());
            }
        });
    }

    public int startingDialogBox(){
        Object[] options = {"2",
                "3"};
        int n = JOptionPane.showOptionDialog(loginFrame,
                "How many players would you play with?",
                "Santorini",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,     //do not use a custom Icon
                options,  //the titles of buttons
                options[0]); //default button title
        return n;
    }

    public void addPlayersList(Message mex){
            players_panel.addPlayers(mex.getData().split(";"));
            players_panel.setVisible(true);
    }

    @Override
    public void update(Observable o, Object arg) {
        Message mex = (Message) arg;
        try {
            System.out.println("list arrived: " + mex.getData());
            switch (mex.getTypeOfMessage()) {
                case (20): //con quanti giocatori vuoi giocare
                    int n = startingDialogBox();
                    objectOutputStream.writeObject(new ClientMessage(20, null, null, -1, -1, -1, -1, Integer.toString((n+2))));
                    break;

                case (0): // required player registration
                    if (mex.getData().equals("registered")) {
                        System.out.println("Successfully registered!");
                        createGameFrame();
                    } else if (mex.getErrorData() == null) {
                        loginWithFrame();//login();

                    } else {
                        System.out.println(mex.getErrorData());
                        //login();
                    }
                    break;
                case(3):

                    if (!mex.getData().equals("false")) {
                        System.out.println("createFrame");
                        addPlayersList(mex);
                        santoriniFrame.validate();
                        santoriniFrame.repaint();
                    }
                    if (mex.getData().equals("false")) {
                        System.out.println("Bad input");
                        System.out.println(mex.getErrorData());
                    }
                    break;
            }
        }
        catch (IOException e){

        }
    }
}
