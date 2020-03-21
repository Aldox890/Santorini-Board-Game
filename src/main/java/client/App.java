package client;

import client.god.Apollo;

/**
 * Hello world!
 *
 */
public class App // !! QUESTO E' SOLO UN TEST !!
{
    static Player l1 [] = new Player [2]; // lista che utilizzeremo per memorizzare i player
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        Apollo p1 = new Apollo(); // creo l'oggetto utilizzando il dio scelto, che eredita dalla classe Player
        l1[0] = p1; // salvo L'oggetto di tipo Apollo in un vettore di Player
        l1[0].test(); // se esiste la funzione test() in apollo, la utilizzo, altrimenti richiamo la rispettiva di Player.
    }
}
