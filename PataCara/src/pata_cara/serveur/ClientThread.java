package pata_cara.serveur;

/**
 * Title:        pata_cara
 * Description:  chat en java
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Rémy Giraud
 * @version 1.0
 */

import java.io.InputStreamReader;

import pata_cara.client.Membre;
import pata_cara.client.PataCara;
import java.net.Socket;
import java.util.StringTokenizer;

class ClientThread extends Thread
{
    Socket clientSocket  = null;
    Server server       = null;
    private String Pseu        = null;

    public ClientThread (Socket socket, Server server)
    {
        this.setClientSocket (socket);
        this.setServer (server);
    } /* ClientThread () */

    private Socket getClientSocket ()
    {
        return this.clientSocket;
    } /* getClientSocket () */

    private Server getServer ()
    {
        return this.server;
    } /* getServer () */

    public void run ()
    {
        java.lang.String data = null;
        try
        {
        //ouvrir Stream pour l'envoie de données au client
            java.io.DataOutputStream out = new
                         java.io.DataOutputStream (this.getClientSocket ()
                                                       .getOutputStream ());
        //ouvrir Stream pour la reception de données du client
            java.io.BufferedReader in = new
                         java.io.BufferedReader (new InputStreamReader (
                                                      this.getClientSocket ()
                                                           .getInputStream ()));
       //lecture du nouveau pseudo  arrivant + couleur (sexe) et du tooltip en 1 fois
           String Arrivant = in.readLine ();
       //0 pseudo
       //1 couleur sexe
       //2 tooltip
           StringTokenizer st = new StringTokenizer (Arrivant, Membre.DELIM);

           /*String*/ Pseu = st.nextToken ();
           String Coul     = st.nextToken ();
           String toolTip  = st.nextToken();
       //on envoie au client que le pseudo existe deja
           boolean rep;
           out.writeBytes ((rep = this.getServer ().estPseudoConnecter (Pseu))?
                           PataCara.FALSE + "\n" :
                            "true"         + "\n");
           out.flush ();
           if (rep)
               return;

       //envoie de tous les connectés au nouvel arriavnt
       //d'abord le nombre puis tous les pseudos
           int clien = getServer ().getClient ();
           out.writeBytes ("" + clien + "\n");
           out.flush ();
           for (int i = 0; i < clien; ++i)
           {
               out.writeBytes (getServer ().tabPseudo  [i] + Membre.DELIM +
                               getServer ().tabPseuCou [i] + Membre.DELIM +
                               getServer ().tabToolTip [i] + "\n");
               out.flush ();
           }

       //informer le serveur d'une nouvelle connection client
           this.getServer ().addConnection (this.getClientSocket (),
                                            Pseu, out, Coul, toolTip);

           while ( (data = in.readLine () ) != null)
           {
               System.out.println ("Message du client dans start ClientThread :"
                                  + data);
              //out.writeBytes (data+ "\n");
              //out.flush ();
              this.getServer ().traitementText (data);
           }
           //Arreter la connection du client
           this.getClientSocket ().close ();
           //Informer le serveur d'un deconnection
           this.getServer ().removeConnection (this.getClientSocket (), Pseu);
        }
        catch (java.io.IOException e)
        {
            //si l'exception est la suivante : Connection reset by peer: JVM_recv in socket input stream read
            //on informe au server d'une deconnection du client!
            if (e.getMessage ().equals("Connection reset by peer: JVM_recv in socket input stream read"))
            {
                System.err.println("Arret brutal du client : " + Pseu);
                try
                {
                    //Arreter la connection du client
                    this.getClientSocket ().close ();
                    //Informer le serveur d'un deconnection
                    this.getServer ().removeConnection (this.getClientSocket (), Pseu);
                }
                catch (java.io.IOException Exc)
                {
                    System.err.println("L'exception suivante est intervenue dans run ClientThread, arret brutal d'un client (JVM_recv in...) : " + Exc);
                }
                return;
            }
            //si l'exception est la suivante : Connection reset by peer: JVM_recv in socket input stream read
            //on informe au server d'une deconnection du client!
            if (e.getMessage ().equals("Connection reset"))
            {
              System.err.println("Arret brutal du client : " + Pseu);
              try
              {
                  //Arreter la connection du client
                  this.getClientSocket ().close ();
                  //Informer le serveur d'un deconnection
                  this.getServer ().removeConnection (this.getClientSocket (), Pseu);
              }
              catch (java.io.IOException Exc)
              {
                  System.err.println("L'exception suivante est intervenue dans run ClientThread, arret brutal d'un client (connection reset : " + Exc);
              }
              return;

            }
            System.err.println ("L'exception suivante est intervenue dans run ClientThread : " + e);
            e.printStackTrace();
            return;
        }
    } /* run () */

    private void setServer (Server tempServer)
    {
        this.server = tempServer;
    } /* setServer () */


    private void setClientSocket (Socket tempSocket)
    {
        this.clientSocket = tempSocket;
    } /* setClientSocket () */
} // Class ClientThread
