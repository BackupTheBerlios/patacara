package util.fichier;

import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class Fichier
{

  public static void copy (final InputStream inStream,
                           final OutputStream outStream, final int bufferSize) throws
      IOException
  {
    final byte[] buffer = new byte[bufferSize];
    int nbRead;
    while ( (nbRead = inStream.read (buffer)) != -1)
    {
      outStream.write (buffer, 0, nbRead);
    }
  } /* copy () */


  public static void copyDirectory (final File from, final File to) throws
      IOException
  {
    if (!to.exists ())
    {
      to.mkdirs ();
    }
    final File[] inDir = from.listFiles ();
    for (int i = 0; i < inDir.length; i++)
    {
      final File file = inDir[i];
      copy (file, new File (to, file.getName ()));
    }
  } /* copyDirectory () */


  public static void copyFile (final File from, final File to) throws
      IOException
  {
    final InputStream inStream = new FileInputStream (from);
    //Vérification que le to n'existe pas.
    if (to.exists())
      to.delete();
    final OutputStream outStream = new FileOutputStream (to);
    if (from.length() > 0)
    {
      copy (inStream, outStream, (int) Math.min (from.length (), 4 * 1024));
    }
    inStream.close ();
    outStream.close ();
  } /* copyFile () */


  /**
   * Copie un répertoire ou un fichier dénoté par from vers to.
   * @param from le fichier/répertoire source.
   * @param to le fichier/répertoire destination.
   * @throws IOException
   */
  public static void copy (final File from, final File to) throws IOException
  {
    if (from.isFile ())
    {
      copyFile (from, to);
    }
    else if (from.isDirectory ())
    {
      copyDirectory (from, to);
    }
    else
    {
      throw new FileNotFoundException (from.toString () + " does not exist");
    }
  } /* copy () */


  public static void deleteRepertoire (File repertoire)
  {
    if (!repertoire.isDirectory())
      return;

    File[] fileList = repertoire.listFiles ();
    for (int i = 0; i < fileList.length; i++)
    {
      if (fileList[i].isDirectory ())
      {
        deleteRepertoire (fileList[i]);
        fileList[i].delete ();
      }
      else//Fichier à détruire.
        fileList[i].delete ();
    }
  } /* deleteRepertoire () */

  public static void deleteRepertoire (String repertoire)
  {
    deleteRepertoire(new File (repertoire));
  } /* deleteRepertoire () */

  public static boolean deleteFichier (String nom)
  {
    return new File (nom).delete();
  } /* deleteFichier () */



  public final static String jpeg = "jpeg";
  public final static String jpg = "jpg";
  public final static String gif = "gif";
  public final static String tiff = "tiff";
  public final static String tif = "tif";
  public final static String png = "png";
  public final static String exe = "exe";


  /**
   * Renvoi l'extension d'un fichier en minuscule.
   * @param f le fichier dont on souhaite connaitre l'extension.
   * @return l'extension du fichier en minuscule.
   */
  public static String getExtension (File f)
  {
    String ext = null;
    String s = f.getName ();
    int i = s.lastIndexOf ('.');

    if (i > 0 && i < s.length () - 1)
    {
      ext = s.substring (i + 1).toLowerCase ();
    }
    return ext;
  } /* getExtension () */

  /**
   * Renvoi l'extension d'un fichier en minuscule.
   * @param f le fichier dont on souhaite connaitre l'extension.
   * @return l'extension du fichier en minuscule.
   */
  public static String getExtension (String f)
  {
    String ext = null;
    int i = f.lastIndexOf ('.');

    if (i > 0 && i < f.length () - 1)
    {
      ext = f.substring (i + 1).toLowerCase ();
    }
    return ext;
  } /* getExtension () */

  /**
   * Renvoi le nom du fichier sans extension.
   * @param f le fichier dont on souhaite connaitre le nom sans extension.
   * @return le nom du fichier sans extension.
   */
  public static String getNomFichierSansExtension (File f)
  {
    String s = f.getName ();
    int i = s.lastIndexOf ('.');
    if (-1 == i)
      return s;
    return s.substring(0, i);
  } /* getNomFichierSansExtension () */

  /**
   * Renvoi le nom du fichier sans extension.
   * @param f le fichier dont on souhaite connaitre le nom sans extension.
   * @return le nom du fichier sans extension.
   */
  public static String getNomFichierSansExtension (String f)
  {
    int i = f.lastIndexOf ('.');
    if (-1 == i)
      return f;
    return f.substring(0, i);
  } /* getNomFichierSansExtension () */


} // Classe Fichier
