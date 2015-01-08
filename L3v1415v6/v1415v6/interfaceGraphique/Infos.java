package interfaceGraphique;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import controle.IConsole;
import element.Element;

public class Infos extends JPanel{
	
	private Font fontPerso;
	private IConsole console;
	
	public Infos(IConsole console) {
		this.console = console;
		setPreferredSize(new Dimension(200, 200));
		try {
            fontPerso = Font.createFont(Font.TRUETYPE_FONT, IHM.class.getResourceAsStream("losbanditos.ttf"));
        } catch (FontFormatException e) {
            e.printStackTrace();  
        } catch (IOException e) {
            e.printStackTrace();  
        }
	}
	
	@Override
	public void paintComponent(Graphics g) {
		//super.paintComponents(g);
		Element elem = null;
		try {
			elem = console.getElement();
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedImage back;
		try {
			back = ImageIO.read(new File("ressources/images/FichePerso.png"));
			g.drawImage(back, 0, 0, null);
			g.setFont(fontPerso.deriveFont(32f));
			g.setColor(Color.WHITE);
			g.drawString(elem.getNom() + " (" + console.getRefRMI() + ")", 13, 40);
			g.setFont(fontPerso.deriveFont(26f));
			g.drawString(""+elem.getCaract("hp"), 50, 88);
			g.drawString(""+elem.getCaract("defense"), 50, 92+(24));
			g.drawString(""+elem.getCaract("vitesse"), 50, 92+(24*2));
			g.drawString(""+elem.getCaract("charisme"), 50, 92+(24*3));
			g.drawString(""+elem.getCaract("force"), 50, 92+(24*4));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
