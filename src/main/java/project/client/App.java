package project.client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App // !! QUESTO E' SOLO UN TEST !!
{
    public static void main( String[] args ) throws IOException, ClassNotFoundException {
        //Client client = new Client("95.248.176.145",26175);
        //client.startClient();
        try {
            Scanner stdin = new Scanner(System.in);
            String address = "";
            System.out.println("Insert IP address of server: ");
            address = stdin.nextLine();

            Socket socket = new Socket(address, 26175); //82.48.25.174     127.0.0.1   95.248.176.145   87.8.115.142
            System.out.println("   _____           _   _  _______  ____   _____   _____  _   _  _____\n" +
                    "  / ____|   /\\    | \\ | ||__   __|/ __ \\ |  __ \\ |_   _|| \\ | ||_   _|\n" +
                    " | (___    /  \\   |  \\| |   | |  | |  | || |__) |  | |  |  \\| |  | |\n" +
                    "  \\___ \\  / /\\ \\  | . ` |   | |  | |  | ||  _  /   | |  | . ` |  | |\n" +
                    "  ____) |/ ____ \\ | |\\  |   | |  | |__| || | \\ \\  _| |_ | |\\  | _| |_\n" +
                    " |_____//_/    \\_\\|_| \\_|   |_|   \\____/ |_|  \\_\\|_____||_| \\_||_____|");
            System.out.println("Connection established");

            ClientView clientView = new ClientView(socket);
            ServerObserver serverObserver = new ServerObserver(socket);

            serverObserver.addObserver(clientView);
            serverObserver.waitFromServer();
        }
        catch (IOException ex){
            System.out.println("Connection has been refused ");
        }
    }

}
