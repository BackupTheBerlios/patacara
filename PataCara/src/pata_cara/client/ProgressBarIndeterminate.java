package pata_cara.client;

import javax.swing.JProgressBar;

/**
 * Title:        pata_cara
 * Description:  Cette classe permet de
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Rémy Giraud
 * @version 1.0
 */


public class ProgressBarIndeterminate extends Thread
{
  private JProgressBar bar = null;

  /**
   * Variable d'arret du thread
   */
  private volatile boolean stop;

  /**
   * Le temps d'attente entre 2 défilements.
   */
  private volatile int tpsAttente;

  private volatile boolean kill;

  /**
   * La value courante du JProgressBar
   */
  private volatile int courant;

  /**
   * Constructeur de la barre de défilement continue.<br>
   * Constuit et lance le thread de la bar.
   */
  public ProgressBarIndeterminate ()
  {
    bar = new JProgressBar ();
    stop = true;
    kill = false;
    tpsAttente = 500;
    this.start();
  } // Constructeur ProgressBarIndeterminate


  /**
   * Constructeur de la barre de défilement continue.<br>
   * Constuit et lance le thread de la bar.
   *
   * @param bar La barre de defilement à prendre en compte.
   */
  public ProgressBarIndeterminate (JProgressBar bar)
  {
    this.bar = bar;
    stop = true;
    kill = false;
    tpsAttente = 500;
    this.start();
  } // Constructeur ProgressBarIndeterminate


  /**
   * Constructeur de la barre de défilement continue.<br>
   * Constuit et lance le thread de la bar.
   *
   * @param bar La barre de defilement à prendre en compte.
   * @param tpsMili le temps en miliseconde entre chaque défilement.
   */
  public ProgressBarIndeterminate (JProgressBar bar, int tpsMili)
  {
    this.bar = bar;
    stop = true;
    kill = false;
    tpsAttente = tpsMili;
    this.start();
  } // Constructeur ProgressBarIndeterminate


  /**
   * Constructeur de la barre de défilement continue.<br>
   * Constuit et lance le thread de la bar.
   * @param tpsMili le temps en miliseconde entre chaque défilement.
   */
  public ProgressBarIndeterminate (int tpsMili)
  {
    bar = new JProgressBar ();
    stop = true;
    kill = false;
    tpsAttente = tpsMili;
    this.start();
  } // Constructeur ProgressBarIndeterminate



  /**
   * @return le JProgressBar associé à l'objet.
   */
  public JProgressBar getProgressBar ()
  {
    return bar;
  } /* getProgressBar () */

  /**
   * La fonction qui fait défiler le progressbar.
   */
  public void run ()
  {
    int min = bar.getMinimum();
    int max = bar.getMaximum() - min +1; //+1 car max est compris
    courant = 0;
    bar.setValue(min);
    while (!kill)
    {
      while (!stop)
      {
        try
        {
          Thread.sleep (tpsAttente);
          courant = (++courant) % max;
          bar.setValue (courant + min);
        }
        catch (InterruptedException ex)
        {
          ex.printStackTrace ();
        }
      } // while !stop
      //Temporisation pour pas prendre trop de ressource
      try
      {
        Thread.sleep (70);
      }
      catch (InterruptedException ex1)
      {
        ex1.printStackTrace();
      }
    } // while !kill
  } /* run () */

  /**
   * Lance ou relance le défilement.
   */
  public void activeProgress ()
  {
    stop = false;
  } /* startProgress () */

  /**
   * Stop la progression du la barre de défilement.
   */
  public void stopProgress ()
  {
    stop = true;
  } /* stopProgress () */

  /**
   * Informe sur la barre de défilement est arretée ou non.
   * @return true si la barre est arretée, false autrement.
   */
  public boolean isArreter ()
  {
    return stop;
  } /* isArreter () */


  /**
   *
   * @return true si la barre de défilement est désactivé définitivement.
   * @see pata_cara.client.ProgressBarIndeterminate#killProgress
   */
  public boolean isKilledProgress ()
  {
    return kill;
  } /* isKilledProgress () */

  /**
   * Désactive définitivement la barre de défilement et libère l'espace mémoire
   * du thread actif.<br>
   * Attention le JProgress bar n'est pas libérer et peut etre encore utilisé mais pas comme barre de progres INDETERMINé.
   * Pour relancer une barre de progres INDETERMINé, il faut utilisé stopProgress et non killProgress.
   * Si killProgress a été appelé le seul moyen de recréer une barre de progres INDETERMINé est de recréer une instance.?
   * @see pata_cara.client.ProgressBarIndeterminate#stopProgress
   */
  public void killProgress ()
  {
    stop = true;
    kill = true;
  }


  /**
   * Fixe le temps d'attente entre 2 défilements.
   * @param tpsMili le nouveau temps entre 2 défilements.
   */
  public void setTpsAttente (int tpsMili)
  {
    tpsAttente = tpsMili;
  } /* setTpsAttente () */


  /**
   * Renvoi le temps d'attente entre 2 défilements.
   * @return le temps d'attente entre 2 défilements.
   */
  public int getTpsAttente ()
  {
    return tpsAttente;
  } /* getTpsAttente () */

  /**
   * Fixe la valeur actuel de la barre de défilement.<br>
   * En passant par getProgressBar.setValue () il n'y aura aucune différence sur
   * la valeur, il faut passer par cette fonction pour voir une différence.
   * @param newValue int
   */
  public void setValue (int newValue)
  {
    courant = newValue;
  } /* setValue () */

  /**
   * Retourne la valeur courante de la barre de défilement.
   * @return la valeur courante de la barre de défilement.
   */
  public int getValue ()
  {
    return courant;
  } /* getValue () */

} // Classe ProgressBarIndeterminate
