package pata_cara.client.systray;

import pata_cara.client.Main;
import snoozesoft.systray4j.SysTrayMenuEvent;
import snoozesoft.systray4j.SysTrayMenuListener;


/**
 * Title:        pata_cara
 * Description:  G�re les �v�nements sur l'icone dans la systray.
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author R�my Giraud
 * @version 1.0
 */


public class SystrayIconeListeneur implements SysTrayMenuListener
{

  private SystrayIcone systray = null; //La r�f�rence vers l'icone
  private Main mainAppli = null; //La r�f�rence vers l'application principale.

  /**
   * Le constructeur de l'ecouteur d'�v�nement de l'icone dans la systray.
   * @param systray l'icone dans la systray.
   */
  public SystrayIconeListeneur (SystrayIcone systray)
  {
    this.systray = systray;
    this.mainAppli = systray.getMainAppli();
  } // Constructeur SystrayIconeListeneur


  /**
   * Traite l'�v�nement lorsque l'utilisateur clique gauche sur l'icone de la systray.
   *
   * @param e SysTrayMenuEvent
   */
  public void iconLeftClicked (SysTrayMenuEvent e)
  {
    System.out.println("iconLeftClicked : " + e.getActionCommand());
  } /* iconLeftClicked () */


  /**
   * Traite l'�v�nement lorsque l'utilisateur double clique sur l'icone de la systray.
   *
   * @param e SysTrayMenuEvent
   */
  public void iconLeftDoubleClicked (SysTrayMenuEvent e)
  {
    System.out.println("iconLeftDoubleClicked : " + e);
    //On affiche la fenetre principale
    systray.actionFenetrePrincipale();
  } /* iconLeftDoubleClicked () */


  /**
   * Traite l'�v�nement lorsque l'utilisateur s�lectionne un menu.
   *
   * @param e SysTrayMenuEvent
   */
  public void menuItemSelected (SysTrayMenuEvent e)
  {
    //System.out.println("MenuIemSelected : " + e.getActionCommand());
    if (e.getActionCommand().equals(SystrayIcone.APROPOS_COMMANDE))
      systray.popupApropos();
    else if (e.getActionCommand().equals(SystrayIcone.FENETRE_PRINCIPALE_COMMANDE))
      systray.actionFenetrePrincipale();
    else if (e.getActionCommand().equals(SystrayIcone.QUITTER_COMMANDE))
      systray.actionQuitter();
    else if (e.getSource().getClass().isAssignableFrom(snoozesoft.systray4j.CheckableMenuItem.class))
      systray.selectFenetrePataCara (e.getActionCommand()); //On souhaite afficher la fenetre e.getActionCommand ()

  } /* menuItemSelected () */

} // Classe SystrayIconeListeneur
