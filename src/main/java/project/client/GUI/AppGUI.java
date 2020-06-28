package project.client.GUI;
import project.ClientMessage;
import project.client.ClientView;
import project.client.ServerObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;


public class AppGUI {
    private static final String graphicsPath = "graphics//"; //"src//main//java//project//client//graphics//";

    public static String openDialog(String message){
        String s = (String)JOptionPane.showInputDialog(
                null,
                "Set address:port",
                 message,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "127.0.0.1:26175");

        //If a string was returned, say so.
        if ((s != null) && (s.length() > 0)) {
            return s;
        }
        return null;
    }

    public static void main( String[] args ) {

            //createFrame();
            String s;
            s = openDialog((args.length == 0 ? "Connect to santorini" : args[0]));

            String[] data = s.split(":");

            try {
            Socket socket = new Socket(data[0],Integer.parseInt(data[1]));
            System.out.println("Connection established");

            ClientViewGUI clientViewGUI = new ClientViewGUI(socket);
            ServerObserver serverObserver = new ServerObserver(socket);
            serverObserver.addObserver(clientViewGUI);


            serverObserver.waitFromServer();
            }catch(SocketException e){
                e.printStackTrace();
                main(new String[]{"socket error"});
            } catch (UnknownHostException e) {
                e.printStackTrace();
                main(new String[]{"unknown host"});
            } catch (IOException e) {
                e.printStackTrace();
                main(new String[]{"IOException"});
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                main(new String[]{"ClassNotFoundException"});
            } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                e.printStackTrace();
                main(new String[]{"Bad input"});
            } catch(Exception e){
                e.printStackTrace();
                main(new String[]{"Unknown error"});
            }
    }
}
