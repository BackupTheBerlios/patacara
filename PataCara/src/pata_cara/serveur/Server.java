package pata_cara.serveur;

/**
 * Title:        pata_cara
 * Description:  chat en java
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Rémy Giraud
 * @version 1.0
 */


import pata_cara.client.Membre;
import pata_cara.client.PataCara;
import pata_cara.client.Main;
import pata_cara.client.Dialogue;
import java.net.ServerSocket;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;


public class Server
{
    private ServerSocket serverSocket = null;
    private ServerThread serverThread = null;
    private int client = 0;
    //TODO changer les portées
    String tabPseudo  [] = new String [Main.MAX_MEMBRES];
    String tabPseuCou [] = new String [Main.MAX_MEMBRES];
    String tabToolTip [] = new String [Main.MAX_MEMBRES];
    DataOutputStream tabOut [] = new DataOutputStream [Main.MAX_MEMBRES];

    private static final int NBLIGNESALON = 10;
    private String [] ligneSalon = new String [NBLIGNESALON];
    private int nbLigne = 0;
    
    private static final String FICHIER_LOG_ERREUR = "log" + File.separator + "log_erreur.txt";


    public Server ()
    {
        super ();
        start ();
    } /* Constructeur Server */
    
    
    /**
     * Stop le serveur
     *
     */
    public void stopServer ()
    {
      //Envoi d'un message aux clients
      envoiInfoMessage("Le serveur PataCara s'est arrêté");
      
      //On ferme la socket
      try
      {
        serverSocket.close();
        serverThread.interrupt();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    
    /**
     * Envoi dans l'onglet info un message a tous les clients.
     * @param message le message a envoyer.
     */
    public void envoiInfoMessage (final String message)
    {
      //Envoi d'un message au client
      for (int i = 0; i < client; ++i)
      {
        try
        {
          tabOut [i].writeBytes(PataCara.INFO + Membre.DELIM +
              message + "\n");
          tabOut [i].flush();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }
      
    }

    public void addConnection (Socket socket,
                               String Pseudo,
                               DataOutputStream out,
                               String Couleur,
                               String toolTip)
    {
        //une nouvelle connection est faite

        //mise a jours des tableaux
        ajouterPseudo (Pseudo, out, Couleur, toolTip);

        //penser au envoi d'un nouveau connecté pour les autres et lui meme
        //0 pseudo
        //1 couleur pseudo et tooltip (le meme)
        //2 tooltip
        String strAenvoyer = PataCara.ARRIVANT + Membre.DELIM +
                             Pseudo             + Membre.DELIM +
                             Couleur            + Membre.DELIM +
                             toolTip;
        try
        {
            for (int i = 0; i < client; ++i)
            {
                tabOut [i].writeBytes (strAenvoyer + "\n");
                tabOut [i].flush ();
            }

            //envoie des ligne dans le salon du nouvelle arrivant
            for (int i = 0; i < nbLigne; ++i)
            {
                out.writeBytes (ligneSalon [i] + "\n");
                out.flush ();
            }
         }
         catch (java.io.IOException e)
         {
             System.err.println ("L'exception suivante est intervenue dans la fonction addConnection : " +e);
             e.printStackTrace();
         }
         System.out.println ("Nombre de connexion : " + client);
    } /* addConnection () */

   public static void main (String [] arg)
   {
       new Server ();
   } /* main () */

    public void removeConnection (Socket socket, String pseudo)
    {
        effacerPseudo (pseudo);
        // detruire dans les tableaux la personne
        try
        {
            for (int i = 0; i < client; ++i)
            {
                tabOut [i].writeBytes (PataCara.DEPART + Membre.DELIM +
                                       pseudo + "\n");
                tabOut [i].flush ();
            }
        }
        catch (java.io.IOException e)
        {
            System.err.println ("L'exception suivante est intervenue dans la fonction removeConnection : " +e);
            e.printStackTrace();
        }

        //une connection est arrété
        //System.out.println ("Destruction de connexion");
        System.out.println ("Nombre de connexion : " + client);

    } /* removeConnection () */

    private void setServerSocket (ServerSocket tempSocket)
    {
        //Noter Socket pour le serveur
        this.serverSocket = tempSocket;
    } /* setServerSocket () */

    private void setServerThread (ServerThread tempThread)
    {
        //Noter Thread du serveur
        this.serverThread = tempThread;
    } /* setServerThread () */

    public void start ()
    {
        //La partie suivante tente de localiser le port 5555
        //Si la tentative échoue aucun Tread serveur n'est lancé
        try
        {
            ServerSocket tempSocket = new ServerSocket (5555);
            this.serverSocket = tempSocket; //attention chgt
        }
        catch (java.io.IOException e)
        {
            System.err.println ("L'exception suivante est intervenue dans la fonction start :" + e);
            e.printStackTrace();
            return;
        }
        //Apres creation  réussi du socket, un Tread est lancé
        //Pour traitement des requetes entrantes
        ServerThread tempThread = new ServerThread (serverSocket, this);
        this.serverThread = tempThread; //attention chgt
        tempThread.start ();
        System.out.println ("Le serveur a été démarré avec succès");
    } /* start () */

   public DataOutputStream getReceveur (String pseudo)
   {
       //fonction qui renvoie le bon flux de sortie
       return tabOut [chercheRang (pseudo)];
   } /* GetReceveur () */


   /**
    * Cherche la position du pseudo Pseudo dans le tableau de pseudo.
    * @param pseudo le pseudo qu'on recherche dans le tableau.
    * @return l'indice (position ou rang) du pseudo dans le tableau,
    * -1 si pas trouvé.
    */
   public int chercheRang (String pseudo)
   {
       boolean trouve = false;
       int i = 0;
       for (; i < client; ++i)
       {
           if (tabPseudo [i].equals (pseudo))
           {
               trouve = true;
               break;
           }
       }
       if (! trouve)
       {   System.out.println ("L'indice du pseudo " + pseudo +
           " n'as pas ete trouve dans la fonction ChercheRang");
           i = -1;
       }
       return i;
   } /* ChercheRang () */

   synchronized void  ajouterPseudo (String nom,
       								 DataOutputStream out,
       								 String couleur,
       								 String toolTip)
   {
       int pos = rechercherIndice (nom);

       if (client == 0 || client == pos)
       {
           tabPseudo  [client] = nom;
           tabOut     [client] = out;
           tabPseuCou [client] = couleur;
           tabToolTip [client] = toolTip;
       }
       else
       {
           String temp1 = tabPseudo [pos];
           String temp2 = null;
           tabPseudo [pos] = nom;
//pareil pour TabOut
            DataOutputStream t1 = tabOut [pos];
            DataOutputStream t2 = null;
            tabOut [pos] = out;

//pareil pour TabPseuCou
            String cou2       = null;
            String cou1       = tabPseuCou [pos];
            tabPseuCou [pos]  = couleur;

//pareil pour toolTip
            String tool2      = null;
            String tool1      = tabToolTip [pos];
            tabToolTip [pos]  = toolTip;


           for (int i = pos+1; i < client; ++i)
           {
               temp2          = tabPseudo [i];
               tabPseudo [i]  = temp1;
               temp1          = temp2;
//pareil pour TabOut
               t2             = tabOut [i];
               tabOut [i]     = t1;
               t1             = t2;
//pareil pour TabPseuCou
               cou2           = tabPseuCou [i];
               tabPseuCou [i] = cou1;
               cou1           = cou2;
//pareil pour TabToolTip
               tool2          = tabToolTip [i];
               tabToolTip [i] = tool1;
               tool1          = tool2;
            }
            tabPseudo  [client] = temp1;
            tabOut     [client] = t1;
            tabPseuCou [client] = cou1;
            tabToolTip [client] = tool1;
      }
      ++client;
  } /* AjouterPseudo () */

  private int rechercherIndice (String nom)
  {
    int i;

    for (i = 0; i < client; ++i)
    {
         if ( nom.compareTo(tabPseudo [i]) < 0)
              break;
    }
     return i;
  } /* RechercheIndice () */

   synchronized void effacerPseudo (String pseudo)
   {
       int rg = chercheRang (pseudo);
       for (; rg < client - 1; ++rg)
       {
          tabPseudo  [rg] = tabPseudo  [rg +1];
          tabOut     [rg] = tabOut     [rg +1];
          tabPseuCou [rg] = tabPseuCou [rg +1];
          tabToolTip [rg] = tabToolTip [rg +1];

       }
       --client;

    } /* EffacerPseudo () */

    public int getClient ()
    {
        return client;
    }

    public void traitementText (String message)
    {
        StringTokenizer st = new StringTokenizer (message, Membre.DELIM);
        if (!st.hasMoreTokens ())
        {
            //System.out.println ("Erreur lors de la reception du string dans la fonction1 : TraitementText");
             return;
         }
        // il y a bien au moins un mot que l'on stock
        String mot = st.nextToken ();
        // on regarde si c'est pour le salon
        if (mot.equals (PataCara.SALON) && st.countTokens () > 5)
        {
            sauvegardeSalon (message);
            try
            {
                for (int i = 0; i < client; ++i)
                {
                    tabOut [i].writeBytes (message + "\n");
                    tabOut [i].flush ();
                }
            }
            catch (java.io.IOException e)
            {
                System.err.println ("L'exception suivante est intervenue dans la fonction2 TraitementText : "
                                     + e);
                 e.printStackTrace();
                return;

            }
        }
        if (mot.equals (PataCara.DIALOGUE) && st.countTokens () > 6)
        {
            try
            {
                String recept = st.nextToken ();
                int rang = chercheRang (recept);
                if (-1 == rang)
                {
                    //Le recepteur n'est plus connecté
                    //il faut prevenir le recepteur
                    String emeteur = st.nextToken ();
                           emeteur = st.nextToken ();
                           emeteur = st.nextToken ();
                    int ind = chercheRang (emeteur);
                    if (-1 == ind) // l'emeteur du message n'a pas été trouvé
                    {
                      System.err.println("On ne peut pas prevenir '" + emeteur +
                                         "' que '" + recept + " est déconnecté\n");
                      return;
                    }
                    //L'emeteur est bien là.
                    tabOut [ind].writeBytes (PataCara.DIALOGUE     + Membre.DELIM +
                                             recept                 + Membre.DELIM +
                                             Dialogue.Couleur   [1] + Membre.DELIM +
                                             Dialogue.ListStyle [3] + Membre.DELIM +
                                             recept                 + Membre.DELIM +
                                             Dialogue.Couleur [1]   + Membre.DELIM +
                                             "deconecté(e)"         + Membre.DELIM +
                                             "est deconnecté(e)"    + "\n");
                     tabOut [ind].flush ();
                }
                else
                {
                    tabOut [rang].writeBytes (message + "\n");
                    tabOut [rang].flush ();
                }
            }
            catch (java.io.IOException e)
            {
                System.err.println ("L'exception suivante est intervenue dans la fonction3 TraitementText : "
                                     + e);
                 e.printStackTrace();
                return;

            }
        }

        if (mot.equals (PataCara.IGNORER) && st.countTokens () >= 2)
        {
            //O IGNORER
            //1 Pseudo ignorer
            //2 Pseudo de celui qui l'ignore

            //String Pseudo = st.nextToken ();
            int rang;
            if (-1 != (rang = chercheRang (st.nextToken ())))
            {
                //L'ignorer est bien présent
                try
                {
                    tabOut [rang].writeBytes (PataCara.INFO + Membre.DELIM +
                                              "vous avez été ignoré(e) par " +
                                              st.nextToken () + "\n");
                    tabOut [rang].flush ();

                }
                catch (java.io.IOException e)
                {
                    System.err.println ("L'exception suivante est intervenue dans la fonction TraitementText (info) : "
                                         + e);
                     e.printStackTrace();
                   return;

                }

            }
        }
    }/* TraitementText () */


    //fonction qui sauvegarde les lignes du salon
    public void sauvegardeSalon (String ligne)
    {
        if (nbLigne < NBLIGNESALON)
            ligneSalon [nbLigne++] = ligne;
        else
        {
            //la premiere ligne doit partir
            for (int i = 0; i < NBLIGNESALON -1; ++i)
            {
                ligneSalon [i] = ligneSalon [i+1];
            }
            ligneSalon [NBLIGNESALON -1] = ligne;
        }
   }/* SauvegardeSalon () */

   public boolean estPseudoConnecter (String pseudo)
   {
       for (int i = 0; i < client; ++i)
       {
           if (tabPseudo [i].equals (pseudo))
               return true;
       }
       return false;
    } /* EstPseudoConnecter () */
   
   
   
   /**
    * Fonction qui sert a loger une exception dans un fichier
    * @param nomFichier nom du fichier avec path
    * @param message le message a mettre avant l'erreur
    * @param exc l'exception dont on veut mettre la stacktrace dans le fichier, peut etre null.
    */
   private static void log (String nomFichier, String message, Throwable exc)
   {
     try
    {
      PrintStream stream = new PrintStream (new BufferedOutputStream (new FileOutputStream (nomFichier, true)));
      stream.println(new Date ().toString () + message);
      if (null != exc)
        exc.printStackTrace(stream);
      stream.flush();
      stream.close();
      
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
   }
   
   /**
    * Fonction qui sert a loger les erreur du serveur PataCara.
    * Les erreurs sont stockées dans le répertoire log.
    * @param message
    * @param exc
    */
   public void logErreur (String message, Throwable exc)
   {
     log (FICHIER_LOG_ERREUR, message, exc);
   }


} // Classe Serveur
