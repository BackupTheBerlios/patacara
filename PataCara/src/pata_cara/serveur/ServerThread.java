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
      if (!serverSocket.getReuseAddress()) //La socket ne peut pas etre réutilisée, il faut changer cette valeur pour pouvoir lancer plusieurs fois le serveur.
        serverSocket.setReuseAddress (true);
    }
    catch (SocketException e)
    {
      e.printStackTrace ();
      Server.logErreur("ServerThread constructeur socket exception : ", e);
    }
  } /* ServerThread () */

    public void run ()
    {
		Server.logInfo("ServeurThread DEMARRE (" + Thread.currentThread().getName() + ")");
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
            Server.logErreur("Run ServeurThread : exception", e);
        }
		Server.logInfo("ServeurThread TERMINE (" + Thread.currentThread().getName() + ")");
		
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
