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
import java.awt.GridLayout;
import javax.swing.JLabel;
import util.fichier.Propriete;

public class MonProfil extends JPanel
{

  public static final Color COULEURINFO;

  static
  {
    String props = Propriete.getPropriete (Main.USER_PROPS,
                                           Main.IS_ACTIF_LOOK_AND_FEEL);
    if ( (props != null) && (props.equals ("true")))
    {
      COULEURINFO = new JPanel ().getBackground ();
    }
    else
    {
      COULEURINFO = new Color (3, 26, 195);

    }
  }


  public MonProfil(Membre membre)
  {
    setBackground(COULEURINFO);
    setLayout (new GridLayout (8, 1));
    add (Constr ("Nom : " + membre.nom));
    add (Constr ("Prénom : " + membre.prenom));
    add (Constr ("Année de naissance : " + membre.anneeNaissance));
    add (Constr ("Pseudo : " + membre.pseudo));
    add (Constr ("Ville : " + membre.ville));
    add (Constr ("Tarif : " + membre.tarif));
    add (Constr ("Sexe : " + (membre.homme ? "homme" : "femme")));
  }

  private static JPanel Constr (String label)
  {
    JPanel pane = new JPanel ();
    pane.setBackground(COULEURINFO);
    JLabel jlabel = new JLabel (label);
    pane.add (jlabel);
    return pane;
  }
}
