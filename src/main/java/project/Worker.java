package project;

import project.server.model.Player;

import java.io.Serializable;

public class Worker implements Serializable {
    private Player owner;
    private Cell cell;
    public Worker(Player p, Cell c){
        this.owner = p;
        this.cell = c;
    }

    /**
     * This method says which player controls a worker
     * @return the player
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * This method save the player that controls a worker
     * @param owner the player that controls a worker
     */
    public void setOwner(Player owner) {
        this.owner = owner;
    }

    /**
     * This method says where is located the worker
     * @return the cell where is the worker
     */
    public Cell getCell() {
        return cell;
    }

    /**
     * This method save the cell where is located the worker
     * @param cell the cell where the worker will be placed
     */
    public void setCell(Cell cell) {
        this.cell = cell;
    }
}
