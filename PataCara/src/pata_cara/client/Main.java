package pata_cara.client;

/**
 * Title:        pata_cara
 * Description:  chat en java
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Rémy Giraud
 * @version 1.0
 */



import com.jgoodies.plaf.plastic.PlasticLookAndFeel;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.util.ArrayList;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.event.KeyAdapter;
import javax.swing.JButton;
import java.awt.event.KeyEvent;
import javax.swing.JProgressBar;
import java.awt.event.ActionEvent;
import java.util.StringTokenizer;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import java.lang.reflect.InvocationTargetException;
import util.fichier.Propriete;
import util.gui.Boite;
import util.bdd.ConnectionServeurMysql;
import util.jni.windows.registre.BaseRegistreWin;
import pata_cara.client.controle.ControleurFenetre;
import pata_cara.client.controle.ControleurFenetreImp;
import javax.swing.JWindow;

public class Main extends JFrame implements ActionListener
{
    public static final int MAX_MEMBRES       = 25;
    static final String NOUVEAU        = "Nouveau";
    static final String ENTRER         = "Entrer";
    static final int    NBTOKENSAISIE  = 8;
    static final String FICHIER        = "fic.pata";
    static final String NOMBASEDONNEE  = "pata_cara";
    static final String NUMPORT        = "3312";

    public static String USER_PROPS = "user.props";
    public static String IS_ACTIF_LOOK_AND_FEEL = "actifLook";

    private Membre table[] = new Membre[MAX_MEMBRES];
    int nbMembres = 0;

    private JTextField      Pseudo;
    private JPasswordField  motPass;

    public ArrayList fenetrePataCara = new ArrayList (MAX_MEMBRES); //Liste des fênetres PataCara pour le client

    private ProgressBarIndeterminate barAttente = null;
    private JPanel panneauPere; // Le panneau qui contient tout.
    private JPanel pBar; // le panneau qui contient la barAttente.

    private ControleurFenetre controleFenetre = null;

    public static boolean isActifLooAndFeel; //Spécifie si le lookAndFeel est present.

    public Main()
    {
        super("L'univers de PataCara");
        setBounds(100, 100, 400, 300);
        //setMenuBar(barreMenus = new BarreMenus(this));

        //Mise en place controleur
        controleFenetre = new ControleurFenetreImp (this);

        panneauPere = new JPanel ();
        panneauPere.setLayout(new GridLayout (2, 1));

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e)
        {
              controleFenetre.closeFenetreMain (getThis());
         }});


        //Changement icone
        setIconImage (new ImageIcon (getClass ().getResource ("/images/Main.gif")).
                      getImage ());


        JPanel paneauCentral = new JPanel (new GridLayout(3, 2));
        JPanel pLabel1 = new JPanel ();
        pLabel1.add (new JLabel ("votre pseudo"));
        paneauCentral.add (pLabel1);

        JPanel pText = new JPanel ();
        pText.add (Pseudo = new JTextField (10));
        paneauCentral.add (pText);

        JPanel pLabel2 = new JPanel ();
        pLabel2.add (new JLabel ("mot de passe"));
        paneauCentral.add (pLabel2);

        JPanel pMotPass = new JPanel ();
        motPass = new JPasswordField (10);
        motPass.addActionListener (this);
        pMotPass.add (motPass);
        paneauCentral.add (pMotPass);

        JButton butEntrer = new JButton (ENTRER);
        butEntrer.addActionListener (this);
        butEntrer.addKeyListener(new KeyAdapter ()
          {//On a appuyé sur enter du bouton ENTRER
            public void 	keyPressed(KeyEvent e)
            {
              if (e.getKeyChar() == '\n') // e.getKeyCode == 10
                VerifPseudoWithBarDefilement ();
            }
          });
        JPanel p1 = new JPanel ();
        p1.add (butEntrer);
        paneauCentral.add (p1);
        JButton butNouveau = new JButton (NOUVEAU);
        butNouveau.addActionListener (this);
        butNouveau.addKeyListener(new KeyAdapter ()
          {//On a appuyé sur enter du bouton ENTRER
            public void 	keyPressed(KeyEvent e)
            {
              if (e.getKeyChar() == '\n') // e.getKeyCode == 10
                Saisie ();
            }
          });

        JPanel p2 = new JPanel ();
        p2.add (butNouveau);
        paneauCentral.add (p2);

        //panneau en haut qui contien le panneau central
        JPanel pHaut = new JPanel ();
        pHaut.add (paneauCentral);
        panneauPere.add (pHaut);


        pBar = new JPanel ();

        getContentPane().add (panneauPere);
        setVisible(true);
    } /* Constructeur Main */

    /**
     *
     * @return la barre de défilement associée à la fenetre.
     */
    public ProgressBarIndeterminate getBarDefilement ()
    {
      return barAttente;
    } /* getBarDefilement () */

    /**
     *
     * @return la référence a l'objet courant.
     */
    private Main getThis ()
    {
      return this;
    } /* getThis() */

    /**
     * @return le controleur de Fenetre associé à la fenetre.
     */
    public ControleurFenetre getControleFenetre ()
    {
      return controleFenetre;
    } /* controleFenetre () */

    /**
     * Initialise la bar de défilement. Alloue la place mémoire et fixe les attributs.
     */
    private void initBarDefilement ()
    {
      //barreAttente
      barAttente = new ProgressBarIndeterminate (30);
      JProgressBar bar = barAttente.getProgressBar ();
      bar.setMaximum (100);
  //        barAttente.setValue(3);
  //        barAttente.setStringPainted(true);
  //        barAttente.setString("test");
    }/* initBarDefilement () */

    public void destroyeBarDefilement ()
    {
      if (null != barAttente)
      {
        barAttente.killProgress ();
        barAttente = null;
      }
    }

    /**
     * Ajoute la barre de défilement au panneau père.
     */
    public void ajoutBarDefilement ()
    {
      panneauPere.remove(pBar);
      pBar = new JPanel ();
      pBar.add (barAttente.getProgressBar());
      panneauPere.add(pBar);
//      pBar.repaint();
//      panneauPere.repaint();
//      pBar.validate();
//      pBar.revalidate();
//      panneauPere.validate();
//      panneauPere.revalidate();
//      repaint ();
//      update(getGraphics());
//      paint (getGraphics());
      validate();
    } /* ajoutBarDefilement () */

    /**
     * Enleve la barre de défilement au panneau père.<br>
     * Ne détruit pas la barre et aucune action est faite sur la barre.
     */
    public void removeBarDefilement ()
    {
      panneauPere.remove(pBar);
      //panneauPere.add((pBar = new JPanel ()));
      //panneauPere.validate();
      //panneauPere.revalidate();
      validate ();
      repaint();
    } /* removeBarDefilement () */

    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand().equals (NOUVEAU))
           Saisie ();
         else if (e.getActionCommand().equals (ENTRER))
         {
           VerifPseudoWithBarDefilement();
         }
         else if (e.getSource().equals(motPass)) //On a appuyer sur entrer dans le motPass
           VerifPseudoWithBarDefilement();
         else
           System.out.println("action performed générer : " + e.getActionCommand() );
   } /* actionPerformed () */

   Membre rechercheMembre (String Pseudo)
   {
      int i = 0;
      for (; i < nbMembres; ++i)
      {
        if (Pseudo == table[i].pseudo) break;
      }
      return table [i];
   } /* rechercheMembre () */

    private void Saisie ()
    {
        if (nbMembres < MAX_MEMBRES)
        {
            Membre unMembre = new Membre();
            if (unMembre.saisir(this))
            {
                unMembre.addNouveauMembre ();
//System.out.println (unMembre.nom+unMembre.prenom+ unMembre.ville+ unMembre.pseudo+ unMembre.anneeNaissance+unMembre.homme+unMembre.motPas);
               table[nbMembres++] = unMembre;
               //barreMenus.ajouterNouveauMembre(unMembre.pseudo);
               new PataCara (this, unMembre.pseudo, unMembre);
            }
            else
                Boite.message(this, "Annulation", "Saisie annulée par l'utilisateur");
         }
         else
             Boite.message(this, "Annulation", "Le quota de membres est atteint");
     } /* saisie () */




     /**
      * Vérifie si le pseudo n'est pas déjà enregistré et lance PataCara s'il n'est pas enregistré.
      */
     private void VerifPseudo ()
     {
       try
       {
         String Pseu = Pseudo.getText ().trim();
         String Pass = new String (motPass.getPassword ());
             if (0 == Pseu.length () || 0 == Pass.length ())
             {
               //On désactive la bar et on la masque
               barAttente.stopProgress();

                  Boite.message (this,
                                 "Erreur",
                                 "Les champs \"pseudo\" et \"mot de passe\" doivent être renseignés");
                  barAttente.setValue(0);
                  removeBarDefilement ();
                  return;
             }
             /* récuperation des données dans la base */
             ConnectionServeurMysql obj = new ConnectionServeurMysql ();
             obj.Connexion ("jdbc:mysql://" + PataCara.HOST + ":" + NUMPORT +
                            "/" + NOMBASEDONNEE,
                            "pata_cara", "patachou");
             obj.execution ("select * from membre_pata where " + Membre.PSEUDO +
                             " = '" + Pseu + "'");
             if (0 == obj.getNbrLigne ())
             {
               //On désactive la bar et on la masque
               barAttente.stopProgress();

               System.err.println ("Aucune personne enregistrée ayant ce pseudo");
               //Affiche message echec
               Boite.message (this, "Echec", "pseudo ou mot de passe erroné");
               barAttente.setValue(0);
               removeBarDefilement ();
               return;
             }
             // on lit le tuple corespondant au pseudo pour vérifier le pass
             obj.getLigneResultat ();

             if (Pass.equals (obj.getValeurChamps (Membre.MOTPASS)))
             {
                 String nom = obj.getValeurChamps (Membre.NOM);
                 String prenom = obj.getValeurChamps (Membre.PRENOM);
                 String ville  = obj.getValeurChamps (Membre.VILLE);
                 int anneeNaissance = Integer.parseInt (obj.getValeurChamps (Membre.AGE));
                 boolean homme = (obj.getValeurChamps (Membre.SEXE).equals ("H") ? true : false);
                 String tarif = obj.getValeurChamps (Membre.GROUPE);
                 int nb = Integer.parseInt (obj.getValeurChamps (Membre.NBLOISIR));
                 String inscriptions [] = new String [nb];
                 StringTokenizer loisir = new StringTokenizer (
                                           obj.getValeurChamps (Membre.LOISIR),
                                           Membre.DELIM);
                 for (int i =0; i < nb; ++i)
                     inscriptions [i] = loisir.nextToken ();
                 //recupere les données du fichier
                 Membre m = new Membre (Pseu, nom, prenom, ville, anneeNaissance, homme, tarif,inscriptions);
                 new PataCara(this, m.pseudo, m);
                 //connection a pata_cara
                 return;
             }
             else
             {
                 //On désactive la bar et on la masque
                 barAttente.stopProgress();

                 //message d'erreur
                  Boite.message (this,
                                 "Echec",
                                 "pseudo ou mot de passe erroné");
                  barAttente.setValue(0);
                  removeBarDefilement();

//System.out.println ("passe incorrect");
                                //Affichage message erreur
                  return;
             }

        } // try
        catch (Exception exc)
        {
          if (exc.getMessage().indexOf("Cannot connect to MySQL server") != -1 )
          {
            //exc.printStackTrace();
            //On désactive la bar et on la masque
            barAttente.stopProgress();

            Boite.message (this, "Erreur Connection (bdd)",
                           "Impossible de se connecter : Le server Pata_Cara n'est pas en route");
          }
          else
          {
            //On désactive la bar et on la masque
            barAttente.stopProgress();

            Boite.message (this, "Annulation",
                           "Impossible de se connecter : Erreur inconnue");
            System.err.println ("probleme exception dans entrer club : " +
                                exc.getLocalizedMessage ());
          }
          barAttente.setValue(0);
          removeBarDefilement();

        }
   } /* VerifPseudo () */


   /**
    * Vérifie le pseudo (voir pata_cara.client.Main#VerifPseudo) et affiche
    * la barre de progression.
    * @see pata_cara.client.Main#VerifPseudo
    */
   private void VerifPseudoWithBarDefilement ()
   {
     Thread verifWithBar = new Thread ()
     {
       public void run ()
       {
         try
         {
           SwingUtilities.invokeAndWait (new Runnable ()
           {
             public void run ()
             {
               //Initialisation et Activation de la barre de défilement.
               if (null == barAttente)
               {
                 initBarDefilement ();
               }
               ajoutBarDefilement ();
               barAttente.activeProgress ();
             }
           });
         }
         catch (InvocationTargetException ex){}
         catch (InterruptedException ex){}
         VerifPseudo ();
       }
     };
     verifWithBar.start ();
   } /* VerifPseudoWithBarDefilement () */



   /**
    * Ferme la fenetre Main ainsi que toutes les fenetres PataCara.
    */
   public void closeFenetreMain ()
   {
     //Fermeture de toutes les fenetres PataCara
     for (int i = 0; i < fenetrePataCara.size(); ++i)
     {
       ((PataCara) fenetrePataCara.get(i)).closeFenetrePataCara();
     }
     //Fermeture de la fenetreMain
     dispose();
     System.exit(-1);
   } /* closeFenetreMain () */


   /**
    * Vérifie la présence des clés dans la base de registre. Si les cles ne
    * sont pas présentes et que le système est Windows alors les clés sont
    * installées.
    */
   private static void verifEtInstallCleSiNecessaire ()
   {
     if (!pata_cara.client.systray.SystrayIcone.isWindowsSystem())
       return;
     if (BaseRegistreWin.getValeurCleRepInstall() == null)
       BaseRegistreWin.installAllClePataCara();
   }

   /**
    * Ajoute une nouvelle fenetre PataCara à la liste de fenetres.
    * @param newFenetre la fenetre a ajouter.
    */
   public void addFenetrePataCara (PataCara newFenetre)
   {
     //On prévient le controleur
     controleFenetre.ajouteFenetrePataCara(newFenetre);

     //On ajoute la fenetre a l'arrayList
     fenetrePataCara.add(newFenetre);
   } /* addFenetrePataCara () */


   /**
    * Supprime une fenetre PataCara à la liste de fenetres.
    * @param removeFenetre la fenetre a supprimer.
    * @return boolean
    */
   public boolean removeFenetrePataCara (PataCara removeFenetre)
   {
     //On supprime la fenetre de l'arrayList
     return fenetrePataCara.remove(removeFenetre);
   } /* removeFenetrePataCara () */


   /**
    * Regroupe l'ensemble des actions à effectuer avant de lancer le Main ().
    */
   private static void postMain ()
   {
     //Vérification clé
     verifEtInstallCleSiNecessaire();
   } /* postMain () */



   /**
    * Affiche une fenetre de chargement avant de lancer l'application réellement.
    * @return JWindow
    */
   private static JWindow createfenetreChargement ()
   {
     JWindow fenetreChargement = new JWindow ();

     //Initialisation de la fenetre
     JLabel lTextAttente = new JLabel();
     JLabel jLabel1 = new JLabel();
     JPanel pCentre = new JPanel();
     java.awt.BorderLayout borderLayout1 = new java.awt.BorderLayout();

     ImageIcon iLogo = new ImageIcon(Main.class.getResource("/images/logo_attente.jpg"));
     lTextAttente.setFont(new java.awt.Font("Dialog", 1, 13));
     lTextAttente.setOpaque(false);
     lTextAttente.setPreferredSize(new java.awt.Dimension(260, 21));
     lTextAttente.setRequestFocusEnabled(true);
     lTextAttente.setToolTipText("Attendez s\'il vous plait");
     lTextAttente.setText("<html>Initialisation de PataCara en cours<br><center>....................</center></html>");
     lTextAttente.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
     lTextAttente.setVerticalTextPosition(javax.swing.SwingConstants.CENTER);
     lTextAttente.setBounds(new java.awt.Rectangle(68, 163, 260, 48));
     fenetreChargement.getContentPane().setBackground(java.awt.SystemColor.info);
     fenetreChargement.setEnabled(false);
     fenetreChargement.getContentPane().setLayout(borderLayout1);
     jLabel1.setIcon(iLogo);
     jLabel1.setText("");
     jLabel1.setBounds(new java.awt.Rectangle(46, 52, 305, 73));
     iLogo.setDescription("");
     pCentre.setLayout(null);
     borderLayout1.setHgap(30);
     borderLayout1.setVgap(30);
//    pCentre.setBackground(SystemColor.info);
     pCentre.setBackground(java.awt.SystemColor.info);
     pCentre.setFont(new java.awt.Font("MS Sans Serif", 0, 11));
     pCentre.setForeground(java.awt.Color.black);
     pCentre.add(lTextAttente, null);
     pCentre.add(jLabel1, null);
     fenetreChargement.getContentPane().add(pCentre, java.awt.BorderLayout.CENTER);


     //Mise en forme
     fenetreChargement.setSize (394, 265);
     java.awt.Rectangle screenRect = fenetreChargement.getGraphicsConfiguration().getBounds();
     fenetreChargement.setLocation(screenRect.x + screenRect.width /2 - fenetreChargement.getSize().width /2,
       screenRect.y + screenRect.height/2 - fenetreChargement.getSize().height/2);

     return fenetreChargement;
   } /* createfenetreChargement() */






    public static void main(String args[])
    {
      JWindow fenetreChargement = createfenetreChargement();
      fenetreChargement.setVisible(true);
      postMain();
      try
      {
        //Le fichier de propriété est présent et le look and feel est activé
        if (Propriete.isFichierPropsPresent(USER_PROPS) && Propriete.getPropriete(USER_PROPS, IS_ACTIF_LOOK_AND_FEEL).equals("true"))
        {
          //System.out.println("Fichier de propriete présent ET look and feel activé");
/*com.jgoodies.plaf.plastic.PlasticXPLookAndFeel.setMyCurrentTheme(new com.jgoodies.plaf.plastic.theme.SkyGreen ());
java.util.List l =  com.jgoodies.plaf.plastic.PlasticXPLookAndFeel.getInstalledThemes();
for (int i = 0; i < l.size(); ++i)
{
  System.out.println("themes " + i + " : " + l.get(i));
}
UIManager.setLookAndFeel (new com.jgoodies.plaf.plastic.
                          PlasticXPLookAndFeel()    );
*/
          UIManager.setLookAndFeel (new com.jgoodies.plaf.plastic.
                                    Plastic3DLookAndFeel ());
          Main main = new Main ();
PlasticLookAndFeel.setTabStyle(PlasticLookAndFeel.TAB_STYLE_METAL_VALUE);
PlasticLookAndFeel.setHighContrastFocusColorsEnabled(true);
          SwingUtilities.updateComponentTreeUI(main);
          isActifLooAndFeel = true;
          //On enleve la fenetre de chargement.
          fenetreChargement.setVisible(false);
          fenetreChargement.dispose();
        }
        //Le fichier de proprieté n'est pas présent, on le crée
        else if (!Propriete.isFichierPropsPresent(USER_PROPS))
        {
          //System.out.println("Fichier de propriete nom présent");
          UIManager.setLookAndFeel (new com.jgoodies.plaf.plastic.
                                    Plastic3DLookAndFeel ());
          Main main = new Main ();
PlasticLookAndFeel.setTabStyle(PlasticLookAndFeel.TAB_STYLE_METAL_VALUE);
PlasticLookAndFeel.setHighContrastFocusColorsEnabled(true);
          SwingUtilities.updateComponentTreeUI(main);
          Propriete.setPropriete(USER_PROPS, IS_ACTIF_LOOK_AND_FEEL, "true");
          isActifLooAndFeel = true;
          //On enleve la fenetre de chargement.
          fenetreChargement.setVisible(false);
          fenetreChargement.dispose();
        }
        else
        {
          isActifLooAndFeel = false;
          new Main ();
          //On enleve la fenetre de chargement.
          fenetreChargement.setVisible(false);
          fenetreChargement.dispose();
        }


      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    } /* main () */
} //Classe Main
