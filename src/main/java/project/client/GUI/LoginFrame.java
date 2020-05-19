package project.client.GUI;

import project.ClientMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class LoginFrame extends JFrame{
    Socket socket;
    ObjectOutputStream objectOutputStream;
    String graphicsPath = "src\\main\\java\\project\\client\\graphics\\";
    JFrame loginFrame;

    LoginFrame( ObjectOutputStream obj) throws IOException{
        //this.socket=s;
        this.objectOutputStream = obj;
        ImageIcon logo = new ImageIcon(graphicsPath+"santorini-logo.png");
        ImageIcon loginBtnImg = new ImageIcon(graphicsPath+"button-play-normal.png");
        //loginFrame = new JFrame("Santorini login");
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

        //loginFrame.setSize(new Dimension(600,300));
        this.setSize(new Dimension(600,300));

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
        this.add(loginPanel);
        this.setResizable(false);
        //this.setVisible(true);
        /*loginFrame.add(loginPanel);
        loginFrame.setResizable(false);
        loginFrame.setVisible(true);*/

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

    public void setInvisible(){
        this.setVisible(false);
    }
}
