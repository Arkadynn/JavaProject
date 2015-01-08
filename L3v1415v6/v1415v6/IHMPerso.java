import interfaceGraphique.Connection;
import interfaceGraphique.Splash;

import javax.swing.JFrame;


public class IHMPerso {

	public static void main(String[] args) {
		JFrame splash = new Splash();
		new Connection();
		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		splash.dispose();
	}
	
}
