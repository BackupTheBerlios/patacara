package pata_cara.client;


/**
 * Title:        pata_cara
 * Description:  chat en java
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Rémy Giraud
 * @version 1.0
 */

import javax.swing.JPanel;
import javax.swing.JTextPane;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import util.fichier.Propriete;
import javax.swing.JButton;
import javax.swing.JList;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class Info extends JPanel implements ActionListener
{
  private JTextPane textPane;
  private static final Color Couleur; // = Color.lightGray;

  /* Pour la gestion du lookAndFeel */
  private JButton butChangerLook = null;
  private JButton butDefautLook = null;
  private JButton butSelectTheme = null;
  private JButton butAnnulerTheme = null;
  private static final String textButChangerLook  = "Changer";
  private static final String textButDefautLook   = "Défaut";
  private static final String textButSelectTheme  = "Select";
  private static final String textbutAnnulerTheme = "Annuler";

  private JList listeLook  = null;
  private JList listeTheme = null;
  private JPanel pEast     = null; //Le panel qui contient le look les themes etc...
  private JPanel pCThemes  = null; //Le panel qui contient les themes
  private JPanel pBasEast  = null; //Le panel en bas a droite
  /*Fin gestion LookAndFeel */


  static
  {
    String props = Propriete.getPropriete (Main.USER_PROPS,
                                           Main.IS_ACTIF_LOOK_AND_FEEL);
    if ( (props != null) && (props.equals ("true")))
    {
      Couleur = new JPanel ().getBackground ();
    }
    else
      Couleur = new Color (3, 26, 195);
  }


  public Info ()
  {
    super ();
    setBackground (Couleur);

    textPane = Dialogue.createTextPane ();
    JScrollPane scrollPane = new JScrollPane (textPane,
                                              JScrollPane.
                                              VERTICAL_SCROLLBAR_ALWAYS,
                                              JScrollPane.
                                              HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setBorder (BorderFactory.createLoweredBevelBorder ());

    JPanel pFin = new JPanel (new BorderLayout ());
    pFin.setBackground (Couleur);
    pFin.add (scrollPane, BorderLayout.CENTER);
    pFin.setBorder (BorderFactory.createCompoundBorder (
        BorderFactory.createTitledBorder ("Informations"),
        BorderFactory.createEmptyBorder (0, 0, 0, 0)));

    setLayout (new BorderLayout ());
    JPanel pHaut = new JPanel (new FlowLayout (FlowLayout.CENTER, 2, 2));
    pHaut.setBackground (Couleur);

    //Création panneau Est
    pEast = createPanelEast();

    add (pHaut, BorderLayout.NORTH);
    add (pFin, BorderLayout.CENTER);
    add (pEast, BorderLayout.EAST);
  } // Constructeur Info






  /**
   * Accesseur au textpane
   * @return JTextPane
   */
  public JTextPane getTextPane ()
  {
    return this.textPane;
  } /* getTextPane () */


  /**
   * Ajoute le panel des themes.
   */
  private void addThemePanel ()
  {
    //Verification si le panel est pas dejà ajouté
    if (null != pCThemes)
      return;
    //Création des boutons
    butSelectTheme = new JButton ();
    butAnnulerTheme = new JButton ();
    //Ajout ecouteur sur bouton
    butSelectTheme.setActionCommand(textButSelectTheme);
    butSelectTheme.addActionListener(this);
    butAnnulerTheme.setActionCommand(textbutAnnulerTheme);
    butAnnulerTheme.addActionListener(this);


    listeTheme = new JList ();
    JPanel pThemes = util.gui.ComposantGraphique.createBoxWith2Bouton1Liste
                     ("Thèmes", butSelectTheme, butAnnulerTheme, textButSelectTheme,
                      textbutAnnulerTheme, listeTheme,
                      "Selectionne le thème",
                      "Annule le changement de thème", Couleur);
    pThemes.setBackground(Couleur);
    pCThemes = new JPanel (new BorderLayout ());
    pCThemes.setBackground(Couleur);
    JPanel pNordThemes = new JPanel (new FlowLayout (FlowLayout.CENTER, 5, 5));
    pNordThemes.setBackground(Couleur);
    pCThemes.add (pNordThemes, BorderLayout.NORTH);
    pCThemes.add (pThemes, BorderLayout.CENTER);
    pEast.add (pCThemes);
    pEast.validate();
  } /* addThemePanel() */

  private void removeThemePanel ()
  {
    //Vérification que PCTheme est bien là
    if (null == pCThemes)
      return;
    butSelectTheme.removeActionListener(this);
    butAnnulerTheme.removeActionListener(this);
    pEast.remove(pCThemes);
    pEast.revalidate();
    pEast.repaint();
    pCThemes = null;
  } /* removeThemePanel() */


  /**
   * Crée le panel qui contient la gestion du lookAndFeel.
   * @return JPanel
   */
  private JPanel createPanelEast ()

  {
    JPanel pPanelEast = new JPanel (new GridLayout (3, 1));
    pPanelEast.setBackground(Couleur);
    pPanelEast.setPreferredSize (new Dimension (200, 423));
    butChangerLook = new JButton ();
    butDefautLook = new JButton ();
    listeLook = new JList ();


    JPanel pLook = util.gui.ComposantGraphique.createBoxWith2Bouton1Liste
                         ("Look / Design", butChangerLook, butDefautLook,
                          textButChangerLook, textButDefautLook, listeLook,
                          "Changer de look", "défaut", Couleur);
    //Ajout du listener pour les boutons
    butChangerLook.setActionCommand(textButChangerLook);
    butChangerLook.addActionListener(this);
    butDefautLook.setActionCommand(textButDefautLook);
    butDefautLook.addActionListener(this);

    pPanelEast.add (pLook);


    return pPanelEast;

  } /* createPanelEast() */


  /**
   * Ajoute le paneau de test un bas.
   */
  private void addPanelTestEast ()
  {
    //Vérification que le panel n'est pas deja présent.
    if (null != pBasEast)
      return;
    pBasEast = new JPanel (new BorderLayout ());
    //pBasEast.setBackground(Couleur);
    JPanel pSudBas = new JPanel ();
    //pSudBas.setBackground(Couleur);
    pSudBas.add ( (new JButton ("test bas")));
    pBasEast.add (pSudBas, BorderLayout.SOUTH);

    pEast.add (pBasEast, -1);
    pEast.validate();

  } /* addPanelTestEast() */

  private void removePanelTestEast ()
  {
    if (null == pBasEast)
      return;
    pEast.remove(pBasEast);
    pEast.validate();
    pEast.repaint();
    pBasEast = null;
  } /* removePanelTestEast() */


  /**
   * actionPerformed
   *
   * @param e ActionEvent
   */
  public void actionPerformed (ActionEvent e)
  {
    System.out.println("e : " + e.getActionCommand());
    if (e.getActionCommand().equals(textButChangerLook))
    {
      System.out.println("changer");
      //addThemePanel();
      addPanelTestEast();
    }
    else if (e.getActionCommand().equals(textButDefautLook))
    {
      System.out.println("defaut");
      //removeThemePanel();
      removePanelTestEast();
    }
  }

} // Classe Info
