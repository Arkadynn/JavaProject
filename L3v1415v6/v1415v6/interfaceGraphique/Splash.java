package interfaceGraphique;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JFrame;


public class Splash extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Splash(){
		this.setUndecorated(true);
		this.setVisible(true);
		setSize(500, 250);
		Dimension resol = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(resol.width/2-250, resol.height/2-125);
		this.setAlwaysOnTop(true);
	}
	
	@Override
	public void paint(Graphics g){
		BufferedImage splash = null;
		try{
			splash = ImageIO.read(getClass().getResourceAsStream("splash.png"));
			g.drawImage(splash, 0, 0,null);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
