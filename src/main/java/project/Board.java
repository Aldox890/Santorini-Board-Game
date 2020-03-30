package project;

public class Board {
    Cell board [][];

    public void Board(){    //board constructor
        this.board = new Cell[5][5];

        for(int i=0; i<5;i++){  //set up of coordinates of the cells
            for(int j=0; j<5;j++){
                this.board[i][j].setCoordinates(i,j);
            }
        }

    }
}
