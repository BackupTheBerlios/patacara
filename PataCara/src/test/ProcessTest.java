/**
 * <p>Title: ProcessTest.java</p>.
 * <p>Copyright: Copyright (c) 2005</p>.
 * @author Rémy GIRAUD
 * @version 1.0
 *
 * Created on 3 févr. 2005
 *
 * 
 */
package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import pata_cara.serveur.Server;

import util.http.RequeteHttp;


/**
 * <p>Classe : ProcessTest</p>.
 * <p>Description: </p>.
 */
public class ProcessTest
{
  private static String TASKLIST = "tasklist /FO CSV /FI \"IMAGENAME eq mysqld.exe\"";
  private static String TASKKILL = "taskkill /F /PID ";
  private static String MYSQLD = "F:\\Program Files\\EasyPHP1-7\\mysql\\bin\\mysqld";
  private static String IP_LOCALHOST = "127.0.0.1";
  private static long INTERVAL_CHECK_IP = 10000L; // 10secondes interval pour se connecter
  private static final long INTERVAL_CHECK_DECONNECTION = 60000L; //1min interval pour détecter une déconnexion
  
  private static Logger logger = Logger.getLogger("test.ProcessTest");
  
  private volatile boolean isOkMysqld; /* quand on lance mysqld, cette variable sera mis a false en cas d'echec */
  private static final long DELAI_ATTENTE_MYSQLD = 10000L;
  private String pidMysqld = null;
  private String ip = null;
  private Server serveurPataCara = null;
  
  
  public ProcessTest ()
  {
    isOkMysqld = true;
  }

  public static void main (String [] args) throws Exception
  {
    ProcessTest test = new ProcessTest ();
    test.startMysqld();
    test.lanceServeurPataCara();
    
    while (true)
    {
      test.waitDeconnection();
      test.stopMysqld();
      test.stopServeurPataCara();
      test.startMysqld();
      test.lanceServeurPataCara();
    }
    //System.out.println ("Main terminé");
  }
  
  
  /**
   * 
   * @return true si connecté a internet, false autrement.
   */
  public static boolean isConnectedToInternet ()
  {
    return !(getIP ().equals (IP_LOCALHOST));    
  }
  
  
  /**
   * Attend que la connexion a internet soit établie et qu'on puisse faire une requete sur google.
   * @return
   * @throws IOException
   */
  public static String waitConnectionEstablish () throws IOException
  {
    String ip = null;
    while (true)
    {
      ip = checkIp ();
      //Vérication en demandant une page internet
      HttpURLConnection conn;
      try
      {
        conn = RequeteHttp.getConnexionHttpPost ("http://www.google.fr");
        logger.info ("reponse : " + conn.getResponseCode ());
        break;
      }
      catch (java.net.ConnectException e)
      {
        logger.severe("Exception ConnectException : " + e.getMessage());
      }
    }
    return ip;
  }
  
  /**
   * Attente d'une déconnection d'internet.
   *
   */
  public void waitDeconnection ()
  {
    while (getIP ().equals (ip))
    {
      try
      {
        Thread.sleep (INTERVAL_CHECK_DECONNECTION);
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
    }
    logger.info("Déconnexion survenue");
  }
  
  /**
   * Fonction qui lance Mysqld et qui s'assure que celui ci est bien lancé
   * @throws IOException
   * @throws InterruptedException
   */
  public void startMysqld () throws IOException, InterruptedException
  {
    //On récupére la liste de tous les mysqld lancé avant de lancer le notre
    ArrayList listPidMysqldStart = getAllPidMysqld();
System.out.println ("Nbr pid : " + listPidMysqldStart.size());
for (int i = 0; i < listPidMysqldStart.size (); ++i)
{
System.out.print (listPidMysqldStart.get (i) + ",");  
}
System.out.println ("");

//On lance le notre
//    runMysqld();
//    
//    // On attend un certain temps (tps = tps considérer pour avoir un message d'erreur
//    Thread.sleep(DELAI_ATTENTE_MYSQLD);

	isOkMysqld = false;

    while (!isOkMysqld)
    {
      if (!isConnectedToInternet())
        waitConnectionEstablish();
      runMysqld();
      Thread.sleep (DELAI_ATTENTE_MYSQLD);
    }
    //On mémorise l'ip ou est lancé le serveur mysqld
    ip = getIP ();
    logger.info("SERVEUR MYSQLD LANCERRRRRRRRRRRRRRRRR sur IP : " + ip);    

    //On récupére la liste de tous les mysqld lancé 
    ArrayList listPidMysqldEnd = getAllPidMysqld();
    
    //Pour les log
    String list = "";
    for (int i = 0; i < listPidMysqldEnd.size (); ++i)
    {
      list = listPidMysqldEnd.get (i) + ",";  
    }
	logger.info("liste pid trouvé apres lancement :  "+ list);

	//On cherche a trouver le pid lancé
    for (int i = 0; i < listPidMysqldEnd.size(); ++i)
    {
      if (!listPidMysqldStart.contains(listPidMysqldEnd.get(i)))
      {
        pidMysqld = (String) listPidMysqldEnd.get (i);
        break;
      }
    }
    logger.info ("PID TROUVER : " + pidMysqld);
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
    System.out.println ("Serveur PataCara Lancé");
  }
  
  
//  public void relanceServeurPataCara ()
//  {
//    serveurPataCara.stopServer();
//    System.out.println ("Stop serveur PataCara");
//    lanceServeurPataCara();
//  }
  
  public void stopServeurPataCara ()
  {
    serveurPataCara.stopServer();    
  }
  
  
  
  public void runMysqld () throws IOException, InterruptedException
  {
    isOkMysqld = true; //Si on est bloqué c'est que tout va bien
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
logger.info("IP : " + ipNow);
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
   * Renvoi la liste de tous les pid du processus mysqld lancé.
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
        System.out.println ("" + new Date () + " Line : " + line);
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
logger.info (TASKKILL + pid + " : executé");
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
 // Classe ProcessTest
