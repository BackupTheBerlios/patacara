package util.gui;

/**
 * Title:        Boite.java
 * Description:  Permet d'avoir des boites de dialogue (popup) pré formattées.
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Rémy Giraud
 * @version 1.0
 */


import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Frame;
import java.awt.Dialog;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class Boite extends JDialog implements ActionListener
{
  /** Bouton 1 cliqué */
  public static final int BOUTON_1 = 1;

  /** Bouton 2 cliqué */
  public static final int BOUTON_2 = 2;

  /** Bouton 3 cliqué */
  public static final int BOUTON_3 = 3;

  private int res;

  private String bouton1;
  private String bouton2;
  private String bouton3;
  private static final int POSITION = 50; //position de la boite par raport a la frame ou dialog
  private static final int MARGE_CONTOUR = 5; // La marge pour le contour de la boite.

 /**
  * Constructeur d'une boite
  * @param cadre le cadre ou la boite est ratachée.
  * @param titre Le titre de la boite.
  * @param texte Le message de la boite.
  * @param question si c'est une question true, false autrement.
  */
 public Boite(Frame cadre, String titre, String texte, boolean question)
  {
    this (cadre, titre, texte, question, MARGE_CONTOUR);
  } // Constructeur Boite


  /**
   * Constructeur d'une boite
   *
   * @param cadre le cadre où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @param question si c'est une question true, false autrement.
   * @param margeContour La marger autour du la boite à donner
   */
  public Boite(Frame cadre, String titre, String texte, boolean question, int margeContour)
  {
    super(cadre, titre, true);
    this.setLocation(cadre.getX() + POSITION, cadre.getY() + POSITION);
    if (question)
      iniBoite(texte, "Oui", "Non", null, margeContour, null, 0);
    else
      iniBoite(texte, "OK", null, null, margeContour, null, 0);
  } // Constructeur Boite


  /**
   * Constructeur d'une boite
   *
   * @param cadre le cadre où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @param question si c'est une question true, false autrement.
   * @param margeContour La marger autour du la boite à donner
   * @param image le chemin de l'image (chargés avec getResource())
   * @param position la position de l'image (FlowLayout.LEFT, FlowLayout.CENTER...)
   */
  public Boite(Frame cadre, String titre, String texte, boolean question,
               int margeContour, String image, int position)
  {
    super(cadre, titre, true);
    this.setLocation(cadre.getX() + POSITION, cadre.getY() + POSITION);
    if (question)
      iniBoite(texte, "Oui", "Non", null, margeContour, image, position);
    else
      iniBoite(texte, "OK", null, null, margeContour, image, position);
  } // Constructeur Boite


  /**
   * Constructeur d'une boite
   *
   * @param cadre le cadre où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @param question si c'est une question true, false autrement.
   * @param image le chemin de l'image (chargés avec getResource())
   * @param position int
   */
  public Boite(Frame cadre, String titre, String texte, boolean question,
               String image, int position)
  {
      this (cadre, titre, texte, question, MARGE_CONTOUR, image, position);
  } // Constructeur Boite


  /**
   * Constructeur d'une boite
   *
   * @param cadre le cadre où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @param image le chemin de l'image (chargés avec getResource())
   * @param position la position de l'image (FlowLayout.LEFT, FlowLayout.CENTER...)
   */
  public Boite(Frame cadre, String titre, String texte, String image, int position)
  {
      this (cadre, titre, texte, false, MARGE_CONTOUR, image, position);
  } // Constructeur Boite


  /**
   * Constructeur d'une boite
   *
   * @param cadre le cadre où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @param image le chemin de l'image (chargés avec getResource())
   */
  public Boite(Frame cadre, String titre, String texte, String image)
  {
      this (cadre, titre, texte, false, MARGE_CONTOUR, image, FlowLayout.CENTER);
  } // Constructeur Boite




  /**
   * Constructeur d'une boite
   * @param dial le cadre (boite de dialogue) où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @param question si c'est une question true, false autrement.
   */
  public Boite(Dialog dial, String titre, String texte, boolean question)
  {
    this (dial, titre, texte, question, MARGE_CONTOUR);
  } // Constructeur Boite


  /**
   * Constructeur d'une boite
   * @param dial le cadre (boite de dialogue) où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @param question si c'est une question true, false autrement.
   * @param margeContour la marge autour du la boite à donner
   */
  public Boite(Dialog dial, String titre, String texte, boolean question, int margeContour)
  {
    this (dial, titre, texte, question, margeContour, null, 0);
  } // Constructeur Boite



  /**
    * Constructeur d'une boite
    * @param dial le cadre (boite de dialogue) où la boite est ratachée.
    * @param titre Le titre de la boite.
    * @param texte Le message de la boite.
    * @param question si c'est une question true, false autrement.
    * @param image le chemin de l'image (chargés avec getResource())
    * @param margeContour la marge autour du la boite à donner
    * @param position la position de l'image (FlowLayout.LEFT, FlowLayout.CENTER...)
    */
   public Boite(Dialog dial, String titre, String texte, boolean question, int margeContour, String image, int position)
   {
     super(dial, titre, true);
     this.setLocation(dial.getX() + POSITION, dial.getY() + POSITION);
     if (question)
       iniBoite(texte, "Oui", "Non", null, margeContour, image, position);
     else
       iniBoite(texte, "OK", null, null, margeContour, image, position);
   } // Constructeur Boite



 /**
   * Constructeur d'une boite
   *
   * @param cadre le cadre où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @param b1 le texte du premier bouton.
   * @param b2 le texte du deuxieme bouton.
   * @param b3 le texte du troisième bouton.
   */
  public Boite(Frame cadre, String titre, String texte, String b1, String b2, String b3)
  {
    this (cadre, titre, texte, b1, b2, b3, MARGE_CONTOUR);
  }// Constructeur Boite


  /**
   * Constructeur d'une boite
   *
   * @param cadre le cadre où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @param b1 le texte du premier bouton.
   * @param b2 le texte du deuxieme bouton.
   * @param b3 le texte du troisième bouton.
   * @param margeContour la marge autour du la boite à donner.
   */
  public Boite(Frame cadre, String titre, String texte, String b1, String b2, String b3, int margeContour)
  {
    this (cadre, titre, texte, b1, b2, b3, margeContour, null, 0);
  }// Constructeur Boite


  /**
   * Constructeur d'une boite
   *
   * @param cadre le cadre où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @param b1 le texte du premier bouton.
   * @param b2 le texte du deuxieme bouton.
   * @param b3 le texte du troisième bouton.
   * @param margeContour la marge autour du la boite à donner.
   * @param image le chemin de l'image (chargés avec getResource())
   * @param position la position de l'image (FlowLayout.LEFT, FlowLayout.CENTER...)
   */
  public Boite(Frame cadre, String titre, String texte, String b1, String b2,
               String b3, int margeContour, String image, int position)
  {
    super (cadre, titre, true);
    this.setLocation (cadre.getX () + POSITION, cadre.getY () + POSITION);
    iniBoite(texte, b1, b2, b3, margeContour, image, position);
  }// Constructeur Boite








  /**
   * Constructeur d'une boite
   *
   * @param dial le cadre (boite de dialogue) où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @param b1 le texte du premier bouton.
   * @param b2 le texte du deuxieme bouton.
   * @param b3 le texte du troisième bouton.
   */
  public Boite(Dialog dial, String titre, String texte, String b1, String b2, String b3)
  {
    this (dial, titre, texte, b1, b2, b3, MARGE_CONTOUR);
  } // Constructeur Boite

  /**
   * Constructeur d'une boite
   *
   * @param dial le cadre (boite de dialogue) où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @param b1 le texte du premier bouton.
   * @param b2 le texte du deuxieme bouton.
   * @param b3 le texte du troisième bouton.
   * @param margeContour la marge autour du la boite à donner
   */
  public Boite(Dialog dial, String titre, String texte, String b1, String b2, String b3, int margeContour)
  {
    super (dial, titre, true);
    this.setLocation (dial.getX () + POSITION, dial.getY () + POSITION);
    iniBoite(texte, b1, b2, b3, margeContour, null, 0);
  } // Constructeur Boite




  /**
   * Constructeur d'une boite avec Image en haut.
   *
   * @param dial le cadre (boite de dialogue) où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @param b1 le texte du premier bouton.
   * @param b2 le texte du deuxieme bouton.
   * @param b3 le texte du troisième bouton.
   * @param margeContour la marge autour du la boite à donner.
   * @param image le chemin de l'image (chargés avec getResource())
   * @param position la position de l'image (FlowLayout.LEFT, FlowLayout.CENTER...)
   */
  public Boite(Dialog dial, String titre, String texte, String b1, String b2,
               String b3, int margeContour, String image, int position)
  {
    super (dial, titre, true);
    this.setLocation (dial.getX () + POSITION, dial.getY () + POSITION);
    iniBoite(texte, b1, b2, b3, margeContour, image, position);
  }// Constructeur Boite



  /**
   * Initialise le contenu de la boite de dialogue.
   *
   * @param texte le texte au centre de la boite. Possibilité d'inclure de
   *   l'HTML.
   * @param b1 Le texte du premier bouton.
   * @param b2 Le texte du deuxieme bouton.
   * @param b3 Le texte du troisieme bouton.
   * @param margeContour la marge pour le contour de la boite
   * @param image le chemin de l'image (chargés avec getResource())
   * @param position la position de l'image (FlowLayout.LEFT, FlowLayout.CENTER...)
   */
  private void iniBoite(String texte, String b1, String b2, String b3,
                        int margeContour, String image, int position)
  {
//		getContentPane().setBackground(Color.lightGray);
//		getContentPane().setLayout(new GridLayout(2, 1));
    getContentPane().setLayout(new BorderLayout (0, 0));
    getContentPane().add(new JLabel(texte, JLabel.CENTER), BorderLayout.CENTER);
    JPanel p = new JPanel(new FlowLayout (FlowLayout.CENTER, 15, 15)); //panneau des boutons

    JButton b = new JButton(bouton1 = b1);
    b.addActionListener(this);
    b.addKeyListener(new KeyAdapter ()
    { //On a appuyé sur enter du bouton1
      public void keyPressed (KeyEvent e)
      {
        if (e.getKeyChar () == '\n') // e.getKeyCode == 10
          actionBouton1 ();
      }
    });

    p.add(b);
    if (b2 != null)
    {
      b = new JButton(bouton2 = b2);
      b.addActionListener(this);
      b.addKeyListener(new KeyAdapter ()
      { //On a appuyé sur enter du bouton2
        public void keyPressed (KeyEvent e)
        {
          if (e.getKeyChar () == '\n') // e.getKeyCode == 10
            actionBouton2 ();
        }
      });

      p.add(b);
      if (b3 != null)
      {
        b = new JButton(bouton3 = b3);
        b.addActionListener(this);
        b.addKeyListener(new KeyAdapter ()
        { //On a appuyé sur enter du bouton3
          public void keyPressed (KeyEvent e)
          {
            if (e.getKeyChar () == '\n') // e.getKeyCode == 10
              actionBouton3 ();
          }
        });

        p.add(b);
      }
    }
    getContentPane().add(p, BorderLayout.SOUTH);

    //Création des panneaux pour le contour
    getContentPane().add(new JPanel (new FlowLayout (FlowLayout.CENTER, margeContour, margeContour)), BorderLayout.WEST);
    getContentPane().add(new JPanel (new FlowLayout (FlowLayout.CENTER, margeContour, margeContour)), BorderLayout.EAST);

    //Image ou contour
    if (null != image)
    {//Il faut placer une image
      java.net.URL u = getClass ().getResource (image);
      if (null != u)//l'image a bien été trouvé
      {
        JLabel lImage = new JLabel (new javax.swing.ImageIcon (u));
        JPanel jImage = new JPanel (new FlowLayout (position,
                                                    margeContour, margeContour));
        jImage.add(lImage);
        getContentPane().add(jImage, BorderLayout.NORTH);
      }
    }
    else
    {
      getContentPane ().add (new JPanel (new FlowLayout (FlowLayout.CENTER,
          margeContour, margeContour)), BorderLayout.NORTH);
    }


    pack();
    setVisible(true);
/*    if (b.requestFocusInWindow())
      System.out.println("request OK");
    else
      System.out.println("request NON OK");
  */

  }

  /**
   * Action a effectuer lorsqu'on appuie sur le bouton 1
   */
  private void actionBouton1 ()
  {
    res = BOUTON_1;
    dispose ();
  } /* actionBouton1 () */

  /**
   * Action a effectuer lorsqu'on appuie sur le bouton 2
   */
  private void actionBouton2 ()
  {
    res = BOUTON_2;
    dispose ();
  } /* actionBouton2 () */

  /**
   * Action a effectuer lorsqu'on appuie sur le bouton 3
   */
  private void actionBouton3 ()
  {
    res = BOUTON_3;
    dispose ();
  } /* actionBouton3 () */

  public void actionPerformed(ActionEvent e)
  {
    if (e.getActionCommand().equals(bouton1))
      actionBouton1();
    else if (e.getActionCommand().equals(bouton2))
      actionBouton2();
    else
      actionBouton3();
  }


  /**
   * Crée une boite d'information avec le message donné.
   * Un seul bouton ok.
   *
   * @param cadre le cadre où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   */
  public static void message(Frame cadre, String titre, String texte)
  {
    new Boite(cadre, titre, texte, false);
  } /* message () */


  /**
   * Crée une boite d'information avec le message donné. Un seul bouton ok.
   *
   * @param cadre le cadre où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @param margeContour la marge pour le contour de la boite
   */
  public static void message(Frame cadre, String titre, String texte, int margeContour)
  {
    new Boite(cadre, titre, texte, false, margeContour);
  } /* message () */


  /**
   * Crée une boite d'information avec le message donné. Un seul bouton ok.
   *
   * @param cadre le cadre où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @param margeContour la marge pour le contour de la boite
   * @param image le chemin de l'image (chargés avec getResource())
   * @param position la position de l'image (FlowLayout.LEFT, FlowLayout.CENTER...)
   */
  public static void message(Frame cadre, String titre, String texte,
                             int margeContour, String image, int position)
  {
    new Boite(cadre, titre, texte, false, margeContour, image, position);
  } /* message () */


  /**
   * Crée une boite d'information avec le message donné. Un seul bouton ok.
   *
   * @param cadre le cadre où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @param image le chemin de l'image (chargés avec getResource())
   * @param position la position de l'image (FlowLayout.LEFT, FlowLayout.CENTER...)
   */
  public static void message(Frame cadre, String titre, String texte,
                             String image, int position)
  {
    new Boite(cadre, titre, texte, false, image, position);
  } /* message () */


  /**
   * Crée une boite d'information avec le message donné. Un seul bouton ok.
   *
   * @param cadre le cadre où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @param image le chemin de l'image (chargés avec getResource())
   */
  public static void message(Frame cadre, String titre, String texte,
                             String image)
  {
    new Boite(cadre, titre, texte, image);
  } /* message () */


  /**
   * Crée une boite d'information avec le message donné. Un seul bouton ok.
   *
   * @param cadre le cadre où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @param margeContour la marge pour le contour de la boite
   * @param image le chemin de l'image (chargés avec getResource())
   */
  public static void message(Frame cadre, String titre, String texte,
                             int margeContour, String image)
  {
    new Boite(cadre, titre, texte, false, margeContour, image, FlowLayout.CENTER);
  } /* message () */



  /**
   * Crée une boite d'information avec le message donné.
   * Un seul bouton ok.
   *
   * @param dial le cadre où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   */
  public static void message(Dialog dial, String titre, String texte)
  {
    new Boite(dial, titre, texte, false);
  } /* message () */


  /**
   * Crée une boite d'information avec le message donné.
   * Un seul bouton ok.
   *
   * @param dial le cadre où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @param margeContour la marge pour le contour de la boite
   */
  public static void message(Dialog dial, String titre, String texte, int margeContour)
  {
    new Boite(dial, titre, texte, false, margeContour);
  } /* message () */


  /**
   * Crée une boite d'information avec le message donné.
   * Un seul bouton ok.
   *
   * @param dial le cadre où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @param margeContour la marge pour le contour de la boite
   * @param image le chemin de l'image (chargés avec getResource())
   * @param position la position de l'image (FlowLayout.LEFT, FlowLayout.CENTER...)
   */
  public static void message(Dialog dial, String titre, String texte, int margeContour, String image, int position)
  {
    new Boite(dial, titre, texte, false, margeContour, image, position);
  } /* message () */



  /**
   * Crée une boite d'information avec le message donné.
   * Un seul bouton ok.
   *
   * @param dial le cadre où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @param margeContour la marge pour le contour de la boite
   * @param image le chemin de l'image (chargés avec getResource())
   */
  public static void message(Dialog dial, String titre, String texte, int margeContour, String image)
  {
    new Boite(dial, titre, texte, false, margeContour, image, FlowLayout.CENTER);
  } /* message () */



  /**
   * Crée une boite d'information avec le message donné.
   * Un seul bouton ok.
   *
   * @param dial le cadre où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @param image le chemin de l'image (chargés avec getResource())
   * @param position la position de l'image (FlowLayout.LEFT, FlowLayout.CENTER...)
   */
  public static void message(Dialog dial, String titre, String texte, String image, int position)
  {
    new Boite(dial, titre, texte, false, MARGE_CONTOUR, image, position);
  } /* message () */

  /**
   * Crée une boite d'information avec le message donné.
   * Un seul bouton ok.
   *
   * @param dial le cadre où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @param image le chemin de l'image (chargés avec getResource())
   */
  public static void message(Dialog dial, String titre, String texte, String image)
  {
    new Boite(dial, titre, texte, false, MARGE_CONTOUR, image, FlowLayout.CENTER);
  } /* message () */




  /**
   * Crée une boite de question avec le message donné.<br>
   * 2 boutons ok annuler.
   *
   * @param cadre le cadre où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @return boolean true si on a appuie sur ok, <br>
   * false si on a appuyé sur annulé.
   */
  public static boolean question(Frame cadre, String titre, String texte)
  {
    Boite b = new Boite(cadre, titre, texte, true);
    return b.res == BOUTON_1;
  } /* question () */


  /**
   * Crée une boite de question avec le message donné.<br>
   * 2 boutons ok annuler.
   *
   * @param cadre le cadre où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @return boolean true si on a appuie sur ok, <br>
   * @param margeContour la marge pour le contour de la boite
   * false si on a appuyé sur annulé.
   */
  public static boolean question(Frame cadre, String titre, String texte, int margeContour)
  {
    Boite b = new Boite(cadre, titre, texte, true, margeContour);
    return b.res == BOUTON_1;
  } /* question () */



  /**
   * Crée une boite de question avec le message donné.<br>
   * 2 boutons ok annuler.
   *
   * @param cadre le cadre où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @param margeContour la marge pour le contour de la boite
   * @param image le chemin de l'image (chargés avec getResource())
   * @param position la position de l'image (FlowLayout.LEFT, FlowLayout.CENTER...)
   */
  public static void question (Frame cadre, String titre, String texte,
                               int margeContour, String image, int position)
  {
    new Boite(cadre, titre, texte, true, margeContour, image, position);
  } /* message () */


  /**
   * Crée une boite de question avec le message donné.<br>
   * 2 boutons ok annuler.
   *
   * @param cadre le cadre où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @param margeContour la marge pour le contour de la boite
   * @param image le chemin de l'image (chargés avec getResource())
   */
  public static void question (Frame cadre, String titre, String texte,
                               int margeContour, String image)
  {
    new Boite(cadre, titre, texte, true, margeContour, image, FlowLayout.CENTER);
  } /* message () */


  /**
   * Crée une boite de question avec le message donné.<br>
   * 2 boutons ok annuler.
   *
   * @param cadre le cadre où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @param image le chemin de l'image (chargés avec getResource())
   */
  public static void question (Frame cadre, String titre, String texte,
                               String image)
  {
    new Boite(cadre, titre, texte, true, MARGE_CONTOUR, image, FlowLayout.CENTER);
  } /* message () */


  /**
   * Crée une boite de question avec le message donné.<br>
   * 2 boutons ok annuler.
   *
   * @param cadre le cadre où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @param image le chemin de l'image (chargés avec getResource())
   * @param position la position de l'image (FlowLayout.LEFT, FlowLayout.CENTER...)
   */
  public static void question (Frame cadre, String titre, String texte,
                               String image, int position)
  {
    new Boite(cadre, titre, texte, true, MARGE_CONTOUR, image, position);
  } /* message () */


  /**
   * Crée une boite de question avec le message donné.<br>
   * 2 boutons ok annuler.
   *
   * @param dial le cadre où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @return boolean true si on a appuie sur ok, <br>
   * false si on a appuyé sur annulé.
   */
  public static boolean question(Dialog dial, String titre, String texte)
  {
    Boite b = new Boite(dial, titre, texte, true);
    return b.res == BOUTON_1;
  } /* question () */

  /**
   * Crée une boite de question avec le message donné.<br>
   * 2 boutons ok annuler.
   *
   * @param dial le cadre où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param margeContour la marge pour le contour de la boite
   * @param texte Le message de la boite.
   * @return boolean true si on a appuie sur ok, <br>
   * false si on a appuyé sur annulé.
   */
  public static boolean question(Dialog dial, String titre, String texte, int margeContour)
  {
    Boite b = new Boite(dial, titre, texte, true, margeContour);
    return b.res == BOUTON_1;
  } /* question () */



  /*
   * @param dial le cadre (boite de dialogue) où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @param margeContour la marge autour du la boite à donner.
   * @param image le chemin de l'image (chargés avec getResource())
   * @param position la position de l'image (FlowLayout.LEFT,
   *   FlowLayout.CENTER...)
   * @return boolean true si on a appuie sur ok, <br>
   * false si on a appuyé sur annulé.
   */
  public static boolean question(Dialog dial, String titre, String texte, int margeContour, String image, int position)
  {
    Boite b = new Boite(dial, titre, texte, true, margeContour, image, position);
    return b.res == BOUTON_1;
  } /* question () */


  /*
   * @param dial le cadre (boite de dialogue) où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @param margeContour la marge autour du la boite à donner.
   * @param image le chemin de l'image (chargés avec getResource())
   *   FlowLayout.CENTER...)
   * @return boolean true si on a appuie sur ok, <br>
   * false si on a appuyé sur annulé.
   */
  public static boolean question(Dialog dial, String titre, String texte, int margeContour, String image)
  {
    Boite b = new Boite(dial, titre, texte, true, margeContour, image, FlowLayout.CENTER);
    return b.res == BOUTON_1;
  } /* question () */



  /*
   * @param dial le cadre (boite de dialogue) où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @param image le chemin de l'image (chargés avec getResource())
   * @param position la position de l'image (FlowLayout.LEFT,
   *   FlowLayout.CENTER...)
   * @return boolean true si on a appuie sur ok, <br>
   * false si on a appuyé sur annulé.
   */
  public static boolean question(Dialog dial, String titre, String texte, String image, int position)
  {
    Boite b = new Boite(dial, titre, texte, true, MARGE_CONTOUR, image, position);
    return b.res == BOUTON_1;
  } /* question () */



  /*
   * @param dial le cadre (boite de dialogue) où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @param image le chemin de l'image (chargés avec getResource())
   * @return boolean true si on a appuie sur ok, <br>
   * false si on a appuyé sur annulé.
   */
  public static boolean question(Dialog dial, String titre, String texte, String image)
  {
    Boite b = new Boite(dial, titre, texte, true, MARGE_CONTOUR, image, FlowLayout.CENTER);
    return b.res == BOUTON_1;
  } /* question () */



  /**
   * Crée une boite avec 3 boutons.<br>
   *
   * @param cadre le cadre où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @param bouton1 texte du bouton 1
   * @param bouton2 texte du bouton 2
   * @param bouton3 texte du bouton 3
   * @return BOUTON_1 si le bouton 1 a été appuyé,<br>
   *         BOUTON_2 si le bouton 2 a été appuyé, <br>
   *         BOUTON_3 si le bouton 3 a été appuyé.
   */
  public static int troisCas(Frame cadre, String titre, String texte,
                  String bouton1, String bouton2, String bouton3)
  {
    Boite b = new Boite(cadre, titre, texte, bouton1, bouton2, bouton3);
    return b.res;
  } /* troisCas () */



  /**
   * Crée une boite avec 3 boutons.<br>
   *
   * @param cadre le cadre où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @param bouton1 texte du bouton 1
   * @param bouton2 texte du bouton 2
   * @param bouton3 texte du bouton 3
   * @param margeContour la marge pour le contour de la boite
   * @return BOUTON_1 si le bouton 1 a été appuyé,<br>
   *         BOUTON_2 si le bouton 2 a été appuyé, <br>
   *         BOUTON_3 si le bouton 3 a été appuyé.
   */
  public static int troisCas(Frame cadre, String titre, String texte,
                  String bouton1, String bouton2, String bouton3, int margeContour)
  {
    Boite b = new Boite(cadre, titre, texte, bouton1, bouton2, bouton3, margeContour);
    return b.res;
  } /* troisCas () */


  /**
   * Crée une boite avec 3 boutons.<br>
   *
   * @param cadre le cadre où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @param bouton1 texte du bouton 1
   * @param bouton2 texte du bouton 2
   * @param bouton3 texte du bouton 3
   * @param margeContour la marge pour le contour de la boite
   * @param image le chemin de l'image (chargés avec getResource())
   * @param position la position de l'image (FlowLayout.LEFT, FlowLayout.CENTER...)
   * @return BOUTON_1 si le bouton 1 a été appuyé,<br>
   *         BOUTON_2 si le bouton 2 a été appuyé,<br>
   *         BOUTON_3 si le bouton 3 a été appuyé.
   */
  public static int troisCas(Frame cadre, String titre, String texte,
                  String bouton1, String bouton2, String bouton3, int margeContour, String image, int position)
  {
    Boite b = new Boite(cadre, titre, texte, bouton1, bouton2, bouton3, margeContour, image, position);
    return b.res;
  } /* message () */


  /**
   * Crée une boite avec 3 boutons.<br>
   *
   * @param cadre le cadre où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @param bouton1 texte du bouton 1
   * @param bouton2 texte du bouton 2
   * @param bouton3 texte du bouton 3
   * @param margeContour la marge pour le contour de la boite
   * @param image le chemin de l'image (chargés avec getResource())
   * @return BOUTON_1 si le bouton 1 a été appuyé,<br>
   *         BOUTON_2 si le bouton 2 a été appuyé,<br>
   *         BOUTON_3 si le bouton 3 a été appuyé.
   */
  public static int troisCas(Frame cadre, String titre, String texte,
                  String bouton1, String bouton2, String bouton3, int margeContour, String image)
  {
    Boite b = new Boite(cadre, titre, texte, bouton1, bouton2, bouton3, margeContour, image, FlowLayout.CENTER);
    return b.res;
  } /* message () */


  /**
   * Crée une boite avec 3 boutons.<br>
   *
   * @param cadre le cadre où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @param bouton1 texte du bouton 1
   * @param bouton2 texte du bouton 2
   * @param bouton3 texte du bouton 3
   * @param image le chemin de l'image (chargés avec getResource())
   * @param position la position de l'image (FlowLayout.LEFT, FlowLayout.CENTER...)
   * @return BOUTON_1 si le bouton 1 a été appuyé,<br>
   *         BOUTON_2 si le bouton 2 a été appuyé,<br>
   *         BOUTON_3 si le bouton 3 a été appuyé.
   */
  public static int troisCas(Frame cadre, String titre, String texte,
                              String bouton1, String bouton2, String bouton3,
                              String image, int position)
  {
    Boite b = new Boite(cadre, titre, texte, bouton1, bouton2, bouton3, MARGE_CONTOUR, image, position);
    return b.res;
  } /* message () */



  /**
   * Crée une boite avec 3 boutons.
   * <br>
   *
   * @param dial le cadre où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @param bouton1 texte du bouton 1
   * @param bouton2 texte du bouton 2
   * @param bouton3 texte du bouton 3
   * @return BOUTON_1 si le bouton 1 a été appuyé,<br>
   *         BOUTON_2 si le bouton 2 a été appuyé,<br>
   *         BOUTON_3 si le bouton 3 a été appuyé.
   */
  public static int troisCas(Dialog dial, String titre, String texte,
                  String bouton1, String bouton2, String bouton3)
  {
    Boite b = new Boite(dial, titre, texte, bouton1, bouton2, bouton3);
    return b.res;
  } /* troisCas () */

  /**
   * Crée une boite avec 3 boutons.
   * <br>
   *
   * @param dial le cadre où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @param bouton1 texte du bouton 1
   * @param bouton2 texte du bouton 2
   * @param bouton3 texte du bouton 3
   * @param margeContour la marge pour le contour de la boite
   * @return BOUTON_1 si le bouton 1 a été appuyé,<br>
   *         BOUTON_2 si le bouton 2 a été appuyé,<br>
   *         BOUTON_3 si le bouton 3 a été appuyé.
   */
  public static int troisCas(Dialog dial, String titre, String texte,
                  String bouton1, String bouton2, String bouton3, int margeContour)
  {
    Boite b = new Boite(dial, titre, texte, bouton1, bouton2, bouton3, margeContour);
    return b.res;
  } /* troisCas () */


  /**
   * Crée une boite avec 3 boutons.
   *
   * @param dial le cadre (boite de dialogue) où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @param bouton1 le texte du premier bouton.
   * @param bouton2 le texte du deuxieme bouton.
   * @param bouton3 le texte du troisième bouton.
   * @param margeContour la marge autour du la boite à donner.
   * @param image le chemin de l'image (chargés avec getResource())
   * @param position la position de l'image (FlowLayout.LEFT,
   *   FlowLayout.CENTER...)
   * @return BOUTON_1 si le bouton 1 a été appuyé,<br>
   *         BOUTON_2 si le bouton 2 a été appuyé,<br>
   *         BOUTON_3 si le bouton 3 a été appuyé.
   */
  public static int troisCas(Dialog dial, String titre, String texte,
                  String bouton1, String bouton2, String bouton3, int margeContour, String image, int position)
  {
    Boite b = new Boite(dial, titre, texte, bouton1, bouton2, bouton3, margeContour, image, position);
    return b.res;
  } /* troisCas () */


  /**
   * Crée une boite avec 3 boutons.
   *
   * @param dial le cadre (boite de dialogue) où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @param bouton1 le texte du premier bouton.
   * @param bouton2 le texte du deuxieme bouton.
   * @param bouton3 le texte du troisième bouton.
   * @param margeContour la marge autour du la boite à donner.
   * @param image le chemin de l'image (chargés avec getResource())
   *   FlowLayout.CENTER...)
   * @return BOUTON_1 si le bouton 1 a été appuyé,<br>
   *         BOUTON_2 si le bouton 2 a été appuyé,<br>
   *         BOUTON_3 si le bouton 3 a été appuyé.
   */
  public static int troisCas(Dialog dial, String titre, String texte,
                  String bouton1, String bouton2, String bouton3, int margeContour, String image)
  {
    Boite b = new Boite(dial, titre, texte, bouton1, bouton2, bouton3, margeContour, image, FlowLayout.CENTER);
    return b.res;
  } /* troisCas () */


  /**
   * Crée une boite avec 3 boutons.
   *
   * @param dial le cadre (boite de dialogue) où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @param bouton1 le texte du premier bouton.
   * @param bouton2 le texte du deuxieme bouton.
   * @param bouton3 le texte du troisième bouton.
   * @param image le chemin de l'image (chargés avec getResource())
   * @param position la position de l'image (FlowLayout.LEFT,
   *   FlowLayout.CENTER...)
   * @return BOUTON_1 si le bouton 1 a été appuyé,<br>
   *         BOUTON_2 si le bouton 2 a été appuyé,<br>
   *         BOUTON_3 si le bouton 3 a été appuyé.
   */
  public static int troisCas(Dialog dial, String titre, String texte,
                  String bouton1, String bouton2, String bouton3, String image, int position)
  {
    Boite b = new Boite(dial, titre, texte, bouton1, bouton2, bouton3, MARGE_CONTOUR, image, position);
    return b.res;
  } /* troisCas () */

  /**
   * Crée une boite avec 3 boutons.
   *
   * @param dial le cadre (boite de dialogue) où la boite est ratachée.
   * @param titre Le titre de la boite.
   * @param texte Le message de la boite.
   * @param bouton1 le texte du premier bouton.
   * @param bouton2 le texte du deuxieme bouton.
   * @param bouton3 le texte du troisième bouton.
   * @param image le chemin de l'image (chargés avec getResource())
   *   FlowLayout.CENTER...)
   * @return BOUTON_1 si le bouton 1 a été appuyé,<br>
   *         BOUTON_2 si le bouton 2 a été appuyé,<br>
   *         BOUTON_3 si le bouton 3 a été appuyé.
   */
  public static int troisCas(Dialog dial, String titre, String texte,
                  String bouton1, String bouton2, String bouton3, String image)
  {
    Boite b = new Boite(dial, titre, texte, bouton1, bouton2, bouton3, MARGE_CONTOUR, image, FlowLayout.CENTER);
    return b.res;
  } /* troisCas () */





}// Classe Boite
