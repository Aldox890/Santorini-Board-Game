package project;

import project.server.Player;

public class RulesManager {
    private Worker lastMoved;
    public Board b;

    //x,y coordinate del worker da muovere, x1,y1 coordinate in cui muoverlo
    public void move(Player player, int x, int y, int x1, int y1){
        Worker worker;

        if(x<0 || x>4 || y<0 || y>4){
            //ECCEZIONE
        }

        worker=b.board[x][y].isOccupiedBy();

        if(worker.getPlayer()!=player) {
            //ECCEZIONE
        }

        if(x1<0 || x1>4 || y1<0 || y1>4){
            //ECCEZIONE
        }

        if(b.board[x1][y1].isOccupiedBy()!=null){
            //ECCEZIONE
        }

        if(x==x1 && y==y1){
            //ECCEZIONE
        }

        if(b.board[x][y].getLevel()>b.board[x1][y1].getLevel()+1){
            //ECCEZIONE
        }

        if(b.board[x][y].getLevel()>3){
            //ECCEZIONE
        }


        int distanzaX = Math.abs(x-x1);
        int distanzaY = Math.abs(y-y1);

        if(distanzaX > 1 || distanzaY > 1){
            //ECCEZIONE
        }

        b.moveWorker(worker,x1,y1);
    }
}
