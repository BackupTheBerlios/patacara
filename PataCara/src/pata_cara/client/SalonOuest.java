package pata_cara.client;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JButton;

/**
 * Title:        pata_cara
 * Description:  chat en java
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Rémy Giraud
 * @version 1.0
 */



public class SalonOuest extends JPanel{
    JTextField textField;
    JTextPane textPane;
    JComboBox ListChoixCouleur;
    JComboBox ListChoixStyle;

    static final String newline = "\n";
    String Nom;
    static final String SALON = " Salon ";

    private GestionLigneTextPane gestionLigneTextPane; //Le gestionnaire pour le retrait de ligne dans le JTextPane



  public SalonOuest (ActionListener auditeur)
  {
      super ();
//System.out.println ("on est entrer dans le constructeur");
      this.Nom = SALON;
      setBackground(Salon.COULEURSALONOUEST);
      textField = new JTextField(34);
      textField.setSelectedTextColor(Color.blue);
      textField.setSelectionColor(Color.pink);
      textField.setCaretColor(Color.black);
      textField.setDisabledTextColor(Color.black);
      textField.setForeground(Color.black);
      //textField.setRequestFocusEnabled(true);
      //textField.setLayout(new FlowLayout ());
      textField.addActionListener(auditeur);
      textField.setBorder (BorderFactory.createLoweredBevelBorder ());

      textPane = Dialogue.createTextPane ();
      gestionLigneTextPane = new GestionLigneTextPane (textPane);
      JScrollPane scrollPane = new JScrollPane(textPane,
                                     JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                     JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      scrollPane.setBorder (BorderFactory.createLoweredBevelBorder ());


      JPanel pBas = new JPanel ();
      pBas.setLayout(new GridLayout (2, 1));
      pBas.setBackground (Salon.COULEURSALONOUEST);

      JPanel pTextField = new JPanel ();
      pTextField.setLayout (new BorderLayout (0,0));
      pTextField.setBackground (Salon.COULEURSALONOUEST);
      JPanel pTFh = new JPanel (new FlowLayout (FlowLayout.CENTER, 3, 3));
      pTFh.setBackground (Salon.COULEURSALONOUEST);
      JPanel pTFb = new JPanel (new FlowLayout (FlowLayout.CENTER, 3, 3));
      pTFb.setBackground (Salon.COULEURSALONOUEST);
      pTextField.add (textField, BorderLayout.CENTER);
      pTextField.add (pTFh, BorderLayout.NORTH);
      pTextField.add (pTFb, BorderLayout.SOUTH);

      JPanel pEstListe = new JPanel (new FlowLayout (FlowLayout.LEFT,5, 5));
      pEstListe.setBackground (Salon.COULEURSALONOUEST);

//creation des listes de choix couleur style
      ListChoixCouleur = new JComboBox (Dialogue.Couleur);
      ListChoixCouleur.setFont (new Font ("Dialog", Font.BOLD, 10));
      //ListChoixCouleur.setBorder (BorderFactory.createRaisedBevelBorder ());
      ListChoixCouleur.setBorder (BorderFactory.createLoweredBevelBorder ());
      ListChoixStyle = new JComboBox (Dialogue.ListStyle);
      ListChoixStyle.setFont (new Font ("Dialog", Font.BOLD, 10));
      ListChoixStyle.setBorder (BorderFactory.createLoweredBevelBorder ());
      pEstListe.add (ListChoixCouleur);
      pEstListe.add (ListChoixStyle);

      JButton bEnv = new JButton (Dialogue.Envoie);
      bEnv.addActionListener(auditeur);
bEnv.setBorder (BorderFactory.createRaisedBevelBorder ());
      JButton bTer = new JButton (Dialogue.Terminer);

      JPanel pOuestBut = new JPanel (new FlowLayout (FlowLayout.RIGHT, 5,5));
      pOuestBut.setBackground(Salon.COULEURSALONOUEST);
      pOuestBut.add(bEnv);

     JPanel p2ligne = new JPanel (new GridLayout (1,2));
     p2ligne.setBackground (Salon.COULEURSALONOUEST);
     p2ligne.add (pEstListe);
     p2ligne.add (pOuestBut);
      pBas.add (pTextField);
      pBas.add (p2ligne);
//fin du panneau bas

      JPanel pTextOuest = new JPanel (new FlowLayout (FlowLayout.CENTER, 2, 2));
      pTextOuest.setBackground(Salon.COULEURSALONOUEST);
      JPanel pTextEst = new JPanel (new FlowLayout (FlowLayout.CENTER, 2, 2));
      pTextEst.setBackground(Salon.COULEURSALONOUEST);

      JPanel pBasForme = new JPanel (new BorderLayout (0,0));
      pBasForme.setBackground(Salon.COULEURSALONOUEST);
      pBasForme.add(pTextOuest, BorderLayout.WEST);
      pBasForme.add(pTextEst, BorderLayout.EAST);
      pBasForme.add(pBas, BorderLayout.CENTER);


      JPanel pEstDial = new JPanel (new FlowLayout (FlowLayout.CENTER, 2, 2));
      pEstDial.setBackground(Salon.COULEURSALONOUEST);
      JPanel pOuestDial = new JPanel (new FlowLayout (FlowLayout.CENTER, 2, 2));
      pOuestDial.setBackground(Salon.COULEURSALONOUEST);

      JPanel pFin = new JPanel (new BorderLayout ());
      pFin.setBackground (Salon.COULEURSALONOUEST);

      pFin.add (scrollPane, BorderLayout.CENTER);
      pFin.add (pBasForme, BorderLayout.SOUTH);
      pFin.add (pEstDial, BorderLayout.EAST);
      pFin.add (pOuestDial, BorderLayout.WEST);


      pFin.setBorder ( BorderFactory.createCompoundBorder (
                BorderFactory.createTitledBorder ("vous etes dans le salon"),
                /*BorderFactory.createTitleBorder (BorderFactory.createEtchedBorder(),
                                                 "Vous êtes dans le salon",
                                                 TitledBorder.DEFAULT_JUSTIFICATION,
                                                 TitledBorder.DEFAULT_POSITION,
                                                 new Font ("", 8, Font.PLAIN),
                                                 Color.blue),*/
                BorderFactory.createEmptyBorder (0, 0, 0, 0 )));

      setLayout (new BorderLayout ());
      JPanel pHaut = new JPanel (new FlowLayout (FlowLayout.CENTER, 2,2));
      pHaut.setBackground(Color.lightGray);
      //add (pHaut, BorderLayout.NORTH);
      add (pFin,  BorderLayout.CENTER);

  }/* Constructeur SalonOuest */

  /**
   *
   * @return le gestionnaire des lignes du textPane
   */
  public GestionLigneTextPane getGestionLigneTextPane ()
  {
    return gestionLigneTextPane;
  } /* getGestionLigneTextPane() */


} // Classe SalonOuest
