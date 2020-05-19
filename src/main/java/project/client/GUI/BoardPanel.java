package project.client.GUI;

import javax.swing.*;
import java.awt.*;
import java.io.ObjectOutputStream;

public class BoardPanel extends JPanel {
    ObjectOutputStream objectOutputStream;
    String graphicsPath = "src\\main\\java\\project\\client\\graphics\\";
    GridBagLayout gridbaglayout;
    GridBagConstraints lim;

    public BoardPanel(/*ObjectOutputStream obj*/) {
        //this.objectOutputStream=obj;


        this.setSize(510,510);
        this.setOpaque(false);
        gridbaglayout = new GridBagLayout();
        lim = new GridBagConstraints();
        this.setLayout(gridbaglayout);

        for(int c=0;c<=4;c++){
            for(int r=0;r<=4;r++){
                JButton component = new JButton("B"+r+","+c);
                //component.setPreferredSize(new Dimension(102,102));
                component.setContentAreaFilled(false);
                component.setOpaque(false);
                component.setBorder(null);
                lim.gridx=r;
                lim.gridy=c;
                lim.weightx=1;
                lim.weighty=1;
                lim.fill = GridBagConstraints.BOTH;
                //lim.insets = new Insets(10, 10, 10, 10); //lim è un GridBagConstraints
                gridbaglayout.setConstraints(component,lim);
                this.add(component);
            }
        }


    }

}
