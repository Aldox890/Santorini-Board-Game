package project.server.networklayer;

import project.ClientMessage;
import project.Message;
import project.server.GameController;
import project.server.model.Game;
import project.server.networklayer.ClientObserver;
import project.server.networklayer.GameObserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private int port;

    private int playerid;
    private ServerSocket serverSocket;
    public Server(int port){
        this.port = port;
        playerid = 0;
    }

    /**
     * Starting method of the server
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void startServer() throws IOException, ClassNotFoundException {
        ExecutorService executor = Executors.newCachedThreadPool();
        try{
            serverSocket = new ServerSocket(port);
        }
        catch(IOException e){
            System.err.println(e.getMessage());
            return;
        }
        System.out.println("Server socket ready on port: " + port);

        Game game = new Game();
        GameController gameController = new GameController(game);

        Socket socket = serverSocket.accept();
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

        int nPlayers = 0;
        while(nPlayers!= 3 && nPlayers!= 2 ) {
            oos.writeObject(new Message(0, 20, "Create room", ""));
            oos.flush();

            ClientMessage mex = (ClientMessage) ois.readObject();
            nPlayers = Integer.parseInt(mex.getData());

            if (nPlayers!= 3 && nPlayers!= 2 ){
                oos.writeObject(new Message(0, 20, "false", ""));
            }
        }

        game.setNPlayers(nPlayers);
        GameObserver gameObserver = new GameObserver(socket,oos,playerid);
        game.addObserver(gameObserver);
        executor.submit(new ClientObserver(gameController, socket, ois,playerid));
        playerid++;

        while(playerid < nPlayers){ // server waits for 3 players to connect to the game
            try {
                socket = serverSocket.accept();
                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());
                gameObserver = new GameObserver(socket,oos,playerid);
                game.addObserver(gameObserver);
                executor.submit(new ClientObserver(gameController, socket, ois,playerid));
                playerid++;
            }
            catch(IOException e){
                break;
            }
        }
        serverSocket.close();
        //executor.shutdown();
    }
}
