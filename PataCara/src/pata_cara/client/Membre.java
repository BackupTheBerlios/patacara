package pata_cara.client;

/**
 * Title:        pata_cara
 * Description:  chat en java
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Rémy Giraud
 * @version 1.0
 */




import java.awt.event.ActionListener;
import java.awt.TextArea;
import java.awt.Checkbox;
import java.awt.event.WindowAdapter;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.CheckboxGroup;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.event.WindowEvent;
import java.util.StringTokenizer;
import javax.swing.JDialog;
import javax.swing.JFrame;
import util.gui.ChaineBornee;
import util.gui.NombreBorne;
import util.gui.Boite;
import util.bdd.ConnectionServeurMysql;


public class Membre
{
    /* variable correspondant au nom des champs de la base de données */
    public static final String DELIM    = " ";
    static final String NOM      = "nom";
    static final String PRENOM   = "prenom";
    static final String VILLE    = "ville";
    static final String PSEUDO   = "pseudo";
    static final String AGE      = "age";
    static final String SEXE     = "sexe";
    static final String LOISIR   = "loisir";
    static final String GROUPE   = "groupe";
    static final String MOTPASS  = "password";
    static final String NBLOISIR = "nbLoisir";

    /* données membre de la classe */
    String nom;
    String prenom;
    String ville;
    String pseudo;
    int anneeNaissance;
    boolean homme;
    String tarif;
    String inscriptions[];
    String motPas;


    static final String OK         = "     OK     ";
    static final String ANNULER    = " Annuler ";

    private boolean succesMask1;
    private boolean succesMask2;


   public Membre () {}


   public Membre (
       String pseudo,
       String nom,
       String prenom,
       String ville,
       int anneeNaissance,
       boolean homme,
       String tarif,
       String inscriptions[])
    {
        this.nom             = nom;
        this.prenom          = prenom;
        this.ville           = ville;
        this.pseudo          = pseudo;
        this.anneeNaissance  = anneeNaissance;
        this.homme           = homme;
        this.tarif           = tarif;
        this.inscriptions    = inscriptions;
    } // Membre ()

    /* Insertion dans la base d'un nouveau membre */
    void addNouveauMembre ()
    {
        String requeteInsertNewMembre =
        "INSERT INTO `membre_pata` (`" + NOM      + "`, `"
                                       + PRENOM   + "`, `"
                                       + PSEUDO   + "`, `"
                                       + MOTPASS  + "`, `"
                                       + AGE      + "`, `"
                                       + VILLE    + "`, `"
                                       + GROUPE   + "`, `"
                                       + SEXE     + "`, `"
                                       + NBLOISIR + "`, `"
                                       + LOISIR   + "`) VALUES ('";

        requeteInsertNewMembre +=
                     this.nom         + "', '" + this.prenom  + "', '" +
                     this.pseudo      + "', '" +
                     this.motPas      + "', '" + this.anneeNaissance + "', '" +
                     this.ville       + "', '" +
                     this.tarif       + "', '" +
                     (homme ? "H"
                              : "F")  + "', '" + inscriptions.length
                     + "', '";

        if (0 != inscriptions.length)
            requeteInsertNewMembre += inscriptions [0];
        for (int i=1; i < inscriptions.length; ++i)
        {
            requeteInsertNewMembre += DELIM + inscriptions [i];
        }
        requeteInsertNewMembre += "')";
//System.out.println(requeteInsertNewMembre);

         try
         {
             /* connexion a la base mysql */
             ConnectionServeurMysql obj = new ConnectionServeurMysql ();
             obj.Connexion ("jdbc:mysql://" + PataCara.HOST + ":" + Main.NUMPORT +
                            "/" + Main.NOMBASEDONNEE,
                            "pata_cara", "patachou");

              obj.execution (requeteInsertNewMembre);
              obj.fermeture ();
           } // try
           catch (java.sql.SQLException E)
           {
                System.err.println ("probleme exception SQL dans addNouveauMembre () : " + E);
           }
           catch (Exception exc)
           {
                System.err.println ("probleme exception dans addNouveauMembre() : " + exc);
           }

    }//addNouveauMembre ()


    /**
     *
     * <p>Title: MasqueSaisie </p>
     * <p>Description: Permet d'afficher la liste des renseignements à completer par l'utilisateur. <br/>
     * on demandera notament le nom prenom sexe loisir etc... </p>
     * <p>Copyright: Copyright (c) 2004</p>
     * <p>Company: </p>
     * @author not attributable
     * @version 1.0
     */
    class MasqueSaisie extends JDialog implements ActionListener
    {
        ChaineBornee saisieNom, saisiePrenom, saisiePseudo, saisieVille;
        NombreBorne saisieAnnee;
        TextArea saisieAdresse;
        Checkbox saisirHomme, saisirFemme;
        java.awt.List saisirTarif, saisirSports;


        /**
         * Le constructeur.<br>
         * Affiche une boite de dialogue pour récuperer des renseignements sur l'utilisateur.
         * @param cadre Frame
         */
        MasqueSaisie(JFrame cadre)
        {
            super(cadre, "Inscription à PataCara", true);
            addWindowListener (new WindowAdapter ()
            {
                public void windowClosing (WindowEvent e)
                {
                    succesMask1 = false;
                    dispose ();
                 }
            });

            setBackground(Color.lightGray);

            saisieNom    = new ChaineBornee(this, "Nom", 3, 20, 20);
            saisiePrenom = new ChaineBornee(this, "Prenom", 3, 20, 20);
            saisieAnnee  = new NombreBorne (this, "Age", 0, 200, 20);
            saisieVille  = new ChaineBornee(this, "Ville", 3, 20, 20);

            //Création des boutons
            JButton bOk = new JButton(OK);
            bOk.addActionListener(this);
            bOk.addKeyListener(new KeyAdapter ()
            {//On a appuyé sur enter du bouton Ok
              public void 	keyPressed(KeyEvent e)
              {
                if (e.getKeyChar() == '\n') // e.getKeyCode == 10
                  actionOk ();
              }
            });

            JButton bAnnul = new JButton(ANNULER);
            bAnnul.addActionListener(this);
            bAnnul.addKeyListener(new KeyAdapter ()
            { //On a appuyé sur enter du bouton annuler
              public void keyPressed (KeyEvent e)
              {
                if (e.getKeyChar () == '\n') // e.getKeyCode == 10
                  actionAnnuler ();
              }
            });


            //saisieAdresse = new TextArea("", 3, 20, TextArea.SCROLLBARS_VERTICAL_ONLY);

            CheckboxGroup groupe = new CheckboxGroup();
            saisirHomme = new Checkbox("Homme");
            saisirHomme.setCheckboxGroup(groupe);
            saisirFemme = new Checkbox("Femme");
            saisirFemme.setCheckboxGroup(groupe);

            saisirTarif = new java.awt.List(6, false);
            saisirTarif.add("enfant");
            saisirTarif.add("étudiant");
            saisirTarif.add("groupe");
            saisirTarif.add("retraité");
            saisirTarif.add("plein");

            saisirSports = new java.awt.List(6, true);
            saisirSports.add("basket");
            saisirSports.add("volley");
            saisirSports.add("hand");
            saisirSports.add("tennis");
            saisirSports.add("football");
            saisirSports.add("rugby");
            saisirSports.add("cheval");
            saisirSports.add("course");
            saisirSports.add("natation");
            saisirSports.add("tir");

            JPanel panneauTextes = new JPanel();
            panneauTextes.setLayout(new GridLayout(2, 2));
            panneauTextes.add(saisieNom);
            panneauTextes.add(saisiePrenom);
            panneauTextes.add(saisieAnnee);
                        panneauTextes.add(saisieVille);

            /*Panel panneauAdresse = new Panel();
            panneauAdresse.add(new Label("Ville"));
            panneauAdresse.add(saisieAdresse);*/

            JPanel panneauHaut = new JPanel();
            panneauHaut.add(panneauTextes);
            //panneauHaut.add(panneauAdresse);

            JPanel panneauMilieu = new JPanel();
            panneauMilieu.add(saisirTarif);
            panneauMilieu.add(saisirSports);

            JPanel panneauBoutons = new JPanel();
            panneauBoutons.add(saisirHomme);
            panneauBoutons.add(saisirFemme);
            panneauBoutons.add(bOk);
            panneauBoutons.add(bAnnul);

            getContentPane().setLayout(new GridLayout(3, 1));
            getContentPane().add(panneauHaut);
            getContentPane().add(panneauMilieu);
            getContentPane().add(panneauBoutons);

            pack();
            setLocation(cadre.getX() + 50, cadre.getY() +50);
            setVisible(true);
        }





        /**
         * Action effectuer lorsqu'on clique sur le bouton ok
         */
        private void actionOk ()
        {
          if ( (nom = ControleNonEspace (saisieNom.getText ()/*, "nom"*/))
                      == null)
          {
            Boite.message (this,
               "Erreur de saisie",
               "Le champ " + "nom" +
               " ne doit pas comporter d'espaces !");

            return;
          }

          if (0 == nom.length ())
          {
            Boite.message (this, "Erreur de saisie",
                           "Le champ Nom n'est pas saisi !");
            return;
          }
          if ( (prenom = ControleNonEspace (saisiePrenom.getText ()/*, "prenom"*/))
                         == null)
          {
            Boite.message (this,
               "Erreur de saisie",
               "Le champ " + "prénom" +
               " ne doit pas comporter d'espaces !");

            return;
          }

          if (0 == prenom.length ())
          {
            Boite.message (this, "Erreur de saisie",
                           "Le champ Prenom n'est pas saisi !");
            return;
          }

          ville = saisieVille.getText ();
          if (0 == ville.length ())
          {
            Boite.message (this, "Erreur de saisie",
                           "Le champ ville n'est pas saisi !");
            return;
          }
          else
          {

            ville = ControleNonEspace (ville /*, "ville"*/);
            if (null == ville)
            {
              Boite.message (this,
                             "Erreur de saisie",
                             "Le champ " + "ville" +
                             " ne doit pas comporter d'espaces !");
              return;
            }
          }

          anneeNaissance = saisieAnnee.getValue ();
          if (anneeNaissance == 0)
          {
            Boite.message (this, "Erreur de saisie",
                           "Le champ age n'est pas saisi !");
            return;
          }

          homme = saisirHomme.getState ();
          if (! (saisirHomme.getState () || saisirFemme.getState ()))
          {
            Boite.message (this, "Erreur de saisie",
                           "Le sexe n'est pas déterminé !");
            return;
          }
          tarif = saisirTarif.getSelectedItem ();
          if (null == tarif)
          {
            Boite.message (this, "Erreur de saisie",
                           "Le Tarif n'est pas saisi !");
            return;
          }

          inscriptions = saisirSports.getSelectedItems ();
          if (0 == inscriptions.length)
          {
            Boite.message (this, "Erreur de saisie",
                           "Les sports ne sont pas saisis !");
            return;
          }

          succesMask1 = true;
          dispose ();
        } /* actionOk () */


        /**
         * Action effectuer lorsqu'on clique sur le bouton annuler
         */
        private void actionAnnuler ()
        {
          succesMask1 = false;
          dispose ();
        }


        public void actionPerformed (ActionEvent e)
        {
          if (e.getActionCommand ().equals (OK))
          {
            actionOk();
          }

          else
          if (e.getActionCommand ().equals (ANNULER))
          {
            actionAnnuler();
          }
        }
    }



    class MasqueSaisie2 extends JDialog
        implements ActionListener
    {

      JPasswordField ConfirmPass;
      JPasswordField motPass;
      JTextField Pseudo;

      MasqueSaisie2 (JFrame cadre)
      {
        super (cadre, "Identification", true);
        addWindowListener (new WindowAdapter ()
        {
          public void windowClosing (WindowEvent e)
          {
            succesMask2 = false;
            dispose ();
          }
        });

        JPanel p = new JPanel ();
        JPanel paneauCentral = new JPanel (new GridLayout (4, 2));

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

        JPanel pLabel3 = new JPanel ();
        pLabel3.add (new JLabel ("confirmation"));
        paneauCentral.add (pLabel3);

        JPanel pConfirmPass = new JPanel ();
        ConfirmPass = new JPasswordField (10);
        ConfirmPass.addActionListener (this);
        pConfirmPass.add (ConfirmPass);
        paneauCentral.add (pConfirmPass);

        //création des boutons
        JButton butEntrer = new JButton (OK);
        butEntrer.addActionListener (this);
        butEntrer.addKeyListener(new KeyAdapter ()
          {//On a appuyé sur enter du bouton Ok
            public void 	keyPressed(KeyEvent e)
            {
              if (e.getKeyChar() == '\n') // e.getKeyCode == 10
                actionOk ();
            }
          });

        JPanel p1 = new JPanel ();
        p1.add (butEntrer);
        paneauCentral.add (p1);

        JButton butNouveau = new JButton (ANNULER);
        butNouveau.addActionListener (this);
        butNouveau.addKeyListener(new KeyAdapter ()
          {//On a appuyé sur enter du bouton ANNULER
            public void 	keyPressed(KeyEvent e)
            {
              if (e.getKeyChar() == '\n') // e.getKeyCode == 10
                actionAnnuler();
            }
          });

        JPanel p2 = new JPanel ();
        p2.add (butNouveau);
        paneauCentral.add (p2);
        p.add (paneauCentral);
        getContentPane().add (p);
        pack ();
        setLocation (cadre.getX () + 50, cadre.getY () + 50);
        setVisible (true);

      } //Constructeur


      /**
       * Action effectuer lorsqu'on clique sur le bouton annuler
       */
      private void actionAnnuler ()
      {
        succesMask2 = false;
        dispose ();
      }


      /**
       * Action effectuer lorsqu'on clique sur le bouton ok
       */
      private void actionOk ()
      {
        String p1 = new String (ConfirmPass.getPassword ());
        motPas = ControleNonEspace (new String (motPass.getPassword ())/*,
                                    "mot de passe"*/);
        if (motPas == null)
        {
          Boite.message (this,
                         "Erreur de saisie",
                         "Le champ " + "mot de passe" +
                         " ne doit pas comporter d'espaces !");

          return;
        }
        pseudo = ControleNonEspace (Pseudo.getText ()/*, "pseudo"*/);
        if (pseudo == null)
        {
          Boite.message (this,
                         "Erreur de saisie",
                         "Le champ " + "pseudo" +
                         " ne doit pas comporter d'espaces !");
          return;
        }
        if (0 == pseudo.length () || 0 == motPas.length ())
        {
          Boite.message (this,
                         "Erreur",
                         "Vous devez saisir les champs");
          return;
        }

        if (!p1.equals (motPas))
        {
          Boite.message (this,
                         "Erreur",
                         "problème dans votre mot de passe");
          return;
        }
        //contrôle que le pseudo n'est pas deja dans la base
        try
        {
          /* connexion a la base mysql */
          ConnectionServeurMysql obj = new ConnectionServeurMysql ();
          obj.Connexion ("jdbc:mysql://" + PataCara.HOST + ":" +
                         Main.NUMPORT + "/" + Main.NOMBASEDONNEE,
                         "pata_cara", "patachou");

          /* Controle si la personne n'existe pas deja dans la base */
          obj.execution ("select * from membre_pata where " + Membre.PSEUDO +
                         " = '" + pseudo + "'");
          if (0 != obj.getNbrLigne ())
          {
            System.out.println ("utilisation d'un pseudo deja existant");
            //Affiche message echec
            Boite.message (this,
                           "Erreur",
                           "Le pseudo : " + pseudo +
                           " est déjà pris");
            return;
          }
        } // try
        catch (java.sql.SQLException E)
        {
          System.err.println ("probleme exception SQL dans saisirMasque2 () : " +
                              E);
          Boite.message (this,
                         "Erreur",
                         "Probleme accés base de données !");
          succesMask2 = false;
          dispose ();
          return;
        }
        catch (Exception exc)
        {
          System.err.println ("probleme exception dans saisirMasque2() : " +
                              exc);
          Boite.message (this,
                         "Erreur",
                         "Probleme inconnu !");
          succesMask2 = false;
          dispose ();
          return;
        }
        succesMask2 = true;
        dispose ();

      }

      public void actionPerformed (ActionEvent e)
      {
        if (e.getActionCommand ().equals (ANNULER))
        {
          actionAnnuler();
        }
        if (e.getActionCommand ().equals (OK))
        {
          actionOk();
        }
        else if (e.getSource().equals(ConfirmPass))
        {
          actionOk();
        }
      }
    } //MasqueSaisie2


  /**
   * Vérifie que la chaine str ne comporte pas d'espaces.
   * @param str String
   * @return null si la chaine contient des espaces, la chaine autrement.
   */
  public static String ControleNonEspace (String str/*, String Champ*/)
    {
      StringTokenizer st = new StringTokenizer (str, " ");
      if (st.countTokens () != 1)
      {/*
        Boite.message (new JFrame (),
                       "Erreur de saisie",
                       "Le champ " + Champ +
                       " ne doit pas comporter d'espaces !");
    }*/
    return null;
      }
      return st.nextToken ();

    } // ControleNonEspace ()


        boolean saisir (JFrame cadre)
        {
          new MasqueSaisie2 (cadre);
          if (!succesMask2)return false;
          new MasqueSaisie (cadre);
          return succesMask1;
        }
}

