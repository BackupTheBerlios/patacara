package pata_cara.client;

import javax.swing.JProgressBar;

/**
 * Title:        pata_cara
 * Description:  Cette classe permet de
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author R�my Giraud
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
   * Le temps d'attente entre 2 d�filements.
   */
  private volatile int tpsAttente;

  private volatile boolean kill;

  /**
   * La value courante du JProgressBar
   */
  private volatile int courant;

  /**
   * Constructeur de la barre de d�filement continue.<br>
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
   * Constructeur de la barre de d�filement continue.<br>
   * Constuit et lance le thread de la bar.
   *
   * @param bar La barre de defilement � prendre en compte.
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
   * Constructeur de la barre de d�filement continue.<br>
   * Constuit et lance le thread de la bar.
   *
   * @param bar La barre de defilement � prendre en compte.
   * @param tpsMili le temps en miliseconde entre chaque d�filement.
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
   * Constructeur de la barre de d�filement continue.<br>
   * Constuit et lance le thread de la bar.
   * @param tpsMili le temps en miliseconde entre chaque d�filement.
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
   * @return le JProgressBar associ� � l'objet.
   */
  public JProgressBar getProgressBar ()
  {
    return bar;
  } /* getProgressBar () */

  /**
   * La fonction qui fait d�filer le progressbar.
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
   * Lance ou relance le d�filement.
   */
  public void activeProgress ()
  {
    stop = false;
  } /* startProgress () */

  /**
   * Stop la progression du la barre de d�filement.
   */
  public void stopProgress ()
  {
    stop = true;
  } /* stopProgress () */

  /**
   * Informe sur la barre de d�filement est arret�e ou non.
   * @return true si la barre est arret�e, false autrement.
   */
  public boolean isArreter ()
  {
    return stop;
  } /* isArreter () */


  /**
   *
   * @return true si la barre de d�filement est d�sactiv� d�finitivement.
   * @see pata_cara.client.ProgressBarIndeterminate#killProgress
   */
  public boolean isKilledProgress ()
  {
    return kill;
  } /* isKilledProgress () */

  /**
   * D�sactive d�finitivement la barre de d�filement et lib�re l'espace m�moire
   * du thread actif.<br>
   * Attention le JProgress bar n'est pas lib�rer et peut etre encore utilis� mais pas comme barre de progres INDETERMIN�.
   * Pour relancer une barre de progres INDETERMIN�, il faut utilis� stopProgress et non killProgress.
   * Si killProgress a �t� appel� le seul moyen de recr�er une barre de progres INDETERMIN� est de recr�er une instance.?
   * @see pata_cara.client.ProgressBarIndeterminate#stopProgress
   */
  public void killProgress ()
  {
    stop = true;
    kill = true;
  }


  /**
   * Fixe le temps d'attente entre 2 d�filements.
   * @param tpsMili le nouveau temps entre 2 d�filements.
   */
  public void setTpsAttente (int tpsMili)
  {
    tpsAttente = tpsMili;
  } /* setTpsAttente () */


  /**
   * Renvoi le temps d'attente entre 2 d�filements.
   * @return le temps d'attente entre 2 d�filements.
   */
  public int getTpsAttente ()
  {
    return tpsAttente;
  } /* getTpsAttente () */

  /**
   * Fixe la valeur actuel de la barre de d�filement.<br>
   * En passant par getProgressBar.setValue () il n'y aura aucune diff�rence sur
   * la valeur, il faut passer par cette fonction pour voir une diff�rence.
   * @param newValue int
   */
  public void setValue (int newValue)
  {
    courant = newValue;
  } /* setValue () */

  /**
   * Retourne la valeur courante de la barre de d�filement.
   * @return la valeur courante de la barre de d�filement.
   */
  public int getValue ()
  {
    return courant;
  } /* getValue () */

} // Classe ProgressBarIndeterminate
