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
import java.net.Authenticator;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;




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
//System.out.println (url.getAuthority());
    URLConnection conn = url.openConnection ();
    conn.setDoOutput (true);

    //envoi de la requête
    if (null != donnees)
    {
      writer = new OutputStreamWriter (conn.getOutputStream ());
      writer.write (donnees);
      writer.flush ();

      writer.close ();
    }
    return conn.getInputStream ();

  }
  
  /**
   * Permet d'effectuer une requete HTTP POST sur l'adresse addresse protégé par un fichier htaccess.
   * @param adresse l'adresse sur laquelle effectuer le doPost.
   * @param donnees la liste des parametres a donner à la requete :
   * @param user le nom de l'utilisateur pour la page demandé
   * @param pass le mot de passe pour l'utilisateur user
   * ex :
   * <pre>
   *   donnees = URLEncoder.encode("clef", "UTF-8")+
   *             "="+URLEncoder.encode("valeur", "UTF-8");
   *   donnees += "&"+URLEncoder.encode("autreClef", "UTF-8")+
   *              "=" + URLEncoder.encode("autreValeur", "UTF-8");
   * </pre>
   * @return la connexion établie.
   * @throws IOException si une quelque chose se passe mal.
   *         java.net.ConnectException: Connection refused: connect   -> si le site distant n'est pas accessible (connexion internet non existante, site inacessible...)
   * 
   */
  public static HttpURLConnection getConnexionHttpPost (final String adresse, String donnees, final String user, final String pass) throws IOException
  {

    if (null != user && null != pass)
    Authenticator.setDefault (new Authenticator () {
      protected PasswordAuthentication getPasswordAuthentication ()
      {
        return new PasswordAuthentication (user, pass.toCharArray ());
      }
    });
    
    OutputStreamWriter writer = null;

    //création de la connection
    URL url = new URL (adresse);

    HttpURLConnection conn = (HttpURLConnection) url.openConnection ();
    conn.setDoOutput (true);


    //envoi de la requête
    if (null != donnees)
    {
      writer = new OutputStreamWriter (conn.getOutputStream ());
      writer.write (donnees);
      writer.flush ();

      writer.close ();
    }
    return conn;
  }

  
  /**
   * Permet d'effectuer une requete HTTP POST sur l'adresse addresse NON protégée par un fichier htaccess.
   * @param adresse l'adresse sur laquelle effectuer le doPost.
   * @param donnees la liste des parametres a donner à la requete :
   * @param user le nom de l'utilisateur pour la page demandé
   * @param pass le mot de passe pour l'utilisateur user
   * ex :
   * <pre>
   *   donnees = URLEncoder.encode("clef", "UTF-8")+
   *             "="+URLEncoder.encode("valeur", "UTF-8");
   *   donnees += "&"+URLEncoder.encode("autreClef", "UTF-8")+
   *              "=" + URLEncoder.encode("autreValeur", "UTF-8");
   * </pre>
   * @return la connexion établie.
   * @throws IOException si une quelque chose se passe mal.
   *         java.net.ConnectException: Connection refused: connect   -> si le site distant n'est pas accessible (connexion internet non existante, site inacessible...)
   * 
   */
  public static HttpURLConnection getConnexionHttpPost (final String adresse, String donnees) throws IOException
  {
    return getConnexionHttpPost (adresse, donnees, null, null);
  }
  

  /**
   * Permet d'effectuer une requete HTTP sur l'adresse addresse NON protégée par un fichier htaccess.
   * @param adresse l'adresse sur laquelle effectuer le doPost.
   * @param donnees la liste des parametres a donner à la requete :
   * @param user le nom de l'utilisateur pour la page demandé
   * @param pass le mot de passe pour l'utilisateur user
   * ex :
   * <pre>
   *   donnees = URLEncoder.encode("clef", "UTF-8")+
   *             "="+URLEncoder.encode("valeur", "UTF-8");
   *   donnees += "&"+URLEncoder.encode("autreClef", "UTF-8")+
   *              "=" + URLEncoder.encode("autreValeur", "UTF-8");
   * </pre>
   * @return la connexion établie.
   * @throws IOException si une quelque chose se passe mal.
   *         java.net.ConnectException: Connection refused: connect   -> si le site distant n'est pas accessible (connexion internet non existante, site inacessible...)
   * 
   */
  public static HttpURLConnection getConnexionHttpPost (final String adresse) throws IOException
  {
    return getConnexionHttpPost (adresse, null, null, null);
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
    String ip = null;
    try
    {
      ip = InetAddress.getLocalHost().getHostAddress ();
    }
    catch (UnknownHostException e1)
    {
      e1.printStackTrace();
    }
    System.out.println ("IP : " + ip);
    try
    {
      donnee = encodeParametreRequete (donnee, "val", "PING");
      //donnee = encodeParametreRequete (donnee, "test", "toto");
      System.out.println ("donne?" + donnee);
      
      //InputStream in = doPost("http://localhost/patacara/serveur/ModifEtatServeur.php", donnee);

      try
      {
        HttpURLConnection conn = getConnexionHttpPost (
            "http://localhost/patacara/serveur/ModifEtatServeur.php", donnee,
            "patachou", "57yZYJAp");
        
        int statutCode = conn.getResponseCode();
        if (statutCode != 200)
        {
          if (statutCode == 401)
            System.out.println ("Acces denied");
          System.out.println ("Erreur statu : " + statutCode + ", message : " + conn.getResponseMessage());
          return;
        }
        System.out.println ("Response : " + conn.getResponseCode ());
        
//System.out.println ("getcontent = " + ((sun.net.www.protocol.http.HttpURLConnection.HttpInputStream) conn.getContent ()));
        //lecture de la réponse
        BufferedReader reader = null;
        reader = new BufferedReader (new InputStreamReader (conn
            .getInputStream ()));
        String ligne;
        while ((ligne = reader.readLine ()) != null)
        {
          //System.out.println (ligne);
        }
      }
      catch (ConnectException ex)
      {
        System.err.println ("Impossible de contacter l'hote");
        ex.printStackTrace ();
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
 // Classe RequeteHttp
