package project.client.GUI;

import project.Cell;
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
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;


public class ClientViewGUI implements Observer {

    Socket socket;
    ObjectOutputStream objectOutputStream;
    private static final String graphicsPath = "graphics//"; //"src//main//java//project//client//graphics//";
    JFrame loginFrame;
    JFrame santoriniFrame;
    LoginFrame login_frame;
    BoardPanel board_panel;
    PlayersPanel players_panel;
    ControlsPanel controls_panel;
    ArrayList<String> players;
    GodsPanel godsPanel;
    ChooseGodPanel chooseGodPanel;
    AlertPanel alertPanel;

    ArrayList<String> availableGods;


    GameState gameState;



    public ClientViewGUI(Socket socket) throws IOException {
        this.socket = socket;
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        players = new ArrayList<String>();
        availableGods = new ArrayList<>();
        gameState = new GameState();
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

        /*loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                uusername = username.getText();
                System.out.println("username: ");
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
                 */
    }

    public void createGameFrame() {
        //loginFrame.setVisible(false);
        login_frame.setVisible(false);
        alertPanel = new AlertPanel();
        santoriniFrame = new JFrame("Santorini");
        ImagePanel imagePanel = new ImagePanel(1280,720);
        board_panel = new BoardPanel(objectOutputStream,gameState);
        players_panel = new PlayersPanel();
        controls_panel = new ControlsPanel(objectOutputStream,graphicsPath+"DecoratedPanel.png",300,720,gameState);
        board_panel.setBounds(380,105,525,525);
        santoriniFrame.setLayout(null);




        imagePanel.setBounds(0,0,1280,720);

        santoriniFrame.getContentPane().add(controls_panel);
        santoriniFrame.getContentPane().add(players_panel);
        santoriniFrame.getContentPane().add(board_panel);
        santoriniFrame.getContentPane().add(imagePanel);
        santoriniFrame.getContentPane().add(alertPanel,0);


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
            String[] serverResponse = mex.getData().split(";");
            for(int i=0;i<=serverResponse.length-1;i++){
                players.add(serverResponse[i]);
            }

            players_panel.addPlayers(mex.getData().split(";"));
            players_panel.setVisible(true);
    }

    public void choseAllowedGods() throws IOException {
        ArrayList<String> listOfGods = new ArrayList<>();
        if(players.get(players.size()-1).equals(login_frame.getUsername())){ //last player in list (eldest) choses the gods
            System.out.println("You are the most godlike player");

            godsPanel = new GodsPanel(objectOutputStream,players.size());
            santoriniFrame.add(godsPanel,0);
            godsPanel.setVisible(true);

            santoriniFrame.validate();
            santoriniFrame.repaint();


        }
    }

    public void addAllowedGods(Message mex){
        String[] serverGodList = mex.getData().split(";");

        System.out.print("LISTA DEI SCELTI: ");
        for(int i=0;i<=serverGodList.length-1;i++){
            availableGods.add(serverGodList[i]);
            System.out.print(serverGodList[i]+" ");
        }
        System.out.println();
    }

    public void choseGod(Message mex) throws IOException {
        if(mex.getTurnOf().equals(login_frame.getUsername()) && !availableGods.isEmpty()){  //tocca a me

            System.out.print("Seleziona il Dio: ");
            chooseGodPanel = new ChooseGodPanel(objectOutputStream, availableGods);
            santoriniFrame.add(chooseGodPanel,0);
            chooseGodPanel.setVisible(true);

            santoriniFrame.validate();
            santoriniFrame.repaint();

            //objectOutputStream.writeObject(new ClientMessage(1,god, null, -1, -1,-1,-1,null));
            //objectOutputStream.flush();
        }
    }

    public void removeAllowedGod(Message mex){
        if(!mex.getData().equals("false")){
            String[] selectedGod = mex.getData().split(";");
            System.out.println(selectedGod[0] + " has selected " + selectedGod[1]);
            players_panel.addGodThumbnail(selectedGod[0], selectedGod[1]);
            availableGods.remove(selectedGod[1]);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        Message mex = (Message) arg;
        String[] parsedMex = mex.getData().split(";");
        try {
            if(alertPanel!=null) alertPanel.setText(mex.getErrorData());
            System.out.println("list arrived: " + mex.getData());
            if(login_frame != null && mex.getTurnOf() != null && login_frame.getUsername() != null && mex.getTurnOf().equals(login_frame.getUsername())){
                gameState.setMyTurn(true);
                if(santoriniFrame != null){
                    santoriniFrame.setEnabled(true);
                    /*board_panel.setEnabled(true);
                    players_panel.setEnabled(true);
                    controls_panel.setEnabled(true);*/
                }

            }
            else{
                gameState.setMyTurn(false);
                if(santoriniFrame != null){
                    santoriniFrame.setEnabled(false);
                    /*board_panel.setEnabled(false);
                    players_panel.setEnabled(false);
                    controls_panel.setEnabled(false);*/
                }
            }

            if(players_panel != null){
                players_panel.setTurn(mex.getTurnOf());
            }

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
                    choseAllowedGods();
                    break;
                case (1): // recived chosen gods list
                    addAllowedGods(mex);
                    choseGod(mex);
                    break;

                case (2): // recived god chosen by a player
                    removeAllowedGod(mex);
                    if(parsedMex.length==2 && parsedMex[0].equals(login_frame.getUsername())){
                        controls_panel.createGodPanel(parsedMex[1]);
                        santoriniFrame.validate();
                        santoriniFrame.repaint();
                    }
                    if(availableGods.isEmpty()){    //when the last player has chosen the personal god, the next turn is of the first player.
                        gameState.setHasSetWorkers(0);  //0:means that the player hasn't set worker yet and should insert the workers
                        break;
                    }
                    if (mex.getData().equals("false")) {
                        System.out.println("Bad input");
                        System.out.println(mex.getErrorData());
                    }
                    choseGod(mex);
                    break;

                case(4): //recived any player worker positions
                    if(!mex.boardIsEmpty()){
                        board_panel.updateBoard(mex);
                    }
                    if(mex.getTurnOf().equals(login_frame.getUsername()) && ((gameState.getHasSetWorkers()>=0 && gameState.getHasSetWorkers()<2) || mex.getData().equals("false"))){
                        if (mex.getData().equals("false")){
                            System.out.println(mex.getErrorData());
                            gameState.setHasSetWorkers(gameState.getHasSetWorkers()-1);
                        }
                    }
                    //else{turnMenu(mex);}
                    break;

                case(5): //if someone has moved and it's me, i build
                case(6):
                    if(!mex.boardIsEmpty()){board_panel.updateBoard(mex);}
                    if (mex.getData().equals("false")) {
                        System.out.println("Bad input");
                        System.out.println(mex.getErrorData());
                    }
                    //turnMenu(mex);
                    //checkTurnPhase(mex);
                    break;

            }
        }
        catch (IOException e){

        }


    }


}
