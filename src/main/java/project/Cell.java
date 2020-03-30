package project;

public class Cell {
    int x; //X coordinates
    int y; //Y coordinates
    int level;
    Worker occupiedBy;

    public void Cell(){ //project.Cell constructor
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
