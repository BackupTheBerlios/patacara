package pata_cara.client.controle;


/**
 * <p>Title: ControleurFenetre</p>
 * <p>Description: Cette classe permet de g�rer les actions sur les differentes
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
   * Fonction a appeler quand une fenetre PataCara veut etre ferm�e.
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
  * @param main la fenetre qui a demand�e a etre visible.
  * @param visibilite true si le fenetre veut �tre visible.<br>
 * false si la fenetre veut etre invisible.
   */
  public void setVisible (Main main, boolean visibilite);


  /**
   * La fenetre fenetrePata souhaite etre visible / invisible.
   *
   * @param fenetrePata la fenetre qui a demand�e a etre visible.
   * @param visibilite true si le fenetre veut �tre visible.<br>
  * false si la fenetre veut etre invisible.
   */
  public void setVisible (PataCara fenetrePata, boolean visibilite);


  /**
   * Le fenetre main souhaite �tre affich�e au premier plan.
   * @param main la fenetre qui souhaite �tre affich�e au premier plan.
   */
  public void toFrontMain (Main main);

  /**
   * Le fen�tre fenetrePata souhaite �tre affich�e au premier plan.
   * @param fenetrePata la fenetre qui souhaite �tre affich�e au premier plan.
   */
  public void toFrontPataCara (PataCara fenetrePata);


  /**
   * Le fen�tre main souhaite �tre affich�e en arri�re plan.
   * @param main la fenetre qui souhaite �tre affich�e en arri�re plan.
   */
  public void toBackMain (Main main);


  /**
   * Le fen�tre fenetrePata souhaite �tre affich�e en arri�re plan.
   * @param fenetrePata la fenetre qui souhaite �tre affich�e en arri�re plan.
   */
  public void tonBackPataCara (PataCara fenetrePata);


} // Interface ControleurFenetre
