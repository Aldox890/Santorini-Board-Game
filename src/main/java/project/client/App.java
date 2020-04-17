package project.client;

import java.io.IOException;
import java.net.Socket;

/**
 * Hello world!
 *
 */
public class App // !! QUESTO E' SOLO UN TEST !!
{
    public static void main( String[] args ) throws IOException, ClassNotFoundException {
        //Client client = new Client("95.248.176.145",26175);
        //client.startClient();

        Socket socket = new Socket("127.0.0.1",26175); //82.48.25.174     127.0.0.1   95.248.176.145
        System.out.println("Connection established");

        ClientView clientView = new ClientView();
        ServerObserver serverObserver = new ServerObserver(socket);

        serverObserver.addObserver(clientView);
        serverObserver.waitFromServer();
    }

}
