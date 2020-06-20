package project.client.GUI;

import project.Cell;
import project.ClientMessage;
import project.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;

import static project.server.model.Color.*;

public class BoardPanel extends JPanel {
    ObjectOutputStream objectOutputStream;
    String graphicsPath = "graphics//";
    GridBagLayout gridbaglayout;
    GridBagConstraints lim;
    GameState gameState;
    CellButton[][] buttonBoard;

    public BoardPanel(ObjectOutputStream obj, GameState gameState) {
        this.objectOutputStream=obj;
        this.gameState = gameState;
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
                /*
                BufferedImage combinedImage = null;
                try {
                    final BufferedImage bg = ImageIO.read(new File(graphicsPath+"buildings//0.png"));
                    combinedImage = new BufferedImage(bg.getWidth(), bg.getHeight(), BufferedImage.TYPE_INT_ARGB );
                    Graphics2D g = combinedImage.createGraphics();
                    g.drawImage(bg,0,0,null);
                    g.dispose();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                 */

                ImagePanel img = new ImagePanel(graphicsPath+"buildings//0.png",102,102);
                buttonBoard[r][c] = new CellButton(r,c);
                //component.setPreferredSize(new Dimension(102,102));
                buttonBoard[r][c].setContentAreaFilled(false);
                buttonBoard[r][c].setOpaque(false);
                buttonBoard[r][c].setBorder(null);
                buttonBoard[r][c].add(img);

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

    public static final String reset = "\u001B[0m";
    public void updateBoard(Message mex) throws IOException {
        Cell[][] board = mex.getBoard();
        for(int i = 0;i<5;i++){
            for(int j = 0;j<5;j++){

                Component[] components = buttonBoard[i][j].getComponents();

                for (Component component : components) {
                    buttonBoard[i][j].remove(component);
                }

                if(board[i][j].isOccupiedBy() != null) {
                    String player = mex.getCell(i,j).isOccupiedBy().getOwner().getName().substring(0,1).toUpperCase();  //iniziale player
                    String color = mex.getCell(i,j).isOccupiedBy().getOwner().getColor().getColor();
                    String lvl = Integer.toString(mex.getCell(i,j).getLevel()); //level
                    System.out.print(color+player+lvl+" "+reset);

                    ImagePanel img = new ImagePanel(graphicsPath + "worker//red_worker.png", 102, 102);

                    if(mex.getCell(i,j).isOccupiedBy().getOwner().getColor() == CYAN) {
                        img = new ImagePanel(graphicsPath + "worker//verdeacqua_worker.png", 102, 102);
                    }
                    else if(mex.getCell(i,j).isOccupiedBy().getOwner().getColor() == YELLOW) {
                        img = new ImagePanel(graphicsPath + "worker//yellow_worker.png", 102, 102);
                    }
                    buttonBoard[i][j].add(img);
                }
                ImagePanel img = new ImagePanel(graphicsPath+"buildings//" + mex.getCell(i,j).getLevel() + ".png",102,102);
                buttonBoard[i][j].add(img);
            }
            System.out.println("");
        }
        System.out.println("turnof: "+ mex.getTurnOf());
        System.out.println("");

        this.validate();
        this.repaint();

        mex.addBoard(null);
        mex = null;
    }


    public void createListener(CellButton component){
        BoardPanel bPanel = this;

        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                //super.mouseEntered(e);
                /*
                component.setOpaque(true);
                component.setBackground(new Color(225, 234, 61, 140));
                 */
                //component.setBorder(new LineBorder(Color.BLACK, 1));
            }
        });

        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                //component.setOpaque(false);
                component.setBorder(null);
            }
        });

        /*TO FIX*/
        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mouseClicked(e);
                if(gameState.isMyTurn()){
                    if(gameState.getHasSetWorkers() == 0 || gameState.getHasSetWorkers() == 1){
                        try {

                            objectOutputStream.writeObject( new ClientMessage(2,null, null, component.getRow(), component.getColumn(),-1,-1,null));
                            objectOutputStream.flush();
                            gameState.setHasSetWorkers(gameState.getHasSetWorkers()+1);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                    }
                    else if(gameState.isMoveFlag()){
                        if(gameState.getxStart() == -1 && gameState.getyStart() == -1){
                            gameState.setxStart(component.getRow());
                            gameState.setyStart(component.getColumn());
                        }
                        else{
                            gameState.setxDest(component.getRow());
                            gameState.setyDest(component.getColumn());
                            try {
                                objectOutputStream.writeObject(new ClientMessage(3,null, null, gameState.getxStart(), gameState.getyStart(),gameState.getxDest(),gameState.getyDest(),null));
                                objectOutputStream.flush();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }

                            gameState.setxStart(-1);
                            gameState.setyStart(-1);
                            gameState.setxDest(-1);
                            gameState.setyDest(-1);

                            gameState.setMoveFlag(false);
                        }
                    }
                    else if(gameState.isBuildFlag()) {
                        if (gameState.getxStart() == -1 && gameState.getyStart() == -1) {
                            gameState.setxStart(component.getRow());
                            gameState.setyStart(component.getColumn());
                        }
                        else {
                            gameState.setxDest(component.getRow());
                            gameState.setyDest(component.getColumn());

                            //add gods checks and alert: line 469 ClientView CLI
                            try {
                                objectOutputStream.writeObject(new ClientMessage(4, null, null, gameState.getxStart(), gameState.getyStart(),gameState.getxDest(),gameState.getyDest(), null));
                                objectOutputStream.flush();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }

                            gameState.setxStart(-1);
                            gameState.setyStart(-1);
                            gameState.setxDest(-1);
                            gameState.setyDest(-1);
                            gameState.setBuildFlag(false);
                        }
                    }
                    System.out.println("R: "+ component.getRow() + " C: "+  component.getColumn());
                }
            }
        });


    }

}
