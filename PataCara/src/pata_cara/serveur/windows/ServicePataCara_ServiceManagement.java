/**
 * <p>Title: ServicePataCara_ServiceManagement.java</p>.
 * <p>Copyright: Copyright (c) 2005</p>.
 * @author Rémy GIRAUD
 * @version 1.0
 *
 * Created on 17 févr. 2005
 *
 * 
 */
package pata_cara.serveur.windows;

import pata_cara.serveur.Server;



/**
 * <p>Classe : ServicePataCara_ServiceManagement</p>.
 * <p>Description: La classe qui gére les méthodes de rappels du service PataCara pour windows lancé par JavaExe.</p>.
 */
public class ServicePataCara_ServiceManagement
{
  
  private static ServicePataCara servicePataCara = null;
  private static Thread currentThreadServicePataCara = null;

  public static boolean serviceIsCreate ()
  {
    Server.logInfo("serviceIsCreate -> Le service va etre créé");
    return true;
  }
  
  
  public static boolean serviceIsLaunch ()
  {
    Server.logInfo("serviceIsLaunch -> Le service va etre lancé");
    return true;
  }
  
  public static boolean serviceIsDelete ()
  { 
    Server.logInfo("serviceIsDelete -> Voulez vous supprimez le service");
    return false; // pas de destruction du service
  }
  
  public static boolean serviceInit ()
  {
    Server.logInfo("serviceInit -> Lancement de l'application du service");
    ServicePataCara.afficheFenetreControle();
    currentThreadServicePataCara = 
    new Thread () {
      /**
       * @see java.lang.Thread#run()
       */
      public void run ()
      {
        if (null == servicePataCara)
          servicePataCara = new ServicePataCara ();
        servicePataCara.startServicePataCara();
        //On enregistre le thread courrant pour l'interrompre plus tard.
        /* run () */
      }
    };
    currentThreadServicePataCara.start();
    return true; // init est bien lancé
  }
  
  public static void serviceFinish ()
  {
//    TestServiceJavaExe.logInfo ("serviceFinish -> Arret du service");
  }
  
  public static String [] serviceGetInfo () 
  {
    String [] info = {"PataCaraService", 
                      "Run server PataCara", 
                      "1" /*lancement automatique avec windows */, 
                      "1" /*le service peut etre arreter manuellement*/, 
                      "D:\\Documents and Settings\\Pat\\Desktop\\Jeux Classique.bat" /* nom du fichier a executer en cas d'erreur*/, 
                      "" /*arg prog erreur*/, 
                      "" /*liste service a lancer avant celui ci*/};
    return info;
  }
  
  public static boolean serviceControl_Pause () 
  {
    Server.logInfo("serviceControl_Pause -> Mise en pause du service, service : " + servicePataCara + ", thread : " + currentThreadServicePataCara);
    if (null != servicePataCara && currentThreadServicePataCara != null)
    {
      servicePataCara.stopServicePataCara(currentThreadServicePataCara);
      servicePataCara = null;
      currentThreadServicePataCara = null;
    }
    return true;
  }
  
  public static boolean serviceControl_Continue () 
  {
    Server.logInfo("serviceControl_Continue -> Service plus en pause");
    serviceInit();
    return true;
  }
  
  public static boolean serviceControl_Stop () 
  {
    Server.logInfo("serviceControl_Stop ->Le service va etre stopé puis la méthode finish va etre appellée!");
    if (null != servicePataCara && currentThreadServicePataCara != null)
    {
      servicePataCara.stopServicePataCara(currentThreadServicePataCara);
      servicePataCara = null;
      currentThreadServicePataCara = null;
    }
    return true;
  }
  
  public static boolean serviceControl_Shutdown () 
  {
    Server.logInfo("serviceControl_Shutdown -> Windows s'arrete ou redemarre");
    return true;
  } 
  

  
}
 // Classe ServicePataCara_ServiceManagement
