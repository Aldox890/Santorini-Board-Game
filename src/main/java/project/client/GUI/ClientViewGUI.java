package project.client.GUI;

import project.ClientMessage;
import project.Message;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

    public void createGameFrame() {
        login_frame.setVisible(false);
        alertPanel = new AlertPanel();
        santoriniFrame = new JFrame("Santorini");
        ImagePanel imagePanel = new ImagePanel(1280,720);
        board_panel = new BoardPanel(objectOutputStream,gameState);
        players_panel = new PlayersPanel();
        controls_panel = new ControlsPanel(objectOutputStream,graphicsPath+"DecoratedPanel.png",300,720,gameState);
        board_panel.setBounds(380,105,525,525);
        santoriniFrame.setLayout(null);

        santoriniFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);


        santoriniFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int choice = JOptionPane.showConfirmDialog(santoriniFrame,
                                "Are you sure you want to close this window?", "Close Window?",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE);

                if(choice == 0){ //yes
                    try {
                        socket.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    System.exit(0);
                }else{
                    santoriniFrame.setVisible(true);
                }
            }
        });


        imagePanel.setBounds(0,0,1280,720);

        santoriniFrame.getContentPane().add(controls_panel);
        santoriniFrame.getContentPane().add(players_panel);
        santoriniFrame.getContentPane().add(board_panel);
        santoriniFrame.getContentPane().add(imagePanel);
        santoriniFrame.getContentPane().add(alertPanel,0);

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
        }
    }

    public void removeAllowedGod(Message mex){
        if(!mex.getData().equals("false")){
            String[] selectedGod = mex.getData().split(";");
            System.out.println(selectedGod[0] + " has selected " + selectedGod[1]);
            players_panel.addGodThumbnail(selectedGod[0], selectedGod[1]);
            availableGods.remove(selectedGod[1]);

            if(selectedGod[0].equals(gameState.getPlayerName())){
                gameState.setPersonalGod(selectedGod[1]);
            }
        }
    }

    public int loadGame(){
        Object[] options = {"YES",
                "NO"};
        int n = JOptionPane.showOptionDialog(santoriniFrame,
                "Do you want to load your previous game ?",
                "Santorini",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,     //do not use a custom Icon
                options,  //the titles of buttons
                options[0]); //default button title
        return n;
    }


    @Override
    public void update(Observable o, Object arg) {
        Message mex = (Message) arg;
        String[] parsedMex = mex.getData().split(";");
        try {
            if(alertPanel!=null) {
                alertPanel.setText(mex.getErrorData());
            }
            System.out.println("list arrived: " + mex.getData());
            if(login_frame != null && gameState.getPlayerName()==null){
                gameState.setPlayerName(login_frame.getUsername());
            }
            if(login_frame != null && mex.getTurnOf() != null && login_frame.getUsername() != null && mex.getTurnOf().equals(login_frame.getUsername())){
                gameState.setMyTurn(true);
                if(santoriniFrame != null){
                    board_panel.setEnabled(true);
                    players_panel.setEnabled(true);
                    controls_panel.setEnabled(true);
                }
            }
            else{
                gameState.setMyTurn(false);
                if(santoriniFrame != null){
                    board_panel.setEnabled(false);
                    players_panel.setEnabled(false);
                    controls_panel.setEnabled(false);
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

                case(25): //crash during god selection
                    System.out.println(mex.getData());
                    System.exit(0);
                    break;

                case(30):   //WIN
                    if(!mex.boardIsEmpty()){board_panel.updateBoard(mex);}
                    WinPanel winPanel = new WinPanel(mex.getTurnOf());
                    board_panel.removeListeners();
                    santoriniFrame.add(winPanel,0);
                    santoriniFrame.validate();
                    santoriniFrame.repaint();
                    controls_panel.disableComponents();
                    System.out.println(mex.getTurnOf()+" has won the game!");
                    break;

                case(40): //Player stuck
                    players.remove(mex.getTurnOf()); //remove player which has both workers stucked
                    santoriniFrame.validate();
                    santoriniFrame.repaint();
                    System.out.println(mex.getTurnOf()+" is stuck and his workers has been removed from the board");
                    break;

                case(60): //Load game
                    if(loadGame() == 0){
                        objectOutputStream.writeObject(new ClientMessage(30, null, null, -1, -1, -1, -1, "true"));
                        objectOutputStream.flush();
                    }
                    else{
                        objectOutputStream.writeObject(new ClientMessage(30, null, null, -1, -1, -1, -1, "false"));
                        objectOutputStream.flush();
                    }
                    break;

                case(65): //A game is loaded
                    System.out.println("You start your old game");
                    System.out.println(mex.getTurnOf()+" is the first player");
                    if(!mex.boardIsEmpty()){board_panel.updateBoard(mex);}
                    break;
                case(70):
                    if(mex.getTurnOf().equals(login_frame.getUsername())){
                        alertPanel.setText(mex.getData());
                        alertPanel.setVisible(true);
                        santoriniFrame.validate();
                        santoriniFrame.repaint();
                        System.out.println(mex.getData());
                    }
                    break;
                case(420):  //print your previous god
                    parsedMex=mex.getData().split(";");
                    if(parsedMex[0].equals(login_frame.getUsername())){
                        gameState.setPersonalGod(parsedMex[1]);
                        controls_panel.createGodPanel(parsedMex[1]);
                        santoriniFrame.validate();
                        santoriniFrame.repaint();
                    }
                    players_panel.addSinglePlayer(parsedMex[0],parsedMex[2]);
                    players_panel.setTurn(mex.getTurnOf());
                    players_panel.addGodThumbnail(parsedMex[0], parsedMex[1]);
                    break;


                case (0): // required player registration
                    if (mex.getData().equals("registered")) {
                        System.out.println("Successfully registered!");
                        createGameFrame();
                    } else if (mex.getErrorData() == null) {
                        loginWithFrame();//login();

                    } else {
                        System.out.println(mex.getErrorData());
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
                    break;

                case(5): //if someone has moved and it's me, i build
                case(6):
                    if(!mex.boardIsEmpty()){board_panel.updateBoard(mex);}
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
