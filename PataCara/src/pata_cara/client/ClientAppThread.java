package pata_cara.client;

import java.io.BufferedReader;

/**
 * Title:        pata_cara
 * Description:  chat en java
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Rémy Giraud
 * @version 1.0
 */



public class ClientAppThread extends Thread
{
     BufferedReader  serverStream = null;
     PataCara clientApp = null;

    public ClientAppThread (BufferedReader in, PataCara app)
    {
        this.serverStream = in;
        this.clientApp    = app;
    }

    public void run ()
    {
        String Data = null;
        try
        {
            while ((Data = serverStream.readLine ()) != null)
            {
                clientApp.ReceptionTexte (Data);
                //System.out.println ("Reception ds fct run de ClientAppThread par le client du texte : "
                //                     + Data);
            }
        }
        catch (java.io.IOException e)
        {
            System.err.println ("L'exception suivante est survenue ds fct run de ClientAppThread : " + e);
        }
        //Le client est déconnecté
        System.out.println ("Client déconnecté");
        clientApp.envoyerMessageInfo("Vous avez été déconnecté(e) du serveur  ", Dialogue.ListStyle [3], java.awt.Color.red.darker(), "Connexion reset");
    }

}
