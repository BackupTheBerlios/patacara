package pata_cara.client;

import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.text.StyledDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyleConstants;
import javax.swing.ImageIcon;
import java.awt.Cursor;
import java.awt.Insets;
import javax.swing.text.Style;
import javax.swing.text.StyleContext;
import javax.swing.JLabel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;


public class Test extends JFrame implements ActionListener
{
      private String newline = "\n";

  private JDialog jd;

  public Test()
  {
    super ("test");
    test1();
  }


  public void test2 ()
  {
    //Create a text pane.
    JTextPane textPane = createTextPane();
    JScrollPane paneScrollPane = new JScrollPane(textPane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                     JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//    paneScrollPane.setVerticalScrollBarPolicy(
//                    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    //paneScrollPane.setPreferredSize(new Dimension(250, 155));
    //paneScrollPane.setMinimumSize(new Dimension(10, 10));
    getContentPane(). add (paneScrollPane);
    this.setBounds (100, 100, 500, 400);
  setVisible (true);
  }

  private JTextPane createTextPane() {
      String[] initString =
              { "This is an editable JTextPane, ",            //regular
                "another ",                                   //italic
                "styled ",                                    //bold
                "text ",                                      //small
                "component, ",                                //large
                "which supports embedded components..." + newline,//regular
                " " + newline,                                //button
                "...and embedded icons..." + newline,         //regular
                " ",                                          //icon
                newline + "JTextPane is a subclass of JEditorPane that " +
                  "uses a StyledEditorKit and StylejhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhdDocument, and provides " +
                  "cover methods for interacting with those objects."
               };

      String[] initStyles =
              { "regular", "italic", "bold", "small", "large",
                "regular", "button", "regular", "icon",
                "regular"
              };

      JTextPane textPane = new JTextPane();
      StyledDocument doc = textPane.getStyledDocument();
      addStylesToDocument(doc);

      try {
          for (int i=0; i < initString.length; i++) {
              doc.insertString(doc.getLength(), initString[i],
                               doc.getStyle(initStyles[i]));
          }
      } catch (BadLocationException ble) {
          System.err.println("Couldn't insert initial text into text pane.");
      }

      return textPane;
  }

  protected void addStylesToDocument(StyledDocument doc) {
      //Initialize some styles.
      Style def = StyleContext.getDefaultStyleContext().
                      getStyle(StyleContext.DEFAULT_STYLE);

      Style regular = doc.addStyle("regular", def);
      StyleConstants.setFontFamily(def, "SansSerif");

      Style s = doc.addStyle("italic", regular);
      StyleConstants.setItalic(s, true);

      s = doc.addStyle("bold", regular);
      StyleConstants.setBold(s, true);

      s = doc.addStyle("small", regular);
      StyleConstants.setFontSize(s, 10);

      s = doc.addStyle("large", regular);
      StyleConstants.setFontSize(s, 16);

      s = doc.addStyle("icon", regular);
      StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER);
      ImageIcon pigIcon = createImageIcon("/images/s0.gif",
                                          "a cute pig");
      if (pigIcon != null) {
          StyleConstants.setIcon(s, pigIcon);
      }

      s = doc.addStyle("button", regular);
      StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER);
      ImageIcon soundIcon = createImageIcon("/images/s1.gif",
                                            "sound icon");
      JButton button = new JButton();
      if (soundIcon != null) {
          button.setIcon(soundIcon);
      } else {
          button.setText("BEEP");
      }
      button.setCursor(Cursor.getDefaultCursor());
      button.setMargin(new Insets(0,0,0,0));
      button.setActionCommand("tot");
      button.addActionListener(this);
      StyleConstants.setComponent(s, button);
  }



  public void test1() {
    setBounds(200, 100, 500, 500);
    setVisible(true);
    jd = new JDialog (this, true);
    jd.getContentPane ().setLayout(new FlowLayout ());
    JButton but = new JButton ("OK");
    but.addActionListener(this);
    jd.getContentPane().add(but);
    JButton but2 = new JButton ("Annuler");
    but2.addActionListener(this);
    jd.getContentPane().add(but2);

    jd.setBounds(200, 100, 400, 400);
    but2.setFocusable(true);

    //jd.show ();
    if (but2.requestFocusInWindow ())
      System.out.println ("Request possible");
    else
      System.out.println ("Request impossible");
      //jd.setModal(true);


    JLabel lab = new JLabel ("patachou>");
    lab.setToolTipText("patachouTool");
    lab.setActionMap(new ActionMap ());
    lab.addMouseListener (new MouseAdapter ()
    {
      public void mouseClicked (MouseEvent e)
      {
        System.out.println (e.getSource ().toString ());
        if (e.getSource().getClass().isAssignableFrom(JLabel.class))
        {
          JLabel l = (JLabel) e.getSource();
          System.out.println ("C'est bien un JLabel : " +  l.getText()
                              + "qui a pour tooltip : " + l.getToolTipText(e));
        }
        else
          System.out.println("Pas un JLabel");
      }
    });
    jd.getContentPane ().add (lab);
    jd.setVisible(true);

  }

  /** Returns an ImageIcon, or null if the path was invalid. */
  protected static ImageIcon createImageIcon(String path,
                                             String description) {
      java.net.URL imgURL = Test.class.getResource ("/images/smile.gif");
      if (imgURL != null) {
          return new ImageIcon(imgURL, description);
      } else {
          System.err.println("Couldn't find file: " + path);
          return null;
      }
  }


  public void actionPerformed(ActionEvent e)
  {
    System.out.println("action performed");
    jd.dispose();
  }

  public static void main(String[] args)
  {
    Test test1 = new Test();
  }
}
