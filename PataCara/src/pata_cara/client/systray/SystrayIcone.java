package pata_cara.client.systray;

import pata_cara.client.Main;
import snoozesoft.systray4j.SysTrayMenuIcon;
import snoozesoft.systray4j.SysTrayMenu;
import snoozesoft.systray4j.SysTrayMenuItem;
import util.fichier.*;
import util.gui.*;
import snoozesoft.systray4j.SubMenu;
import pata_cara.client.controle.ControleurFenetre;
import java.util.ArrayList;
import pata_cara.client.PataCara;
import snoozesoft.systray4j.CheckableMenuItem;

/**
 * Title:        SystrayIcone
 * Description:  Permet de gérer l'icone et les menus dans la systray.
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Rémy Giraud
 * @version 1.0
 */


public class SystrayIcone
{

  public static final String QUITTER_COMMANDE = "quit |";
  public static final String APROPOS_COMMANDE = "Apropos |";
  public static final String FENETRE_PRINCIPALE_COMMANDE = "fenetrePrinc |";


  public static final String CURRENT_VERSION = "Version 1.0.0 (béta)";

  private static final String PATH_ICONE; // Le chemin réel de l'icone.
  private static final String DEFAULT_PATH_ICONE = "/images/Main"; //le chemin par defaut de l'icone.
  private static final String NOM_PROPS_ICONE = "systray"; //Le nom de la propriété pour retrouvé l'icone de l'utilisateur.

  private boolean isOperationnelSystray; //Vrai si le systray a pu etre bien initialisé.


  private Main mainAppli; // L'application principale.
  private ControleurFenetre controleFenetre; //Le controleur de fenetre.

  private SysTrayMenuIcon systrayIcon;
  private SystrayIconeListeneur systrayListeneur = null; //L'ecouteur d'événément.
  private SubMenu sousMenuPataCara = null; // Le sous menu qui gère les fenetres PataCara

  private ArrayList listFenetrePataCara = null;

  static //Initialise le chemin pour trouver l'icone.
  {
    String props = Propriete.getPropriete (Main.USER_PROPS,
                                           NOM_PROPS_ICONE);
    if ( (props != null) && (props.trim ().length() != 0))
    {
      PATH_ICONE = props.trim();
    }
    else
      PATH_ICONE = DEFAULT_PATH_ICONE;
  } // Bloc Static

  public SystrayIcone (Main appli, ControleurFenetre controleFenetre)
  {
    mainAppli = appli;
    this.controleFenetre = controleFenetre;


/*if (true)
{
  isOperationnelSystray = false;
return;
}*/

    //Recherche de l'icone de la systray
    java.net.URL urlIcone = this.getClass().
                            getResource(PATH_ICONE + SysTrayMenuIcon.getExtension());
    if (null == urlIcone)
    {
      System.out.println ("url nulle");
      Boite.message (new javax.swing.JFrame (), "Avertissement",
                     "L'icone necessaire pour bénéficier d'un accès via la zone de notification (Systray) est introuvable.");
      isOperationnelSystray = false;
      //plus rien a faire
      return;
    }
    //Le system supporte la Systray
    isOperationnelSystray = true;

    //Initialisation de la liste de Fenetre PataCara
    listFenetrePataCara = new ArrayList ();

    //Initialisation de l'image dans la systray
    systrayIcon = new SysTrayMenuIcon (urlIcone);

    //Création et ajout de l'écouteur
    systrayListeneur = new SystrayIconeListeneur (this);
    systrayIcon.addSysTrayMenuListener(systrayListeneur);


    //Création des menus
    createMenuSystrayIcone ();
  } // Constructeur SystrayIcone


  /**
   * Crée le popup menu de l'icone de la systray.
   */
  private void createMenuSystrayIcone ()
  {
    //Création effective de l'icone
    SysTrayMenu systrayMenu = new SysTrayMenu (systrayIcon);

    //ToolTip
    systrayMenu.setToolTip("PataCara : chat en Java");


    SysTrayMenuItem menu;

    //Création menu quitter
    systrayMenu.addItem((menu = new SysTrayMenuItem ("Quitter", QUITTER_COMMANDE)));
    menu.addSysTrayMenuListener(systrayListeneur);

    systrayMenu.addSeparator();

    //Création menu A propos
    systrayMenu.addItem((menu = new SysTrayMenuItem ("A propos de PataCara", APROPOS_COMMANDE)));
    menu.addSysTrayMenuListener(systrayListeneur);

    systrayMenu.addSeparator();

    //Création du sous menu qui gere les fenetres PataCara
    sousMenuPataCara = new SubMenu ("Fenêtre(s) PataCara");
    sousMenuPataCara.setEnabled(false);
    systrayMenu.addItem(sousMenuPataCara);

    //Création d'une icone d'acces à la fenetre principale
    systrayMenu.addItem((menu = new SysTrayMenuItem ("Fenêtre Principale",
                                                     FENETRE_PRINCIPALE_COMMANDE)));
    menu.addSysTrayMenuListener(systrayListeneur);
  } /* createMenuSystrayIcone () */


  /**
   * Crée la fenetre à Propos
   */
  public void popupApropos ()
  {
    boolean lastVisibilite = mainAppli.isVisible();
    controleFenetre.setVisible(mainAppli, true);
    controleFenetre.toFrontMain(mainAppli);
    Boite.message(mainAppli, "A propos de PataCara",
                  "<html><b>PataCara</b><br>" + CURRENT_VERSION +
                  "<br>Copyright (C) 2004 Patachou. Tous droits réservés. <br><br>" +
                  "Ce logiciel permet de dialoguer avec d'autres personnes sur Internet.<br> <br><br>" +
                  "Pour toutes remarques ou sugestions, contactez : <font color=#3423D1><a href=\"mailto:patachou2002@hotmail.com\">patachou2002@hotmail.com</a></font>" +
                 "</html>", "/images/logo_apropos.jpg", java.awt.FlowLayout.CENTER );
   if (!lastVisibilite)
   {//La fenetre main n'etai pas visible, on la recache
     controleFenetre.toBackMain(mainAppli);
     controleFenetre.setVisible(mainAppli, false);
   }
  } /* popupApropos () */


  /**
   * Quitte l'application.
   */
  public void actionQuitter ()
  {
    controleFenetre.quitterApplication();
  } /* actionQuitter() */


  /**
   * Affiche la fenetre principale.
   */
  public void actionFenetrePrincipale ()
  {
    controleFenetre.setVisible(mainAppli, true);
    controleFenetre.toFrontMain(mainAppli);
  } /* actionFenetrePrincipale() */


  /**
   * Ajoute la nouvelle fenetre PataCara au sous menu de la systray.
   * @param newFenetre la nouvelle fenetre à ajouter.
   */
  public void ajouteFenetrePataCara (PataCara newFenetre)
  {
    if (!isOperationnelSystray()) //rien à faire
      return;
    //Ajout de la fenetre dans la liste
    listFenetrePataCara.add(newFenetre);

    //Création du sous menu
    CheckableMenuItem sousMenuFenetrePata =
      new CheckableMenuItem ("Fenêtre '" + newFenetre.getPseudo() + "'",
                             newFenetre.getPseudo());
    sousMenuFenetrePata.setState(false);
    sousMenuFenetrePata.addSysTrayMenuListener(systrayListeneur);

    //Vérification que le nombre de sous Menu n'etait pas à zero
    if (sousMenuPataCara.getItemCount() == 0)
    {//On permet de sélectionner une fenetre.
      sousMenuPataCara.setEnabled(true);
    }

    //Ajout du sous menu
    sousMenuPataCara.addItem(sousMenuFenetrePata);

  } /* ajouteFenetrePataCara() */


  /**
   * Retire l'ancienne fenetre PataCara au sous menu de la systray.
   * @param oldFenetre l'ancienne fenetre à supprimer.
   */
  public void retireFenetrePataCara (PataCara oldFenetre)
  {
    if (!isOperationnelSystray()) //rien à faire
      return;
    //Ajout de la fenetre dans la liste
    listFenetrePataCara.remove(oldFenetre);

    //On retire le menu correspondant
    sousMenuPataCara.removeItemAt(getItemFenetrePataCara (oldFenetre));

    //Vérification que le nombre de sous Menu n'est pas à zero
    if (sousMenuPataCara.getItemCount() == 0)
    {//On ne permet plus de sélectionner une fenetre.
      sousMenuPataCara.setEnabled(false);
    }


  } /* retireFenetrePataCara() */


  /**
   * Une fenetre PataCara vient d'etre activée.
   * @param fenetreActive la fenetre nouvellement activée.
   */
  public void fenetrePataCaraActive (PataCara fenetreActive)
  {
    if (!isOperationnelSystray()) //rien à faire
      return;
  } /* fenetrePataCaraActive () */


  /**
   * Une fenetre PataCara vient d'etre désactivée.
   * @param fenetreDesactive la fenetre nouvellement désactivée.
   */
  public void fenetrePataCaraDesactive (PataCara fenetreDesactive)
  {
    if (!isOperationnelSystray()) //rien à faire
      return;
  } /* fenetrePataCaraDesactive() */

  /**
   *
   * @return true si l'initialisation du systray c'est bien passé (icone bien chargé).<br>
   * false autrement.
   */
  public boolean isOperationnelSystray ()
  {
    return isOperationnelSystray;
  } /* isOperationnelSystray () */



  /**
   * Vérifie que le système d'exploitation accepte la Systray.
   * @return true si le système accepte la systray, <br>
   *  false autrement.
   */
  public static boolean isSystrayOperatingSystem ()
  {

    String os = System.getProperty ("os.name");
    if (os.toLowerCase().indexOf("windows") != -1)
      return true;
    return false;
  } /* isSystrayOperatingSystem () */


  /**
   * Vérifie que le systeme d'exploitation appartient à la famille Windows.
   * @return true si le système appartient à la famille Windows.<br>
   * false autrement.
   */
  public static boolean isWindowsSystem ()
  {
    String os = System.getProperty ("os.name");
    if (os.toLowerCase().indexOf("windows") != -1)
      return true;
    return false;
  } /* isWindowsSystem ()*/

  /**
   * Renvoi la référence vers l'application principale.
   * @return l'application principale.
   */
  public Main getMainAppli ()
  {
    return mainAppli;
  } /* getMainAppli () */


  /**
   * Une fenetre PataCara vient d'etre désactivé. On la décheck dans le sous menu de
   * la systray.
   * @param fenetre la fenetre qui vient d'etre désactivée.
   */
  public void fenetrePataDesactivateWindow (PataCara fenetre)
  {
    SysTrayMenuItem itemPata = sousMenuPataCara.getItemAt(getItemFenetrePataCara(fenetre));
//System.out.println("menu desactivate (" + fenetre.getPseudo() + ") à trouver : " + itemPata);
    if (null == itemPata)
      return;
    //On coche le menu
    ((CheckableMenuItem) itemPata).setState(false);
  } /* fenetrePataDesactivateWindow() */



  /**
   * Une fenetre PataCara vient d'etre activé. On la check dans le sous menu de
   * la systray.
   * @param fenetre la fenetre qui vient d'etre activée.
   */
  public void fenetrePataActivateWindow (PataCara fenetre)
  {
    //On récupere le bon item.
    int index = getItemFenetrePataCara(fenetre);
    if (-1 == index) return; //Indice non valide
    SysTrayMenuItem itemPata = sousMenuPataCara.getItemAt(index);
//System.out.println("menu activate '" + fenetre.getPseudo() + ") à trouver : " + itemPata);
    if (null == itemPata)
      return;
    //On coche le menu
    ((CheckableMenuItem) itemPata).setState(true);
  } /* fenetrePataActivateWindow() */


  /**
   * Affiche la fenetre PataCara qui a pour pseudo pseudo.
   * @param pseudo le pseudo qui concerne la fenetre PataCara a afficher.
   */
  public void selectFenetrePataCara (String pseudo)
  {
    PataCara fenetre = getFenetrePataCara(pseudo);
    if (null == fenetre)
      return;
    //Il faut rendre visible la fenetre PataCara fenetre
    controleFenetre.setVisible(fenetre, true);
    controleFenetre.toFrontPataCara(fenetre);
  } /* selectFenetrePataCara() */



  /**
   * Renvoi l'indice de le sous menu des fenetre PataCara qui correspont à la fenetre fenetreCherche
   * @param fenetreCherche la fenetre dont on souhaite connaitre l'indice
   * @return l'indice de la fenetre dans le sous menu, -1 si pas trouvé.
   */
  private int getItemFenetrePataCara (PataCara fenetreCherche)
  {
    int taille = sousMenuPataCara.getItemCount();
    String pseudo = fenetreCherche.getPseudo();
    for (int i = 0; i < taille; ++i)
    {
      if (sousMenuPataCara.getItemAt(i).getActionCommand().equals(pseudo))
        return i;
    }
    return -1;
  } /* getItemFenetrePataCara() */


  /**
   * Cherche la fenetre PataCara qui correspond au pseudo pseudo.
   * @param pseudo le pseudo de la fenetre qui doit etre trouvée.
   * @return la fenetre PataCara concernée,<br>
   * null si pas trouvée.
   */
  private PataCara getFenetrePataCara (String pseudo)
  {
    int taille = listFenetrePataCara.size();
    PataCara fenetre;
    for (int i = 0; i < taille; ++i)
    {
      fenetre = (PataCara)listFenetrePataCara.get(i);
      if (fenetre.getPseudo().equals(pseudo))
        return fenetre;
    }
    return null;
  } /* getFenetrePataCara() */


} // Classe SystrayIcone
