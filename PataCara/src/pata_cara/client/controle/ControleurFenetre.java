package pata_cara.client.controle;


/**
 * <p>Title: ControleurFenetre</p>
 * <p>Description: Cette classe permet de gérer les actions sur les differentes
 * fenetre de l'application PataCara (Main PataCara etc...).</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * @author Remy GIRAU
 * @version 1.0
 */

import pata_cara.client.PataCara;
import pata_cara.client.Main;


public interface ControleurFenetre
{

  /**
   * Fonction a appeler quand une nouvelle fenetre PataCara est ouverte.
   * @param newFenetre PataCara
   */
  public void ajouteFenetrePataCara (PataCara newFenetre);


  /**
   * Fonction a appeler quand une fenetre PataCara veut etre fermée.
   * @param oldFenetre la fenetre sur que l'on souhaite fermer.
   */
  public void closeFenetrePataCara (PataCara oldFenetre);


  /**
   * Fonction a appeler quand on souhaite fermer une fenetre Main.
   * @param main Main
   */
  public void closeFenetreMain (Main main);


  /**
   * On souhaite quitter l'application.
   */
  public void quitterApplication ();


  /**
   * La fenetre main souhaite etre visible.
  * @param main la fenetre qui a demandée a etre visible.
  * @param visibilite true si le fenetre veut être visible.<br>
 * false si la fenetre veut etre invisible.
   */
  public void setVisible (Main main, boolean visibilite);


  /**
   * La fenetre fenetrePata souhaite etre visible / invisible.
   *
   * @param fenetrePata la fenetre qui a demandée a etre visible.
   * @param visibilite true si le fenetre veut être visible.<br>
  * false si la fenetre veut etre invisible.
   */
  public void setVisible (PataCara fenetrePata, boolean visibilite);


  /**
   * Le fenetre main souhaite être affichée au premier plan.
   * @param main la fenetre qui souhaite être affichée au premier plan.
   */
  public void toFrontMain (Main main);

  /**
   * Le fenêtre fenetrePata souhaite être affichée au premier plan.
   * @param fenetrePata la fenetre qui souhaite être affichée au premier plan.
   */
  public void toFrontPataCara (PataCara fenetrePata);


  /**
   * Le fenêtre main souhaite être affichée en arrière plan.
   * @param main la fenetre qui souhaite être affichée en arrière plan.
   */
  public void toBackMain (Main main);


  /**
   * Le fenêtre fenetrePata souhaite être affichée en arrière plan.
   * @param fenetrePata la fenetre qui souhaite être affichée en arrière plan.
   */
  public void tonBackPataCara (PataCara fenetrePata);


} // Interface ControleurFenetre
