package project;

public class Board {
    Cell board [][];

    public Board(){    //board constructor
        board = new Cell[5][5];
        for(int i=0; i<5;i++){  //set up of coordinates of the cells
            for(int j=0; j<5;j++){
                this.board[i][j] = new Cell(i,j);
            }
        }

    }

    public void moveWorker(Worker worker,int posX,int posY){
        worker.setCell(board[posX][posY]);
        board[posX][posY].setOccupiedBy(worker);
    }

    public void printBoard(){}

    public void createWorker(){}

    public void buildInPos(Worker worker,int posX,int posY){}

    public boolean checkStuck(){ return false; }

    public void removeWorker(Worker worker){}

    }

