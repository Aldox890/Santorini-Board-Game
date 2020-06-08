package project.server;

import project.server.networklayer.Server;

import java.io.IOException;

public class ServerMain // !! QUESTO E' SOLO UN TEST !!
{
    /**
     * Main method of the server
     * @param args
     * @throws IOException
     */
    public static void main( String[] args ) throws IOException {
        Server server = new Server(26175);
        try {
            server.startServer();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
