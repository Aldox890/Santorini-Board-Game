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


    /*checks if it's possible to move from (x_start, y_start) to (x_dest, y_dest)
     * checks if the requested position is already occupied by another worker
     * moves the worker in the position at coordinates (x,y) or sends back an error message
     * sets the previous occupied cell to "null" (so that the cell is now empty)
     * */
    public boolean move(Player player, int x_start, int y_start, int x_dest, int y_dest){

        if(x_start<0 || x_start>4 || y_start<0 || y_start>4){   //check if current worker position are inside the board
            return false;
        }
        if(x_dest<0 || x_dest>4 || y_dest<0 || y_dest>4){   // can't move outside the board
            return false;
        }
        if(x_start==x_dest && y_start==y_dest){ //can't move into your current position
            return false;
        }
        if(board[x_dest][y_dest].getLevel()>3){ //can't move up on a Dome
            return false;
        }
        if(board[x_dest][y_dest].getLevel()>board[x_start][y_start].getLevel()+1){ //can't move up more than one level
            return false;
        }
        if(Math.abs(x_start-x_dest) > 1 || Math.abs(y_start-y_dest) > 1){ //can't move into a cell which is not adjacent to starting position
            return false;
        }


        Worker worker=board[x_start][y_start].isOccupiedBy();

        if(worker == null){             //check if there is a worker in (x_start,y_start)
            return false;
        }

        if(worker.getOwner()!=player) { //check if the worker considered by coordinates (x_starr, y_start)is owned by the player
            return false;
        }


        if(board[x_dest][y_dest].isOccupiedBy()!=null) { //can't move into an occupied cell with the exception of those players who have Apollo or Minotaur as God.
            if(player.getGod().equals("Apollo")){   //
                //TODO Apollo's move
                Worker temp = board[x_dest][y_dest].isOccupiedBy();
                this.moveWorker(worker, x_dest, y_dest);    //moves my worker into requested position (x_dest, y_dest)
                this.moveWorker(temp, x_start, y_start);    //moves other worker into my previous position (x_start, y_start)
                return true;
            }
            else if(player.getGod().equals("Minotaur")){
                //TODO Minotaur's move

                if(Math.abs(x_start-x_dest) <= 1 && Math.abs(y_start-y_dest) <= 1){
                    Worker temp = board[x_dest][y_dest].isOccupiedBy();
                    int xNew = x_dest - (x_start-x_dest);
                    int yNew = y_dest - (y_start-y_dest);

                    if(board[xNew][yNew].isOccupiedBy()==null){
                        this.moveWorker(worker, x_dest, y_dest);    //moves my worker into requested position (x_dest, y_dest)
                        this.moveWorker(temp, xNew, yNew);    //moves other worker into my previous position (xNew, yNew)
                        return true;
                    }

                    /*
                    if(x_start==x_dest){   //stessa riga
                        if(y_start > y_dest){
                            //TODO Minotaur's move

                        }
                        else if(y_start < y_dest){
                            //TODO Minotaur's move
                        }
                    }
                    else if(y_start==y_dest){  //stessa colonna
                        if(x_start > x_dest){
                            //TODO Minotaur's move
                        }
                        else if(x_start < x_dest){
                            //TODO Minotaur's move
                        }

                    }
                    else if(){ //diagonale

                    } */

                }
                return false;
            }
            return false;
        }










        this.moveWorker(worker,x_dest,y_dest);
        /*if(board[x_dest][y_dest].getLevel()>3){
            ///WIN
        }*/


        //passTurn();  TODO un giocatore dopo aver mosso deve poter costruire prima di passare il turno
        return true;
    }

    /*
     * sets the previous occupied cell to "null" (so that the cell is now empty)
     * sets the worker into the requested cell
     * */
    public boolean moveWorker(Worker worker, int posX, int posY){
        worker.getCell().setOccupiedBy(null);
        worker.setCell(board[posX][posY]);
        board[posX][posY].setOccupiedBy(worker);
        return true; //returns true if the worker got moved into a new position
        //board[worker.getCell().getX()][worker.getCell().getY()].setOccupiedBy(null);
    }

    //controls if is possible to builds in (x1,y1)
    public boolean build(Player player,int level,int x, int y, int x1, int y1){
        Worker worker;

        if(x<0 || x>4 || y<0 || y>4){
            return false;
        }

        worker = board[x][y].isOccupiedBy();

        if(worker.getOwner()!=player){
            return false;
        }

        if(x1<0 || x1>4 || y1<0 || y1>4){
            return false;
        }

        if(board[x1][y1].getLevel()>4){
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

        this.buildInPos(worker,level,x1,y1);
        //passTurn();
        return false;
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
    public void buildInPos(Worker worker,int level, int posX,int posY){
        if(level == 1) {
            board[posX][posY].setLevel(board[posX][posY].getLevel() + 1);
        }
        else{
            board[posX][posY].setLevel(4);
        }
    }

    public boolean checkStuck(){ return false; }

    /*
    * Set the cell previously occupied by a worker to empty
    * */
    public void removeWorker(Worker worker){

    }

    }

