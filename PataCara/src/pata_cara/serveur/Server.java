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
import java.net.Socket;
import java.util.StringTokenizer;


public class Server
{
    private ServerSocket serverSocket = null;
    private ServerThread serverThread = null;
    private int client = 0;
    //TODO changer les portées
    String TabPseudo  [] = new String [Main.MAX_MEMBRES];
    String TabPseuCou [] = new String [Main.MAX_MEMBRES];
    String TabToolTip [] = new String [Main.MAX_MEMBRES];
    DataOutputStream TabOut [] = new DataOutputStream [Main.MAX_MEMBRES];

    private static final int NBLIGNESALON = 10;
    private String [] LigneSalon = new String [NBLIGNESALON];
    private int NbLigne = 0;


    public Server ()
    {
        super ();
        start ();
    } /* Constructeur Server */

    public void addConnection (Socket socket,
                               String Pseudo,
                               DataOutputStream out,
                               String Couleur,
                               String toolTip)
    {
        //une nouvelle connection est faite

        //mise a jours des tableaux
        AjouterPseudo (Pseudo, out, Couleur, toolTip);

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
                TabOut [i].writeBytes (strAenvoyer + "\n");
                TabOut [i].flush ();
            }

            //envoie des ligne dans le salon du nouvelle arrivant
            for (int i = 0; i < NbLigne; ++i)
            {
                out.writeBytes (LigneSalon [i] + "\n");
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
        EffacerPseudo (Pseudo);
        // detruire dans les tableaux la personne
        try
        {
            for (int i = 0; i < client; ++i)
            {
                TabOut [i].writeBytes (PataCara.DEPART + Membre.DELIM +
                                       Pseudo + "\n");
                TabOut [i].flush ();
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

   public DataOutputStream GetReceveur (String Pseudo)
   {
       //fonction qui renvoie le bon flux de sortie
       return TabOut [ChercheRang (Pseudo)];
   } /* GetReceveur () */


   /**
    * Cherche la position du pseudo Pseudo dans le tableau de pseudo.
    * @param Pseudo le pseudo qu'on recherche dans le tableau.
    * @return l'indice (position ou rang) du pseudo dans le tableau,
    * -1 si pas trouvé.
    */
   public int ChercheRang (String Pseudo)
   {
       boolean Trouve = false;
       int i = 0;
       for (; i < client; ++i)
       {
           if (TabPseudo [i].equals (Pseudo))
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

   void AjouterPseudo (String Nom,
                       DataOutputStream out,
                       String Couleur,
                       String toolTip)
   {
       int pos = RechercherIndice (Nom);

       if (client == 0 || client == pos)
       {
           TabPseudo  [client] = Nom;
           TabOut     [client] = out;
           TabPseuCou [client] = Couleur;
           TabToolTip [client] = toolTip;
       }
       else
       {
           String Temp1 = TabPseudo [pos];
           String Temp2 = null;
           TabPseudo [pos] = Nom;
//pareil pour TabOut
            DataOutputStream T1 = TabOut [pos];
            DataOutputStream T2 = null;
            TabOut [pos] = out;

//pareil pour TabPseuCou
            String Cou2       = null;
            String Cou1       = TabPseuCou [pos];
            TabPseuCou [pos]  = Couleur;

//pareil pour toolTip
            String tool2      = null;
            String tool1      = TabToolTip [pos];
            TabToolTip [pos]  = toolTip;


           for (int i = pos+1; i < client; ++i)
           {
               Temp2          = TabPseudo [i];
               TabPseudo [i]  = Temp1;
               Temp1          = Temp2;
//pareil pour TabOut
               T2             = TabOut [i];
               TabOut [i]     = T1;
               T1             = T2;
//pareil pour TabPseuCou
               Cou2           = TabPseuCou [i];
               TabPseuCou [i] = Cou1;
               Cou1           = Cou2;
//pareil pour TabToolTip
               tool2          = TabToolTip [i];
               TabToolTip [i] = tool1;
               tool1          = tool2;
            }
            TabPseudo  [client] = Temp1;
            TabOut     [client] = T1;
            TabPseuCou [client] = Cou1;
            TabToolTip [client] = tool1;
      }
      ++client;
  } /* AjouterPseudo () */

  private int RechercherIndice (String Nom)
  {
    int i;

    for (i = 0; i < client; ++i)
    {
         if ( Nom.compareTo(TabPseudo [i]) < 0)
              break;
    }
     return i;
  } /* RechercheIndice () */

   void EffacerPseudo (String pseudo)
   {
       int rg = ChercheRang (pseudo);
       for (; rg < client - 1; ++rg)
       {
          TabPseudo  [rg] = TabPseudo  [rg +1];
          TabOut     [rg] = TabOut     [rg +1];
          TabPseuCou [rg] = TabPseuCou [rg +1];
          TabToolTip [rg] = TabToolTip [rg +1];

       }
       --client;

    } /* EffacerPseudo () */

    public int getClient ()
    {
        return client;
    }

    public void TraitementText (String Message)
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
            SauvegardeSalon (Message);
            try
            {
                for (int i = 0; i < client; ++i)
                {
                    TabOut [i].writeBytes (Message + "\n");
                    TabOut [i].flush ();
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
                int rang = ChercheRang (Recept);
                if (-1 == rang)
                {
                    //Le recepteur n'est plus connecté
                    //il faut prevenir le recepteur
                    String Emeteur = st.nextToken ();
                           Emeteur = st.nextToken ();
                           Emeteur = st.nextToken ();
                    int ind = ChercheRang (Emeteur);
                    if (-1 == ind) // l'emeteur du message n'a pas été trouvé
                    {
                      System.err.println("On ne peut pas prevenir '" + Emeteur +
                                         "' que '" + Recept + " est déconnecté\n");
                      return;
                    }
                    //L'emeteur est bien là.
                    TabOut [ind].writeBytes (PataCara.DIALOGUE     + Membre.DELIM +
                                             Recept                 + Membre.DELIM +
                                             Dialogue.Couleur   [1] + Membre.DELIM +
                                             Dialogue.ListStyle [3] + Membre.DELIM +
                                             Recept                 + Membre.DELIM +
                                             Dialogue.Couleur [1]   + Membre.DELIM +
                                             "deconecté(e)"         + Membre.DELIM +
                                             "est deconnecté(e)"    + "\n");
                     TabOut [ind].flush ();
                }
                else
                {
                    TabOut [rang].writeBytes (Message + "\n");
                    TabOut [rang].flush ();
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
            if (-1 != (rang = ChercheRang (st.nextToken ())))
            {
                //L'ignorer est bien présent
                try
                {
                    TabOut [rang].writeBytes (PataCara.INFO + Membre.DELIM +
                                              "vous avez été ignoré(e) par " +
                                              st.nextToken () + "\n");
                    TabOut [rang].flush ();

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
    public void SauvegardeSalon (String Ligne)
    {
        if (NbLigne < NBLIGNESALON)
            LigneSalon [NbLigne++] = Ligne;
        else
        {
            //la premiere ligne doit partir
            for (int i = 0; i < NBLIGNESALON -1; ++i)
            {
                LigneSalon [i] = LigneSalon [i+1];
            }
            LigneSalon [NBLIGNESALON -1] = Ligne;
        }
   }/* SauvegardeSalon () */

   public boolean EstPseudoConnecter (String Pseudo)
   {
       for (int i = 0; i < client; ++i)
       {
           if (TabPseudo [i].equals (Pseudo))
               return true;
       }
       return false;
    } /* EstPseudoConnecter () */


} // Classe Serveur
