/**
 * <p>Title: RequeteHttp.java</p>.
 * <p>Copyright: Copyright (c) 2005</p>.
 * @author Rémy GIRAUD
 * @version 1.0
 *
 * Created on 23 janv. 2005
 *
 * 
 */
package util.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;



/**
 * <p>Classe : RequeteHttp</p>.
 * <p>Description: Cette classe permet d'effectuer differente opération HTTP</p>.
 */
public class RequeteHttp
{

  /**
   * Permet d'effectuer une requete HTTP POST sur l'adresse addresse.
   * @param adresse l'adresse sur laquelle effectuer le doPost.
   * @param donnees la liste des parametres a donner à la requete :
   * ex :
   * <pre>
   *   donnees = URLEncoder.encode("clef", "UTF-8")+
   *             "="+URLEncoder.encode("valeur", "UTF-8");
   *   donnees += "&"+URLEncoder.encode("autreClef", "UTF-8")+
   *              "=" + URLEncoder.encode("autreValeur", "UTF-8");
   * </pre>
   * @return la réponse à la requete sous forme de flux.
   * @throws IOException si une quelque chose se passe mal.
   */
  public static InputStream doPost (String adresse, String donnees)
      throws IOException
  {
    OutputStreamWriter writer = null;
    //encodage des paramètres de la requête
    //       String donnees = URLEncoder.encode("clef", "UTF-8")+
    //                         "="+URLEncoder.encode("valeur", "UTF-8");
    //       donnees += "&"+URLEncoder.encode("autreClef", "UTF-8")+
    //                         "=" + URLEncoder.encode("autreValeur", "UTF-8");

    //création de la connection
    URL url = new URL (adresse);
    URLConnection conn = url.openConnection ();
    conn.setDoOutput (true);

    //envoi de la requête
    writer = new OutputStreamWriter (conn.getOutputStream ());
    writer.write (donnees);
    writer.flush ();

    writer.close ();

    return conn.getInputStream ();

  }

  /**
   * Ajoute aux données initiales le parametre newParam et sa valeur Value.
   * @param donneeInitiale la chaine à partir de laquel on souhaite ajouter le paramètre.
   * @param newParam le nom du paramètre.
   * @param newValue la valeur du paramétre.
   * @param encodage l'encodage par ex : UTF-8
   * @return la nouvelle donneeInitial avec le param en plus.
   * @throws IOException
   */
  public static String encodeParametreRequete (String donneeInitiale, String newParam, String newValue, String encodage) throws IOException
  {
    StringBuffer res = new StringBuffer (donneeInitiale);
    if (donneeInitiale.length() != 0)
      res.append ("&");
    res.append (URLEncoder.encode(newParam, encodage));
    res.append ("=");
    res.append (URLEncoder.encode(newValue, encodage));
    return res.toString();
  /* encodeParametreRequete () */
  }

  /**
   * Ajoute aux données initiales le parametre newParam et sa valeur Value.
   * @param donneeInitiale la chaine à partir de laquel on souhaite ajouter le paramètre.
   * @param newParam le nom du paramètre.
   * @param newValue la valeur du paramétre.
   * @return la nouvelle donneeInitial avec le param en plus.
   * @throws IOException
   */
  public static String encodeParametreRequete (String donneeInitiale, String newParam, String newValue) throws IOException
  {
    return encodeParametreRequete (donneeInitiale, newParam, newValue, "UTF-8");
    /* encodeParametreRequete () */
  }


  
  public static void main (String [] args)
  {
    String donnee = "";
    try
    {
      donnee = encodeParametreRequete (donnee, "val", "12");
      donnee = encodeParametreRequete (donnee, "test", "toto");
      System.out.println ("donne?" + donnee);
      
      InputStream in = doPost("http://patachou.dyndns.org/patacara/serveur/ModifEtatServeur.php", donnee);
      //lecture de la réponse
      BufferedReader reader = null;
      reader = new BufferedReader (new InputStreamReader (in));
      String ligne;
      while ((ligne = reader.readLine ()) != null)
      {
        System.out.println (ligne);
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
 // Classe RequeteHttp
