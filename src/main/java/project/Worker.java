package project;

public class Worker {
    Player player;
    Cell cell;
    public void worker(Player p, Cell c){
        this.player = p;
        this.cell = c;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }
}
