package util.jni.windows.registre;

/**
 * <p>Title: Patch pour l'application PataCara</p>
 * <p>Description: Ce projet � pour but d'installer les mises � jour de PataCara sur la machine du client. Apllication d�di�e � Windows</p>
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
 * Description:  Permet d'enregistrer les cl�s dans la base de registre de windows
 *               necessaire � l'application PataCara, et pouvoir les consulter.
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author R�my Giraud
 * @version 1.0
 */



public class BaseRegistreWin
{

    private static final String TOP_CLE_DEST = "HKCU"; //La racine de la cl�
    private static final String SUB_CLE_DEST = "Software"; //Les sous cl�s, par ex Software\sousRep\SousSousRep
    private static final String NOM_CLE = "PataCara"; //Le nom de la cl� finale.
    private static final String NOM_ATTR_REP_INSTALL = "repInstall"; //Le nom de l'attribut.


    private static final int ERREUR = -1; //Une erreur retourn�e.
    private static final int CLE_CREE = 0; //Lorsque la cl� � �t� cr�� avec succes.
    private static final int CLE_EXISTE = 1; //Lorsque la cl� exist� d�j�.
    private static final int CLE_SUPPRIMER = 2; //Lorsqu'une cl� est supprim�e.
    private static final int CLE_INEXISTANTE = 3; //Lorsque la cl� est inexistante.
    private static final int ATTR_SUPPRIMER = 4; //Lorsqu'un attribut a �t� supprim�.
    private static final int ATTR_INEXISTANT = 5; //Lorsque l'attribut n'est pas pr�sent.



    /**
     * Installe toutes les cl�s (dans la base de registre) n�cessaires � la
     * gestion de PataCara sous Windows.
     * @return true si toutes les cl�s ont pu �tre correctement install�es.<br>
     *         false autrement.
     */
    public static boolean installAllClePataCara ()
    {
      return installCleRepInstall();
    } /* installAllClePataCara () */


    /**
     * V�rifie que l'on peut acceder � la cl� topCle\nomCle
     * @param topCle la topCle ("HKCU", "HKLM"...).
     * @param nomCle le nom de la cl� (Sofware\PataCara).
     * @return true si on a pu y acceder,<br>
     *         false autrement.
     */
    private static boolean verifCleExiste (String topCle, String nomCle)
    {
      RegistryKey keyPata = Registry.getTopLevelKey (topCle);
      if (keyPata == null) //La cl� a pas �t� trouv�e
      {
        System.err.println ("La cl� : " + TOP_CLE_DEST + " n'est pas trouv�.");
        return false;
      }

      RegistryKey cheminCle = null;

      try
      { //On acc�de � la cl� voulue.
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
      //V�rification que la cl� a bien �t� trouv�e.
      if (cheminCle == null)
        return false;

      return true;
    } /* verifCleExiste () */

    /**
     * Installe l'attribut RepInstall dans la base de registre.
     * Si la cl� de l'attribut n'existe pas, alors celle ci est cr�e.
     * @return true si l'attribut a pu etre ins�r�, false autrement.
     */
    public static boolean installCleRepInstall ()
    {
      return installCleRepInstall(System.getProperty ("user.dir"));
    } /* installCleRepInstall () */


  /**
   * Installe l'attribut RepInstall dans la base de registre. Si la cl� de
   * l'attribut n'existe pas, alors celle ci est cr�e.
   * @param rep le nouveau repertoire d'installation.
   *
   * @return true si l'attribut a pu etre ins�r�, false autrement.
   */
  public static boolean installCleRepInstall (String rep)
    {
      /*V�rification que la cl� TOP_CLE_DEST\SUB_CLE_DEST\NOM_CLE n'existe pas.*/
      RegistryKey keyPata = Registry.getTopLevelKey (TOP_CLE_DEST);
      if (keyPata == null) //La cl� a pas �t� trouv�e
      {
        System.err.println ("La cl� : " + TOP_CLE_DEST + " n'est pas trouv�.");
        return false;
      }

      if (ERREUR == creerOuOuvrirCle (keyPata, SUB_CLE_DEST + "\\" + NOM_CLE))
      {
        System.err.println ("Impossible de cr�er la cl�" + SUB_CLE_DEST + "\\" +
                            NOM_CLE);
        return false;
      }
      //La cl� a soit �t� cr��e soit �t� ouverte, initialisation de l'attribut
      //avec le r�pertoire du client.
      return setValueAttr (keyPata, SUB_CLE_DEST + "\\" + NOM_CLE,
                           NOM_ATTR_REP_INSTALL,
                           rep);
    } /* installCleRepInstall () */


    /**
     * Renvoi la valeur de la cl� RepInstall.
     * @return la valeur de la cl� RepInstall,<br>
     *         null en cas de probl�me.
     */
    public static String getValeurCleRepInstall ()
    {
      return getValueAttr(TOP_CLE_DEST, SUB_CLE_DEST + java.io.File.separator + NOM_CLE,
                          NOM_ATTR_REP_INSTALL);
    }





    /**
     * Efface l'attribut RepInstall du registre sans effacer la cl�.
     * @return true en cas de succ�s,<br>
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
     * Ajoute � la base de registre de Windows l'attribut NOM_ATTR_REP_INSTALL
     * et qui � pour valeur le r�pertoire o� est lanc� l'application. Cet
     * attribut est rajout� � la cl� DESTINATION_CLE\\NOM_CLE.
     *
     * @return true si l'attribut � bien pu etre ins�r� dans la base de registre.
     */
    private static boolean createAttrRepInstall ()
    {
        /*V�rification que la cl� TOP_CLE_DEST\SUB_CLE_DEST\NOM_CLE n'existe pas.*/
        RegistryKey keyPata = Registry.getTopLevelKey (TOP_CLE_DEST);
        if (keyPata == null) //La cl� a pas �t� trouv�e
        {
            System.err.println("La cl� : " + TOP_CLE_DEST + " n'est pas trouv�.");
            return false;
        }

        int reponse;
        if (ERREUR == (reponse = creerOuOuvrirCle(keyPata, SUB_CLE_DEST + java.io.File.separator + NOM_CLE)))
        {
          System.err.println("Impossible de cr�er la cl�" + SUB_CLE_DEST + java.io.File.separator + NOM_CLE);
          return false;
        }
        else if (CLE_EXISTE == reponse)
        {
          System.out.println("La cl� '" + SUB_CLE_DEST + java.io.File.separator + NOM_CLE +"' existait d�j�");

          //On tente de la supprimer
          if (ERREUR == (reponse = supprimerCle (keyPata, SUB_CLE_DEST, NOM_CLE)))
            System.out.println("Erreur suppression cl�");
          else if (CLE_INEXISTANTE == reponse)
            System.out.println("La cl� est inexitante");
          else //Succ�s
            System.out.println("La cl� a �t� supprim�e avec succ�s");
        }

        //On test de r�cup�rer une pr�c�dente valeur
        String repInstall = getValueAttr(keyPata, SUB_CLE_DEST + java.io.File.separator + NOM_CLE,
                                         NOM_ATTR_REP_INSTALL);
        if (null == repInstall)
          System.out.println("Attribut non pr�sent");
        else
        {//Attribut pr�sent, on le supprime
          System.out.println ("Attribut pr�sent avec pour valeur : " + repInstall);
          if (ERREUR ==
              (reponse = supprimerAttribut (keyPata, SUB_CLE_DEST + java.io.File.separator + NOM_CLE,
                                            NOM_ATTR_REP_INSTALL)))
            System.out.println ("Impossible de suppimer l'attribut");
          else if (ATTR_INEXISTANT == reponse)
          {
            System.out.println ("L'attribut �t� d�j� supprim�");
          }
          else if (ATTR_SUPPRIMER == reponse)
            System.out.println("L'attribut � bien �t� supprim�");
        }


        //La cl� est cr�er il faut initialis� l'attribut avec le r�pertoire ou la commande java est ex�cut�e.
        return setValueAttr(keyPata, SUB_CLE_DEST + java.io.File.separator + NOM_CLE, NOM_ATTR_REP_INSTALL,
                     System.getProperty("user.dir"));



    } /* createAttrRepInstall () */



    /**
     * Cr�e ou ouvre la cl� ayant comme topCle (HKCU, HKLM...) topCle et pour
     * nom de cl� nomCle.
     * Par exemple, pour cr�er HKCU\Software\PataCara, topCle sera une instance
     * optenu par exemple avec Registry.getTopLevelKey ("HKCU") et topCle
     * contiendra Software\PataCara.
     * @param topCle l'instance de la cl� representant le topLevel.
     * @param nomCle le chemin de la nouvelle cl�.
     * @return CLE_CREE si la cl� a �t� cr��e, CLE_EXISTE si la cl� a �t� ouverte.
     *         ERREUR en cas d'erreur.
     */
    private static int creerOuOuvrirCle (RegistryKey topCle, String nomCle)
    {
        RegistryKey cheminCle ;

        try
        {//On acc�de � la cl� voulue.
            cheminCle = topCle.createSubKey (nomCle, "",
                                             RegistryKey.ACCESS_WRITE);
        }
        catch (RegistryException ex)
        {
            cheminCle = null ;
            System.err.println ("Erreur en cr�ant la sous cl�: " +
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
                ("Erreur en flushant ou en fermant le fux de la cl�. "
                 + ex.getMessage ());
            return ERREUR;
          }
        }
        //V�rification si le flux a �t� ouvert ou cr��.
        return (cheminCle.wasCreated() ? CLE_CREE : CLE_EXISTE);
    } /* creerOuOuvrir () */


    /**
     * Supprime la cl� nomCleASupprimer qui � pour
     * chemin topCle\nomCle\nomCleASupprimer o� topCle est un objet cr�� avec
     * (par ex) Registry.getTopLevelKey ("HKCU"), nomCle le chemin juste avant ma cl� � supprimer.
     * @param topCle l'instance de la cl� representant le topLevel.
     * @param nomCle le chemin de la cl�.
     * @param nomCleASupprimer le nom de la cl� � supprimer.
     * @return int ERREUR en cas d'erreur,<br>
     *             CLE_SUPPRIMER en cas de succ�s,<br>
     *             CLE_INEXISTANTE si la cl� n'exist�e pas.
     */
    private static int supprimerCle (RegistryKey topCle, String nomCle, String nomCleASupprimer)
    {
      RegistryKey cheminCle = null;
      try
      { //On acc�de � la cl� voulue.
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
      //V�rification que la cl� a bien �t� trouv�e.
      if (cheminCle == null)
        return ERREUR;

      try
      {
        cheminCle.deleteSubKey (nomCleASupprimer);
      }
      catch (NoSuchKeyException ex)
      {
        System.err.println
            ("La cl�'" + nomCle + java.io.File.separator
             + nomCleASupprimer + "' n'existe pas.");
        return CLE_INEXISTANTE;
      }
      catch (RegistryException ex)
      {
        System.err.println
            ("Erreur en effa�ant la cl�'" + nomCleASupprimer
             + "', " + ex.getMessage ());
        return ERREUR;
      }
      return CLE_SUPPRIMER;
    } /* supprimerCle () */


    /**
     * Supprime la cl� nomCleASupprimer qui � pour
     * chemin topCle\nomCle\nomCleASupprimer o� topCle est par ex "HKCU",
     * nomCle le chemin juste avant ma cl� � supprimer.
     * @param topCle ("HKCU", "HKLM"...).
     * @param nomCle le chemin de la cl�.
     * @param nomCleASupprimer le nom de la cl� � supprimer.
     * @return int ERREUR en cas d'erreur,<br>
     *             CLE_SUPPRIMER en cas de succ�s,<br>
     *             CLE_INEXISTANTE si la cl� n'exist�e pas.
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
     * trouve a la cl� : topCle\nomCle ou topCle est un objet cr�� avec (par
     * ex) Registry.getTopLevelKey ("HKCU").
     *
     * @param topCle l'instance de la cl� representant le topLevel.
     * @param nomCle le chemin de la cl�.
     * @param nomAttr le nom de l'attribut � supprimer.
     * @return ATT_SUPPRIMER en cas de succ�s<br> ERREUR en cas d'erreur et
     * CLE_INEXISTANTE si la topcle\nomcl� n'existe pas et
     * ATTR_INEXISTANT si l'attribut n'existait pas.
     */
    private static int supprimerAttribut (RegistryKey topCle, String nomCle,
                                          String nomAttr)
    {
      RegistryKey cheminCle = null;
      try
      { //On acc�de � la cl� voulue.
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
      //V�rification que la cl� a bien �t� trouv�e.
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
            ("Erreur en d�truisant l'attribut : '" + nomAttr
             + "', " + ex.getMessage ());
        return ERREUR;
      }



      return ATTR_SUPPRIMER;
    }
    /* supprimerAttribut () */


    /**
     * Supprime l'attribut nomAttr qui se
     * trouve a la cl� : topCle\nomCle.
     *
     * @param topCle le topLevel (HKCU, HKLM...).
     * @param nomCle le chemin de la cl�.
     * @param nomAttr le nom de l'attribut � supprimer.
     * @return ATT_SUPPRIMER en cas de succ�s<br> ERREUR en cas d'erreur et
     * CLE_INEXISTANTE si la topcle\nomcl� n'existe pas et
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
     * Renvoi la valeur de l'attribut nomAttr qui se trouve a la cl� :
     * topCle\nomCle ou topCle est un objet cr�� avec (par ex) Registry.getTopLevelKey ("HKCU").
     * @param topCle l'instance de la cl� representant le topLevel.
     * @param nomCle le chemin de la cl�.
     * @param nomAttr le nom de l'attribut.
     * @return la valeur de la chaine de l'attribut.
     * null en cas de probl�me.
     */
    private static String getValueAttr (RegistryKey topCle, String nomCle, String nomAttr)
    {
      RegistryKey cheminCle = null;
      try
      {//On acc�de � la cl� voulue.
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
      //V�rification que la cl� a bien �t� trouv�e.
      if (cheminCle == null)
        return null;

      try
      {//On r�cupere la valeur de l'attribut voulu.
        String value = cheminCle.getStringValue (nomAttr);
        return value;
      }
      catch (RegistryException ex)
      {
        System.err.println
            ("Erreur en acc�dant � la valeur de '" + nomAttr + "', "
             + ex.getMessage ());
        return null;
      }
    } /* getValueAttr () */



    /**
     * Renvoi la valeur de l'attribut nomAttr qui se trouve a la cl� :
     * topCle\nomCle.
     * @param topCle ("HKCU", "HKLM"...).
     * @param nomCle le chemin de la cl�.
     * @param nomAttr le nom de l'attribut.
     * @return la valeur de la chaine de l'attribut.
     * null en cas de probl�me.
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
     * trouve a la cl� : topCle\nomCle ou topCle est un objet cr�� avec (par
     * ex) Registry.getTopLevelKey ("HKCU").
     *
     * @param topCle l'instance de la cl� representant le topLevel.
     * @param nomCle le chemin de la cl�.
     * @param nomAttr le nom de l'attribut.
     * @param newValeur la nouvelle valeur de l'attribut.
     * @return true en cas de succ�s<br> false autrement.
     */
    private static boolean setValueAttr (RegistryKey topCle, String nomCle,
                                         String nomAttr, String newValeur)
    {
      RegistryKey cheminCle = null;
      try
      { //On acc�de � la cl� voulue.
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
      //V�rification que la cl� a bien �t� trouv�e.
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
     * trouve a la cl� : topCle\nomCle.
     *
     * @param topCle ("HKCU", "HKLM"...).
     * @param nomCle le chemin de la cl�.
     * @param nomAttr le nom de l'attribut.
     * @param newValeur la nouvelle valeur de l'attribut.
     * @return true en cas de succ�s<br> false autrement.
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
        System.out.println ("la cl� existe bien");
      else
        System.out.println("La cl� n'existe pas");
      //installCleRepInstall ();
//      removeCleRepInstall();
      removeAllClePataCara();
    }
} // BaseRegistreWin
