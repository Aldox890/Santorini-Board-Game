package project;

public class Board {
    Cell board [][];

    public Board(){    //board constructor
        this.board = new Cell[5][5];

        for(int i=0; i<5;i++){  //set up of coordinates of the cells
            for(int j=0; j<5;j++){
                this.board[i][j].setCoordinates(i,j);
            }
        }

    }

    public void moveWorker(Worker worker,int posX,int posY){
        if(posX<0 || posX>4 || posY<0 || posY>4){
            //ECCEZIONE
        }
        if(board[posX][posY].isOccupiedBy()!=null){
            //ECCEZIONE
        }

        if(posX==worker.getCell().getX() && posY==worker.getCell().getY()){
            //ECCEZIONE
        }
        int distanzaX = Math.abs(posX-worker.getCell().getX());
        int distanzaY = Math.abs(posY-worker.getCell().getY());

        if(distanzaX > 1 || distanzaY > 1){
            //ECCEZIONE
        }
        worker.setCell(board[posX][posY]);
        board[posX][posY].setOccupiedBy(worker);
    }

    public void printBoard(){}

    public void createWorker(){}

    public void buildInPos(Worker worker,int posX,int posY){}

    public boolean checkStuck(){ return false; }

    public void removeWorker(Worker worker){}

    }

