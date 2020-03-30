package project.server;

import java.io.IOException;

public class ServerMain // !! QUESTO E' SOLO UN TEST !!
{
    public static void main( String[] args ) throws IOException {
        Server server = new Server(26175);
        server.startServer();
    }
}
