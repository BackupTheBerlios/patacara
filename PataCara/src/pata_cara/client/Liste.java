package pata_cara.client;

import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import javax.swing.JLabel;
import javax.swing.ListCellRenderer;
import java.awt.Color;
import javax.swing.JToolTip;
import java.awt.Font;
import java.awt.Component;


/**
 * Title:        pata_cara
 * Description:  chat en java
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Rémy Giraud
 * @version 1.0
 */




public class Liste extends JList
{
    String           ListPseudo  [] = new String [Main.MAX_MEMBRES];
    String           ListPseuCou [] = new String [Main.MAX_MEMBRES];
    String           ListToolTip [] = new String [Main.MAX_MEMBRES];
    int              nbPseudo       = 0;
    int              IndiceCourant  = 0;
    DefaultListModel listModel      = new DefaultListModel ();


    public Liste()
    {
        super ();
        setModel (listModel);
        setSelectionMode (ListSelectionModel.SINGLE_SELECTION);
        MyCellRenderer renderer = new MyCellRenderer();
        setCellRenderer(renderer);
    } /* Constructeur Liste */

    void AjouterPseudo (String Nom,
                        String Couleur,
                        String toolTip)
    {
        int pos = RechercherIndice (Nom);
        listModel.add (pos, Nom);

        if (0 == nbPseudo || nbPseudo == pos)
        {
            ListPseudo  [nbPseudo] = Nom;
            ListPseuCou [nbPseudo] = Couleur;
            ListToolTip [nbPseudo] = toolTip;
        }
        else
        {
            String Temp2     = null;
            String Temp1     = ListPseudo [pos];
            ListPseudo [pos] = Nom;

//pareil pour la couleur
            String Cou2       = null;
            String Cou1       = ListPseuCou [pos];
            ListPseuCou [pos] = Couleur;

//pareil pour le tool tip
            String tool2      = null;
            String tool1      = ListToolTip [pos];
            ListToolTip [pos] = toolTip;

            for (int i = pos+1; i < nbPseudo; ++i)
            {
                Temp2          = ListPseudo [i];
                ListPseudo [i] = Temp1;
                Temp1          = Temp2;
//pareil pour la couleur
                Cou2           = ListPseuCou [i];
                ListPseuCou[i] = Cou1;
                Cou1           = Cou2;
//pareil pour le tool tip
                tool2          = ListToolTip [i];
                ListToolTip[i] = tool1;
                tool1          = tool2;
            }
            ListPseudo [nbPseudo] = Temp1;
            ListPseuCou[nbPseudo] = Cou1;
            ListToolTip[nbPseudo] = tool1;
        }
        ++nbPseudo;
    } /* AjouterPseudo () */

    private int RechercherIndice (String Nom)
    {
        int i;

        for (i = 0; i < nbPseudo; ++i)
        {
             if ( Nom.compareTo(ListPseudo [i]) < 0)
                  break;
        }
        return i;

    } /* RechercherIndice () */

    private int ChercheRang (String pseudo)
    {
        int i = 0;
        for (; i < nbPseudo; ++i)
        {
            if (pseudo.equals (ListPseudo[i]))
                break;
        }
        return i;
    } /* ChercheRang () */

    void EffacerPseudo (String pseudo)
    {
        int rg = ChercheRang (pseudo);
        listModel.removeElement (pseudo);

        for (; rg < nbPseudo - 1; ++rg)
        {
            ListPseudo [rg] = ListPseudo [rg +1];
            ListPseuCou[rg] = ListPseuCou[rg +1];
            ListToolTip[rg] = ListToolTip[rg +1];
        }
        --nbPseudo;

    } /* EffacerPseudo () */


    class MyCellRenderer extends JLabel
                    implements ListCellRenderer
    {
      //EtchedBorder border = new EtchedBorder();
      //EtchedBorder blackLine = new EtchedBorder(EtchedBorder.RAISED);
      public MyCellRenderer()
      {
        setOpaque(true);
      } /* Constructeur MyCellRenderer */

      public Component getListCellRendererComponent(
               JList list, Object value, int index,
               boolean isSelected, boolean cellHasFocus)
      {
        // Indice courant sert d'indice pour la fonction createToolTip pour choisir la bonne couleur
        IndiceCourant = index;
/* System.out.println("indice courant :" + IndiceCourant + "valeur : " + value); */
        setText(""+value);
        setForeground (PataCara.getColor (ListPseuCou [index]));
        setBackground(Color.white);

        //préparation du toolTip avec mise des espaces
        String tool = ListToolTip [index];
        this.setToolTipText (Dialogue.addSpaceToolTip (tool));

        //setBorder(border);
        if(isSelected)
        {
          setForeground(Color.white);
          setBackground(Color.blue);
        }

        /*
        if(cellHasFocus)
        {
          setBackground(Color.blue);
          //setBorder(blackLine);
        }*/

        return this;
      } /* getListCellRendererComponent () */

   }// Classe MyCellRendere

   public JToolTip createToolTip() {
      JToolTip r = super.createToolTip();
      // Mise en place de la couleur de fond du tooltip
      r.setBackground(Dialogue.findCouleurToolTip(ListToolTip [IndiceCourant]));
      r.setFont(new Font("Dialog", Font.PLAIN, 12));
      return r;
  } /* createToolTip () */


} // Classe List
