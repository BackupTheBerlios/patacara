package util.gui;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JList;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import java.awt.CardLayout;
import javax.swing.BorderFactory;
import java.awt.Color;


public  class  ComposantGraphique
{

  /**
   * Crée un JPanel avec une liste au milieu, 2 boutons en dessous avec les
   * textes spécifié. Le JPanel est entouré d'un cadre avec le titre titre.
   * @param titreBoite le titre du cadre.
   * @param but1 le bouton situé à droite.
   * @param but2 le bouton situé à gauche.
   * @param textBut1 le texte du premier bouton.
   * @param textBut2 le texte du second bouton.
   * @param liste la liste.
   * @param toolTipBut1 le toolTip du bouton 1.
   * @param toolTipBut2 le toolTip du bouton 2.
   * @return JPanel
   */
  public static JPanel createBoxWith2Bouton1Liste (String titreBoite, JButton but1,
                                             JButton but2, String textBut1,
                                             String textBut2, JList liste,
                                             String toolTipBut1, String toolTipBut2)
  {
    return createBoxWith2Bouton1Liste(titreBoite, but1, but2, textBut1, textBut2,
                                      liste, toolTipBut1, toolTipBut2,
                                      new JPanel ().getBackground());
  } /* createBoxWith2Bouton1Liste() */


  /**
   * Crée un JPanel avec une liste au milieu, 2 boutons en dessous avec les
   * textes spécifié. Le JPanel est entouré d'un cadre avec le titre titre.
   *
   * @param titreBoite le titre du cadre.
   * @param but1 le bouton situé à droite.
   * @param but2 le bouton situé à gauche.
   * @param textBut1 le texte du premier bouton.
   * @param textBut2 le texte du second bouton.
   * @param liste la liste.
   * @param toolTipBut1 le toolTip du bouton 1.
   * @param toolTipBut2 le toolTip du bouton 2.
   * @param background la couleur d'arriere plan des panel.
   * @return JPanel
   */
  public static JPanel createBoxWith2Bouton1Liste (String titreBoite, JButton but1,
                                             JButton but2, String textBut1,
                                             String textBut2, JList liste,
                                             String toolTipBut1, String toolTipBut2,
                                             Color background)
  {
    JPanel pPrincipale = new JPanel (new BorderLayout (0,5));
    pPrincipale.setBackground(background);
    JPanel pBut = new JPanel (new FlowLayout (FlowLayout.CENTER,10,0));
    pBut.setBackground(background);
    JPanel pBut1 = new JPanel();
    pBut1.setBackground(background);
    JPanel pBut2 = new JPanel();
    pBut2.setBackground(background);

    but1.setText(textBut1);

    but2.setText(textBut2);
    pBut1.add(but1, "b1");



    JScrollPane scrollPaneListe = new JScrollPane(liste,
                                         JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                         JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPaneListe.setPreferredSize(new Dimension(258, 70));

    scrollPaneListe.getVerticalScrollBar().setPreferredSize(new Dimension(10, 10));
    scrollPaneListe.getHorizontalScrollBar().setPreferredSize(new Dimension (10, 10));

    pBut.setLayout(new BorderLayout ());

    pBut2.setLayout(new CardLayout ());
    pBut1.setLayout(new CardLayout ());
    but1.setToolTipText(toolTipBut1);

    pBut.add(pBut2, BorderLayout.WEST);
    pBut.add(pBut1, BorderLayout.EAST);
    but2.setToolTipText(toolTipBut2);
    pBut2.add(but2, "b2");

    pBut.setPreferredSize(new Dimension(112, 19));


    pPrincipale.setBorder ( BorderFactory.createCompoundBorder (
              BorderFactory.createTitledBorder (titreBoite),
              BorderFactory.createEmptyBorder (10, 10, 10, 10 )));

    pPrincipale.add(scrollPaneListe, BorderLayout.CENTER);
    pPrincipale.add (pBut, BorderLayout.SOUTH);

    return pPrincipale;
  } /* createBoxWith2Bouton1Liste() */


} // Classe ComposantGraphique
