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
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
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

    public void removeConnection (Socket socket, String Pseudo)
    {
        effacerPseudo (Pseudo);
        // detruire dans les tableaux la personne
        try
        {
            for (int i = 0; i < client; ++i)
            {
                tabOut [i].writeBytes (PataCara.DEPART + Membre.DELIM +
                                       Pseudo + "\n");
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

   public DataOutputStream getReceveur (String Pseudo)
   {
       //fonction qui renvoie le bon flux de sortie
       return tabOut [chercheRang (Pseudo)];
   } /* GetReceveur () */


   /**
    * Cherche la position du pseudo Pseudo dans le tableau de pseudo.
    * @param Pseudo le pseudo qu'on recherche dans le tableau.
    * @return l'indice (position ou rang) du pseudo dans le tableau,
    * -1 si pas trouvé.
    */
   public int chercheRang (String Pseudo)
   {
       boolean Trouve = false;
       int i = 0;
       for (; i < client; ++i)
       {
           if (tabPseudo [i].equals (Pseudo))
           {
               Trouve = true;
               break;
           }
       }
       if (! Trouve)
       {   System.out.println ("L'indice du pseudo " + Pseudo +
           " n'as pas ete trouve dans la fonction ChercheRang");
           i = -1;
       }
       return i;
   } /* ChercheRang () */

   synchronized void  ajouterPseudo (String Nom,
       								 DataOutputStream out,
       								 String Couleur,
       								 String toolTip)
   {
       int pos = rechercherIndice (Nom);

       if (client == 0 || client == pos)
       {
           tabPseudo  [client] = Nom;
           tabOut     [client] = out;
           tabPseuCou [client] = Couleur;
           tabToolTip [client] = toolTip;
       }
       else
       {
           String Temp1 = tabPseudo [pos];
           String Temp2 = null;
           tabPseudo [pos] = Nom;
//pareil pour TabOut
            DataOutputStream T1 = tabOut [pos];
            DataOutputStream T2 = null;
            tabOut [pos] = out;

//pareil pour TabPseuCou
            String Cou2       = null;
            String Cou1       = tabPseuCou [pos];
            tabPseuCou [pos]  = Couleur;

//pareil pour toolTip
            String tool2      = null;
            String tool1      = tabToolTip [pos];
            tabToolTip [pos]  = toolTip;


           for (int i = pos+1; i < client; ++i)
           {
               Temp2          = tabPseudo [i];
               tabPseudo [i]  = Temp1;
               Temp1          = Temp2;
//pareil pour TabOut
               T2             = tabOut [i];
               tabOut [i]     = T1;
               T1             = T2;
//pareil pour TabPseuCou
               Cou2           = tabPseuCou [i];
               tabPseuCou [i] = Cou1;
               Cou1           = Cou2;
//pareil pour TabToolTip
               tool2          = tabToolTip [i];
               tabToolTip [i] = tool1;
               tool1          = tool2;
            }
            tabPseudo  [client] = Temp1;
            tabOut     [client] = T1;
            tabPseuCou [client] = Cou1;
            tabToolTip [client] = tool1;
      }
      ++client;
  } /* AjouterPseudo () */

  private int rechercherIndice (String Nom)
  {
    int i;

    for (i = 0; i < client; ++i)
    {
         if ( Nom.compareTo(tabPseudo [i]) < 0)
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

    public void traitementText (String Message)
    {
        StringTokenizer st = new StringTokenizer (Message, Membre.DELIM);
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
            sauvegardeSalon (Message);
            try
            {
                for (int i = 0; i < client; ++i)
                {
                    tabOut [i].writeBytes (Message + "\n");
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
                String Recept = st.nextToken ();
                int rang = chercheRang (Recept);
                if (-1 == rang)
                {
                    //Le recepteur n'est plus connecté
                    //il faut prevenir le recepteur
                    String Emeteur = st.nextToken ();
                           Emeteur = st.nextToken ();
                           Emeteur = st.nextToken ();
                    int ind = chercheRang (Emeteur);
                    if (-1 == ind) // l'emeteur du message n'a pas été trouvé
                    {
                      System.err.println("On ne peut pas prevenir '" + Emeteur +
                                         "' que '" + Recept + " est déconnecté\n");
                      return;
                    }
                    //L'emeteur est bien là.
                    tabOut [ind].writeBytes (PataCara.DIALOGUE     + Membre.DELIM +
                                             Recept                 + Membre.DELIM +
                                             Dialogue.Couleur   [1] + Membre.DELIM +
                                             Dialogue.ListStyle [3] + Membre.DELIM +
                                             Recept                 + Membre.DELIM +
                                             Dialogue.Couleur [1]   + Membre.DELIM +
                                             "deconecté(e)"         + Membre.DELIM +
                                             "est deconnecté(e)"    + "\n");
                     tabOut [ind].flush ();
                }
                else
                {
                    tabOut [rang].writeBytes (Message + "\n");
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
    public void sauvegardeSalon (String Ligne)
    {
        if (nbLigne < NBLIGNESALON)
            ligneSalon [nbLigne++] = Ligne;
        else
        {
            //la premiere ligne doit partir
            for (int i = 0; i < NBLIGNESALON -1; ++i)
            {
                ligneSalon [i] = ligneSalon [i+1];
            }
            ligneSalon [NBLIGNESALON -1] = Ligne;
        }
   }/* SauvegardeSalon () */

   public boolean estPseudoConnecter (String Pseudo)
   {
       for (int i = 0; i < client; ++i)
       {
           if (tabPseudo [i].equals (Pseudo))
               return true;
       }
       return false;
    } /* EstPseudoConnecter () */


} // Classe Serveur
