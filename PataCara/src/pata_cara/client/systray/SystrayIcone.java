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
 * Description:  Permet de g�rer l'icone et les menus dans la systray.
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author R�my Giraud
 * @version 1.0
 */


public class SystrayIcone
{

  public static final String QUITTER_COMMANDE = "quit |";
  public static final String APROPOS_COMMANDE = "Apropos |";
  public static final String FENETRE_PRINCIPALE_COMMANDE = "fenetrePrinc |";


  public static final String CURRENT_VERSION = "Version 1.0.0 (b�ta)";

  private static final String PATH_ICONE; // Le chemin r�el de l'icone.
  private static final String DEFAULT_PATH_ICONE = "/images/Main"; //le chemin par defaut de l'icone.
  private static final String NOM_PROPS_ICONE = "systray"; //Le nom de la propri�t� pour retrouv� l'icone de l'utilisateur.

  private boolean isOperationnelSystray; //Vrai si le systray a pu etre bien initialis�.


  private Main mainAppli; // L'application principale.
  private ControleurFenetre controleFenetre; //Le controleur de fenetre.

  private SysTrayMenuIcon systrayIcon;
  private SystrayIconeListeneur systrayListeneur = null; //L'ecouteur d'�v�n�ment.
  private SubMenu sousMenuPataCara = null; // Le sous menu qui g�re les fenetres PataCara

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
                     "L'icone necessaire pour b�n�ficier d'un acc�s via la zone de notification (Systray) est introuvable.");
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

    //Cr�ation et ajout de l'�couteur
    systrayListeneur = new SystrayIconeListeneur (this);
    systrayIcon.addSysTrayMenuListener(systrayListeneur);


    //Cr�ation des menus
    createMenuSystrayIcone ();
  } // Constructeur SystrayIcone


  /**
   * Cr�e le popup menu de l'icone de la systray.
   */
  private void createMenuSystrayIcone ()
  {
    //Cr�ation effective de l'icone
    SysTrayMenu systrayMenu = new SysTrayMenu (systrayIcon);

    //ToolTip
    systrayMenu.setToolTip("PataCara : chat en Java");


    SysTrayMenuItem menu;

    //Cr�ation menu quitter
    systrayMenu.addItem((menu = new SysTrayMenuItem ("Quitter", QUITTER_COMMANDE)));
    menu.addSysTrayMenuListener(systrayListeneur);

    systrayMenu.addSeparator();

    //Cr�ation menu A propos
    systrayMenu.addItem((menu = new SysTrayMenuItem ("A propos de PataCara", APROPOS_COMMANDE)));
    menu.addSysTrayMenuListener(systrayListeneur);

    systrayMenu.addSeparator();

    //Cr�ation du sous menu qui gere les fenetres PataCara
    sousMenuPataCara = new SubMenu ("Fen�tre(s) PataCara");
    sousMenuPataCara.setEnabled(false);
    systrayMenu.addItem(sousMenuPataCara);

    //Cr�ation d'une icone d'acces � la fenetre principale
    systrayMenu.addItem((menu = new SysTrayMenuItem ("Fen�tre Principale",
                                                     FENETRE_PRINCIPALE_COMMANDE)));
    menu.addSysTrayMenuListener(systrayListeneur);
  } /* createMenuSystrayIcone () */


  /**
   * Cr�e la fenetre � Propos
   */
  public void popupApropos ()
  {
    boolean lastVisibilite = mainAppli.isVisible();
    controleFenetre.setVisible(mainAppli, true);
    controleFenetre.toFrontMain(mainAppli);
    Boite.message(mainAppli, "A propos de PataCara",
                  "<html><b>PataCara</b><br>" + CURRENT_VERSION +
                  "<br>Copyright (C) 2004 Patachou. Tous droits r�serv�s. <br><br>" +
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
   * @param newFenetre la nouvelle fenetre � ajouter.
   */
  public void ajouteFenetrePataCara (PataCara newFenetre)
  {
    if (!isOperationnelSystray()) //rien � faire
      return;
    //Ajout de la fenetre dans la liste
    listFenetrePataCara.add(newFenetre);

    //Cr�ation du sous menu
    CheckableMenuItem sousMenuFenetrePata =
      new CheckableMenuItem ("Fen�tre '" + newFenetre.getPseudo() + "'",
                             newFenetre.getPseudo());
    sousMenuFenetrePata.setState(false);
    sousMenuFenetrePata.addSysTrayMenuListener(systrayListeneur);

    //V�rification que le nombre de sous Menu n'etait pas � zero
    if (sousMenuPataCara.getItemCount() == 0)
    {//On permet de s�lectionner une fenetre.
      sousMenuPataCara.setEnabled(true);
    }

    //Ajout du sous menu
    sousMenuPataCara.addItem(sousMenuFenetrePata);

  } /* ajouteFenetrePataCara() */


  /**
   * Retire l'ancienne fenetre PataCara au sous menu de la systray.
   * @param oldFenetre l'ancienne fenetre � supprimer.
   */
  public void retireFenetrePataCara (PataCara oldFenetre)
  {
    if (!isOperationnelSystray()) //rien � faire
      return;
    //Ajout de la fenetre dans la liste
    listFenetrePataCara.remove(oldFenetre);

    //On retire le menu correspondant
    sousMenuPataCara.removeItemAt(getItemFenetrePataCara (oldFenetre));

    //V�rification que le nombre de sous Menu n'est pas � zero
    if (sousMenuPataCara.getItemCount() == 0)
    {//On ne permet plus de s�lectionner une fenetre.
      sousMenuPataCara.setEnabled(false);
    }


  } /* retireFenetrePataCara() */


  /**
   * Une fenetre PataCara vient d'etre activ�e.
   * @param fenetreActive la fenetre nouvellement activ�e.
   */
  public void fenetrePataCaraActive (PataCara fenetreActive)
  {
    if (!isOperationnelSystray()) //rien � faire
      return;
  } /* fenetrePataCaraActive () */


  /**
   * Une fenetre PataCara vient d'etre d�sactiv�e.
   * @param fenetreDesactive la fenetre nouvellement d�sactiv�e.
   */
  public void fenetrePataCaraDesactive (PataCara fenetreDesactive)
  {
    if (!isOperationnelSystray()) //rien � faire
      return;
  } /* fenetrePataCaraDesactive() */

  /**
   *
   * @return true si l'initialisation du systray c'est bien pass� (icone bien charg�).<br>
   * false autrement.
   */
  public boolean isOperationnelSystray ()
  {
    return isOperationnelSystray;
  } /* isOperationnelSystray () */



  /**
   * V�rifie que le syst�me d'exploitation accepte la Systray.
   * @return true si le syst�me accepte la systray, <br>
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
   * V�rifie que le systeme d'exploitation appartient � la famille Windows.
   * @return true si le syst�me appartient � la famille Windows.<br>
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
   * Renvoi la r�f�rence vers l'application principale.
   * @return l'application principale.
   */
  public Main getMainAppli ()
  {
    return mainAppli;
  } /* getMainAppli () */


  /**
   * Une fenetre PataCara vient d'etre d�sactiv�. On la d�check dans le sous menu de
   * la systray.
   * @param fenetre la fenetre qui vient d'etre d�sactiv�e.
   */
  public void fenetrePataDesactivateWindow (PataCara fenetre)
  {
    SysTrayMenuItem itemPata = sousMenuPataCara.getItemAt(getItemFenetrePataCara(fenetre));
//System.out.println("menu desactivate (" + fenetre.getPseudo() + ") � trouver : " + itemPata);
    if (null == itemPata)
      return;
    //On coche le menu
    ((CheckableMenuItem) itemPata).setState(false);
  } /* fenetrePataDesactivateWindow() */



  /**
   * Une fenetre PataCara vient d'etre activ�. On la check dans le sous menu de
   * la systray.
   * @param fenetre la fenetre qui vient d'etre activ�e.
   */
  public void fenetrePataActivateWindow (PataCara fenetre)
  {
    //On r�cupere le bon item.
    int index = getItemFenetrePataCara(fenetre);
    if (-1 == index) return; //Indice non valide
    SysTrayMenuItem itemPata = sousMenuPataCara.getItemAt(index);
//System.out.println("menu activate '" + fenetre.getPseudo() + ") � trouver : " + itemPata);
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
   * Renvoi l'indice de le sous menu des fenetre PataCara qui correspont � la fenetre fenetreCherche
   * @param fenetreCherche la fenetre dont on souhaite connaitre l'indice
   * @return l'indice de la fenetre dans le sous menu, -1 si pas trouv�.
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
   * @param pseudo le pseudo de la fenetre qui doit etre trouv�e.
   * @return la fenetre PataCara concern�e,<br>
   * null si pas trouv�e.
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
