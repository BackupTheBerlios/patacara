package util.fichier;
/**
 * Title : Propriete.java
 * Description : Classe qui permet de les fichiers propriétés de manière plus souple
 * @version	1.2, Aout 2004
 * @author 	Giraud Rémy
**/


import java.util.Properties;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.File;

public class Propriete
{

  private Properties propert;
  private String  nomFic;

  /**
   * Nouveau fichier de propriété qui aura pour nom nomFicProp.
   *
   * @param nomFichProp le nom du fichier proprété qui sera crée.
   */
  public Propriete (String nomFichProp)
  {
    this.nomFic = nomFichProp;
    propert = new Properties ();
    try
    {
      BufferedInputStream bis = new BufferedInputStream(
                                    new FileInputStream(nomFichProp));
      propert.load(bis);
    }
    catch (java.io.FileNotFoundException Ex)
    {
      System.out.println ("Pas de fichier proprété présent pour l'instant");
    }
    catch (java.io.IOException Ex)
    {
      Ex.printStackTrace();
    }
  }

  /**
   * Fixe le propriété <B>key</B> à la valeur <B>value</B>.
   * @param key le nom de la propriété à changer, ajouter ou modifier.
   * @param value la valeur de la propriété.
   */
  public void setPropriete (String key, String value)
  {
    propert.setProperty(key, value);
  }

  /**
   * Renvoie la valeur de la propriété key.
   * @param key le nom de la propriété dont ou souhaite la valeur.
   * @return la valeur de la propriété,
   *         null si pas trouvée.
   */
  public String getPropriete (String key)
  {
    return propert.getProperty(key);
  }

  /**
   * Vérifie si le propriété <B>key</B> existe dans le fichier de propriété.
   * @param key le nom de la propriété à tester.
   * @return true si la propriété existe dans le fichier, false autrement.
   */
  public boolean containKey (String key)
  {
    if (null == propert.getProperty(key)) return false;
    return true;
  }

  /**
   * Enregistre les propriétés ajoutés dans le fichier de propiètés.
   */
  public void saveProprietes ()
  {
    try
    {
      BufferedOutputStream bos = new BufferedOutputStream(
                                           new FileOutputStream (nomFic));
      propert.store(bos, "Propriétés " + nomFic);
      bos.close();
    }
    catch (java.io.FileNotFoundException Ex)
    {
      Ex.printStackTrace();
      System.err.println ("Impossible d'ecrire dans le fichier " + nomFic);
    }
    catch (java.io.IOException Ex)
    {
      Ex.printStackTrace ();
    }
  }

  /**
   * Ajoute la propriété si elle n'existe pas déjà.
   * @param key le nom de la propriété à changer, ajouter ou modifier.
   * @param value la valeur de la propriété.
   */
  public void addProprieteIfNotExist (String key, String value)
  {
    if (!this.containKey(key))
      this.setPropriete(key, value);
  }

  /**
   * Supprime la propriété du fichier de propriété.
   * @param key la propriété a supprimer.
   */
  public void removePropriete (String key)
  {
    propert.remove(key);
  }

  /**
   * Supprime le fichier propriété du disque.
   * @return true si le fichier a bien été supprimé,
   *         false sinon.
   */
  public boolean deleteFilePropriete ()
  {
    File file = new File (nomFic);
    return file.delete();
  }

  /**
   * Supprime le fichier propriété du disque.
   * @param nomFic le nom di ficher à supprimer.
   * @return true si le fichier a bien été supprimé,
   *         false sinon.
   */
  public static boolean deleteFilePropriete (String nomFic)
  {
    File file = new File (nomFic);
    return file.delete();
  }


  /**
   * Ajoute/met à jour la propriété key à la veleur value dans le fichier de
   * propriété nomFic.
   *
   * @param nomFic Nom du fichier de propriété.
   * @param key le nom de la clé.
   * @param value la nouvelle valeur.
   */
  public static void setPropriete (String nomFic, String key, String value)
  {
    Propriete p = new Propriete (nomFic);
    p.setPropriete(key, value);
    p.saveProprietes();
  }

    /**
   * Renvoi la valeur de la propriété key dans le fichier de propriété nomFic.
   * @param nomFic le nom complet du fichier de propriété.
   * @param key le nom de la propriété dont ou souhaite la valeur.
   * @return la valeur de la propriété,
   *         null si pas trouvée.
   */
  public static String getPropriete (String nomFic, String key)
  {
    Propriete p = new Propriete (nomFic);
    return p.getPropriete(key);
  }

  /**
   * Renvoi vrai si le fichier nomFic est présent sur le disque.
   * @param nomFic le nom du fichier de propriété a tester.
   * @return true si le fichier est present, false autrement
   */
  public static boolean isFichierPropsPresent (String nomFic)
  {
    File file = new File (nomFic);
    return file.exists();
  }



  public static void main (String [] args)
  {
    System.out.println ("Début test");
/*    Propriete p = new Propriete ("propriete.props");
    if (! p.containKey("test"))
    {
      System.out.println ("Le clé n'existe pas");
      p.setPropriete("test", "ok");
    }
    else System.out.println("La clé existe deja");
    p.saveProprietes();
    p.setPropriete("test2", "nok");
    p.saveProprietes();
    System.out.println("Contient test2 ? " + p.containKey("test2"));
    System.out.println("valeur de la clé test2 : " + p.getPropriete("test2"));
    p.removePropriete("test2");
    System.out.println("Contient test2 ? " + p.containKey("test2"));
    System.out.println("valeur de la clé test2 : " + p.getPropriete("test2"));
    p.saveProprietes();

    System.out.println ("Supression reussi ? " + deleteFilePropriete ("propriete.props"));
    System.out.println ("Supression reussi ? " + p.deleteFilePropriete ());

    setPropriete ("propriete1.props", "test", "ok");
    setPropriete ("propriete1.props", "test2", "ok");
    System.out.println("Nom de la propriété associé a test2 : " +
                        getPropriete ("propriete1.props", "test2"));
    System.out.println("Nom de la propriété associé a test3 : " +
                        getPropriete ("propriete1.props", "test3"));
*/
  }

} // Classe Propriete
