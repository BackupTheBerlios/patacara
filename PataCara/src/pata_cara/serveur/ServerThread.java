package pata_cara.serveur;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;


/**
 * Title:        pata_cara
 * Description:  chat en java
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Rémy Giraud
 * @version 1.0
 */




class ServerThread extends Thread
{
    ServerSocket serverSocket  = null;
    Server server              = null;

    public ServerThread (ServerSocket socket, Server server)
    {
        this.setServerSocket (socket);
        this.setServer (server);
try
{
  System.out.println ("ServerThread constructeur, reuse addresse ? " + serverSocket.getReuseAddress());
  serverSocket.setReuseAddress(true);
}
catch (SocketException e)
{
  e.printStackTrace();
}
    } /* ServerThread () */

    public void run ()
    {
System.out.println ("ServeurThread DEMARRE (" + Thread.currentThread().getName() + ")");
        //Socket pour la conection avec le client
        Socket clientSocket = null;
        try
        {
            //attente d'une connection
            while ( (clientSocket = serverSocket.accept ()) != null)
            {
                ClientThread clientThread
                                    = new ClientThread (clientSocket, server);
                clientThread.start ();
            }
        }
        catch (java.io.IOException e)
        {
            System.err.println ("L'exception suivante est intervenue : " + e);
            e.printStackTrace();
        }
System.out.println ("ServeurThread TERMINE (" + Thread.currentThread().getName() + ")");
    } /* run () */

    private void setServer (Server tempServer)
    {
        this.server = tempServer;
    } /* setServer () */


    private void setServerSocket (ServerSocket tempSocket)
    {
        this.serverSocket = tempSocket;
    } /* setServerSocket () */
} // Class ServerThread
