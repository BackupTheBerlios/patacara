package pata_cara.client.controle;

import java.awt.event.*;

import pata_cara.client.*;
import pata_cara.client.systray.*;


/**
 * <p>Title: ControleurFenetreImp</p>
 * <p>Description: Cette classe permet de gérer les actions sur les differentes
* fenetre de l'application PataCara (Main PataCara etc...) et controle aussi la systray.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * @author Remy GIRAU
 * @version 1.0
 */

public class ControleurFenetreImp
    implements ControleurFenetre
{
  private Main appli = null; //La reférence vers l'application Main.
  private SystrayIcone systray;
  private WindowListener listeneurFocus = null;


  /**
   * Construit le nouveau controleur de fenetre.
   * @param appli Main
   */
  public ControleurFenetreImp(Main appli)
  {
    this.appli = appli;
    if (SystrayIcone.isSystrayOperatingSystem())
    {
      systray = new SystrayIcone (appli, this);
      //On test si le systray a pu etre bien initialisé
      if (!systray.isOperationnelSystray()) //echec
        systray = null;
    }

    //Création du listeneur communs
    listeneurFocus = new controleFocusPataCara ();

    //On ajoute un listeneur sur l'appli
    appli.addWindowListener(listeneurFocus);


  } //Constructeur


  /**
   * Fonction a appeler quand une nouvelle fenetre PataCara est ouverte.
   * C'est le main qui prévient le controleur de l'ajout d'une nouvelle fenetre.
   * @param newFenetre PataCara
   */
  public void ajouteFenetrePataCara (PataCara newFenetre)
  {
    //Une fenetre PataCara s'ouvre
//System.out.println("Une fenetre patacara est ajoutée");
    //On ajoute le listeneur
    newFenetre.addWindowListener(listeneurFocus);


    if (isSystrayOperationnel())
    {
      systray.ajouteFenetrePataCara (newFenetre);//Ajout de la fenetre à la systray.
      toBackMain(appli);
      setVisible(appli, false);
      
      //On initialise le flah qui est opérationnel sur ce system
      newFenetre.initFlashWindow();
      newFenetre.startFlashFenetrePataCara();

    }
    else //Gestion sans systray
    {
        appli.setState(java.awt.Frame.ICONIFIED);
        appli.toBack();
    }
  } /* ajouteFenetrePataCara() */

  /**
   * Fonction a appeler quand une fenetre PataCara est fermée.
   * C'est la classe PataCara elle meme qui l'appelle.
   * @param oldFenetre la fenetre sur que l'on souhaite fermer.
   */
  public void closeFenetrePataCara (PataCara oldFenetre)
  {
//System.out.println("Une fenetre patacara veut etre fermée");
    //On retire le listeneur
    oldFenetre.removeWindowListener(listeneurFocus);
    //On ferme la fenetre PataCara
    oldFenetre.closeFenetrePataCara();
    //On previent le main qu'une fenetre PataCara a été fermée.
    appli.removeFenetrePataCara(oldFenetre);

    if (isSystrayOperationnel())
    {//gestion avec systray

      //On prévient qu'une fenetre a été retirée.
      setVisible(appli, true);
      systray.retireFenetrePataCara(oldFenetre);
      toFrontMain(appli);
    }
    else
    {//Gestion sans systray (Configuration des fenetres)
        /*System.out.println("normal :" + java.awt.Toolkit.getDefaultToolkit(). isFrameStateSupported(java.awt.Frame.NORMAL));
        System.out.println("iconifié : " + java.awt.Toolkit.getDefaultToolkit(). isFrameStateSupported(java.awt.Frame.ICONIFIED));*/
        appli.setExtendedState(java.awt.Frame.NORMAL);
        appli.toFront();
    }
  } /* closeFenetrePataCara() */


  /**
   * Fonction a appeler quand on souhaite fermer une fenetre Main.
   * C'est le main qui appelle cette fonction.
   * @param main Main
   */
  public void closeFenetreMain (Main main) //On clique sur fermer du main
  {
//System.out.println("Une fenetre Main veut etre fermée");
    if (isSystrayOperationnel())
    {//Il fenetre passe en arriere plan puis est masquée
      toBackMain(main);
      setVisible(main, false);
    }
    else
    {
      boolean lastVisibilite = appli.isVisible();
      setVisible(appli, true);
      toFrontMain(appli);

      if (util.gui.Boite.question (appli, "Avertissement",
                                   "Voulez vous vraiment tout quitter ?"))
      {
        quitterApplication();
      }
      if (!lastVisibilite)
      {//La fenetre main n'etai pas visible, on la recache
        toBackMain(appli);
        setVisible(appli, false);
      }
    }
  } /* closeFenetreMain() */





  /**
   * Quitte l'application proprement.
   */
  public void quitterApplication ()
  {
    //On ferme le Main qui se charge de fermer tte les fenetres patacara
    appli.closeFenetreMain ();
  }
  /* quitterApplication() */



  /**
   * Le fenetre main souhaite être affichée au premier plan.
   * @param main la fenetre qui souhaite être affichée au premier plan.
   */
  public void toFrontMain (Main main)
  {
    main.setExtendedState(java.awt.Frame.NORMAL);
    main.toFront();
  } /* toFrontMain() */

  /**
   * Le fenêtre fenetrePata souhaite être affichée au premier plan.
   * @param fenetrePata la fenetre qui souhaite être affichée au premier plan.
   */
  public void toFrontPataCara (PataCara fenetrePata)
  {
    fenetrePata.setExtendedState(java.awt.Frame.NORMAL);
    fenetrePata.toFront();
  } /* toFrontMain() */


  /**
   * Le fenêtre main souhaite être affichée en arrière plan.
   * @param main la fenetre qui souhaite être affichée en arrière plan.
   */
  public void toBackMain (Main main)
  {
    main.setExtendedState(java.awt.Frame.ICONIFIED);
    main.toBack();
  } /* toBackMain() */


  /**
   * Le fenêtre fenetrePata souhaite être affichée en arrière plan.
   * @param fenetrePata la fenetre qui souhaite être affichée en arrière plan.
   */
  public void tonBackPataCara (PataCara fenetrePata)
  {
    fenetrePata.setExtendedState(java.awt.Frame.ICONIFIED);
    fenetrePata.toBack();
  } /* tonBackPataCara() */




  /**
   *
   * @return true si le controleur possède (gère) un systray,
   * false autrement.
   */
  private boolean isSystrayOperationnel ()
  {
    return systray != null;
  } /* isSystrayOperationnel () */



  /**
   * La fenetre (PataCara) a perdu le focus.
   * @param fenetre PataCara
   */
  private void fenetrePataDesactivateWindow (PataCara fenetre)
  {
    if (!isSystrayOperationnel())
      return;
    systray.fenetrePataDesactivateWindow(fenetre);
  } /* fenetrePataDesactivateWindow() */


  /**
   * La fenetre (PataCara) a gagner le focus.
   * @param fenetre PataCara
   */
  private void fenetrePataActivateWindow (PataCara fenetre)
  {
    if (!isSystrayOperationnel())
      return;
    systray.fenetrePataActivateWindow(fenetre);
  } /* fenetrePataActivateWindow () */


  /**
   * La fenetre (Main) a perdu le focus.
   * @param fenetre PataCara
   */
  private void fenetreMainDesactivateWindow (Main fenetre)
  {
    //System.out.println ("Une fenetre Main a perdu le focus");
  } /* fenetreMainDesactivateWindow() */


  /**
   * La fenetre (Main) a gagné le focus.
   * @param fenetre PataCara
   */
  private void fenetreMainActivateWindow (Main fenetre)
  {
    //System.out.println("Une fenetre Main à gagner le focus");
  } /* fenetreMainActivateWindow() */



  /**
   * setVisible
   *
   * @param main Main
   * @param visibilite boolean
   */
  public void setVisible (Main main, boolean visibilite)
  {
    main.setVisible(visibilite);
  } /* setVisible() */

  /**
   * setVisible
   *
   * @param fenetrePata PataCara
   * @param visibilite boolean
   */
  public void setVisible (PataCara fenetrePata, boolean visibilite)
  {
    fenetrePata.setVisible(visibilite);
  } /* setVisible() */



  public void startFlashPataCara (PataCara fenetrePata)
  {

  /* startFlashPataCara () */
  }
  public void stopFlashPataCara (PataCara fenetrePata)
  {

  /* stopFlashPataCara () */
  }
  
  
  
  private class controleFocusPataCara implements WindowListener
  {
    /**
     * windowActivated
     *
     * @param e WindowEvent
     */
    public void windowActivated (WindowEvent e)
    {
      Object source = e.getSource();
      Class sourceClasse = source.getClass();
      if (sourceClasse.isAssignableFrom(PataCara.class))
      {//Une fenetre PataCara est activée
        fenetrePataActivateWindow((PataCara) source);
      }
      else if (sourceClasse.isAssignableFrom(Main.class))
      {
        fenetreMainActivateWindow((Main) source);
      }
    } /* windowActivated() */


    /**
     * windowDeactivated
     *
     * @param e WindowEvent
     */
    public void windowDeactivated (WindowEvent e)
    {
      Object source = e.getSource();
      Class sourceClasse = source.getClass();
      if (sourceClasse.isAssignableFrom(PataCara.class))
      {//Une fenetre PataCara à le focus
        fenetrePataDesactivateWindow((PataCara) source);
      }
      else if (sourceClasse.isAssignableFrom(Main.class))
      {
        fenetreMainDesactivateWindow((Main) source);
      }
    } /* windowDeactivated() */




    /**
     * windowClosed
     *
     * @param e WindowEvent
     */
    public void windowClosed (WindowEvent e)
    {
    }


    /**
     * windowClosing
     *
     * @param e WindowEvent
     */
    public void windowClosing (WindowEvent e)
    {
    }




    /**
     * windowDeiconified
     *
     * @param e WindowEvent
     */
    public void windowDeiconified (WindowEvent e)
    {
    }


    /**
     * windowIconified
     *
     * @param e WindowEvent
     */
    public void windowIconified (WindowEvent e)
    {
    }


    /**
     * windowOpened
     *
     * @param e WindowEvent
     */
    public void windowOpened (WindowEvent e)
    {
    }
  } // InerClass controleFocusPataCara


} //Classe ControleurFenetre
