package test;

import pata_cara.client.Info;
import javax.swing.JFrame;
import javax.swing.UIManager;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Collection;
import javax.swing.plaf.ColorUIResource;
import javax.swing.SwingUtilities;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Remy GIRAUD
 * @version 1.0
 */

public class Test2
{
  public JFrame f;
  public int r = 50;
  public int g = 125;
  public int b = 244;
  public Test2()
  {
    f = new JFrame ("super");
    f.getContentPane().add(new Info ());
    JButton but = new JButton ("test");
    but.addActionListener(new ActionListener () {
      /**
       * actionPerformed
       *
       * @param e ActionEvent
       */
      public void actionPerformed (ActionEvent e)
      {
        r = (r +51)%256;
        g = (g +21)%256;
        b = (b + 41) % 256;
        UIManager.getDefaults().put("Panel.background", new ColorUIResource (r, g, b));
        SwingUtilities.updateComponentTreeUI(f);

      }
    });
    f.getContentPane().add(but, BorderLayout.EAST);
    f.setBounds(100, 200, 690, 560);
    f.setVisible(true);

  }
  public static void main(String[] args)
  {
    Hashtable l = UIManager.getDefaults();
    Enumeration e = l.elements();
    Collection val = l.values();
    Enumeration key = l.keys();

//    System.out.println("clé : " + key.size());
    System.out.println("valeur : " + val.size());
//    System.out.println("hash : " + l);

    int i;
    for (i = 0; e.hasMoreElements(); i++)
    {
      Object ob = e.nextElement();
      Object obKey = key.nextElement();
//      Object cle = l.
      if (ob instanceof java.awt.Color)
      {
        System.out.println("clé : " + obKey + ", ob : " + ob);
//        System.out.println("ouiiiii");
      }

    }
    System.out.println("nb : " + i);

/*    Object o = UIManager.getDefaults().get ("Color");

    if (null != o)    System.out.println(o);
    else System.out.println("pas trouvé");
*/
ColorUIResource c;

    Test2 test21 = new Test2();
    UIManager.getDefaults().put("Panel.background", new ColorUIResource (133, 203, 96));
    SwingUtilities.updateComponentTreeUI(test21.f);

  }

}
