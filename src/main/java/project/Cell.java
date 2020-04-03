package project;

public class Cell {
    private int x; //X coordinates
    private int y; //Y coordinates
    private int level;
    private Worker occupiedBy;

    public Cell(int x,int y){ //project.Cell constructor
        this.x = x;
        this.y = y;
        level = 0;
        occupiedBy = null;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Worker isOccupiedBy() {
        return occupiedBy;
    }   //returns who is in this cell

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
