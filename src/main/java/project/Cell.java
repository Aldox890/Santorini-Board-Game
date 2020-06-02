package project;

import java.io.Serializable;

public class Cell implements Serializable {
    private int x; //X coordinates
    private int y; //Y coordinates
    private int level;
    private Worker occupiedBy;

    /**
     * Constructor of a cell , set level = 0 and occupiedBy = null
     * @param x x coordinate of the cell
     * @param y y coordinate of the cell
     */
    public Cell(int x,int y){
        this.x = x;
        this.y = y;
        level = 0;
        occupiedBy = null;
    }

    public int getLevel() {
        return level;
    }

    /**
     * Used when a worker build in the cell
     * @param level {0,1,2,3,4}
     */
    public void setLevel(int level) {
        this.level = level;
    }

    public Worker isOccupiedBy() {
        return occupiedBy;
    }   //returns who is in this cell

    /**
     * Used when a worker moves in the cell
     * @param occupiedBy worker in the cell
     */
    public void setOccupiedBy(Worker occupiedBy) {
        this.occupiedBy = occupiedBy;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


}
