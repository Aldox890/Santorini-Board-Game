package project.client.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ControlsPanel extends ImagePanel {
    private static final String graphicsPath = "graphics//btncontrols//";
    JPanel containerPanel;
    JPanel godCardPanel;
    //JPanel fillerPanel;
    JPanel buttonsPanel;
    JButton moveBtn, buildBtn, endTurnBtn, godButton;

    ControlsPanel(String bgpath,int width, int height){
        super(bgpath,width,height);
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        //this.setAlignmentY(Component.CENTER_ALIGNMENT);
        this.setBounds(990,0,300,720);
        this.setOpaque(true);

        createContainerPanel();

        this.add(Box.createVerticalStrut(102));
        this.add(containerPanel);
    }

    void createContainerPanel(){
        containerPanel = new JPanel();
        containerPanel.setBounds(990,100,200,538);

        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.PAGE_AXIS));

        containerPanel.setMinimumSize(new Dimension(200, 535));
        containerPanel.setPreferredSize(new Dimension(200, 535));
        containerPanel.setMaximumSize(new Dimension(200, 535));

        containerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        containerPanel.setOpaque(false);

        createGodPanel();
        createButtonPanel();


        //containerPanel.add(Box.createVerticalStrut(50));
        containerPanel.add(godCardPanel);
        containerPanel.add(Box.createVerticalStrut(30));
        //containerPanel.add(Box.createRigidArea(new Dimension(200,50)));
        containerPanel.add(buttonsPanel);

        godCardPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

    }

    void createGodPanel(){
        godCardPanel = new JPanel();//new ImagePanel(bgpath+"SidePanel.png",200,300);
        godCardPanel.setMinimumSize(new Dimension(200, 250));
        godCardPanel.setPreferredSize(new Dimension(200, 250));
        godCardPanel.setMaximumSize(new Dimension(200, 250));
        godCardPanel.setBackground(Color.BLUE);
        godCardPanel.setLayout(new BoxLayout(godCardPanel, BoxLayout.PAGE_AXIS));
    }


    void createButtonPanel(){

        buttonsPanel = new JPanel();
        buttonsPanel.setOpaque(false);
        //buttonsPanel.setBackground(Color.BLACK);

        buttonsPanel.setMinimumSize(new Dimension(200, 235));
        buttonsPanel.setPreferredSize(new Dimension(200, 235));
        buttonsPanel.setMaximumSize(new Dimension(200, 235));
        //buttonsPanel.setBounds(600,440,200,220);   //buttonsPanel.setBounds(1048,440,200,220);
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.PAGE_AXIS));

        moveBtn=new JButton();
        buildBtn=new JButton();
        endTurnBtn=new JButton();

        setButton(moveBtn,graphicsPath+"btn_MOVE.png",190,70);
        setButton(buildBtn,graphicsPath+"btn_BUILD.png",190,70);
        setButton(endTurnBtn,graphicsPath+"btn_ENDTURN.png",190,70);

        addMouseEnteredToButtons();
        addMouseExitedToButtons();

        buttonsPanel.add(moveBtn);
        buttonsPanel.add(buildBtn);
        buttonsPanel.add(endTurnBtn);

    }

    void setButton(JButton b, String path, int width, int height){
        ImageIcon imgbtn = resizeImageButton(new ImageIcon(path),width,height);
        b.setIcon(imgbtn);
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
    }

    void addMouseEnteredToButtons(){
        moveBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                //super.mouseEntered(e);
                ImageIcon imgbtn = resizeImageButton(new ImageIcon(graphicsPath+"btn_MOVE_pressed.png"),190,70);
                moveBtn.setIcon(imgbtn);
            }
        });
        buildBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                //super.mouseEntered(e);
                ImageIcon imgbtn = resizeImageButton(new ImageIcon(graphicsPath+"btn_BUILD_pressed.png"),190,70);
                buildBtn.setIcon(imgbtn);
            }
        });
        endTurnBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                //super.mouseEntered(e);
                ImageIcon imgbtn = resizeImageButton(new ImageIcon(graphicsPath+"btn_ENDTURN_pressed.png"),190,70);
                endTurnBtn.setIcon(imgbtn);
            }
        });
    }

    void addMouseExitedToButtons(){
        moveBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                //super.mouseExited(e);
                ImageIcon imgbtn = resizeImageButton(new ImageIcon(graphicsPath+"btn_MOVE.png"),190,70);
                moveBtn.setIcon(imgbtn);
            }
        });
        buildBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                //super.mouseExited(e);
                ImageIcon imgbtn = resizeImageButton(new ImageIcon(graphicsPath+"btn_BUILD.png"),190,70);
                buildBtn.setIcon(imgbtn);
            }
        });

        endTurnBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                //super.mouseExited(e);
                ImageIcon imgbtn = resizeImageButton(new ImageIcon(graphicsPath+"btn_ENDTURN.png"),190,70);
                endTurnBtn.setIcon(imgbtn);
            }
        });
    }

    ImageIcon resizeImageButton(ImageIcon img, int width, int height){
        Image i = img.getImage();
        Image newImg = i.getScaledInstance(width,height,Image.SCALE_DEFAULT);
        return new ImageIcon(newImg);
    }

}
