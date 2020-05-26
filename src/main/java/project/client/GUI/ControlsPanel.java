package project.client.GUI;

import javax.swing.*;
import java.awt.*;

public class ControlsPanel extends ImagePanel {
    private static final String graphicsPath = "graphics//";
    JPanel godCardPanel;
    JPanel buttonsPanel;
    JButton moveBtn, buildBtn, endTurnBtn, saveBtn;

    ControlsPanel(String bgpath,int width, int height){
        super(bgpath,width,height);

        ImageIcon imgbtn = new ImageIcon(graphicsPath+"btncontrols//btn_MOVE.png");
        moveBtn=new JButton("",imgbtn);
        moveBtn.setBounds(1051,440,197,55);

        imgbtn = new ImageIcon(graphicsPath+"btncontrols//btn_BUILD.png");
        buildBtn=new JButton("",imgbtn);
        buildBtn.setBounds(1051,496,197,55);

        imgbtn = new ImageIcon(graphicsPath+"btncontrols//btn_ENDTURN.png");
        endTurnBtn=new JButton("",imgbtn);
        endTurnBtn.setBounds(1051,552,197,55);

        imgbtn = new ImageIcon(graphicsPath+"btncontrols//btn_SAVE.png");
        saveBtn=new JButton("",imgbtn);
        saveBtn.setBounds(1051,608,197,55);


        godCardPanel = new JPanel();
        godCardPanel.setLayout(null);
        godCardPanel.setBounds(1048,131,200,300);

        buttonsPanel = new JPanel();
        buttonsPanel.setOpaque(false);
        buttonsPanel.setBounds(1048,440,200,220);
        buttonsPanel.setLayout(null);
        buttonsPanel.add(moveBtn);
        buttonsPanel.add(buildBtn);
        buttonsPanel.add(endTurnBtn);
        buttonsPanel.add(saveBtn);

        this.setLayout(null);
        this.setBounds(990,0,300,720);
        this.setOpaque(true);
        this.add(godCardPanel);


        JPanel fillPanel = new JPanel();
        fillPanel.setPreferredSize(new Dimension(300,100));
        fillPanel.setOpaque(false);
        buttonsPanel.add(fillPanel);
    }

}
