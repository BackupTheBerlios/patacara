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
   * @param isInvert
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
}
// Classe FlashWindow
