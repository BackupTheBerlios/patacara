package util.jni.windows.registre;

/**
 * <p>Title: Patch pour l'application PataCara</p>
 * <p>Description: Ce projet à pour but d'installer les mises à jour de PataCara sur la machine du client. Apllication dédiée à Windows</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Patachou
 * @version 1.0
 */


import com.ice.jni.registry.Registry;
import com.ice.jni.registry.RegistryKey;
import com.ice.jni.registry.RegistryException;
import com.ice.jni.registry.NoSuchKeyException;
import com.ice.jni.registry.NoSuchValueException;
import com.ice.jni.registry.RegStringValue;

/**
 * Title:        BaseRegistreWin
 * Description:  Permet d'enregistrer les clés dans la base de registre de windows
 *               necessaire à l'application PataCara, et pouvoir les consulter.
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Rémy Giraud
 * @version 1.0
 */



public class BaseRegistreWin
{

    private static final String TOP_CLE_DEST = "HKCU"; //La racine de la clé
    private static final String SUB_CLE_DEST = "Software"; //Les sous clés, par ex Software\sousRep\SousSousRep
    private static final String NOM_CLE = "PataCara"; //Le nom de la clé finale.
    private static final String NOM_ATTR_REP_INSTALL = "repInstall"; //Le nom de l'attribut.


    private static final int ERREUR = -1; //Une erreur retournée.
    private static final int CLE_CREE = 0; //Lorsque la clé à été créé avec succes.
    private static final int CLE_EXISTE = 1; //Lorsque la clé existé déjà.
    private static final int CLE_SUPPRIMER = 2; //Lorsqu'une clé est supprimée.
    private static final int CLE_INEXISTANTE = 3; //Lorsque la clé est inexistante.
    private static final int ATTR_SUPPRIMER = 4; //Lorsqu'un attribut a été supprimé.
    private static final int ATTR_INEXISTANT = 5; //Lorsque l'attribut n'est pas présent.



    /**
     * Installe toutes les clés (dans la base de registre) nécessaires à la
     * gestion de PataCara sous Windows.
     * @return true si toutes les clés ont pu être correctement installées.<br>
     *         false autrement.
     */
    public static boolean installAllClePataCara ()
    {
      return installCleRepInstall();
    } /* installAllClePataCara () */


    /**
     * Vérifie que l'on peut acceder à la clé topCle\nomCle
     * @param topCle la topCle ("HKCU", "HKLM"...).
     * @param nomCle le nom de la clé (Sofware\PataCara).
     * @return true si on a pu y acceder,<br>
     *         false autrement.
     */
    private static boolean verifCleExiste (String topCle, String nomCle)
    {
      RegistryKey keyPata = Registry.getTopLevelKey (topCle);
      if (keyPata == null) //La clé a pas été trouvée
      {
        System.err.println ("La clé : " + TOP_CLE_DEST + " n'est pas trouvé.");
        return false;
      }

      RegistryKey cheminCle = null;

      try
      { //On accéde à la clé voulue.
        cheminCle = keyPata.openSubKey (nomCle, RegistryKey.ACCESS_READ);
      }
      catch (NoSuchKeyException ex1)
      {
        return false;
      }
      catch (RegistryException ex1)
      {
        return false;
      }
      //Vérification que la clé a bien été trouvée.
      if (cheminCle == null)
        return false;

      return true;
    } /* verifCleExiste () */

    /**
     * Installe l'attribut RepInstall dans la base de registre.
     * Si la clé de l'attribut n'existe pas, alors celle ci est crée.
     * @return true si l'attribut a pu etre inséré, false autrement.
     */
    public static boolean installCleRepInstall ()
    {
      return installCleRepInstall(System.getProperty ("user.dir"));
    } /* installCleRepInstall () */


  /**
   * Installe l'attribut RepInstall dans la base de registre. Si la clé de
   * l'attribut n'existe pas, alors celle ci est crée.
   * @param rep le nouveau repertoire d'installation.
   *
   * @return true si l'attribut a pu etre inséré, false autrement.
   */
  public static boolean installCleRepInstall (String rep)
    {
      /*Vérification que la clé TOP_CLE_DEST\SUB_CLE_DEST\NOM_CLE n'existe pas.*/
      RegistryKey keyPata = Registry.getTopLevelKey (TOP_CLE_DEST);
      if (keyPata == null) //La clé a pas été trouvée
      {
        System.err.println ("La clé : " + TOP_CLE_DEST + " n'est pas trouvé.");
        return false;
      }

      if (ERREUR == creerOuOuvrirCle (keyPata, SUB_CLE_DEST + "\\" + NOM_CLE))
      {
        System.err.println ("Impossible de créer la clé" + SUB_CLE_DEST + "\\" +
                            NOM_CLE);
        return false;
      }
      //La clé a soit été créée soit été ouverte, initialisation de l'attribut
      //avec le répertoire du client.
      return setValueAttr (keyPata, SUB_CLE_DEST + "\\" + NOM_CLE,
                           NOM_ATTR_REP_INSTALL,
                           rep);
    } /* installCleRepInstall () */


    /**
     * Renvoi la valeur de la clé RepInstall.
     * @return la valeur de la clé RepInstall,<br>
     *         null en cas de problème.
     */
    public static String getValeurCleRepInstall ()
    {
      return getValueAttr(TOP_CLE_DEST, SUB_CLE_DEST + java.io.File.separator + NOM_CLE,
                          NOM_ATTR_REP_INSTALL);
    }





    /**
     * Efface l'attribut RepInstall du registre sans effacer la clé.
     * @return true en cas de succès,<br>
     * false autrement.
     */
    private static boolean removeCleRepInstall ()
    {
      return (ATTR_SUPPRIMER ==
              supprimerAttribut (TOP_CLE_DEST, SUB_CLE_DEST + java.io.File.separator + NOM_CLE,
                                 NOM_ATTR_REP_INSTALL));
    }/* removeCleRepInstall */


    public static boolean removeAllClePataCara ()
    {
      return (CLE_SUPPRIMER == supprimerCle(TOP_CLE_DEST, SUB_CLE_DEST, NOM_CLE));
    } /* removeAllClePataCara () */


    /**
     * -------------------------------------------------------------------------
     *
     *           EXEMPLEEEEE D'UTILISATION DES FONCTIONS.
     *
     * -------------------------------------------------------------------------
     * Ajoute à la base de registre de Windows l'attribut NOM_ATTR_REP_INSTALL
     * et qui à pour valeur le répertoire où est lancé l'application. Cet
     * attribut est rajouté à la clé DESTINATION_CLE\\NOM_CLE.
     *
     * @return true si l'attribut à bien pu etre inséré dans la base de registre.
     */
    private static boolean createAttrRepInstall ()
    {
        /*Vérification que la clé TOP_CLE_DEST\SUB_CLE_DEST\NOM_CLE n'existe pas.*/
        RegistryKey keyPata = Registry.getTopLevelKey (TOP_CLE_DEST);
        if (keyPata == null) //La clé a pas été trouvée
        {
            System.err.println("La clé : " + TOP_CLE_DEST + " n'est pas trouvé.");
            return false;
        }

        int reponse;
        if (ERREUR == (reponse = creerOuOuvrirCle(keyPata, SUB_CLE_DEST + java.io.File.separator + NOM_CLE)))
        {
          System.err.println("Impossible de créer la clé" + SUB_CLE_DEST + java.io.File.separator + NOM_CLE);
          return false;
        }
        else if (CLE_EXISTE == reponse)
        {
          System.out.println("La clé '" + SUB_CLE_DEST + java.io.File.separator + NOM_CLE +"' existait déjà");

          //On tente de la supprimer
          if (ERREUR == (reponse = supprimerCle (keyPata, SUB_CLE_DEST, NOM_CLE)))
            System.out.println("Erreur suppression clé");
          else if (CLE_INEXISTANTE == reponse)
            System.out.println("La clé est inexitante");
          else //Succès
            System.out.println("La clé a été supprimée avec succès");
        }

        //On test de récupérer une précédente valeur
        String repInstall = getValueAttr(keyPata, SUB_CLE_DEST + java.io.File.separator + NOM_CLE,
                                         NOM_ATTR_REP_INSTALL);
        if (null == repInstall)
          System.out.println("Attribut non présent");
        else
        {//Attribut présent, on le supprime
          System.out.println ("Attribut présent avec pour valeur : " + repInstall);
          if (ERREUR ==
              (reponse = supprimerAttribut (keyPata, SUB_CLE_DEST + java.io.File.separator + NOM_CLE,
                                            NOM_ATTR_REP_INSTALL)))
            System.out.println ("Impossible de suppimer l'attribut");
          else if (ATTR_INEXISTANT == reponse)
          {
            System.out.println ("L'attribut été déjà supprimé");
          }
          else if (ATTR_SUPPRIMER == reponse)
            System.out.println("L'attribut à bien été supprimé");
        }


        //La clé est créer il faut initialisé l'attribut avec le répertoire ou la commande java est exécutée.
        return setValueAttr(keyPata, SUB_CLE_DEST + java.io.File.separator + NOM_CLE, NOM_ATTR_REP_INSTALL,
                     System.getProperty("user.dir"));



    } /* createAttrRepInstall () */



    /**
     * Crée ou ouvre la clé ayant comme topCle (HKCU, HKLM...) topCle et pour
     * nom de clé nomCle.
     * Par exemple, pour créer HKCU\Software\PataCara, topCle sera une instance
     * optenu par exemple avec Registry.getTopLevelKey ("HKCU") et topCle
     * contiendra Software\PataCara.
     * @param topCle l'instance de la clé representant le topLevel.
     * @param nomCle le chemin de la nouvelle clé.
     * @return CLE_CREE si la clé a été créée, CLE_EXISTE si la clé a été ouverte.
     *         ERREUR en cas d'erreur.
     */
    private static int creerOuOuvrirCle (RegistryKey topCle, String nomCle)
    {
        RegistryKey cheminCle ;

        try
        {//On accéde à la clé voulue.
            cheminCle = topCle.createSubKey (nomCle, "",
                                             RegistryKey.ACCESS_WRITE);
        }
        catch (RegistryException ex)
        {
            cheminCle = null ;
            System.err.println ("Erreur en créant la sous clé: " +
                                ex.getMessage ());
            return ERREUR;
        }

        if (cheminCle != null)
        {
          try
          {//Fermeture des flux
            cheminCle.flushKey ();
            cheminCle.closeKey ();
          }
          catch (RegistryException ex)
          {
            cheminCle = null;
            System.err.println
                ("Erreur en flushant ou en fermant le fux de la clé. "
                 + ex.getMessage ());
            return ERREUR;
          }
        }
        //Vérification si le flux a été ouvert ou créé.
        return (cheminCle.wasCreated() ? CLE_CREE : CLE_EXISTE);
    } /* creerOuOuvrir () */


    /**
     * Supprime la clé nomCleASupprimer qui à pour
     * chemin topCle\nomCle\nomCleASupprimer où topCle est un objet créé avec
     * (par ex) Registry.getTopLevelKey ("HKCU"), nomCle le chemin juste avant ma clé à supprimer.
     * @param topCle l'instance de la clé representant le topLevel.
     * @param nomCle le chemin de la clé.
     * @param nomCleASupprimer le nom de la clé à supprimer.
     * @return int ERREUR en cas d'erreur,<br>
     *             CLE_SUPPRIMER en cas de succès,<br>
     *             CLE_INEXISTANTE si la clé n'existée pas.
     */
    private static int supprimerCle (RegistryKey topCle, String nomCle, String nomCleASupprimer)
    {
      RegistryKey cheminCle = null;
      try
      { //On accéde à la clé voulue.
        cheminCle = topCle.openSubKey (nomCle, RegistryKey.ACCESS_WRITE);
      }
      catch (NoSuchKeyException ex1)
      {
        System.err.println
      ("Erreur supprimeCle (2) de '" + nomCleASupprimer + "', "
       + ex1.getMessage ());

        return ERREUR;
      }
      catch (RegistryException ex1)
      {
        System.err.println
            ("Erreur supprimeCle (1) de '" + nomCleASupprimer + "', "
             + ex1.getMessage ());

        return ERREUR;
      }
      //Vérification que la clé a bien été trouvée.
      if (cheminCle == null)
        return ERREUR;

      try
      {
        cheminCle.deleteSubKey (nomCleASupprimer);
      }
      catch (NoSuchKeyException ex)
      {
        System.err.println
            ("La clé'" + nomCle + java.io.File.separator
             + nomCleASupprimer + "' n'existe pas.");
        return CLE_INEXISTANTE;
      }
      catch (RegistryException ex)
      {
        System.err.println
            ("Erreur en effaçant la clé'" + nomCleASupprimer
             + "', " + ex.getMessage ());
        return ERREUR;
      }
      return CLE_SUPPRIMER;
    } /* supprimerCle () */


    /**
     * Supprime la clé nomCleASupprimer qui à pour
     * chemin topCle\nomCle\nomCleASupprimer où topCle est par ex "HKCU",
     * nomCle le chemin juste avant ma clé à supprimer.
     * @param topCle ("HKCU", "HKLM"...).
     * @param nomCle le chemin de la clé.
     * @param nomCleASupprimer le nom de la clé à supprimer.
     * @return int ERREUR en cas d'erreur,<br>
     *             CLE_SUPPRIMER en cas de succès,<br>
     *             CLE_INEXISTANTE si la clé n'existée pas.
     */
    private static int supprimerCle (String topCle, String nomCle, String nomCleASupprimer)
    {
      RegistryKey cle = Registry.getTopLevelKey(topCle);
      if (null == cle)
        return ERREUR;
      return supprimerCle(cle, nomCle, nomCleASupprimer);
    } /* supprimerCle () */




    /**
     * Supprime l'attribut nomAttr qui se
     * trouve a la clé : topCle\nomCle ou topCle est un objet créé avec (par
     * ex) Registry.getTopLevelKey ("HKCU").
     *
     * @param topCle l'instance de la clé representant le topLevel.
     * @param nomCle le chemin de la clé.
     * @param nomAttr le nom de l'attribut à supprimer.
     * @return ATT_SUPPRIMER en cas de succès<br> ERREUR en cas d'erreur et
     * CLE_INEXISTANTE si la topcle\nomclé n'existe pas et
     * ATTR_INEXISTANT si l'attribut n'existait pas.
     */
    private static int supprimerAttribut (RegistryKey topCle, String nomCle,
                                          String nomAttr)
    {
      RegistryKey cheminCle = null;
      try
      { //On accéde à la clé voulue.
        cheminCle = topCle.openSubKey (nomCle, RegistryKey.ACCESS_WRITE);
      }
      catch (NoSuchKeyException ex1)
      {
        System.err.println
      ("Erreur 2 de '" + nomAttr + "', "
       + ex1.getMessage ());

        return CLE_INEXISTANTE;
      }
      catch (RegistryException ex1)
      {
        System.err.println
            ("Erreur 1 de '" + nomAttr + "', "
             + ex1.getMessage ());

        return ERREUR;
      }
      //Vérification que la clé a bien été trouvée.
      if (cheminCle == null)
        return ERREUR;

      try
      {
        cheminCle.deleteValue (nomAttr);
      }
      catch (NoSuchValueException ex)
      {
        System.err.println
            ("Attribut '" + nomAttr + "' n'existe pas.");
        return ATTR_INEXISTANT;
      }
      catch (RegistryException ex)
      {
        System.err.println
            ("Erreur en détruisant l'attribut : '" + nomAttr
             + "', " + ex.getMessage ());
        return ERREUR;
      }



      return ATTR_SUPPRIMER;
    }
    /* supprimerAttribut () */


    /**
     * Supprime l'attribut nomAttr qui se
     * trouve a la clé : topCle\nomCle.
     *
     * @param topCle le topLevel (HKCU, HKLM...).
     * @param nomCle le chemin de la clé.
     * @param nomAttr le nom de l'attribut à supprimer.
     * @return ATT_SUPPRIMER en cas de succès<br> ERREUR en cas d'erreur et
     * CLE_INEXISTANTE si la topcle\nomclé n'existe pas et
     * ATTR_INEXISTANT si l'attribut n'existait pas.
     */
    private static int supprimerAttribut (String topCle, String nomCle,
                                          String nomAttr)
    {
      RegistryKey cle = Registry.getTopLevelKey(topCle);
      if (null == cle)
        return ERREUR;
      return supprimerAttribut(cle, nomCle, nomAttr);
    }
    /* supprimerAttribut () */


    /**
     * Renvoi la valeur de l'attribut nomAttr qui se trouve a la clé :
     * topCle\nomCle ou topCle est un objet créé avec (par ex) Registry.getTopLevelKey ("HKCU").
     * @param topCle l'instance de la clé representant le topLevel.
     * @param nomCle le chemin de la clé.
     * @param nomAttr le nom de l'attribut.
     * @return la valeur de la chaine de l'attribut.
     * null en cas de problème.
     */
    private static String getValueAttr (RegistryKey topCle, String nomCle, String nomAttr)
    {
      RegistryKey cheminCle = null;
      try
      {//On accéde à la clé voulue.
        cheminCle = topCle.openSubKey (nomCle, RegistryKey.ACCESS_READ);
      }
      catch (NoSuchKeyException ex1)
      {
        return null;
      }
      catch (RegistryException ex1)
      {
        return null;
      }
      //Vérification que la clé a bien été trouvée.
      if (cheminCle == null)
        return null;

      try
      {//On récupere la valeur de l'attribut voulu.
        String value = cheminCle.getStringValue (nomAttr);
        return value;
      }
      catch (RegistryException ex)
      {
        System.err.println
            ("Erreur en accédant à la valeur de '" + nomAttr + "', "
             + ex.getMessage ());
        return null;
      }
    } /* getValueAttr () */



    /**
     * Renvoi la valeur de l'attribut nomAttr qui se trouve a la clé :
     * topCle\nomCle.
     * @param topCle ("HKCU", "HKLM"...).
     * @param nomCle le chemin de la clé.
     * @param nomAttr le nom de l'attribut.
     * @return la valeur de la chaine de l'attribut.
     * null en cas de problème.
     */
    private static String getValueAttr (String topCle, String nomCle, String nomAttr)
    {
      RegistryKey cle = Registry.getTopLevelKey(topCle);
      if (null == cle)
        return null;
      return getValueAttr(cle, nomCle, nomAttr);

    } /* getValueAttr () */



    /**
     * fixe la valeur de l'attribut nomAttr (<b>existant ou non</b>) qui se
     * trouve a la clé : topCle\nomCle ou topCle est un objet créé avec (par
     * ex) Registry.getTopLevelKey ("HKCU").
     *
     * @param topCle l'instance de la clé representant le topLevel.
     * @param nomCle le chemin de la clé.
     * @param nomAttr le nom de l'attribut.
     * @param newValeur la nouvelle valeur de l'attribut.
     * @return true en cas de succès<br> false autrement.
     */
    private static boolean setValueAttr (RegistryKey topCle, String nomCle,
                                         String nomAttr, String newValeur)
    {
      RegistryKey cheminCle = null;
      try
      { //On accéde à la clé voulue.
        cheminCle = topCle.openSubKey (nomCle, RegistryKey.ACCESS_WRITE);
      }
      catch (NoSuchKeyException ex1)
      {
        System.err.println
      ("Erreur setValueAttr 2 de '" + nomAttr + "', "
       + ex1.getMessage ());

        return false;
      }
      catch (RegistryException ex1)
      {
        System.err.println
            ("Erreur 1 de '" + nomAttr + "', "
             + ex1.getMessage ());

        return false;
      }
      //Vérification que la clé a bien été trouvée.
      if (cheminCle == null)
        return false;

      //On fixe la valeur de l'attribut voulu.
      RegStringValue val = new RegStringValue (cheminCle, nomAttr, newValeur);

      try
      {
        cheminCle.setValue (val);
        cheminCle.flushKey ();
      }
      catch (RegistryException ex)
      {
        System.err.println
            ("Erreur 3 '" + nomAttr + "', "
             + ex.getMessage ());

        return false;
      }
      return true;
    } /* setValueAttr () */




    /**
     * fixe la valeur de l'attribut nomAttr (<b>existant ou non</b>) qui se
     * trouve a la clé : topCle\nomCle.
     *
     * @param topCle ("HKCU", "HKLM"...).
     * @param nomCle le chemin de la clé.
     * @param nomAttr le nom de l'attribut.
     * @param newValeur la nouvelle valeur de l'attribut.
     * @return true en cas de succès<br> false autrement.
     */
    private static boolean setValueAttr (String topCle, String nomCle,
                                         String nomAttr, String newValeur)
    {
      RegistryKey cle = Registry.getTopLevelKey (topCle);
      if (null == cle)
        return false;
      return setValueAttr (cle, nomCle, nomAttr, newValeur);
    } /* setValueAttr () */






    public static void main (String arg [])
    {
      if (verifCleExiste("HKCU", "Software\\Patacara"))
        System.out.println ("la clé existe bien");
      else
        System.out.println("La clé n'existe pas");
      //installCleRepInstall ();
//      removeCleRepInstall();
      removeAllClePataCara();
    }
} // BaseRegistreWin
