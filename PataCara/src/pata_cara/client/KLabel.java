package pata_cara.client;

import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JToolTip;
import java.awt.Font;

/**
 * Title:        pata_cara
 * Description:  chat en java
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Rémy Giraud
 * @version 1.0
 */



public class KLabel extends JLabel
{
    private Color Couleur;

    public KLabel ()
    {
        this.Couleur = Color.orange;
    }

    public KLabel (String Text, String Couleur)
    {
        setText (Text);
        this.Couleur = PataCara.getColor (Couleur);
    } /* Constructeur KLabel */

    public KLabel (String Text, Color Couleur)
    {
        setText (Text);
        this.Couleur = Couleur;
    } /* Constructeur KLabel */

    public KLabel (ImageIcon icon, Color Couleur)
    {
        setIcon (icon);
        this.Couleur = Couleur;
    } /* Constructeur KLabel */

    public void setCouleur (String Couleur)
    {
         this.Couleur = PataCara.getColor (Couleur);
    } /* setCouleur () */

    public void setCouleur (Color Couleur)
    {
         this.Couleur = Couleur;
    } /* setCouleur () */

    public Color getCouleur ()
    {
        return this.Couleur;
    } /* getCouleur () */

    public JToolTip createToolTip() {
        JToolTip r = super.createToolTip();
        r.setBackground(Couleur);
        r.setFont(new Font("Dialog", Font.PLAIN, 12));
        return r;
    } /* createToolTip () */
} // Classe KLabel

