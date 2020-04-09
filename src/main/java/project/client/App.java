package project.client;
import project.server.Player;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App // !! QUESTO E' SOLO UN TEST !!
{
    public static void main( String[] args ) throws IOException {
        Client client = new Client("95.248.176.145",26175); //82.48.25.174     127.0.0.1
        client.startClient();
    }

}
