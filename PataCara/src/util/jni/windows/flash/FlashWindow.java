/**
 * <p>
 * Title: FlashWindow.java
 * </p>.
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>.
 * 
 * @author Rémy GIRAUD
 * @version 1.0
 * 
 * Created on 19 oct. 2004
 * 
 *  
 */
package util.jni.windows.flash;

 
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * <p>
 * Classe : FlashWindow
 * </p>.
 * <p>
 * Description: Permet de faire clignoter une fenetre sous windows en passsant par JNI.<br>
 * La librarie flashjni.dll doit etre disponible.
 * </p>.
 */
public class FlashWindow
{

  /**
   * Méthode native qui fait clignoter un component.
   * @param c le component a faire clignoter
   * @param bInvert 
   */
  public native void flash (Component c, boolean bInvert);


  static
  {
    System.loadLibrary ("flashjni");
  }

  private Component component = null;
  private volatile boolean isStopFlash = false;
  private Thread           t;
  private long sleep;
  private boolean isInvert;

  /**
   * 
   * Constructeur de la classe FlashWindow.
   * @param c le composant qui devra clignoter.
   */
  public FlashWindow (Component c)
  {
    component = c;
    sleep = 500;
    isInvert = true;
  } // Constructeur
  
  

  /**
   * Constructeur de la classe FlashWindow.
   * @param component le composant qui devra clignoter.
   * @param sleep le delai entre 2 clignotements
   * @param isInvert si true clignote, si false reste allumé.
   */
  public FlashWindow (Component component, long sleep, boolean isInvert)
  {
    this.component = component;
    this.sleep = sleep;
    this.isInvert = isInvert;
  }
  /**
   *  Arrete de faire clignoter le component.
   */
  public void stopFlash ()
  {
    isStopFlash = true;

    /* stopFlash () */
  }


  /**
   * Débute le clignotement de la fenetre. Si celle ci clignote, rien ne se passe.
   *
   */
  public void startFlash ()
  {
    startFlash (sleep, isInvert);
  } /* startFlash () */
  
  /**
   * Débute le clignotement de la fenetre. Si celle ci clignote, rien ne se passe.
   * @param sleep le temps entre 2 clignotements.
   * @param isInvert 
   */
  public void startFlash (long sleep, boolean isInvert)
  {
    final long _sleep = sleep;
    final boolean _isInvert = isInvert;
    if (t == null || !t.isAlive())
    {
      t = new Thread (new Runnable () {

        public void run ()
        {
          while (!isStopFlash)
          {
            flash (component, _isInvert);
            try
            {
              Thread.sleep (_sleep);
            }
            catch (InterruptedException e)
            {
              e.printStackTrace ();
            }
          }

          /* run () */
        }
      });
      isStopFlash = false;
      t.start ();
    }    
  } /* startFlash () */
  
  /**
   * Fait clignoter pendant tps puis laisse allumé
   *
   */
  public void startFlashProgressif (long tps, long sleep)
  {
    final long __tps = tps;
    final long __sleep = sleep;
    if (t == null || !t.isAlive ())
    {
      t = new Thread (new Runnable () {

        public void run ()
        {
          try
          {
            long _tps = __tps;
            long _sleep = __sleep;
            while (!isStopFlash && _tps > 0)
            {
              flash (component, true);
              Thread.sleep (_sleep);
              _tps -=  _sleep;
              //System.out.println ("tps : " + _tps);
            }
            //System.out.println ("2eme boucle");

            while (!isStopFlash)
            {
              flash (component, false);
              Thread.sleep (200);
            }
            //System.out.println ("terminé boucle");
            /* run () */
          }
          catch (InterruptedException e)
          {
            e.printStackTrace ();
          }

        }
      });
      isStopFlash = false;
      t.start ();
    }

  } /* startFlashProgressif () */
  
  

  /**
   * @return Retourne le isInvert.
   */
  public boolean isInvert ()
  {
    return isInvert;

    /* isInvert () */
  }
  /**
   * @param isInvert Le isInvert à modifier.
   */
  public void setInvert (boolean isInvert)
  {
    this.isInvert = isInvert;

    /* setInvert () */
  }
  /**
   * @return Retourne le sleep.
   */
  public long getSleep ()
  {
    return sleep;

    /* getSleep () */
  }
  /**
   * @param sleep Le sleep à modifier.
   */
  public void setSleep (long sleep)
  {
    this.sleep = sleep;

    /* setSleep () */
  }
  
  public static void main (String [] args)
  {
    JFrame f = new JFrame ("Flash Window");
    final FlashWindow w = new FlashWindow (f);

    JButton b1 = new JButton ("start");
    b1.addActionListener (new ActionListener () {

      public void actionPerformed (ActionEvent e)
      {
        w.startFlashProgressif(10000, 500);

        /* actionPerformed () */
      }

    });
    JButton b2 = new JButton ("stop");
    b2.addActionListener (new ActionListener () {

      public void actionPerformed (ActionEvent e)
      {

        w.stopFlash();
        /* actionPerformed () */
      }
    });
    f.getContentPane ().setLayout (
        new java.awt.FlowLayout (java.awt.FlowLayout.CENTER, 50, 15));
    f.getContentPane ().add (b1);
    f.getContentPane ().add (b2);
    f.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
    //f.setBounds (300, 400, 650, 700);
    f.pack ();
    f.setVisible (true);
  }
}
// Classe FlashWindow
