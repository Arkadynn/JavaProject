package interfaceGraphique;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import controle.IConsole;

public class FichePerso extends JFrame {
	
	private IConsole observe;
	private JPanel infos;
	
	public FichePerso(IConsole observable) {
		this.observe = observable;
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.infos = new Infos(this.observe);
		this.add(infos);
		this.pack();
		this.setVisible(true);
		new ThreadFiche().start();
	}
	
	
	class ThreadFiche extends Thread{
		
		@Override
		public void run() {
			while(true) {
				try {Thread.sleep(35);} catch (InterruptedException e) {e.printStackTrace();}
				infos.repaint(); 
			}
		}
		
	}

}
