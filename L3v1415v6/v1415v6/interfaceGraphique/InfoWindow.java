package interfaceGraphique;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.rmi.RemoteException;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;

import controle.IConsole;
import element.Element;
import element.Personnage;

public class InfoWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private IConsole observe;
	private JLabel name,type,carac;
	private boolean isChar = false;
	
	public InfoWindow(IConsole observable){
		Container c = this.getContentPane();
		c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
		this.observe = observable;
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setPreferredSize(new Dimension(200, 200));
		this.setResizable(false);
		this.setBackground(Color.WHITE);
		try {
			name = new JLabel(observe.getElement().getNom() + " (" + observe.getRefRMI() + ")");
			name.setFont(new Font("Arial", Font.PLAIN, 32));
			this.add(name);
			if(observe.getElement() instanceof Personnage){
				isChar = true;
				type = new JLabel("Personnage");
			}else{
				isChar = false;
				type = new JLabel("Potion");
			}
			type.setFont(new Font("Arial", Font.PLAIN, 24));
			this.add(type);
			carac = new JLabel();
			rebuildCarac();
			this.add(carac);
			new Refresh().start();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.pack();
		this.setVisible(true);
	}
	
	public void rebuildCarac(){
		String infos = "<html><br><br>";
		Element toDisplay = null;
		try {
			toDisplay = observe.getElement();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		if(isChar){
			infos+= "Vie      : " + toDisplay.getCaract("hp") + "<br>Force    : " + toDisplay.getCaract("force") + "<br>Defense  : " + toDisplay.getCaract("defense") + "<br>Charisme : " + toDisplay.getCaract("charisme") + "<br>Vitesse  : " + toDisplay.getCaract("vitesse") + "</html>";
		}else{
			infos+= "Vie      : " + toDisplay.getCaract("HP") + "<br>Force    : " + toDisplay.getCaract("force") + "<br>Defense  : " + toDisplay.getCaract("defense") + "<br>Charisme : " + toDisplay.getCaract("charisme") + "<br>Vitesse  : " + toDisplay.getCaract("vitesse") + "</html>";
		}
		carac.setText(infos);
	}
	
	class Refresh extends Thread{
		
		@Override
		public void run() {
			while(true){
				try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
				rebuildCarac();
			}
		}
		
		
	}


}
