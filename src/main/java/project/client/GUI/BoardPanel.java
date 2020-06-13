package project.client.GUI;

import project.ClientMessage;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class BoardPanel extends JPanel {
    ObjectOutputStream objectOutputStream;
    String graphicsPath = "graphics//";
    GridBagLayout gridbaglayout;
    GridBagConstraints lim;
    GameState gs;
    CellButton[][] buttonBoard;



    public BoardPanel(ObjectOutputStream obj, GameState gameState) {
        this.objectOutputStream=obj;
        this.gs = gameState;
        buttonBoard = new CellButton[5][5];

        this.setSize(510,510);
        this.setOpaque(false);
        gridbaglayout = new GridBagLayout();
        lim = new GridBagConstraints();
        this.setLayout(gridbaglayout);

        for(int r=0;r<=4;r++){
            for(int c=0;c<=4;c++){
                //ImageIcon img1 = new ImageIcon(graphicsPath+"levelNumber\\3.png");
                //ImageIcon img = new ImageIcon(graphicsPath+"levelNumber\\dome45.png");
                BufferedImage combinedImage = null;
                try {
                    final BufferedImage bg = ImageIO.read(new File(graphicsPath+"buildings//0.png"));
                    final BufferedImage dome = ImageIO.read(new File(graphicsPath+"buildings//dome45.png"));
                    combinedImage = new BufferedImage(bg.getWidth(), bg.getHeight(), BufferedImage.TYPE_INT_ARGB );
                    Graphics2D g = combinedImage.createGraphics();
                    g.drawImage(bg,0,0,null);
                    g.drawImage(dome,26,26,null);
                    g.dispose();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                buttonBoard[r][c] = new CellButton(r,c,new ImageIcon(combinedImage));
                //component.setPreferredSize(new Dimension(102,102));
                buttonBoard[r][c].setContentAreaFilled(false);
                buttonBoard[r][c].setOpaque(false);
                buttonBoard[r][c].setBorder(null);

                lim.gridx=c;
                lim.gridy=r;
                lim.weightx=1;
                lim.weighty=1;
                lim.fill = GridBagConstraints.BOTH;
                //lim.insets = new Insets(10, 10, 10, 10); //lim è un GridBagConstraints
                gridbaglayout.setConstraints(buttonBoard[r][c],lim);
                this.add(buttonBoard[r][c]);

                createListener(buttonBoard[r][c]);


            }
        }


    }


    public void createListener(CellButton component){
        BoardPanel bPanel = this;

        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                //super.mouseEntered(e);
                component.setBorder(new LineBorder(Color.BLACK, 1));
            }
        });

        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                component.setBorder(null);
            }
        });

        /*TO FIX*/
        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(gs.isMyTurn()){

                    if(gs.getHasSetWorkers() == 0 || gs.getHasSetWorkers() == 1){
                        try {

                            objectOutputStream.writeObject( new ClientMessage(2,null, null, component.getRow(), component.getColumn(),-1,-1,null));
                            objectOutputStream.flush();
                            gs.setHasSetWorkers(gs.getHasSetWorkers()+1);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                    }

                    System.out.println("R: "+ component.getRow() + " C: "+  component.getColumn());
                }
            }
        });


    }

}
