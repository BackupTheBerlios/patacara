package util.bdd;


/**
 * Title:        ConnectionServeurMysql.java
 * Description:  Permet de se connecter au serveur MySQL. <br>
*                Il est necessaire d'avoir les drivers adequat (org.gjt.mm.mysql.Driver)
*                pour pouvoir utiliser cette classe.
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Rémy Giraud
 * @version 1.0
 */

import java.sql.*;
import java.io.IOException;

public class ConnectionServeurMysql
{
    private Connection cx;
    private Statement Req;
    private ResultSet res;
    private ResultSetMetaData resDonnee;

    public ConnectionServeurMysql ()
    {
        this.cx        = null;
        this.Req       = null;
        this.res       = null;
        this.resDonnee = null;
    } /* ConnectionServeurMysql () */

    public void Connexion (String url, String user, String password)
                                 throws SQLException, IOException, Exception
    {
        Class.forName("org.gjt.mm.mysql.Driver").newInstance();
        cx = DriverManager.getConnection(url, user, password);
    } /* Connexion () */

    /* execute la requete query */
    public void execution (String Query) throws SQLException
    {
        if (null == this.Req)
             this.Req = this.cx.createStatement ();
        this.res =  this.Req.executeQuery (Query);
    } /* execution () */

    /* renvoi le nombre de champs dans le resultat */
    public int getNbrChamps () throws SQLException
    {
        if (null == this.resDonnee)
           this.resDonnee = this.res.getMetaData ();
        return this.resDonnee.getColumnCount();
    } /* getNbrChamps () */

    /* positionne la compteur de ligne à la ligne suivante,
       renvoi vrai si possible faux autrement */
    public boolean getLigneResultat () throws SQLException
    {
        return this.res.next();
    } /* getLigneResultat () */

    /* renvoi la nom du champ position */
    public String getNomChamps (int position) throws SQLException
    {
        if (null == this.resDonnee)
           this.resDonnee = this.res.getMetaData ();
        return this.resDonnee.getColumnLabel (position);
    } /* getNomChamps () */

    /* renvoi la valeur de la case ligne courante et colone position */
    public String getValeurChamps (int position) throws SQLException
    {
        return this.res.getString (position);
    } /* getValeurChamps () */

    /* renvoi la valeur de la case ligne courante et colone position */
    public String getValeurChamps (String nomColone) throws SQLException
    {
        return this.res.getString (nomColone);
    } /* getValeurChamps () */

    public int getNbrLigne () throws SQLException
    {
         int nb = 0;
         int curent = this.res.getRow();

         if (!this.res.first())
         {System.out.println("on est dans !first");
             return 0;}
         for (nb = 1 /* first () */; getLigneResultat (); ++nb);
         this.res.beforeFirst();

         if (0 != curent)
             this.res.relative(curent);

         return nb;


    } /* getNbrLigne () */

    public void fermeture () throws SQLException
    {
        this.res.close ();
        this.Req.close ();
        this.cx.close();
        this.cx        = null;
        this.Req       = null;
        this.res       = null;
        this.resDonnee = null;
    } /* fermeture () */
} // Classe ConnectionServeurMysql
