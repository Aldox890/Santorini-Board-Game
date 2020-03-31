package project;

public class Cell {
    private int x; //X coordinates
    private int y; //Y coordinates
    private int level;
    private Worker occupiedBy;

    public Cell(){ //project.Cell constructor
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

    public void setCoordinates(int X, int Y){   //sets cell's coordinates
        this.x=X;
        this.y=Y;
    }

}
