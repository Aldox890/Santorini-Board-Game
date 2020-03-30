package client;

public class Board {
    Cell board[][];

    public Board(){
        board = new Cell[5][5];
    }

    public void moveWorker(Worker worker,int posX,int posY){}

    public void printBoard(){}

    public void createWorker(){}

    public void buildInPos(Worker worker,int posX,int posY){}

    public boolean checkStuck(){ return false; }

    public void removeWorker(Worker worker){}

}
