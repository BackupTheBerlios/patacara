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
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Panel;
import java.awt.Insets;
import java.awt.GridBagLayout;
import util.fichier.Propriete;

public class Salon extends JPanel
{
  SalonEst salonEst;
  SalonOuest salonOuest;
  public static final Color COULEURSALONOUEST;
  public static final Color COULEURSALONEST;
  public static final Color COULEURSALON;

  static
  {
    String props = Propriete.getPropriete(Main.USER_PROPS, Main.IS_ACTIF_LOOK_AND_FEEL);
    if ((props != null) && (props.equals("true")))
    {
      COULEURSALONOUEST = new JPanel ().getBackground();
      COULEURSALONEST   = COULEURSALONOUEST;
      COULEURSALON      = COULEURSALONOUEST;
    }
    else
    {
      COULEURSALONOUEST = new Color (3, 26, 195);
      COULEURSALONEST   = new Color (3, 26, 195);
      COULEURSALON      = new Color (3, 26, 195);

    }
  }









  public Salon(ActionListener auditeur)
  {
    salonEst = new SalonEst (auditeur);
    salonOuest = new SalonOuest (auditeur);
    JPanel pNord = new JPanel (new FlowLayout (FlowLayout.CENTER, 3, 3));
    pNord.setBackground (COULEURSALONOUEST);

    setLayout (new BorderLayout ());
    add (pNord, BorderLayout.NORTH);
    add (salonEst, BorderLayout.EAST);
    add (salonOuest, BorderLayout.CENTER);
    setBackground(COULEURSALON);
  } /* Constructeur Salon */


  Component placer(GridBagLayout grille, Component composant,
            int gridx, int gridy, int gridwidth, int gridheight,
            int weightx, int weighty, int fill, int anchor)
  {
      GridBagConstraints contraintes = new GridBagConstraints();

      contraintes.gridx = gridx;
      contraintes.gridy = gridy;
      contraintes.gridwidth  = gridwidth;
      contraintes.gridheight = gridheight;
      contraintes.weightx = weightx;
      contraintes.weighty = weighty;
      contraintes.fill   = fill;

      contraintes.anchor = anchor;
      grille.setConstraints(composant, contraintes);
      return composant;
  } /* placer () */

  class Panneau extends Panel
  {

      public Panneau(Color couleur)
      {
          setBackground(couleur);
          setLayout(new BorderLayout());
      } /* Constructeur Panneau */

      public Insets getInsets()
      {
          return new Insets(1, 1, 1, 1);
      } /* getInsets () */

  } // Classe Panneau
  public Panel encadrer (Component comp, Color couleur)
  {
      Panel t1 = new Panel (new FlowLayout (FlowLayout.CENTER,0,5));
      t1.setBackground(couleur);
      Panel t2 = new Panel (new FlowLayout (FlowLayout.CENTER,0,5));
      t2.setBackground(couleur);
      Panel t3 = new Panel (new FlowLayout (FlowLayout.CENTER,5,0));
      t3.setBackground(couleur);
      Panel t4 = new Panel (new FlowLayout (FlowLayout.CENTER,5,0));
      t4.setBackground(couleur);
      Panneau p = new Panneau (Color.white);
      p.setLayout (new BorderLayout ());

      p.add(t1,BorderLayout.NORTH);
      p.add(t2,BorderLayout.SOUTH);
      p.add(comp,BorderLayout.CENTER);
      p.add(t3,BorderLayout.WEST);
      p.add(t4,BorderLayout.EAST);

      Panel p1 = new Panel (new FlowLayout (FlowLayout.CENTER,0,5));
      p1.setBackground(couleur);
      Panel p2 = new Panel (new FlowLayout (FlowLayout.CENTER,0,5));
      p2.setBackground(couleur);
      Panel p3 = new Panel (new FlowLayout (FlowLayout.CENTER,5,0));
      p3.setBackground(couleur);
      Panel p4 = new Panel (new FlowLayout (FlowLayout.CENTER,5,0));
      p4.setBackground(couleur);
      Panel pa = new Panel (new BorderLayout ());
      pa.add(p1,BorderLayout.NORTH);
      pa.add(p2,BorderLayout.SOUTH);
      pa.add(p,BorderLayout.CENTER);
      pa.add(p3,BorderLayout.WEST);
      pa.add(p4,BorderLayout.EAST);
      return pa;

  } /* Constructeur Salon */

} // Classe Salon
