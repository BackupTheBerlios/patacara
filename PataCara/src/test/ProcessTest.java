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
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;


/**
 * <p>Classe : ProcessTest</p>.
 * <p>Description: </p>.
 */
public class ProcessTest
{
  private static String TASKLIST = "tasklist /FO CSV /FI \"IMAGENAME eq mysqld.exe\"";
  private static String MYSQLD = "F:\\Program Files\\EasyPHP1-7\\mysql\\bin\\mysqld";
  
  private volatile boolean isOkMysqld; /* quand on lance mysqld, cette variable sera mis a false en cas d'echec */
  private static final long DELAI_ATTENTE = 10000;
  private String pidMysqld = null;
  
  
  public ProcessTest ()
  {
    isOkMysqld = true;
  }

  public static void main (String [] args) throws Exception
  {
    ProcessTest test = new ProcessTest ();
    test.startMysqld();
  }
  
  public static String getPidMysqld (String line)
  {
    StringTokenizer st = new StringTokenizer (line, ",");
    if (st.countTokens() < 3)
      return null;
    st.nextToken(); /* Image Name */
    return st.nextToken();
  }
  
  /**
   * Fonction qui lance Mysqld et qui s'assure que celui ci est bien lancé
   * @return
   * @throws IOException
   * @throws InterruptedException
   */
  public String startMysqld () throws IOException, InterruptedException
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
    runMysqld();
    
    // On attend un certain temps (tps = tps considérer pour avoir un message d'erreur
    Thread.sleep(DELAI_ATTENTE);
    
    while (!isOkMysqld)
    {
      runMysqld();
      Thread.sleep (DELAI_ATTENTE);
    }
    
System.out.println ("SERVEUR LANCERRRRRRRRRRRRRRRRR");    
    //On récupére la liste de tous les mysqld lancé 
    ArrayList listPidMysqldEnd = getAllPidMysqld();
for (int i = 0; i < listPidMysqldEnd.size (); ++i)
{
System.out.print (listPidMysqldEnd.get (i) + ",");  
}
System.out.println ("");

    for (int i = 0; i < listPidMysqldEnd.size(); ++i)
    {
      if (!listPidMysqldStart.contains(listPidMysqldEnd.get(i)))
      {
        pidMysqld = (String) listPidMysqldEnd.get (i);
        break;
      }
    }
System.out.println ("PID TROUVER : " + pidMysqld);
    return null;
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
   * Affiche sur la sortie standart le message de in
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
  
  
  
}
 // Classe ProcessTest
