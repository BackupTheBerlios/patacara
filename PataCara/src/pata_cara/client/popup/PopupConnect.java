/**
 * <p>Title: PopupConnect.java</p>.
 * <p>Copyright: Copyright (c) 2004</p>.
 * @author Rémy GIRAUD
 * @version 1.0
 *
 * Created on 23 oct. 2004
 *
 * 
 */
package pata_cara.client.popup;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;


/**
 * <p>Classe : PopupConnect</p>.
 * <p>Description: Affiche un popup de nouvea connecté.</p>.
 */
public class PopupConnect extends JWindow
{
  
  private String pseudo;
  
  public PopupConnect (String pseudo)
  {
    super ();
    this.pseudo = pseudo;
    initialize();
  }

  
  
  /**
	 * This method initializes the popup.
	 * 
	 * @return void
	 */
	private void initialize()         
	{	
	  	Color c = new Color (255, 254, 200);
        this.setName("popup");
        this.setSize(200, 150);
        JPanel contentPane = new JPanel (new BorderLayout ());
        contentPane.setBorder(javax.swing.BorderFactory.createMatteBorder(2,2,2,2,new Color (255, 254, 200)));
        this.setContentPane(contentPane);
        //this.getContentPane().setLayout(new BorderLayout ());
        
        JPanelImageBg panelCenter = new JPanelImageBg ("images/PopupConnecte.jpg", JPanelImageBg.CENTRE);
        panelCenter.setLayout(new BorderLayout ());
        //panelCenter.setBorder(javax.swing.BorderFactory.createMatteBorder(2,2,2,2,new Color (255, 254, 200)));
        panelCenter.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(1,1,1,1,new Color (255, 254, 200)), javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED)));
        //panelCenter.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(1,1,1,1,new Color (255, 254, 200)), javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED)));
        JPanel p = new JPanel (new FlowLayout (FlowLayout.CENTER, 0, 40));
        p.setOpaque(false);
        p.add (new JLabel ("<html><body><center>" + pseudo + "<br>vient de se connecter</center></body></html>"));
        //panelCenter.add (new JLabel ("<html><body><center>Je suis patachou<br>vient de se connecter</center></body></html>"), BorderLayout.CENTER);
        panelCenter.add (p, BorderLayout.CENTER);
        this.getContentPane().add (panelCenter, BorderLayout.CENTER);

        JPanelImageBg panelNord = new JPanelImageBg ("images/PopupConnecte.jpg", JPanelImageBg.CENTRE);
        panelNord.setLayout(new BorderLayout (0, 0));
        panelNord.add( (new JLabel ("PataCara")));
        JLabel labClose = new JLabel (new ImageIcon ("images/close.gif"));
        labClose.setOpaque(false);
        labClose.addMouseListener(new MouseAdapter () {

          public void mouseClicked (MouseEvent e)
          {
            //System.out.println ("Clicked");
            dispose();
            /* mouseClicked () */
          }

          public void mouseEntered (MouseEvent e)
          {
            //System.out.println ("entered");
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            /* mouseEntered () */
          }

          public void mouseExited (MouseEvent e)
          {
            //System.out.println ("exited");
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            /* mouseExited () */
          }

        });
        JPanel pClose = new JPanel (new FlowLayout ());
        pClose.setOpaque(false);
        pClose.add (labClose);        
        panelNord.add (pClose, BorderLayout.EAST);
        
        JPanel pWeast = new JPanel (new FlowLayout (FlowLayout.LEFT, 5, 5));
        pWeast.setOpaque(false);
        panelNord.add (pWeast, BorderLayout.WEST);
        //panelNord.setBorder(javax.swing.BorderFactory.createMatteBorder(2,2,0,2,new Color (255, 254, 200)));
        //panelNord.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(1,1,1,1,c), javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED)));
        //panelNord.setBorder (javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.LOWERED));
        this.getContentPane().add (panelNord, BorderLayout.NORTH);
        
	}
	
	/**
	 * Deplace le popup.
	 * @param sleep le temps entre chaque déplacement du popup.
	 * @param hauteurDeplacement la hauteur entre chaque déplacement.
	 */
	public void movePopup (final long sleep, final long hauteurDeplacement, final long delaiAvtEffacement)
	{
	  Thread t = new Thread (new Runnable () {

      public void run ()
      {
 		Dimension tailleEcran = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
 		int hauteur = (int)tailleEcran.getHeight();
 		int largeur = (int)tailleEcran.getWidth() - getWidth() - 5;
 		
 		//On position la fenetre au bon endroit
 		setLocation (largeur, hauteur);
 		setVisible(true);
 		//toFront();
 		
 		//La hauteur courante
 		int hauteurCourante = hauteur;
 		//La hauteur a atteindre
        hauteur = hauteur - getHeight() -50;
        while (hauteurCourante > hauteur)
        {
          setLocation(largeur, hauteurCourante);
         
          //toFront();
          hauteurCourante -= hauteurDeplacement;
          try
          {
            Thread.sleep (sleep);
          }
          catch (InterruptedException e)
          {
            e.printStackTrace();
          }
        }
        toFront();
        //La boucle avant l'effacement du popup (destruction);
        long tpsRestant = delaiAvtEffacement;
        while (tpsRestant > 0)
        {
          try
          {
            Thread.sleep (20);
          }
          catch (InterruptedException e)
          {
            e.printStackTrace();
          }
          tpsRestant -= 20;
        }
        //Destruction du popup
        dispose();
        
        /* run () */
      }});
	  t.start();
	}

	
	/**
	 * Deplace le popup.
	 */
	public void movePopup ()
	{
	  movePopup(20L, 10L, 7000L);
	}

	
	public static void main (String [] args)
	{
	  new PopupConnect ("patachou").movePopup();
	}
	
	private class JPanelImageBg extends JComponent
	{
		private int mode;
		private TexturePaint texture; 
		private BufferedImage bufferedImage; 

		public static final int CENTRE = 0;
		public static final int TEXTURE = 1;

		JPanelImageBg( String fileName, int mode )
		{	this.mode = mode;
			this.bufferedImage = this.toBufferedImage(Toolkit.getDefaultToolkit().getImage(fileName));
			this.texture = new TexturePaint(bufferedImage,new Rectangle(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight())); 
		} 

		public void paintComponent(Graphics g)
		{	switch( mode )
			{	case TEXTURE :
					Graphics2D g2d = (Graphics2D)g; 
					g2d.setPaint(texture);
					g2d.fillRect(0, 0, getWidth(), getHeight() );
					break;
				case CENTRE :
					g.setColor(this.getBackground());
					g.fillRect(0,0,getWidth(), getHeight() );
					g.drawImage(bufferedImage,(getWidth()-bufferedImage.getWidth())/2,(getHeight()-bufferedImage.getHeight())/2,null);
					break;
				default :
					super.paintComponents(g);
			}
		}


		private BufferedImage toBufferedImage(Image image)
		{	image = new ImageIcon(image).getImage(); 

			BufferedImage bufferedImage = new BufferedImage( image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB); 
			Graphics g = bufferedImage.createGraphics(); 

			g.setColor(Color.white); 
			g.fillRect(0, 0, image.getWidth(null), 
			image.getHeight(null)); 
			g.drawImage(image, 0, 0, null); 
			g.dispose(); 
			return bufferedImage; 
		}

	}
}
 // Classe PopupConnect
