package project.client;
import project.Client;
import project.Player;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App // !! QUESTO E' SOLO UN TEST !!
{
    static Player l1 [] = new Player[2]; // lista che utilizzeremo per memorizzare i player
    public static void main( String[] args ) throws IOException {
        Client client = new Client("127.0.0.1",26175);
        client.startClient();

    }
}
