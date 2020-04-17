package project;

import project.server.Player;

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

    public Cell[][] getBoard() {
        return board;
    }

    public void moveWorker(Worker worker, int posX, int posY){
        worker.getCell().setOccupiedBy(null);
        worker.setCell(board[posX][posY]);
        board[posX][posY].setOccupiedBy(worker);
    }

    public void printBoard(){}

    /*
    * returns true if there is no worker already in the selected cell
    * returns false if there is already a worker inside the cell*/
    public boolean createWorker(Player p, int x, int y){
        if(x >= 0 && x <= 4 && y >= 0 && y <= 4) {
            if (this.board[x][y].isOccupiedBy() == null) {   //if there is no worker inside, puts the new worker in the cell at x and y
                Worker w = new Worker(p, this.board[x][y]);
                p.addWorker(w);
                this.board[x][y].setOccupiedBy(w);
                return true;
            }
        }
        return false;
    }

    public void buildInPos(Worker worker,int posX,int posY){
        board[posX][posY].setLevel(board[posX][posY].getLevel()+1);
    }

    public boolean checkStuck(){ return false; }

    public void removeWorker(Worker worker){}

    }

