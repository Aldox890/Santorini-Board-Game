package project;

import project.server.Player;

public class Board {
    Cell board [][];
    private boolean canMoveUp;  //flag to check if it's possibile to move into a cell on a higher level.


    public Board(){    //board constructor
        board = new Cell[5][5];
        for(int i=0; i<5;i++){  //set up of coordinates of the cells
            for(int j=0; j<5;j++){
                this.board[i][j] = new Cell(i,j);
            }
        }
        canMoveUp=true;

    }

    public Cell[][] getBoard() {
        return board;
    }


    /*checks if it's possible to move from (x_start, y_start) to (x_dest, y_dest)
     * checks if the requested position is already occupied by another worker
     * moves the worker in the position at coordinates (x,y) or sends back an error message
     * sets the previous occupied cell to "null" (so that the cell is now empty)
     * */
    public int move(Player player, int x_start, int y_start, int x_dest, int y_dest){

        if(x_start<0 || x_start>4 || y_start<0 || y_start>4){   //check if current worker position are inside the board
            return 0;
        }
        if(x_dest<0 || x_dest>4 || y_dest<0 || y_dest>4){   // can't move outside the board
            return 0;
        }
        if(x_start==x_dest && y_start==y_dest){ //can't move into your current position
            return 0;
        }
        if(board[x_dest][y_dest].getLevel()>3){ //can't move up on a Dome
            return 0;
        }
        if(board[x_dest][y_dest].getLevel()>board[x_start][y_start].getLevel()+1){ //can't move up more than one level
            return 0;
        }
        if(Math.abs(x_start-x_dest) > 1 || Math.abs(y_start-y_dest) > 1){ //can't move into a cell which is not adjacent to starting position
            return 0;
        }
        if(board[x_dest][y_dest].getLevel()>board[x_start][y_start].getLevel() && !canMoveUp){ //Athena check
            return 0;
        }

        Worker worker=board[x_start][y_start].isOccupiedBy();

        if(worker == null){             //check if there is a worker in (x_start,y_start)
            return 0;
        }

        if(worker.getOwner()!=player) { //check if the worker considered by coordinates (x_starr, y_start)is owned by the player
            return 0;
        }


        if(board[x_dest][y_dest].isOccupiedBy()!=null) { //can't move into an occupied cell with the exception of those players who have Apollo or Minotaur as God.
            if(player.getGod().equals("Apollo")){   //
                Worker temp = board[x_dest][y_dest].isOccupiedBy();
                this.moveWorker(worker, x_dest, y_dest);    //moves my worker into requested position (x_dest, y_dest)
                this.moveWorker(temp, x_start, y_start);    //moves other worker into my previous position (x_start, y_start)
                return 1;
            }
            else if(player.getGod().equals("Minotaur")){
                if(Math.abs(x_start-x_dest) <= 1 && Math.abs(y_start-y_dest) <= 1){
                    Worker temp = board[x_dest][y_dest].isOccupiedBy();
                    int xNew = x_dest - (x_start-x_dest);
                    int yNew = y_dest - (y_start-y_dest);

                    if(board[xNew][yNew].isOccupiedBy()==null){
                        this.moveWorker(worker, x_dest, y_dest);    //moves my worker into requested position (x_dest, y_dest)
                        this.moveWorker(temp, xNew, yNew);    //moves other worker into my previous position (xNew, yNew)
                        return 1;
                    }
                }
            }
            else if (player.getGod().equals("Athena")){
                if(board[x_dest][y_dest].getLevel()>board[x_start][y_start].getLevel()){ //Athena check
                    canMoveUp = false;
                }
            }
        }

        this.moveWorker(worker,x_dest,y_dest);

        if(board[x_dest][y_dest].getLevel()==3 || ((player.getGod().equals("Pan"))&&
                ((board[x_start][y_start].getLevel()-board[x_dest][y_dest].getLevel())>=2)) ){  //check if worker has moved on top of a level 3

            return -1;  //win
        }

        return 1;
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
    /*
    * adds a level on the cell at position (xBuild, yBuild) based on worker current position (xPos, yPos)
    * */
    public boolean build(Player player,int level, int xPos, int yPos, int xBuild, int yBuild){
        Worker worker;

        if(xPos<0 || xPos>4 || yPos<0 || yPos>4){   // Ha senso controllare che il worker è fuori dalla board se l'ho già fatto nella move?
            return false;
        }

        worker = board[xPos][yPos].isOccupiedBy();

        if(worker.getOwner()!=player){
            return false;
        }

        if(xBuild<0 || xBuild>4 || yBuild<0 || yBuild>4){   //can't build outside the board
            return false;
        }

        if(board[xBuild][yBuild].getLevel()>4){ //can't add a building upon a dome
            return false;
        }

        if(board[xBuild][yBuild].isOccupiedBy()!=null){ // can't build in (xBuild, yBuild) if is occupied
            return false;
        }

        if(xBuild == xPos && yBuild==yPos){ //can't build in the same position where the worker is currently at.
            return false;
        }

        int distanceX = Math.abs(xPos-xBuild);
        int distanceY = Math.abs(yPos-yBuild);

        if(distanceX > 1 || distanceY > 1) {    //can't build in a spot not adjacent to worker position
            return false;
        }


        this.buildInPos(worker,level,xBuild,yBuild);
        return true;
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

    public void resetCanMoveUp(){
        canMoveUp = true;
    }

    //build in (posX,posY)
    public void buildInPos(Worker worker,int level,int posX,int posY){
        if(level < 4) {
            board[posX][posY].setLevel(board[posX][posY].getLevel() + level);
        }
        else if (level == 4) {
            board[posX][posY].setLevel(level);
        }
    }

    public boolean checkStuck(){ return false; }

    /*
    * Set the cell previously occupied by a worker to empty
    * */
    public void removeWorker(Worker worker){

    }

    }

