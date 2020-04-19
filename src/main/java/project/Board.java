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

    //controls if is possible to move from (x,y) in (x1,y1)
    public boolean move(Player player, int x, int y, int x1, int y1,int socketId){
        Worker worker;

        if(!inBoard(x,y)){
            return false;
        }

        worker=board[x][y].isOccupiedBy();

        if(worker == null ){
            return false;
        }

        if(worker.getOwner()!=player) {
            return false;
        }

        if(!inBoard(x1,y1)){
            return false;
        }

        if(board[x1][y1].isOccupiedBy()!=null){
            return false;
        }

        if(tooHighToMove(x,y,x1,y1)){
            return false;
        }

        if(thereIsADome(x1,y1)){
            return false;
        }


        int distanceX = Math.abs(x-x1);
        int distanceY = Math.abs(y-y1);

        if(distanceX > 1 || distanceY > 1){
            return false;
        }

        this.moveWorker(worker,x1,y1);
        if(board[x1][y1].getLevel()==3){
            ///WIN
        }
        //passTurn();  TODO un giocatore dopo aver mosso deve poter costruire prima di passare il turno
        return true;
    }

    //controls if is possible to builds in (x1,y1)
    public boolean build(Player player,int x, int y, int x1, int y1,int socketId){
        Worker worker;

        if(!inBoard(x,y)){
            return false;
        }

        worker = board[x][y].isOccupiedBy();

        if(worker==null){
            return false;
        }

        if(worker.getOwner()!=player){
            return false;
        }

        if(!inBoard(x1,y1)){
            return false;
        }

        if(thereIsADome(x1,y1)){
            return false;
        }

        if(board[x1][y1].isOccupiedBy()!=null){
            return false;
        }

        int distanceX = Math.abs(x-x1);
        int distanceY = Math.abs(y-y1);

        if(distanceX > 1 || distanceY > 1) {
            return false;
        }

        this.buildInPos(worker,x1,y1);
        //passTurn();
        return true;
    }

    //move the worker in (posX,posY)
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

    //build in (posX,posY)
    public void buildInPos(Worker worker,int posX,int posY){
        board[posX][posY].setLevel(board[posX][posY].getLevel()+1);
    }

    public boolean checkStuck(){ return false; }

    public void removeWorker(Worker worker){}

    //This method checks if a cell is inside the board
    public boolean inBoard(int x,int y){
        if(x<0 || x>4 || y<0 || y>4)
            return false;
        else
            return true;
    }

    public boolean tooHighToMove(int x,int y,int x1,int y1){
        if(board[x1][y1].getLevel()>board[x][y].getLevel()+1)
            return true;
        else return false;
    }

    public boolean thereIsADome(int x,int y){
        if(board[x][y].getLevel()==4)
            return true;
        else
            return false;
    }

    }

