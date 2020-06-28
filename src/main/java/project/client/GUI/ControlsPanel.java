package project.client.GUI;

import project.ClientMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectOutputStream;

/*
* Class dedicated to managing the controls of the game: "Move", "Build" and "End Turn"
* */
public class ControlsPanel extends ImagePanel {
    private static final String graphicsPath = "graphics//btncontrols//";
    JPanel containerPanel;
    ImagePanel godCardPanel;
    JPanel buttonsPanel;
    JButton moveBtn, buildBtn, endTurnBtn;
    GameState gameState;
    ObjectOutputStream objectOutputStream;

    ControlsPanel(ObjectOutputStream oos, String bgpath,int width, int height, GameState gs){
        super(bgpath,width,height);
        objectOutputStream = oos;
        gameState = gs;
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setBounds(990,0,300,720);
        this.setOpaque(true);

        createContainerPanel();

        this.add(Box.createVerticalStrut(102));
        this.add(containerPanel);
    }

    /*
    * It instantiates the container for the containers of the controls and the god card
    * */
    void createContainerPanel(){
        containerPanel = new JPanel();
        containerPanel.setBounds(990,100,200,538);

        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.PAGE_AXIS));

        containerPanel.setMinimumSize(new Dimension(200, 535));
        containerPanel.setPreferredSize(new Dimension(200, 535));
        containerPanel.setMaximumSize(new Dimension(200, 535));

        containerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        containerPanel.setOpaque(false);

        createButtonPanel();

        containerPanel.add(Box.createVerticalStrut(30));
        containerPanel.add(buttonsPanel);
        buttonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    /*
    * Creates an ImagePanel containing the card of the god, given the name.
    * */
    public void createGodPanel(String godName){
        godCardPanel = new ImagePanel("graphics//gods//cards//"+ godName+".png",200,250);//new ImagePanel(bgpath+"SidePanel.png",200,300);
        godCardPanel.setMinimumSize(new Dimension(200, 250));
        godCardPanel.setPreferredSize(new Dimension(200, 250));
        godCardPanel.setMaximumSize(new Dimension(200, 250));
        godCardPanel.setBackground(Color.BLUE);
        godCardPanel.setLayout(new BoxLayout(godCardPanel, BoxLayout.PAGE_AXIS));
        godCardPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.containerPanel.add(godCardPanel);
    }

    /*
    * Creates the panel containing all the controls and adds to them Listeners
    * */
    void createButtonPanel(){
        buttonsPanel = new JPanel();
        buttonsPanel.setOpaque(false);

        buttonsPanel.setMinimumSize(new Dimension(200, 235));
        buttonsPanel.setPreferredSize(new Dimension(200, 235));
        buttonsPanel.setMaximumSize(new Dimension(200, 235));
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.PAGE_AXIS));

        moveBtn=new JButton();
        buildBtn=new JButton();
        endTurnBtn=new JButton();

        setButton(moveBtn,graphicsPath+"btn_MOVE.png",190,70);
        setButton(buildBtn,graphicsPath+"btn_BUILD.png",190,70);
        setButton(endTurnBtn,graphicsPath+"btn_ENDTURN.png",190,70);

        addMouseEnteredToButtons();
        addMouseExitedToButtons();
        addMouseClickToButtons();

        buttonsPanel.add(moveBtn);
        buttonsPanel.add(buildBtn);
        buttonsPanel.add(endTurnBtn);
    }

    /*
    * Sets the button "b" with the given width, height and image.
    * */
    void setButton(JButton b, String path, int width, int height){
        ImageIcon imgbtn = resizeImageButton(new ImageIcon(path),width,height);
        b.setIcon(imgbtn);
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
    }

    /*
    * Adds to each control button a mouseEntered listener.
    * */
    void addMouseEnteredToButtons(){
        moveBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ImageIcon imgbtn = resizeImageButton(new ImageIcon(graphicsPath+"btn_MOVE_pressed.png"),190,70);
                moveBtn.setIcon(imgbtn);
            }
        });
        buildBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ImageIcon imgbtn = resizeImageButton(new ImageIcon(graphicsPath+"btn_BUILD_pressed.png"),190,70);
                buildBtn.setIcon(imgbtn);
            }
        });
        endTurnBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ImageIcon imgbtn = resizeImageButton(new ImageIcon(graphicsPath+"btn_ENDTURN_pressed.png"),190,70);
                endTurnBtn.setIcon(imgbtn);
            }
        });
    }

    /*
     * Adds to each control button a mouseExited listener.
     * */
    void addMouseExitedToButtons(){
        moveBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                ImageIcon imgbtn = resizeImageButton(new ImageIcon(graphicsPath+"btn_MOVE.png"),190,70);
                moveBtn.setIcon(imgbtn);
            }
        });
        buildBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                ImageIcon imgbtn = resizeImageButton(new ImageIcon(graphicsPath+"btn_BUILD.png"),190,70);
                buildBtn.setIcon(imgbtn);
            }
        });

        endTurnBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                ImageIcon imgbtn = resizeImageButton(new ImageIcon(graphicsPath+"btn_ENDTURN.png"),190,70);
                endTurnBtn.setIcon(imgbtn);
            }
        });
    }

    /*
     * Adds to each control button a mouseEntered listener, to manage what each control does when is pressed
     * */
    void addMouseClickToButtons(){
        moveBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                gameState.setxStart(-1);
                gameState.setyStart(-1);
                gameState.setxDest(-1);
                gameState.setyDest(-1);
                gameState.setMoveFlag(true);
                gameState.setBuildFlag(false);
            }
        });

        buildBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                gameState.setxStart(-1);
                gameState.setyStart(-1);
                gameState.setxDest(-1);
                gameState.setyDest(-1);
                gameState.setBuildFlag(true);
                gameState.setMoveFlag(false);
            }
        });

        endTurnBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                gameState.setxStart(-1);
                gameState.setyStart(-1);
                gameState.setxDest(-1);
                gameState.setyDest(-1);
                gameState.setBuildFlag(false);
                gameState.setMoveFlag(false);

                try {
                    objectOutputStream.writeObject(new ClientMessage(10,null, null, -1, -1,-1,-1,null));
                    objectOutputStream.flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void disableComponents(){
        moveBtn.setEnabled(false);
        buildBtn.setEnabled(false);
        endTurnBtn.setEnabled(false);
    }

    /*
    * Given an ImageIcon and the desirable width and height, return a new ImageIcon with the given dimension.
    * */
    ImageIcon resizeImageButton(ImageIcon img, int width, int height){
        Image i = img.getImage();
        Image newImg = i.getScaledInstance(width,height,Image.SCALE_DEFAULT);
        return new ImageIcon(newImg);
    }

}
