package pata_cara.client;

/**
 * Title:        pata_cara
 * Description:  chat en java
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Rémy Giraud
 * @version 1.0
 */


import javax.swing.JFrame;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeListener;
import javax.swing.JTabbedPane;
import java.net.Socket;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import javax.swing.event.ChangeEvent;
import java.util.StringTokenizer;
import java.awt.event.ActionEvent;
import java.io.InputStreamReader;
import util.gui.Boite;
import util.jni.windows.flash.FlashWindow;

import java.io.*;
import javax.swing.JTextPane;

import pata_cara.client.popup.PopupConnect;


public class PataCara extends JFrame implements ActionListener, ChangeListener
{
  public static final String SALON     = "Salon";
  public static final String DIALOGUE  = "dialogue";
  public static final String ARRIVANT  = "arrivant";
  public static final String DEPART    = "depart";
  public static final String IGNORER   = "Ignorer";
  public static final String INFO      = "Info";
  public static final String FALSE     = "false";

  public static final String      HOST         = "patachou.dyndns.org";
  //static final String      HOST         = "localhost";

  static final int NumFicheInfo          = 1;
  static final int NumFicheSalon         = 2;
  static final int NumFicheFirstDialogue = 3; /* Indice de la premiere fiche qui peut etre un dialogue */
  static final int TAILLEMAXMSG          = 150; // taille maximum d'un message
  static       Color COULEUR_FICHE_NEW = Color.yellow; // La couleur des fiches quand nouveau message

  private int nbChateur = 0;
  private Dialogue tableChateur [] = new Dialogue [Main.MAX_MEMBRES];
  private String   ListeIgnorer [] = new String   [Main.MAX_MEMBRES];
  private JTabbedPane panneauOnglet;
  private String Pseud;
  private Salon salon;
  private Membre membre;
  private String toolTip;

  private Socket           clientSocket = null;
  private DataOutputStream out          = null;
  private BufferedReader   in           = null;

  private Main appli = null;
  private FlashWindow flashFenetrePataCara = null;


   private int NbIgnorer = 0;
   private Info info;


  public PataCara(Main application, String Pseudo, Membre membre)
  {
      super ("Bienvenue " + Pseudo + " sur Pata_Cara");
      try {
        setIconImage (new ImageIcon (getClass ().getResource ("/images/Main.gif")).
                      getImage ());
      //setBounds (225,150,550,450);
      setBounds (130,80,750,600);
      //getContentPane ().setBackground (/*Color.black*/new Color (245, 245, 227));
      this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

      this.Pseud = Pseudo;
      this.membre = membre;
      this.appli = application;

// ATTENTION LE TOOLTIP NE DOIT PAS CONTENIR D'ESPACES ! --> vérifié à la saisie des données
//STRUCTURE (délimiteur ",") :
//     1 -> AGE
//     2 -> Sexe
//     3 -> Ville
//     4 -> email

      this.toolTip = "" + membre.anneeNaissance + ","
                     + (membre.homme ? "H" : "F") + ","
                     + membre.ville  + "," + Pseudo
                     + "@pata_cara.com";

      panneauOnglet = new JTabbedPane ();
//panneauOnglet.setUI(new javax.swing.plaf.metal.MetalTabbedPaneUI ());
//panneauOnglet.putClientProperty(Options.NO_CONTENT_BORDER_KEY, Boolean.TRUE);
//panneauOnglet.putClientProperty(com.jgoodies.plaf.plastic.PlasticTabbedPaneUI.MARK_CONTENT_BORDERS_KEY, Boolean.TRUE);
//panneauOnglet.putClientProperty(Options.EMBEDDED_TABS_KEY, Boolean.TRUE);
//System.out.println(panneauOnglet);
      panneauOnglet.addChangeListener(this);

      panneauOnglet.addTab("mon profil", new MonProfil (membre));
      panneauOnglet.addTab("Info", (info = new Info ()));

      salon = new Salon (this);
      panneauOnglet.addTab(SALON, salon);
      panneauOnglet.setBackground(salon.getBackground());
      if (Main.isActifLooAndFeel) //La couleur des fiches quand vient un nouveau msg est calculé
      {/*
        Color c = panneauOnglet.getBackground ();
        System.out.println (c);
        COULEUR_FICHE_NEW = new Color ( (c.getRed () + 128) % 256,
                                       (c.getGreen () + 128) % 256,
                                       (c.getBlue () + 128) % 256);
        System.out.println (COULEUR_FICHE_NEW);
        }
       */
      COULEUR_FICHE_NEW = Color.red;
     }
      salon.salonEst.ListeConnecte.addMouseListener (new MouseAdapter ()
      {
          public void mouseClicked (MouseEvent e)
          {
              if (2 == e.getClickCount ())
              {//double click
                  Dialoguer ((String)
                             (salon.salonEst.ListeConnecte.
                             getSelectedValue ()));

              }
           }
      });

      panneauOnglet.setSelectedIndex(NumFicheSalon);

      JLabel Logo = new JLabel (new ImageIcon (getClass ().getResource("/images/logo.jpg")));
      getContentPane ().add (Logo, BorderLayout.NORTH);

      getContentPane ().add (panneauOnglet);

      addWindowListener (new WindowAdapter ()
      {
        public void windowClosing (WindowEvent e)
        {
             System.out.println("déconexion de " + Pseud);
          //on prévient le controlleur qu'on veut fermer la fenetre.
          appli.getControleFenetre().closeFenetrePataCara(getThis());
        }
      });
      //setResizable(false);
      int Choix = openConnection ();
      switch (Choix)
      {
        case 0 :
          //On désactive la bar et on la masque
          appli.getBarDefilement().stopProgress();
          appli.removeBarDefilement();

          Boite.message(this, "Annulation",
                        "Connection refusée : trop de monde");
          appli.getBarDefilement().setValue(0);
          appli.removeBarDefilement();
          return;

        case 1 :
          appli.getBarDefilement().stopProgress();
          Boite.message(this, "Annulation", "Le pseudo " + Pseud +
                                      " est déja connecté");
          appli.getBarDefilement().setValue(0);
          appli.removeBarDefilement();
          return;

        case 2 :
          appli.getBarDefilement().stopProgress();
          Boite.message(this, "Annulation",
                       "Impossible de se connecter : l'hote n'a pas été trouvé");
         appli.getBarDefilement().setValue(0);
         appli.removeBarDefilement();
          return;

        case 3 :
          appli.getBarDefilement().stopProgress();
          Boite.message(this, "Erreur Connection (s)",
                        "Impossible de se connecter : Le server Pata_Cara n'est pas en route");
          appli.getBarDefilement().setValue(0);
          appli.removeBarDefilement();
          return;

        default :
      }


      //On tue définitivement la barre de défilement apres l'avoir retirée.
      appli.removeBarDefilement();
      appli.destroyeBarDefilement();

      
      //Affichage de la fenetre
      setVisible (true);

      //Tout c'est bien passé on peut enregistrer cette fenetre dans l'application Main
      appli.addFenetrePataCara(this);

      }//try
      catch (java.lang.ArrayIndexOutOfBoundsException Exc)
      {
          System.err.println ("L'erreur suivante est intervenue : " + Exc);
      }
  } /* Constructeur pata_cara */



  /**
   *
   * @return renvoi this.
   */
  private PataCara getThis ()
  {
    return this;
  } /* getThis () */


  /**
   *
   * @return Le pseudo qui concerne cette fenetre.
   */
  public String getPseudo ()
  {
    return Pseud;
  } /* getPseudo() */


  /**
   * Action a effectuer lorsqu'on ferme la fenetre PataCara.
   */
  public void closeFenetrePataCara ()
  {
    try
    { //fermeture du flux du client.
      out.close ();
      dispose ();
    }
    catch (IOException ex)
    {
      System.err.println("Erreur fermeture du flux du client");
      ex.printStackTrace();
    }

  }



      public void actionPerformed(ActionEvent e)
      {
        int Fiche = panneauOnglet.getSelectedIndex();


        if (e.getActionCommand().equals(Dialogue.Terminer))
        {
              Terminer (Fiche);
        }
        else if (e.getActionCommand().equals(Dialogue.Envoie))
        {
          if (SALON == panneauOnglet.getTitleAt(Fiche))
              EnvoieSalon ();
          else
              EnvoieDial (Fiche);
        }
        else if (e.getActionCommand().equals(SalonEst.Dialoguer))
        {
          Dialoguer ((String) salon.salonEst.ListeConnecte.getSelectedValue());
        }
        else if (e.getActionCommand().equals(SalonEst.Ignorer))
        {
          if (SALON == panneauOnglet.getTitleAt(Fiche))
          {
            //System.out.println ("on a appuyer sur Ignorer dans le salon");
              String Pseudo = null;
              if (null == (Pseudo = (String) (salon.salonEst.ListeConnecte.
                                    getSelectedValue())))
              //pas de pseudo selectionner
              return;
              //un pseudo est selectionner
              EnvoieIgnorerServeur (Pseudo);

          }
          else
          {
            //System.out.println ("on a appuyer sur Ignorer dans un dialogue");
            EnvoieIgnorerServeur (panneauOnglet.getTitleAt(Fiche));
            //on ferme le dialogue
            Terminer (Fiche);
          }
        }
        else if ((! e.getActionCommand().equals(Dialogue.Envoie)) &&
                 (SALON == panneauOnglet.getTitleAt(Fiche)))
        {
            EnvoieSalon ();
        }
        else
        {
            if (SALON != panneauOnglet.getTitleAt(Fiche))
                EnvoieDial (Fiche);
        }
   } /* actionPerformed () */

   public void stateChanged (ChangeEvent e)
   {
     remettreCouleurOngletInitiale();
   } /* stateChanged () */


   /**
    * Remet la couleur initiale de l'onglet courant.<br>
    * Si le look and feel est présent, c'est le foreground que l'on modifie
    * (pb avec les background), sinon on modifie le background.
    */
   public void remettreCouleurOngletInitiale ()
   {
     if (Main.isActifLooAndFeel)
       changeCouleurOnglet (panneauOnglet.getComponentAt (
           panneauOnglet.getSelectedIndex ()).getForeground ());
     else
       changeCouleurOnglet (panneauOnglet.getComponentAt (
           panneauOnglet.getSelectedIndex ()).getBackground ());

   } /* remettreCouleurOngletInitiale () */


   /**
    * Change la couleur du l'onglet avec la couleur fourni.<br>
    * Si le look and feel est présent, c'est le foreground que l'on modifie
    * (pb avec les background), sinon on modifie le background.
    * @param c la nouvelle couleur de l'onglet.
    */
   public void changeCouleurOnglet (Color c)
   {
     if (Main.isActifLooAndFeel) //LookAndFeel activé
       panneauOnglet.setForegroundAt (panneauOnglet.getSelectedIndex (), c);

     else
       panneauOnglet.setBackgroundAt (panneauOnglet.getSelectedIndex (), c);


   } /* changeCouleurOnglet () */


   /**
    * Change la couleur du l'onglet avec la couleur fourni.<br>
    * Si le look and feel est présent, c'est le foreground que l'on modifie
    * (pb avec les background), sinon on modifie le background.
    * @param index l'indice de l'onglet à modifier la couleur.
    * @param c la nouvelle couleur de l'onglet.
    */
   public void changeCouleurOnglet (int index, Color c)
   {
     if (Main.isActifLooAndFeel) //LookAndFeel activé
       panneauOnglet.setForegroundAt (index, c);

     else
       panneauOnglet.setBackgroundAt (index, c);


   } /* changeCouleurOnglet () */



   Dialogue getDialogue (String Pseudo)
   {
      int i = 0;
      for (; i < nbChateur; ++i)
      {
        if (Pseudo.equals (tableChateur[i].Nom)) break;
      }
      //attention pas gerer si pas trouver
      return tableChateur [i];
   } /* getDialogue () */



    private void Dialoguer (String item)
    {
      if (null == item)
      {
        //pas d'item selectioner
        return;
      }

//on deselectione l'item qui avait était sélectionné dans la liste des connecté
//salon.salonEst.ListeConnecte.deselect(salon.salonEst.ListeConnecte.getSelectedIndex());
salon.salonEst.ListeConnecte.removeSelectionInterval(salon.salonEst.ListeConnecte.getSelectedIndex(), salon.salonEst.ListeConnecte.getSelectedIndex());

      // on regarde si la fiche existe avant de la creer
      // qd on a choisi une personne dans la liste des connectés
      for (int i = 0; i < nbChateur; i ++)
      {
        if (tableChateur [i].Nom.equals (item))
        {
          panneauOnglet.setSelectedComponent(tableChateur [i]);
          return;
        }
      } // for

      // la fiche n'existe pas on la créée
      Dialogue temp = new Dialogue (this, item);
      tableChateur [nbChateur++] = temp;
      panneauOnglet.addTab (temp.Nom,temp);
      panneauOnglet.setSelectedComponent(temp);
    } /* Dialoguer () */

    private void Terminer (int Fiche)
    {
      //on eleve dans notre tab de chatteur celui a qui on veut plus parler
      String Nom = panneauOnglet.getTitleAt(Fiche);
      int i = 0;
      for (; i < nbChateur; ++i)
      {
        if (Nom.equals (tableChateur[i].Nom)) break;
      }
      for (; i < nbChateur - 1; ++i)
      {
        tableChateur [i] = tableChateur [i +1];
      }
      --nbChateur;

      //on enleve la fiche de la personne de la fenetre et on met le salon comme fiche courante
      panneauOnglet.removeTabAt(Fiche);
      panneauOnglet.setSelectedIndex(NumFicheSalon);

    } /* Terminer () */

    private void EnvoieSalon ()
    {
     //on a appuyer sur Envoie dans le salon

      //on recupere le text et on le remet a 0
      //apres récupération du message, on vérifie qu'il dépasse pas une certaine taille TAILLEMAXMSG
      String message = salon.salonOuest.textField.getText();
//System.out.println("longueur message : " + message.length());
//System.out.println("message : " + message);
      if (message.length() > TAILLEMAXMSG)
      {
           message = message.substring(0, TAILLEMAXMSG);
//System.out.println("longueur message : " + message.length());
//System.out.println("message : " + message);
      }
      salon.salonOuest.textField.setText("");
      //on recupere la couleur a afficher pour le salon
String CouEnv = salon.salonOuest.ListChoixCouleur.getSelectedItem ().toString ();
      Color CouleurText = getColor (CouEnv);
      //on recupere le style
      String StyleText = salon.salonOuest.ListChoixStyle.getSelectedItem ().toString ();

//construction du string que l'on va envoyer avec
// 0 le lieux concerné (salon, dialogue, info...)
// 1 la couleur,
// 2 le style,
// 3 le pseudo
// 4 couleur pseudo
// 5 tooltip
// 6 le texte a envoyer

// ATTENTION LE TOOLTIP NE DOIT PAS CONTENIR D'ESPACES !

String StrAenvoyer = SALON + Membre.DELIM + CouEnv
                           + Membre.DELIM + StyleText
                           + Membre.DELIM + Pseud  + Membre.DELIM
                           + (membre.homme ? Dialogue.Couleur [7] :
                                             Dialogue.Couleur [5])
                           + Membre.DELIM      + toolTip
                           + Membre.DELIM      + message;


      //on envoie le message au serveur pour qu'il difuse le message
      //a tout le salon
      try
      {
          out.writeBytes (StrAenvoyer + "\n");
          out.flush ();
      }
      catch (java.io.IOException e)
      {
          //System.err.println ("L'exception suivante est intervenue dans la fonction EnvoieSalon : " +e);
      }
    } /* EnvoieSalon () */


    /**
     * Envoie le message qui provient de la fiche n° Fiche (dialogue) au serveur
     * et l'affiche dans la fiche du dialogue concerné.
     * @param Fiche le numéro de la fiche de l'onglet (= dialogue concerné par le message).
     */
    private void EnvoieDial (int Fiche)
   {
      //on recupere la fiche dialogue de l'emeteur
      Dialogue Emetteur = getDialogue (panneauOnglet.getTitleAt(Fiche));
      //on recupere toutes les infos neccessaire couleur text style
      String text = Emetteur.textField.getText();
      //Si pas de texte on sort
      if (0 == text.length())
        return;
      // vérification que le texte ne dépasse pas TAILLEMAXMSG
      if (text.length() > TAILLEMAXMSG)
      {
           text = text.substring(0, TAILLEMAXMSG);
      }
      //Si que des blancs on sort
      if (0 == text.trim().length())
      {
        Emetteur.textField.setText ("");
        return;
      }

      Object CouleurText = Emetteur.ListChoixCouleur.getSelectedItem ();
      Object StyleText = Emetteur.ListChoixStyle.getSelectedItem ();
      Color CoulLis = getColor (CouleurText.toString ());


//preparation du string a  envoyer au serveur
//construction du string que l'on va envoyer avec
// 0 le lieux concerné (salon, dialogue, info...)
// 1 recepteur
// 2 la couleur,
// 3 le style,
// 4 le pseudo
// 5 couleur pseudo
// 6 tooltip
// 7 le texte a envoyer

// ATTENTION LE TOOLTIP NE DOIT PAS CONTENIR D'ESPACES !


String StrAenvoyer = DIALOGUE                              + Membre.DELIM +
                     panneauOnglet.getTitleAt (Fiche)      + Membre.DELIM +
                     CouleurText                           + Membre.DELIM +
                     StyleText.toString ()                 + Membre.DELIM +
                     Pseud                                 + Membre.DELIM +
                    (membre.homme ? Dialogue.Couleur [7] :
                                    Dialogue.Couleur [5])  + Membre.DELIM +
                     toolTip                               + Membre.DELIM +
                     text;
//envoie au serveur
      try
      {
          out.writeBytes (StrAenvoyer + "\n");
          out.flush ();


          // envoie dans la zone de texte de l'emeteur
               //on peut changer ici la couleur du pseudo pour pas confondre
         envoyerMessageDialogue (Emetteur, Pseud, text,
                         membre.homme ? getColor ("bleu") : getColor ("rose"),
                         CoulLis, StyleText.toString (), toolTip, false);
         Emetteur.textField.setText ("");

      }
      catch (java.io.IOException e)
      {
          System.err.println ("L'exception suivante est intervenue dans la fonction EnvoieDial : " +e);
      }
    } /* EnvoieDial () */


    /**
     * Affiche le message text dans le textPane concerné.
     * @param pseudo le pseudo de celui qui envoie le message.
     * @param textPane l'endroit ou le message doit etre affiché.
     * @param text le message.
     * @param CouPseudo la couleur du pseudo.
     * @param TextCoul la couleur du message.
     * @param style le style (police taille) du message.
     * @param toolTip le toolTip qui sera associé au message.
     * @param Bold si le message doit etre en gras.
     */
    private static void EnvoyerMessage (String pseudo, JTextPane textPane, String text,
                                 Color CouPseudo, Color TextCoul, String style,
                                 String toolTip, boolean Bold)
    {
       //envoie du pseudo puis du message
      Dialogue.addLabel (textPane, pseudo + "> ", toolTip, CouPseudo,
                         Bold, "" /*-> defaut style*/);
      Dialogue.addTextLabel (textPane, text + " "/* + dialogue.newline*/, style
				,TextCoul, toolTip);
    } /* EnvoyerMessage () */



    /**
     * Affiche le message text dans le textPane concerné et supprime le text dans
     * le TextPane s'il est trop conséquent.
     * @param dialConcerne le dialogue qui est concerné par le message.
     * @param pseudo le pseudo de celui qui envoie le message.
     * @param text le message.
     * @param CouPseudo la couleur du pseudo.
     * @param TextCoul la couleur du message.
     * @param style le style (police taille) du message.
     * @param toolTip le toolTip qui sera associé au message.
     * @param bold si le message doit etre en gras.
     */
    private void envoyerMessageDialogue (Dialogue dialConcerne, String pseudo,
                                         String text,
                                         Color CouPseudo, Color TextCoul,
                                         String style, String toolTip, boolean bold)
    {

      dialConcerne.getGestionLigneTextPane ().verifNbrLigneTextPane ();
      PataCara.EnvoyerMessage (pseudo, dialConcerne.textPane, text, CouPseudo,
                               TextCoul,
                               style, toolTip, bold);
      dialConcerne.getGestionLigneTextPane ().
          ajouteLongueurLigneTextPane (dialConcerne.textPane.getDocument ().
                                       getLength ());



    } /* envoyerMessageDialogue() */


    /**
     * Affiche le message text dans le textPane concerné et supprime le text dans
     * le TextPane s'il est trop conséquent.
     * @param salon le salon qui est concerné par le message.
     * @param pseudo le pseudo de celui qui envoie le message.
     * @param text le message.
     * @param CouPseudo la couleur du pseudo.
     * @param TextCoul la couleur du message.
     * @param style le style (police taille) du message.
     * @param toolTip le toolTip qui sera associé au message.
     * @param bold si le message doit etre en gras.
     */
    private void envoyerMessageSalon (SalonOuest salon, String pseudo,
                                         String text,
                                         Color CouPseudo, Color TextCoul,
                                         String style, String toolTip, boolean bold)
    {

      salon.getGestionLigneTextPane ().verifNbrLigneTextPane ();
      PataCara.EnvoyerMessage (pseudo, salon.textPane, text, CouPseudo,
                               TextCoul,
                               style, toolTip, bold);
      salon.getGestionLigneTextPane ().
          ajouteLongueurLigneTextPane (salon.textPane.getDocument ().
                                       getLength ());



    } /* envoyerMessageDialogue() */




    static public Color getColor (String CouleurText)
    {
      int pos = 0;
       Color CoulLis;
      for (; pos < Dialogue.nbCouleur; ++pos)
      {
		if (Dialogue.Couleur[pos].equals (CouleurText))
                   break;
       }
       switch (pos)
       {
         case 0:
           CoulLis = Color.black;
           break;
         case 1:
           CoulLis = Color.red;
           break;
         case 2:
           CoulLis = new Color (82, 175, 37); /*Color.green;*/
           break;
         case 3:
           CoulLis = new Color (242, 188, 2) /*Color.yellow*/;
           break;
         case 4:
           CoulLis = Color.lightGray;
           break;
         case 5:
           CoulLis = new Color (207, 33, 181); /*Color.pink;*/
           break;
         case 6:
           CoulLis = new Color (221, 147, 23); /*Color.orange;*/
           break;
         case 7:
           CoulLis = Color.blue;
           break;

         default:
           System.err.println ("probleme niveau couleur");
           CoulLis = Color.black;
       }
        return CoulLis;
   } /* getColor () */

// fontion qui traite l'arrivée d'un message
   public void ReceptionTexte (String Message)
   {
              StringTokenizer st = new StringTokenizer (Message, Membre.DELIM);
              if (!st.hasMoreTokens ())
              {
                   //System.out.println ("Erreur lors de la reception du string dans la fonction ReceptionTexte");
                   return;
              }
              // il y a bien au moins un mot que l'on stock
              String mot = st.nextToken ();

             // on regarde si c'est pour le salon et si alors nbr token est superieur ou egale a 6
             if (mot.equals (SALON) && st.countTokens () > 5)
             {
             // 0 le lieux concerné (salon, dialogue, info...)
             // 1 la couleur,
             // 2 le style,
             // 3 le pseudo,
             // 4 couleur pseudo,
             // 5 tooltip,
             // 6 le texte a envoyer

             // on recupere toutes les données neccessaires pour l'ajout du texte
                   Color couleur = getColor (st.nextToken ());
                   String style = st.nextToken ();
                   String pseudoAenv = st.nextToken ();
                   if (EstIgnorer (pseudoAenv))
                       return;

                     Color couleurPseudo = getColor (st.nextToken ());
                     String tool = st.nextToken ();

                     String txt = Message.substring (Message.indexOf (tool) + tool.length () +
                                                     1,
                                                     Message.length ());

                     envoyerMessageSalon (salon.salonOuest, pseudoAenv, txt, couleurPseudo,
                                     couleur, style, tool, false);
//                   Dialogue.addLabel (salon.salonOuest.textPane,
//                                      pseudoAenv + "> ", tool,
//                                      couleurPseudo, false, "" /*-> defaut style*/);
//                  Dialogue.addTextLabel (salon.salonOuest.textPane,
//                                         txt + " "/* + dialogue.newline*/,
//                                         style, couleur, tool);

             }
             // on regarde si c'est pour le salon et si alors nbr token est superieur ou egale a 6
             if (mot.equals (DIALOGUE) && st.countTokens () > 5)
             {

             //construction du string que l'on va envoyer avec
             // 0 le lieux concerné (salon, dialogue, info...)
             // 1 recepteur
             // 2 la couleur,
             // 3 le style,
             // 4 le pseudo
             // 5 couleur pseudo
             // 6 tooltip
             // 7 le texte a envoyer

// ATTENTION LE TOOLTIP NE DOIT PAS CONTENIR D'ESPACES !

             // on recupere toutes les données neccessaires pour l'ajout du texte
                   String Recept = st.nextToken ();
                   Color couleur = getColor (st.nextToken ());
                   String style = st.nextToken ();
                   String pseudoAenv = st.nextToken ();
                   if (EstIgnorer (pseudoAenv))
                       return;

                   Color couleurPseudo = getColor (st.nextToken ());
                   String tool = st.nextToken ();
                   String txt = Message.substring (
                                                   Message.indexOf (tool)
                                                   + tool.length () +1,
                                                   Message.length ());


            //verification de l'onglet emmetteur pour le recepteur

             boolean Trouve = false;
             for (int i = 0; i < nbChateur; ++i)
             {
                 if (pseudoAenv.equals (tableChateur[i].Nom))
                 {
                   Trouve = true;
                    break;
                 }
              }

               //on ne parlait pas a la personne il faut lui creer une nouvelle fiche
               if (!Trouve)
               {
                   Dialogue temp = new Dialogue (this, /*Pseud*/ pseudoAenv);
                   tableChateur [nbChateur++] = temp;
                   panneauOnglet.addTab (temp.Nom,temp);
                }
                // faire en sorte que l'onglet change de couleur
                // recherche de l'indice de la fiche de l'emeteur chez le recepteur
                // attention si on ajoute une rubrique info i = 3
                int index = 0;
                for (int i = NumFicheFirstDialogue; i < panneauOnglet.getTabCount(); ++i)
                {
                   if (pseudoAenv.equals (panneauOnglet.getTitleAt(i)))
                   {
                     index = i;
                   }
                 }

                 // verifie si la fiche courante du recepteur est celle
                 //de l'emmetteur pour mettre la couleur
                 if (panneauOnglet.getSelectedIndex() != index)
                         changeCouleurOnglet (index, COULEUR_FICHE_NEW);
                 
                 // Verification que la fenetre n'est pas deja active, sinon on la fai clignoter
                 if (!this.isActive())
                  appli.getControleFenetre().startFlashPataCara(this);

      //envoie du message chez le recepteur
                 envoyerMessageDialogue (getDialogue(pseudoAenv), pseudoAenv,
                                         txt + " ",
                                         couleurPseudo,
                                         couleur, style, tool, false);

             }
             if (mot.equals (ARRIVANT) && st.countTokens () >= 3)
             {
               String pseudo = null;
               System.out.println ("arrivant-----------------------");
             //il y a un nouvel arrivant a inclure dans la liste du salon
                 salon.salonEst.ListeConnecte.AjouterPseudo ((pseudo = st.nextToken ())
                                                             /*pseudo*/,
                                                             st.nextToken ()
                                                             /*couleur*/,
                                                             st.nextToken()
                                                             /*toolTip*/);
                //Popup de nouveau connecté si c'est pas notre pseudo
                 System.out.println ("pseudo : " + pseudo + ", pseud : " + this.Pseud);
                if (!pseudo.equals (this.Pseud))
                {
                  System.out.println ("lancement popup");
                  PopupConnect popup = new PopupConnect (pseudo);
                  popup.movePopup();
                }
             }
             if (mot.equals (DEPART) && st.countTokens () >= 1)
             {
             //il y a un pseudo a enlever  dans la liste du salon
                 salon.salonEst.ListeConnecte.EffacerPseudo (st.nextToken ());
             }
             // on regarde si c'est pour l'info
            if (mot.equals (INFO))
            {
                String Txt = Message.substring (
                                        Message.indexOf (INFO)
                                        + INFO.length () +1,
                                        Message.length ());

            //Si l'on souhaite juste insérer du texte sans smiley ou label
            //dialogue.addMessage (info.textPane, Txt + dialogue.newline,
            //                     dialogue.ListStyle [3],
            //                     getColor (dialogue.Couleur   [1]);

            //Si l'on souhaite avoir un text label
                try
                {
                Dialogue.addTextLabel (info.getTextPane (),
                                       Txt + " " +
                                       Dialogue.TabSmileyRepr [1] +
                                       " "/* + dialogue.newline*/,
                                       Dialogue.ListStyle [3],
                                       Color.red,
                                       "Message d'information");
                 }
                 catch (java.lang.NullPointerException E)
                 {
                     System.err.println ("erreur dans recttextinfo : " + E);
                 }
            //On vérifie la fiche courante et on la met en jaune si neccesaire
                if (panneauOnglet.getSelectedIndex() != NumFicheInfo)
                {
                  changeCouleurOnglet (NumFicheInfo, COULEUR_FICHE_NEW);
                }

          }


   } /* ReceptionTexte () */

   private int openConnection ()
   {
    //construction de la connection serveur
       try
       {
           this.clientSocket = new Socket (HOST, 5555);
        //ouvrir Stream pour l'envoie des données au serveur
           this.out = new DataOutputStream (clientSocket.getOutputStream ());
           this.in  = new BufferedReader (new InputStreamReader (
                                           clientSocket.getInputStream  ()));
       //on envoie son pseudo au serveur pour qu'il informe tout le monde
       //de son arrivé et pour mettre à jour les connectés
           out.writeBytes (this.Pseud    + Membre.DELIM +
                           (membre.homme ? Dialogue.Couleur [7]/*bleu*/
                                         : Dialogue.Couleur [5]/*rose*/)
                                         + Membre.DELIM +
                            this.toolTip + "\n");
           out.flush ();

       //recupere la reponse du serveur pour savoir si le pseudo est connecté
           String reponse = in.readLine ();
           if (reponse.equals (FALSE))
           {
               return 1;
           }

       //on recupere le nombre de connecte
           String NbreConect = null;
           NbreConect = in.readLine ();
           int CstNbre = Integer.parseInt (NbreConect);
      //on regarde si le nombre n'est pas trop grand
           if (Main.MAX_MEMBRES == CstNbre)
               return 0;
      //on envoie dans le liste de connecte du salon tous les connectés
           for (int i = 0; i < CstNbre; ++i)
           {
//vérifier s'il faut pas mettre un espace apres la couleur pour ne pas lire
// le caractere \n dans la couleur
               //0 pseudo
               //1 couleur
               //2 tooltip
               StringTokenizer st = new StringTokenizer (in.readLine (), Membre.DELIM);
               salon.salonEst.ListeConnecte.AjouterPseudo (st.nextToken ()
                                                           /*pseudo*/,
                                                           st.nextToken ()
                                                           /*couleur*/,
                                                           st.nextToken());
           }
       }
       catch (java.net.UnknownHostException e)
       {
           System.err.println ("La connection a l'hote " + HOST +
                               " n'as pu etre établie  dans openConnection !");
           return 2;
       }
       catch (java.io.IOException e)
       {
           //System.err.println ("L'exception suivante est intervenue dans la fonction openConnection : " + e);
           return 3;
       }
       ClientAppThread clientAppThread = new ClientAppThread (in, this);
       clientAppThread.start ();
       return 4;
   }// openConnections ()


   //fonction qui envoie au serveur le pseudo ignorer
   private void EnvoieIgnorerServeur (String Pseudo)
   {
        //on l'ajoute dans la liste des ignorer
        ListeIgnorer [NbIgnorer++] = Pseudo;

        //envoie au serveur d'un string
        //pour prevenir l'ignorer de la chose
        //O IGNORER
        //1 Pseudo ignorer
        //2 Pseudo de celui qui l'ignore
        try
        {
            out.writeBytes (IGNORER + Membre.DELIM +
                            Pseudo  + Membre.DELIM +
                            Pseud   + Membre.DELIM +
                            "\n");
            out.flush ();
        }
        catch (java.io.IOException e)
        {
            //System.err.println ("L'exception suivante est intervenue dans la fonction action perfor IgnorerSalon : " +e);
        }
   } // EnvoieIgnorerServeur ()


   private boolean EstIgnorer (String Pseudo)
   {
       //on vérifie s'il est ignoré
       for (int i = 0; i < NbIgnorer; ++i)
       {
           if (Pseudo.equals (ListeIgnorer [i]))
           {
               //Le pseudo est ignorer
               System.out.println ("le pseudo : " + ListeIgnorer [i] +
                                   " a été ignoré par :" + Pseud);
               return true;
           }
       }
       return false;
   } // Est
   
   /**
    * Initialisation du flashWindow pour la fenetre. Cette méthode sera appelée par le controleur de fenetre si le systeme d'exploitation le permet.
    *
    */
   public void initFlashWindow ()
  {
     //Création du gestionnaire pour le clignotement de la fenetre
     flashFenetrePataCara = new FlashWindow (this);
  } /* initFlashWindow () */
   
   /**
    * Fait clignoter la fenetre. Cette méthode sera appelée par le controleur de fenetre si le systeme d'exploitation le permet.
    *
    */
   public void startFlashFenetrePataCara ()
   {
     if (null != flashFenetrePataCara)
       flashFenetrePataCara.startFlashProgressif(10000, 500);
   } /* startFlashFenetrePatacara () */

   /**
    * Fait arreter de clignoter la fenetre. Cette méthode sera appelée par le controleur de fenetre si le systeme d'exploitation le permet.
    *
    */
   public void stopFlashFenetrePataCara ()
  {
     if (null != flashFenetrePataCara)
       flashFenetrePataCara.stopFlash ();
  } /* stopFlashFenetrePataCara () */
}
