package util.gui;



/**
 * Title:        NombreBorne.java
 * Description:  Classe servant à integrer un nouveau champs de saisie ou la
 * saisie sera controllée (saisie d'un nombre avec borne inférieur et supérieur).
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Rémy Giraud
 * @version 1.0
 */

import javax.swing.JPanel;
import javax.swing.JTextField;
import util.gui.Boite;
import java.awt.event.FocusListener;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.event.FocusEvent;

public class NombreBorne extends JPanel implements FocusListener
{
    private Dialog dial;
    private int min, max;
    private JTextField texte;
    private int valeur;

    public NombreBorne(Dialog dial, String label, int min, int max, int colonnes)
    {
        this.dial = dial;
        this.min = min;
        this.max = max;

        //setBackground(dial.getBackground());
        setLayout(new FlowLayout(FlowLayout.RIGHT));

        texte = new JTextField(colonnes);
        texte.addFocusListener(this);
        add(new Label(label));
        add(texte);
    }

    public void focusLost(FocusEvent e)
    {
        String s = texte.getText();
        boolean mauvaisFormat = false;
        if (s.length() > 0)
        {
            try
            {
                valeur = Integer.parseInt(s);
            }
            catch (Exception exc)
            {
                mauvaisFormat = true;
            }
            if (mauvaisFormat)
            {
                Boite.message(dial, "Erreur de saisie", "Mauvais format : " + s);
                texte.setText("");
                valeur = 0;
            }
            else if (valeur < min)
            {
                Boite.message(dial, "Erreur de saisie", "Valeur trop petite : " + s);
                texte.setText("");
                valeur = 0;
            }
            else if (valeur > max)
            {
                Boite.message(dial, "Erreur de saisie", "Valeur trop grande : " + s);
                texte.setText("");
                valeur = 0;
            }

        }
    }
    public void focusGained(FocusEvent e) { }

    public void setValue(int v)
    {
        valeur = v;
        texte.setText("" + v);
    }

    public int getValue()
    {
        return valeur;
    }
} //Classe NombreBornée
