package pata_cara.client;

import java.util.ArrayList;
import javax.swing.text.JTextComponent;
import javax.swing.text.Document;
import javax.swing.text.BadLocationException;

/**
 * <p>Title: GestionLigneTextPane.java</p>
 * <p>Description: Permet d'enlever les lignes du JTextPane (ou JTextComponent) </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Remy GIRAUD
 * @version 1.0
 */

public class GestionLigneTextPane
{
  private ArrayList longueurLigneTextPaneDelete = new ArrayList (); /* mémorise la taille de chaque ligne pas encore détuite */
  private int nbrLigneCouranteTextPane; //Le nombre de ligne courant dans le textpane
  private static final int NBR_LIGNE_MAX = 75; //le nombre de ligne a pas dépasser.

  private JTextComponent textComponent = null; //le text component concerné.

  public GestionLigneTextPane(JTextComponent textComponent)
  {
    this.textComponent = textComponent;
    nbrLigneCouranteTextPane = 0;
  } // Constructeur GestionLigneTextPane ()


  /**
   * Ajoute la longueur d'une nouvelle ligne en vu de la retirer plus tard.
   * @param longeurLigne la longueur de la ligne
   */
  public void ajouteLongueurLigneTextPane (int longeurLigne)
  {
    longueurLigneTextPaneDelete.add (new Integer (longeurLigne));
    ++nbrLigneCouranteTextPane;
  } /* ajouteLongueurLigneTextPane() */


  public void verifNbrLigneTextPane ()
  {
    if (nbrLigneCouranteTextPane >= NBR_LIGNE_MAX) //trop de lignes il fau en supprimer une
    {
/*        SwingUtilities.invokeLater (new Runnable () {

          public void run ()
          {
*/            try
            {
              Document d = textComponent.getDocument();
              Integer len = ((Integer) longueurLigneTextPaneDelete.get(0));
              d.remove (0, len.intValue ());
              miseAjourLongueurTextPane ();
            }
            catch (BadLocationException ex)
            {
              System.out.println ("verifnbrligne");
              ex.printStackTrace ();
            }
//          }

//          });

    }
  } /* verifNbrLigneTextPane() */


  /**
   * miseAjourLongueurTextPane
   *
   */
  private void miseAjourLongueurTextPane ()
  {
    //suppression 1er element
    Integer len = (Integer) longueurLigneTextPaneDelete.remove (0);
    Integer tmp;
    for (int i = 0; i < longueurLigneTextPaneDelete.size (); ++i)
    {
      tmp = (Integer) longueurLigneTextPaneDelete.get (i);
      tmp = new Integer (tmp.intValue () - len.intValue ());
      longueurLigneTextPaneDelete.set (i, tmp);
    }
  } /* miseAjourLongueurTextPane () */




} // Classe GestionLigneTextPane
