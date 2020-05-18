package project.client.GUI;
import project.client.ClientView;
import project.client.ServerObserver;

import java.io.IOException;
import java.net.Socket;


public class AppGUI {
        public static void main( String[] args ) throws IOException, ClassNotFoundException {

            Socket socket = new Socket("localhost",26175); //82.48.25.174     127.0.0.1   95.248.176.145   87.8.115.142
            System.out.println("Connection established");

            ClientViewGUI clientViewGUI = new ClientViewGUI(socket);
            ServerObserver serverObserver = new ServerObserver(socket);
            serverObserver.addObserver(clientViewGUI);

            serverObserver.waitFromServer();


    }
}
