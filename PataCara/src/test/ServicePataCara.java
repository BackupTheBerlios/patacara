/**
 * <p>Title: ServicePataCara.java</p>.
 * <p>Copyright: Copyright (c) 2005</p>.
 * @author R�my GIRAUD
 * @version 1.0
 *
 * Created on 3 f�vr. 2005
 *
 * 
 */
package test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import pata_cara.serveur.Server;

import util.http.RequeteHttp;


/**
 * <p>Classe : ServicePataCara</p>.
 * <p>Description: </p>.
 */
public class ServicePataCara
{
  private static String TASKLIST = "tasklist /FO CSV /FI \"IMAGENAME eq mysqld.exe\"";
  private static String TASKKILL = "taskkill /F /PID ";
  private static String MYSQLD = "F:\\Program Files\\EasyPHP1-7\\mysql\\bin\\mysqld";
  private static String IP_LOCALHOST = "127.0.0.1";
  private static long INTERVAL_CHECK_IP = 10000L; // 10secondes interval pour se connecter
  private static final long INTERVAL_CHECK_DECONNECTION = 60000L; //1min interval pour d�tecter une d�connexion
  
  /* Le LOGGER_INFO d'information qui va loger les infos erreur dans un fichier */
  public static Logger LOGGER_INFO = null;
  public static Logger LOGGER_ERREUR = null;
  static
  {    
    LOGGER_INFO = Logger.getLogger("ServicePataCara.info");
    LOGGER_ERREUR = Logger.getLogger ("ServicePataCara.erreur");
    Handler fileHandler;
    try
    {
      //fileHandler = new StreamHandler (new FileOutputStream (Server.FICHIER_LOG_INFO), new SimpleFormatter ());
      fileHandler = new FileHandler (Server.FICHIER_LOG_INFO, true);
      fileHandler.setFormatter(new SimpleFormatter ());
      LOGGER_INFO.addHandler(fileHandler);
      //fileHandler = new StreamHandler (new FileOutputStream (Server.FICHIER_LOG_ERREUR), new SimpleFormatter ());
      fileHandler = new FileHandler (Server.FICHIER_LOG_ERREUR, true);
      fileHandler.setFormatter(new SimpleFormatter ());
      LOGGER_ERREUR.addHandler(fileHandler);
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (SecurityException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    

  }
  
  private volatile boolean isOkMysqld; /* quand on lance mysqld, cette variable sera mis a false en cas d'echec */
  private static final long DELAI_ATTENTE_MYSQLD = 10000L;
  private String pidMysqld = null;
  private String ip = null;
  private Server serveurPataCara = null;
  
  
  public ServicePataCara ()
  {
    isOkMysqld = true;
  }
  

  public static void main (String [] args) throws IOException 
  {

    ServicePataCara service = new ServicePataCara ();
    Thread t = Thread.currentThread();
    try
    {
      service.testAenlever(t);
      //System.out.println ("Main termin�");
    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
    }
    System.out.println ("Termin�");
  }
  
  public void testAenlever (final Thread t) throws IOException, InterruptedException
  {
    JFrame f = new JFrame ("test");
    f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    JButton but = new JButton ("stop");

    but.addActionListener(new ActionListener () {

    public void actionPerformed (ActionEvent e)
    {
      stopServicePataCara(t);
      System.out.println ("Stop handler");
      
      /* actionPerformed () */
    }}); 
    f.getContentPane().add(but);
    f.pack();
    f.setVisible(true);

    startServicePataCara();
    //System.out.println ("Main termin�");
    
  }
  
  /**
   * Lance le service PataCara.
   *
   */
  public void startServicePataCara ()
  {
    try
    {
      startMysqld();
      Server.informeSiteStopServeur(); //On stoppe le dernier serveur si besoin
      lanceServeurPataCara();
      while (true)
      {
        waitDeconnection();
        stopMysqld();
        stopServeurPataCara();
        
        //reconnexion
        waitConnectionEstablish ();
        Server.informeSiteStopServeur(); //On a �t� d�connect�, on � de nouveau connect� et on peut inform� que le serveur est arreter avant d'essayer d'en relancer un autre.
        startMysqld();
        lanceServeurPataCara();
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
    }
    LOGGER_INFO.info("Fin startServicePataCara");
  }
  
  /**
   * Arrete le service PataCara.
   *
   */
  public void stopServicePataCara (Thread threadCourant)
  {
    Server.informeSiteStopServeur();
    stopMysqld();
    stopServeurPataCara();
    //On envoi une execption interupt pour arreter les threads
    threadCourant.interrupt();
  }
  
  
  /**
   * 
   * @return true si connect� a internet, false autrement.
   */
  public static boolean isConnectedToInternet ()
  {
    return !(getIP ().equals (IP_LOCALHOST));    
  }
  
  
  /**
   * Attend que la connexion a internet soit �tablie et qu'on puisse faire une requete sur google.
   * @return
   * @throws IOException
   */
  public static String waitConnectionEstablish () throws IOException, InterruptedException
  {
    String ip = null;
    while (true)
    {
      ip = checkIp ();
      //V�rication en demandant une page internet
      HttpURLConnection conn;
      try
      {
        conn = RequeteHttp.getConnexionHttpPost ("http://www.google.fr");
        LOGGER_INFO.info ("reponse : " + conn.getResponseCode ());
        break;
      }
      catch (java.net.ConnectException e)
      {
        LOGGER_INFO.severe("Exception ConnectException : " + e.getMessage());
      }
      Thread.sleep (500);
    }
    return ip;
  }
  
  /**
   * Attente d'une d�connection d'internet.
   * @throws InterruptedException
   *
   */
  public void waitDeconnection () throws InterruptedException
  {
    while (getIP ().equals (ip))
    {
        Thread.sleep (INTERVAL_CHECK_DECONNECTION);
    }
    LOGGER_INFO.info("D�connexion survenue");
  }
  
  /**
   * Fonction qui lance Mysqld et qui s'assure que celui ci est bien lanc�
   * @throws IOException
   * @throws InterruptedException
   */
  public void startMysqld () throws IOException, InterruptedException
  {
    //On r�cup�re la liste de tous les mysqld lanc� avant de lancer le notre
    ArrayList listPidMysqldStart = getAllPidMysqld();
LOGGER_INFO.info ("Nbr pid : " + listPidMysqldStart.size());
String pidList = null;
for (int i = 0; i < listPidMysqldStart.size (); ++i)
{
  pidList = listPidMysqldStart.get (i) + ",";  
}
if (null != pidList)
LOGGER_INFO.info (pidList);

	isOkMysqld = false;

    while (!isOkMysqld)
    {
      if (!isConnectedToInternet())
        waitConnectionEstablish();
      runMysqld();
      Thread.sleep (DELAI_ATTENTE_MYSQLD);
    }
    //On m�morise l'ip ou est lanc� le serveur mysqld
    ip = getIP ();
    LOGGER_INFO.info("SERVEUR MYSQLD LANCERRRRRRRRRRRRRRRRR sur IP : " + ip);    

    //On r�cup�re la liste de tous les mysqld lanc� 
    ArrayList listPidMysqldEnd = getAllPidMysqld();
    
    //Pour les log
    String list = "";
    for (int i = 0; i < listPidMysqldEnd.size (); ++i)
    {
      list = listPidMysqldEnd.get (i) + ",";  
    }
	LOGGER_INFO.info("liste pid trouv� apres lancement :  "+ list);

	//On cherche a trouver le pid lanc�
    for (int i = 0; i < listPidMysqldEnd.size(); ++i)
    {
      if (!listPidMysqldStart.contains(listPidMysqldEnd.get(i)))
      {
        pidMysqld = (String) listPidMysqldEnd.get (i);
        break;
      }
    }
    LOGGER_INFO.info ("PID TROUVER : " + pidMysqld);
  }
  
  public void lanceServeurPataCara ()
  {
    new Thread () 
    {
      /**
       * @see java.lang.Thread#run()
       */
      public void run ()
      {
        serveurPataCara = new Server ();
      }
       
    }.start();
    LOGGER_INFO.info ("Serveur PataCara Lanc�");
  }
  
  
  
  public void stopServeurPataCara ()
  {
    if (null != serveurPataCara)
      serveurPataCara.stopServer();    
  }
  
  
  
  public void runMysqld () throws IOException, InterruptedException
  {
    isOkMysqld = true; //Si on est bloqu� c'est que tout va bien
    new Thread () 
    {/**
     * @see java.lang.Thread#run()
     */
    public void run ()
    {
      Runtime run = Runtime.getRuntime();
      Process p = null;
      try
      {
        p = run.exec(MYSQLD);
        int exitCode = p.waitFor();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
      isOkMysqld = false;
      


afficheMsg(p.getErrorStream());

      /* run () */
    }}.start();
  }

  /**
   * Stop le serveur mysqld en tuant son processus
   *
   */
  public void stopMysqld ()
  {
    if (pidMysqld != null)
    {
      stopProcess(pidMysqld);
      pidMysqld = null;
    }
  }

  
  /**
   * Retourne le pid de mysqld sur une ligne de type ImageName,pid,....
   * @param line
   * @return
   */
  public static String getPidMysqld (String line)
  {
    StringTokenizer st = new StringTokenizer (line, ",");
    if (st.countTokens() < 3)
      return null;
    st.nextToken(); /* Image Name */
    return st.nextToken();
  }
  
  
  public static String checkIp ()
  {
    String ipNow = getIP ();
    while (!isConnectedToInternet())
    {
LOGGER_INFO.info("IP : " + ipNow);
      try
      {
        Thread.sleep (INTERVAL_CHECK_IP);
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
      ipNow = getIP ();
    }
    return ipNow;
  }
  
  
  /**
   * 
   * @return l'ip actuel de la machine, IP_LOCALHOST en cas d'erreur
   */
  public static String getIP ()
  {
    try
    {
      return InetAddress.getLocalHost().getHostAddress ();
    }
    catch (UnknownHostException e)
    {
      return IP_LOCALHOST;
    }
  }
  
  
  /**
   * Renvoi la liste de tous les pid du processus mysqld lanc�.
   * @return la liste de tous les pid, la liste peut etre vide
   * @throws IOException
   * @throws InterruptedException
   */
  public static ArrayList getAllPidMysqld () throws IOException, InterruptedException
  {
    ArrayList listPidMysqld = new ArrayList ();
    Runtime run = Runtime.getRuntime();
    Process p = run.exec(TASKLIST);
    
    int exitCode = p.waitFor();

    if (exitCode != 0)
      return listPidMysqld;
    
    BufferedReader outBuff = new BufferedReader (new InputStreamReader (p.getInputStream()));
    String line;
    while (null != (line = outBuff.readLine()))
    {
//      System.out.println ("Line : " + line);
//      System.out.println ("Pid : " + getPidMysqld(line));
      String pid = getPidMysqld(line);
      if (pid == null || pid.equals ("") || pid.equalsIgnoreCase("\"PID\""))
        continue;
      
      listPidMysqld.add (pid);
    }
    return listPidMysqld;
    
  }
  
  
  /**
   * Affiche sur la sortie standart le message de in.
   * @param in
   */
  public static void afficheMsg (InputStream in)
  {
    BufferedReader outBuff = new BufferedReader (new InputStreamReader (in));
    String line;
    try
    {
      while (null != (line = outBuff.readLine()))
      {
        LOGGER_INFO.info (" Line : " + line);
        break;
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    
  }
  
  /**
   * Stop le processus avec le pid PID
   * @param pid
   */
  public static void stopProcess (String pid)
  {
    Runtime run = Runtime.getRuntime();
    Process p = null;
    try
    {
      p = run.exec(TASKKILL + pid);
      int exitCode = p.waitFor();
LOGGER_INFO.info (TASKKILL + pid + " : execut�");
afficheMsg(p.getErrorStream());
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
    }
    
  }
  
  
  
}
 // Classe ServicePataCara
