package pata_cara.client;

import javax.swing.JPanel;
import java.awt.event.ActionListener;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import java.awt.FlowLayout;

/**
 * Title:        pata_cara
 * Description:  chat en java
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Rémy Giraud
 * @version 1.0
 */



public class SalonEst extends JPanel
{
  Liste ListeConnecte;
  static final String Dialoguer = "Dialoguer";
  static final String Ignorer   = "Ignorer";


  public SalonEst (ActionListener auditeur)
  {
      super ();
      JPanel pForme = new JPanel (new BorderLayout (0,5));
      pForme.setBackground (Salon.COULEURSALONEST);

      ListeConnecte = new Liste ();
      //ListeConnecte.addActionListener(auditeur);
      JScrollPane scrollPaneListe = new JScrollPane(ListeConnecte,
                                           JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                           JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

      pForme.add(scrollPaneListe, BorderLayout.CENTER);

      JButton bDial = new JButton (Dialoguer);
      bDial.addActionListener(auditeur);
//bDial.setFont (new Font ("sansserif", 0, 8));
bDial.setBorder (BorderFactory.createRaisedBevelBorder ());
      JButton bIgno = new JButton (Ignorer);
      bIgno.addActionListener(auditeur);
bIgno.setBorder (BorderFactory.createRaisedBevelBorder ());
      JPanel pBut = new JPanel (new FlowLayout (FlowLayout.CENTER,10,0));
      pBut.setBackground(Salon.COULEURSALONEST);
      pBut.add(bDial);
      pBut.add(bIgno);

      pForme.add (pBut, BorderLayout.SOUTH);

      pForme.setBorder ( BorderFactory.createCompoundBorder (
                BorderFactory.createTitledBorder ("Liste des connectés"),
                BorderFactory.createEmptyBorder (10, 10, 10, 10 )));

      setLayout (new BorderLayout ());
      setBackground(Salon.COULEURSALONEST);
      JPanel pEs = new JPanel (new FlowLayout (FlowLayout.CENTER, 3,3));
      pEs.setBackground (Salon.COULEURSALONEST);
      add (pForme, BorderLayout.CENTER);
      add (pEs,  BorderLayout.EAST);


  } /* Constructeur SalonEst */
} // Classe SalonEst
