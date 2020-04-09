package project;

import project.server.Player;

public class Worker {
    private Player owner;
    private Cell cell;
    public Worker(Player p, Cell c){
        this.owner = p;
        this.cell = c;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }
}
