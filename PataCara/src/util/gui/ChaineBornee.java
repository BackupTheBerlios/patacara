package util.gui;


/**
 * Title:        ChaineBornee.java
 * Description:  Ajoute un champ de saisie ou le texte saisie aura une borne
 * supérieur et inférieur.
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Rémy Giraud
 * @version 1.0
 */

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import util.gui.Boite;
import java.awt.event.FocusListener;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.event.FocusEvent;


public class ChaineBornee extends JPanel implements FocusListener
{
    private Dialog dial;
    private int longMin, longMax;
    private JTextField texte;

    private String reponse;

    public ChaineBornee(Dialog dial, String label, int longMin, int longMax, int colonnes)
    {
        this.dial = dial;
        this.longMin = longMin;
        this.longMax = longMax;

        //setBackground(dial.getBackground());
        setLayout (new FlowLayout (FlowLayout.RIGHT));

        texte = new JTextField(colonnes);
        texte.addFocusListener(this);
        add(new JLabel(label));
        add(texte);
    }

    public void focusLost(FocusEvent e)
    {
        String s = texte.getText();
        if (s.length() != 0)
            if (s.length() < longMin)
            {
                Boite.message(dial, "Erreur de saisie", "Trop court : " + s);
                texte.setText("");
            }
            else if (s.length() > longMax)
            {
                Boite.message(dial, "Erreur de saisie", "Trop long : " + s);
                texte.setText("");
            }
    }

    public void focusGained(FocusEvent e) { }

    public static String getTexte (Dialog dial, String label, int longMin, int longMax, int colonnes, Dialog p)
    {
      ChaineBornee Ch = new ChaineBornee (dial, label, longMin, longMax, colonnes);
      p.add(Ch);
      return Ch.reponse;
    }

    public void setText(String s)
    {
        texte.setText(s);
    }

    public String getText()
    {
        return texte.getText();
    }
}
