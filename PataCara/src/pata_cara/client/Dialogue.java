package pata_cara.client;


/**
 * Title:        pata_cara
 * Description:  chat en java
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Rémy Giraud
 * @version 1.0
 */




import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

import util.fichier.*;


public class Dialogue extends JPanel //implements ActionListener
{
    JTextField textField;
    JTextPane textPane;
    JComboBox ListChoixCouleur;
    JComboBox ListChoixStyle;

    static final String PathImages = "/images/";

    static       Style regular; // pere des styles
    static       Style def; // pere des styles
    static final String newline = "\n";
    static final String Envoie = " envoi ";
    static final String Terminer = " Terminer ";
    public static final      String [] Couleur = {"noir", "rouge", "vert", "jaune",
                                      "gris", "rose", "orange", "bleu" };
    static final int nbCouleur = 8;
    public static final String [] ListStyle = {"parler", "hurler", "murmurer", "large"};

    static ImageIcon[] TabSmiley;

    static String [] imagePathSmiley = { "smile.gif", "s1.gif" };
    static String [] TabSmileyRepr = { ":-)", ":-(" };
    static final int  nbSmiley = 2;
    static final int nbStyle = 4;

    static final Color CouleurToolTipHomme = new Color (35, 136, 248);
    static final Color CouleurToolTipFemme = new Color (207, 33, 181);
    static final Color COULEURDIALOGUE;//     = new Color (3, 26, 195);

    static final int SEXE_HOMME    = 0;
    static final int SEXE_FEMME    = 1;
    static final int SEXE_INDEFINI = 2;

    private GestionLigneTextPane gestionLigneTextPane; //Le gestionnaire pour le retrait de ligne dans le JTextPane


    static
    {
      //Initialisation du tableau d'images
      TabSmiley = new ImageIcon [nbSmiley];
      java.net.URL url;
      for (int i = 0; i < TabSmiley.length; ++i)
      {
        url = Dialogue.class.getClass().
                            getResource (PathImages + imagePathSmiley [i]);
        if (null == url) //Images pas trouvées
        {
          System.out.println("Image : " + imagePathSmiley [i] + " pas trouvée.");
          continue;
        }
        TabSmiley [i] = new ImageIcon (url);
      }
//      = { new ImageIcon (Dialogue.class.
//                         getResource (PathImages + "smile.gif")),
//          new ImageIcon (Dialogue.class.
//                        getResource (PathImages + "s1.gif"))};

    } // Bloc static


    String Nom;

    static
    {
      String props = Propriete.getPropriete(Main.USER_PROPS, Main.IS_ACTIF_LOOK_AND_FEEL);
      if ((props != null) && (props.equals("true")))
      {
        COULEURDIALOGUE = new JPanel ().getBackground();
      }
      else
        COULEURDIALOGUE = new Color (3, 26, 195);
    }


  public Dialogue(ActionListener auditeur, String Nom)
  {
      super ();
      this.Nom = Nom;
      setBackground(COULEURDIALOGUE);
      textField = new JTextField(34);
      textField.setSelectedTextColor(Color.black);
      textField.setSelectionColor(Color.blue);
      //textField.setCaretColor(Color.pink);
      textField.setDisabledTextColor(Color.pink);
      textField.setForeground(Color.black);
      //textField.setRequestFocusEnabled(true);
      //textField.setLayout(new FlowLayout ());
      textField.addActionListener(auditeur);
      textField.setBorder (BorderFactory.createLoweredBevelBorder ());

      textPane = createTextPane ();
      gestionLigneTextPane = new GestionLigneTextPane (textPane);

      JScrollPane scrollPane = new JScrollPane(textPane,
                                     JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                     JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      scrollPane.setBorder (BorderFactory.createLoweredBevelBorder ());

      JPanel pBas = new JPanel ();
      pBas.setLayout(new GridLayout (2, 1));
      pBas.setBackground (COULEURDIALOGUE);

      JPanel pTextField = new JPanel ();
      pTextField.setLayout (new BorderLayout (0,0));
      pTextField.setBackground (COULEURDIALOGUE);
      JPanel pTFh = new JPanel (new FlowLayout (FlowLayout.CENTER, 3, 3));
      pTFh.setBackground (COULEURDIALOGUE);
      JPanel pTFb = new JPanel (new FlowLayout (FlowLayout.CENTER, 3, 3));
      pTFb.setBackground (COULEURDIALOGUE);
      pTextField.add (textField, BorderLayout.CENTER);
      pTextField.add (pTFh, BorderLayout.NORTH);
      pTextField.add (pTFb, BorderLayout.SOUTH);

      JPanel pEstListe = new JPanel (new FlowLayout (FlowLayout.LEFT,5, 5));
      pEstListe.setBackground (COULEURDIALOGUE);

//creation des listes de choix couleur style
      ListChoixCouleur = new JComboBox (Couleur);
      ListChoixCouleur.setFont (new Font ("Dialog", Font.BOLD, 10));
      //ListChoixCouleur.setBorder (BorderFactory.createRaisedBevelBorder ());
      ListChoixCouleur.setBorder (BorderFactory.createLoweredBevelBorder ());
      ListChoixStyle = new JComboBox (ListStyle);
      ListChoixStyle.setFont (new Font ("Dialog", Font.BOLD, 10));
      ListChoixStyle.setBorder (BorderFactory.createLoweredBevelBorder ());
      pEstListe.add (ListChoixCouleur);
      pEstListe.add (ListChoixStyle);

      JButton bIgn = new JButton (SalonEst.Ignorer);
      bIgn.addActionListener(auditeur);
bIgn.setBorder (BorderFactory.createRaisedBevelBorder ());
      JButton bEnv = new JButton (Dialogue.Envoie);
      bEnv.addActionListener(auditeur);
bEnv.setBorder (BorderFactory.createRaisedBevelBorder ());
      JButton bTer = new JButton (Terminer);
bTer.setBorder (BorderFactory.createRaisedBevelBorder ());
      bTer.addActionListener(auditeur);

      JPanel pOuestBut = new JPanel (new FlowLayout (FlowLayout.RIGHT, 5,5));
      pOuestBut.setBackground(COULEURDIALOGUE);
      pOuestBut.add(bIgn);
      pOuestBut.add(bEnv);
      pOuestBut.add(bTer);

     JPanel p2ligne = new JPanel (new GridLayout (1,2));
     p2ligne.setBackground (COULEURDIALOGUE);
     p2ligne.add (pEstListe);
     p2ligne.add (pOuestBut);

      pBas.add (pTextField);
      pBas.add (p2ligne);
//fin du panneau bas

      JPanel pTextOuest = new JPanel (new FlowLayout (FlowLayout.CENTER, 2, 2));
      pTextOuest.setBackground(COULEURDIALOGUE);
      JPanel pTextEst = new JPanel (new FlowLayout (FlowLayout.CENTER, 2, 2));
      pTextEst.setBackground(COULEURDIALOGUE);

      JPanel pBasForme = new JPanel (new BorderLayout (0,0));
      pBasForme.setBackground(COULEURDIALOGUE);
      pBasForme.add(pTextOuest, BorderLayout.WEST);
      pBasForme.add(pTextEst, BorderLayout.EAST);
      pBasForme.add(pBas, BorderLayout.CENTER);


      JPanel pEstDial = new JPanel (new FlowLayout (FlowLayout.CENTER, 2, 2));
      pEstDial.setBackground(COULEURDIALOGUE);
      JPanel pOuestDial = new JPanel (new FlowLayout (FlowLayout.CENTER, 2, 2));
      pOuestDial.setBackground(COULEURDIALOGUE);

      JPanel pFin = new JPanel (new BorderLayout ());
      pFin.setBackground (COULEURDIALOGUE);

      //add (pHaut, BorderLayout.NORTH);
      pFin.add (scrollPane, BorderLayout.CENTER);
      pFin.add (pBasForme, BorderLayout.SOUTH);
      pFin.add (pEstDial, BorderLayout.EAST);
      pFin.add (pOuestDial, BorderLayout.WEST);


      /*setBorder ( BorderFactory.createCompoundBorder (
		BorderFactory.createTitledBorder (
		BorderFactory.createEtchedBorder(Color.white, Color.blue),
		"dialogue", 1, 1,new Font ("SansSerif", Font.BOLD,12),
		 Color.red),
		BorderFactory.createEmptyBorder (5, 5, 5, 5 )));*/
      pFin.setBorder ( BorderFactory.createCompoundBorder (
		BorderFactory.createTitledBorder ("vous dialoguez avec " + Nom),
		BorderFactory.createEmptyBorder (0, 0, 0, 0 )));

      setLayout (new BorderLayout ());
      JPanel pHaut = new JPanel (new FlowLayout (FlowLayout.CENTER, 2,2));
      pHaut.setBackground(COULEURDIALOGUE);
      add (pHaut, BorderLayout.NORTH);
      add (pFin,  BorderLayout.CENTER);
  } /* Constructeur dialogue */

  static public JTextPane createTextPane ()
  {
      JTextPane textPane = new JTextPane ();
      textPane.setEditable (false);
      def = StyleContext.getDefaultStyleContext ().
                            getStyle (StyleContext.DEFAULT_STYLE);
      regular = textPane.addStyle (ListStyle [0], def);
      StyleConstants.setFontFamily (def, "SansSerif");

      Style s = textPane.addStyle (ListStyle [2], regular);
      StyleConstants.setFontSize (s, 10);

      s = textPane.addStyle (ListStyle [1], regular);
      StyleConstants.setFontSize (s, 16);
      //StyleConstants.setBold (s, true);

      s = textPane.addStyle (ListStyle [3], regular);
      StyleConstants.setFontSize (s, 17);
      StyleConstants.setBold (s, true);


      return textPane;

  } /* createTextPane () */


  /**
   * Utiliser pour inserer du texte dans un JTextPane. rajouter des options
   * comme parler, hurler voir la couleur on pourra faire un travail sur le msg
   * pour mettre les smileys.
   *
   *  PLUS UTILISé
   *
   * @param textPane JTextPane
   * @param msg String
   * @param style String
   * @param couleur Color
   */
  public static void addMessage (JTextPane textPane, String msg,
				String style, Color couleur)
  {
      Style s = textPane.getStyle (style);
      //java.lang.ArrayIndexOutOfBoundsException: 1
      //at javax.swing.text.StyleConstants.setForeground(StyleConstants.java:531
      //        at dialogue.addMessage(dialogue.java:202)
      //        at dialogue.addTextLabel(dialogue.java:260)

      StyleConstants.setForeground (s, couleur);

      Document doc = textPane.getDocument ();
      try
      {
          doc.insertString (doc.getLength (), msg, s);
          textPane.setCaretPosition(doc.getLength());
      }
      catch (BadLocationException ble)
      {
           System.err.println ("Couldn't insert initial text.");
      }
  } /* addMessage () */


  public static void addNewLine (JTextPane textPane)
  {
      Document doc = textPane.getDocument ();
      try
      {
          doc.insertString (doc.getLength (), "\n", def);
          textPane.setCaretPosition(doc.getLength());
      }
      catch (BadLocationException ble)
      {
           System.err.println ("Couldn't insert initial text.");
      }
  }/* addNewLine () */


  /**
   * La fonction appeler pour ajouter une ligne qui porte le message <b>msg</b>,
   * avec le style <b>Style</b>, la couleur du message <b>couleur</b>, et le
   * toolTip <b>toolTip</b>
   * @param textPane le textPane concerné par le message.
   * @param msg le message text a marquer
   * @param Style le style du message.
   * @param couleur la couleur du message.
   * @param toolTip le toolTip.
   */
  public static void addTextLabel (JTextPane textPane, String msg, String Style,
                                   Color couleur, String toolTip)
  {
    int pos = 0;
    String str;
    StringTokenizer st = new StringTokenizer (msg, " ");
    boolean Trouve = false;
    String mesg = new String (msg);

    for (; st.hasMoreTokens (); )
    {
      str = st.nextToken ();

      for (; pos < nbSmiley; ++pos)
      {
        if (str.equals (TabSmileyRepr[pos]))
        {
          Trouve = true;
          break;
        }
      }
      if (Trouve)
      {
        addLabel (textPane,
                  mesg.substring (0, mesg.indexOf (TabSmileyRepr[pos])),
                  toolTip, couleur, false, Style);

        addSmiley (textPane, TabSmiley[pos], toolTip, couleur);
        //Ajout d'un espace apres le smiley
        addLabel (textPane, " ", toolTip, couleur, false, Style);

        mesg = mesg.substring (mesg.indexOf (TabSmileyRepr[pos])
                               + TabSmileyRepr[pos].length (),
                               mesg.length ());
      }
      else // On décompose chaque mot en label qu'on ajoute au JTextPane
      {
        addLabel (textPane,
                  str + " ",
                  toolTip, couleur, false, Style);


        mesg = mesg.substring (mesg.indexOf (str)
                               + str.length ()+1, //+1 pour l'espace
                               mesg.length ());

      }
      Trouve = false;
      pos = 0;
    }
    addLabel (textPane, mesg, toolTip, couleur, false, Style);
    //pour le '\0'
    //addMessage (textPane, dialogue.newline , Style, couleur);
    addNewLine (textPane);
  }/* addTextLabel () */

    public static String addSpaceToolTip (String ToolTip)
    {
        if (null == ToolTip)
        {
            return ToolTip;
        }
        return ToolTip.replaceAll (",", ", ");
    } /* addSpaceToolTip () */



  public static void addSmiley (JTextPane textPane, ImageIcon icon,
                                String toolTip, Color couleur /*= Color.black*/)
  {
      Style s = textPane.addStyle (null, def);
      StyleConstants.setAlignment (s, StyleConstants.ALIGN_CENTER);
//trouver le sexe
      StringTokenizer st = new StringTokenizer (toolTip, ",");
      st.nextToken (); //sert a rien donne l'age
      KLabel label = new KLabel(icon,
                         findCouleurToolTip(toolTip));
      label.setForeground(couleur);
      label.setToolTipText (addSpaceToolTip (toolTip));
      StyleConstants.setComponent (s, label);

      Document doc = textPane.getDocument ();
      try
      {
          doc.insertString (doc.getLength (), " ", s);
          textPane.setCaretPosition(doc.getLength());
      }
      catch (BadLocationException ble)
      {
           System.err.println ("Couldn't insert initial text.");
      }
  } /* addSmiley () */


  /**
   * En fonction du toolTip fourni, renvoi un chiffre representant le sexe. <br>
   * Peut renvoyer les valeurs :
   * SEXE_HOMME   = 0
   * SEXE_FEMME   = 1
   * SEXE_INDEFINI = 2
   * @param toolTip le tool tip
   * @return SEXE_HOMME si le sexe est homme,<br>
   *         SEXE_FEMME si le sexe est femme.
   *         SEXE_INDEFINI si le sexe est indéfini.
   */
  public static int findSexe (String toolTip)
  {
    //Rappel formation toolTip (créer dans constructeur de PataCara
    //     1 -> AGE
    //     2 -> Sexe
    //     3 -> Ville
    //     4 -> email

    StringTokenizer st = new StringTokenizer (toolTip, ",");
    if (st.countTokens() < 2) //On à pas un vrai tooltip, par exemple c le cas pour le tooltip dans info
      return SEXE_INDEFINI;
    st.nextToken (); //sert a rien donne l'age
    String sexe = st.nextToken();
    if (sexe.equals("H"))
      return SEXE_HOMME;
    else if (sexe.equals("F"))
      return SEXE_FEMME;
    return SEXE_INDEFINI;
  } /* findSexe () */


  /**
   * Détermine la couleur à utilisé en fonction du toolTip
   * @param toolTip le toolTip a étudier.
   * @return la couleur du toolTip à utiliser pour ce sexe là.
   */
  public static Color findCouleurToolTip (String toolTip)
  {
    switch (findSexe (toolTip))
    {
      case SEXE_HOMME:
        return CouleurToolTipHomme;
      case SEXE_FEMME:
        return CouleurToolTipFemme;
      default:
        return Color.lightGray;
    }
  }
  /* findCouleurToolTip () */

  public static void addLabel (JTextPane textPane, String text, String toolTip,
				Color couleur, boolean Bold, String Style)
  {
//System.out.println ("tool tip : " + toolTip);
      Style s = textPane.addStyle (null, def);
      StyleConstants.setAlignment (s, StyleConstants.ALIGN_CENTER);

      KLabel label = new KLabel(text,
                      findCouleurToolTip(toolTip));
      //label.setFont (new Font ("jk",Bold ? Font.BOLD : 0, 16));
      if (Style.equals (ListStyle [0]))
      {
          label.setFont (new Font ("SansSerif", 0, (Bold ? 15 : 14)));
      }
      else if (Style.equals (ListStyle [1]))
      {
          label.setFont (new Font ("SansSerif", 0, (Bold ? 17 : 16)));
      }
      else if (Style.equals (ListStyle [2]))
      {
          label.setFont (new Font ("SansSerif", 0, (Bold ? 11 : 10)));
      }
      else if (Style.equals (ListStyle [3]))
      {
          label.setFont (new Font ("SansSerif", 0, (Bold ? 18 : 17)));
      }
      else //Default style  --> utilisé pour écrire les pseudos.
      {
          label.setFont (new Font ("jk",Bold ? Font.BOLD : 0, 16));
      }
      label.setForeground(couleur);
      label.setToolTipText (addSpaceToolTip (toolTip));
      StyleConstants.setComponent (s, label);

      Document doc = textPane.getDocument ();
      try
      {
          doc.insertString (doc.getLength (), " ", s);
          textPane.setCaretPosition(doc.getLength());
      }
      catch (BadLocationException ble)
      {
           System.err.println ("Couldn't insert initial text.");
      }
   } /*addLabel () */


   /**
    *
    * @return le gestionnaire des lignes du textPane
    */
   public GestionLigneTextPane getGestionLigneTextPane ()
   {
     return gestionLigneTextPane;
   } /* getGestionLigneTextPane() */



} // Classe dialogue
