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

/**
 * Class dedicated to managing the game board
 */
public class BoardPanel extends JPanel {
    ObjectOutputStream objectOutputStream;
    String graphicsPath = "graphics//";
    GridBagLayout gridbaglayout;
    GridBagConstraints lim;
    GameState gameState;
    CellButton[][] buttonBoard;
    public static final String reset = "\u001B[0m";

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
                ImagePanel img = new ImagePanel(graphicsPath+"buildings//0.png",102,102);
                buttonBoard[r][c] = new CellButton(r,c);
                buttonBoard[r][c].setContentAreaFilled(false);
                buttonBoard[r][c].setOpaque(false);
                buttonBoard[r][c].setBorder(null);
                buttonBoard[r][c].add(img);

                lim.gridx=c;
                lim.gridy=r;
                lim.weightx=1;
                lim.weighty=1;
                lim.fill = GridBagConstraints.BOTH;
                gridbaglayout.setConstraints(buttonBoard[r][c],lim);
                this.add(buttonBoard[r][c]);

                createListener(buttonBoard[r][c]);


            }
        }
    }

    /**
     * Removes the CellButton cells of the board
    */
    public void removeListeners(){
        Component[] compArray = this.getComponents();

        for (Component c : compArray){
            if(c instanceof CellButton) {
                this.remove(c);
            }
        }
    }


    /**
     * Updates the board everytime a move of a worker or a build happens
     * @param mex
     * @throws IOException
     */
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

    /**
     * Creates all the MouseListeners for each CellButton of the game board
     * @param component
     */
    public void createListener(CellButton component){
        BoardPanel bPanel = this;

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
                            if(gameState.getPersonalGod().equals("atlas")){
                                int n = useGodPowerDialogBox(gameState.getPersonalGod());

                                if(n == 0){ //YES
                                    try{
                                        objectOutputStream.writeObject(new ClientMessage(7,null, null, gameState.getxStart(), gameState.getyStart(),gameState.getxDest(),gameState.getyDest(),null));
                                        objectOutputStream.flush();
                                        return;
                                    }catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            }
                            else if(gameState.getPersonalGod().equals("hephaestus")){
                                int n = useGodPowerDialogBox(gameState.getPersonalGod());   //asks if does want to use god power

                                if(n == 0){ //YES
                                    try{
                                        objectOutputStream.writeObject(new ClientMessage(6,null, null, gameState.getxStart(), gameState.getyStart(),gameState.getxDest(),gameState.getyDest(),null));
                                        objectOutputStream.flush();
                                        return;
                                    }catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            }
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

    /**
     * Opens up a pop-up dialog whenever the usage of certain gods' power should be asked to the user. If "Yes", the god power will be used otherwise it wont.
     * @param godName
     * @return
     */
    public int useGodPowerDialogBox(String godName){
        Object[] options = {"YES",
                "NO"};
        int n = JOptionPane.showOptionDialog(this.getParent(),
                "Do you want to use "+ godName+"'s power?",
                "Santorini",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,     //do not use a custom Icon
                options,  //the titles of buttons
                options[0]); //default button title
        return n;
    }
}