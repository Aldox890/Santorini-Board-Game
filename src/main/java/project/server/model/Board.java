package project.server.model;

import project.Cell;
import project.Worker;

import java.io.Serializable;
import java.util.ArrayList;

public class Board implements Serializable {
    private static final long serialVersionUID = 65296812;
    public Cell board [][];
    private boolean canMoveUp;  //flag to check if it's possibile to move into a cell on a higher level.
    private Worker currentWorker;
    private Cell oldCell;   //used to saved where worker comes from
    private Cell oldBuild;
    private int numberOfMoves;  //counts how many times worker has moved.
    private int numberOfBuild; //counts how many times worker has moved.
    private boolean prometheusFlag;

    /**
     * Board constructor : initialize cells and set starting flags values
     */
    public Board(){
        board = new Cell[5][5];
        for(int i=0; i<5;i++){  //set up of coordinates of the cells
            for(int j=0; j<5;j++){
                this.board[i][j] = new Cell(i,j);
            }
        }
        canMoveUp=true;
        currentWorker = null;
        oldCell = null;
        numberOfMoves = 0;
        numberOfBuild = 0;
        prometheusFlag = false;
    }

    public Cell[][] getBoard() {
        return board;
    }

    /**
     * Method that sets which worker has done the first move for a player
     * @param x x coordinate of the worker
     * @param y y coordinate of the worker
     */
    public void setCurrentWorker(int x,int y){
        currentWorker = board[x][y].isOccupiedBy();
    }

    /**
     * Reset currentWorker to null every time a player ends his turn
     */
    public void resetCurrentWorker(){
        currentWorker = null;
    }

    /**
     * Checks if it's possible to move from (x_start, y_start) to (x_dest, y_dest)
     * Checks if the requested position is already occupied by another worker
     * Moves the worker in the position at coordinates (x,y) or sends back an error message
     * Sets the previous occupied cell to "null" (so that the cell is now empty)
     * @return 0 if a move is not allowed, 1 if a move is allowed, -1 if a move bring a player to win
     * @param player
     * @param x_start
     * @param y_start
     * @param x_dest
     * @param y_dest
     * @return 0 if something went wrong, else 1.
     */
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
        if(tooHighToMove(x_start, y_start, x_dest, y_dest)){ //can't move up more than one level
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

        if(currentWorker == null){
            setCurrentWorker(x_start,y_start);
        }
        else if (currentWorker != worker){
            return 0;
        }

        if(!player.getGod().equals("artemis") && numberOfMoves ==1){   //if the god isn't Arthemis and worker has already moved ->
            return 0;
        }
        if(player.getGod().equals("artemis") && numberOfMoves ==1){
            if(oldCell.getX()== x_dest && (oldCell.getY()== y_dest)){
                return 0;
            }
        }
        if(numberOfMoves >=2){    // BUG: numberOfMoves ==2
            return 0;
        }


        if(board[x_dest][y_dest].isOccupiedBy()!=null) { //can't move into an occupied cell with the exception of those players who have Apollo or Minotaur as God.
            if(player.getGod().equals("apollo")){
                Worker temp = board[x_dest][y_dest].isOccupiedBy();
                this.moveWorker(worker, x_dest, y_dest);    //moves my worker into requested position (x_dest, y_dest)
                temp.setCell(board[x_start][y_start]);
                board[x_start][y_start].setOccupiedBy(temp);    //moves other worker into my previous position (x_start, y_start)
                return 1;
            }
            else if(player.getGod().equals("minotaur")){
                if(Math.abs(x_start-x_dest) <= 1 && Math.abs(y_start-y_dest) <= 1){
                    Worker temp = board[x_dest][y_dest].isOccupiedBy();
                    int xNew = x_dest - (x_start-x_dest);
                    int yNew = y_dest - (y_start-y_dest);

                    if(xNew<0 || xNew>4 || yNew<0 || yNew>4){
                        return 0;
                    }

                    if(board[xNew][yNew].isOccupiedBy()==null && board[xNew][yNew].getLevel()<4){
                        this.moveWorker(temp, xNew, yNew);    //moves other worker into my previous position (xNew, yNew)
                        this.moveWorker(worker, x_dest, y_dest);    //moves my worker into requested position (x_dest, y_dest)
                        return 1;
                    }
                    else {
                        return 0;
                    }
                }
            }
            else return 0;  //can't move into cell
        }

        if (player.getGod().equals("prometheus")){
            if(board[x_dest][y_dest].getLevel()>board[x_start][y_start].getLevel()){ //Check if a worker who has Prometheus' power is moving up
                if(numberOfBuild == 1){ return 0;}
            }
        }

        if (player.getGod().equals("athena")){
            if(board[x_dest][y_dest].getLevel()>board[x_start][y_start].getLevel()){ //Check if a worker who has Athena's power is moving up
                canMoveUp = false;  //Athena's power enabled, the other players can't move up till it's Athena's player turn again.
            }
        }



        this.moveWorker(worker,x_dest,y_dest);  //effective move of the worker

        //win condition
        if((board[x_dest][y_dest].getLevel()==3 && board[x_start][y_start].getLevel()!=3)|| ((player.getGod().equals("pan"))&&
                ((board[x_start][y_start].getLevel()-board[x_dest][y_dest].getLevel())>=2)) ){  //check if worker has moved on top of a level 3

            return -1;  //win
        }

        return 1;   //moved into a new cell correctly
    }

    /**
     * Sets the previous occupied cell to "null" (so that the cell is now empty)
     * Sets the worker into the requested cell.
     * @param worker
     * @param posX
     * @param posY
     * @return true if the worker got moved into a new position, else false
     */
    public boolean moveWorker(Worker worker, int posX, int posY){
        oldCell = new Cell(worker.getCell().getX(), worker.getCell().getY());//previously occupied cell
        worker.getCell().setOccupiedBy(null);
        worker.setCell(board[posX][posY]);
        board[posX][posY].setOccupiedBy(worker);
        this.numberOfMoves++;
        return true;
    }


    /**
     * adds a level on the cell at position (xBuild, yBuild) based on worker current position (xPos, yPos)
     * checks if is possible to builds in (xBuild,yBuild)
     * @param level 1 if standard build, 2 if hephaestus build, 4 if atlas build
     * @return true if build is allowed, false if build isn't allowed
     */
    public boolean build(Player player,int level, int xPos, int yPos, int xBuild, int yBuild){
        Worker worker;

        if(xPos<0 || xPos>4 || yPos<0 || yPos>4){
            return false;
        }

        worker = board[xPos][yPos].isOccupiedBy();

        if(worker == null || worker.getOwner()!=player){
            return false;
        }

        if(currentWorker == null){
            setCurrentWorker(xPos,yPos);
        }
        else if (currentWorker != worker){
            return false;
        }

        if(xBuild<0 || xBuild>4 || yBuild<0 || yBuild>4){   //can't build outside the board
            return false;
        }

        if(board[xBuild][yBuild].getLevel()>=4){ //can't add a building upon a dome
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

        if(level==2 && !player.getGod().equals("hephaestus")){
            return false;
        }

        if(level==2 && player.getGod().equals("hephaestus")){
            if((board[xBuild][yBuild].getLevel()+2)>=4){    //if a worker wants to build 2 consecutive blocks upon the starting cell, and the height is bigger or equal to 4
                return false;
            }
        }


        if(level==4 && !player.getGod().equals("atlas")){
            return false;
        }

        if(numberOfMoves == 0 && !player.getGod().equals("prometheus")){ //can't build if worker hasn't already moved && god isn't Prometheus
            return false;
        }

        if(numberOfBuild >= 2){
            return false;
        }

        if(numberOfBuild == 1 && (!player.getGod().equals("demeter") && !player.getGod().equals("prometheus"))){
            return false;
        }

        if(numberOfBuild == 1){
            if( (player.getGod().equals("demeter")) && oldBuild.getX() == xBuild && oldBuild.getY() == yBuild){
                return false;
            }
            if((player.getGod().equals("prometheus")) && (numberOfMoves == 0 || !prometheusFlag)){
                return false;
            }
        }

        if(numberOfMoves == 0 && player.getGod().equals("prometheus")){
            prometheusFlag = true;
        }

        this.buildInPos(worker,level,xBuild,yBuild);
        return true;
    }

    /**
     * Creates a worker in (x,y) cell
     * @return true if there is no worker already in the selected cell, false if there is already a worker inside the cell
     */
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

    /**
     * canMoveUp flag is resetted to true
     */
    public void resetCanMoveUp(){
        canMoveUp = true;
    }

    /**
     * Add level to actual level, set level to 4 if parameter is 4
     * @param worker
     * @param level 1 if standard build, 2 if hephaestus build, 4 if atlas build
     * @param posX
     * @param posY
     */
    public void buildInPos(Worker worker,int level,int posX,int posY){
        if(level < 4) {
            board[posX][posY].setLevel(board[posX][posY].getLevel() + level);
        }
        else if (level == 4) {
            board[posX][posY].setLevel(level);
        }
        numberOfBuild ++;
        oldBuild = new Cell(posX,posY);
    }

    /**
     * Checks if a worker at a certain position is stucked
     * @param x
     * @param y
     * @return true if the worker is stuck, else false
     */
    public boolean checkStuckWorker(int x, int y){
        int i=0,j=0,n,m;
        n=(x==4)? x : x+1;
        m=(y==4)? y : y+1;
        boolean stuck=true;
        for(i=(x>0)? x-1 : x ; i <= n ; i++){
            for(j=(y>0)? y-1 : y ; j <= m ; j++){
                if(!tooHighToMove(x,y,i,j) && x!=i && y!=j){
                    if(board[i][j].isOccupiedBy()==null) {
                        stuck = false;
                    }
                    else if(board[x][y].isOccupiedBy().getOwner().getGod().equals("apollo")){
                        stuck = false;
                    }
                    else if(board[x][y].isOccupiedBy().getOwner().getGod().equals("minothaur")){
                        int xNew = x - (i-x);
                        int yNew = y - (j-y);
                        if(0<xNew && xNew<4 && 0<yNew && yNew<4){
                            stuck=false;
                        }
                    }
                }
            }
        }
        return stuck;
    }

    /**
     * Set the cell previously occupied by a worker to empty
     * Calls the removeWorker() method from Player class, to remove the worker from it's Workers list.
     * @param worker worker to remove
     */
    public void removeWorker(Worker worker){
        if(worker != null) {
            worker.getCell().setOccupiedBy(null);
            worker.getOwner().removeWorker(worker);
        }
    }

    /**
     * Checks if (x_dest,y_dest) level is lower than (x_start,y_start) level + 1
     * @param x_start
     * @param y_start
     * @param x_dest
     * @param y_dest
     * @return true if (x_dest,y_dest) level is lower than (x_start,y_start) level + 1, else false
     */
    public boolean tooHighToMove(int x_start,int y_start,int x_dest,int y_dest){
        //can't move up more than one level
        return (board[x_dest][y_dest].getLevel() - board[x_start][y_start].getLevel() > 1); //return (board[x_dest][y_dest].getLevel() > board[x_start][y_start].getLevel() + 1);
    }

    /**
     * Resets flags
     * Used when a player ends his turn
     */
    public void resetState(){
        numberOfMoves = 0;
        numberOfBuild = 0;
        oldCell = new Cell(-1,-1);
        oldBuild = new Cell(-1,-1);
        prometheusFlag = false;
    }

    /**
     * set player-workers correspondence if a game is loaded
     * @param playerlist actual list of player in game
     */
    public void setCorrectPlayers(ArrayList<Player> playerlist){
        for(int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                if(board[i][j].isOccupiedBy()!=null){
                    String name = board[i][j].isOccupiedBy().getOwner().getName();
                    for(int x=0;x<playerlist.size();x++){
                        if(name.equals(playerlist.get(x).getName())){
                            playerlist.get(x).addWorker(board[i][j].isOccupiedBy());
                            board[i][j].isOccupiedBy().setOwner(playerlist.get(x));
                        }
                    }
                }
            }
        }
    }

    /**
     * Set numberOfMoves flag to 1
     */
    public void setNumberOfMoves(){
        numberOfMoves=1;
    }

    public int getNumberOfMoves() {
        return numberOfMoves;
    }

    public int getNumberOfBuild() {
        return numberOfBuild;
    }



}

