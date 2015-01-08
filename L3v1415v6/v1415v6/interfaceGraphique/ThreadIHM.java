package interfaceGraphique;

public class ThreadIHM extends Thread{

	private IHM ihm;
	
	public ThreadIHM(int port, String ipServeur) {
		this.ihm = new IHM(port, ipServeur);
		
	}
	
	@Override
	public void run() {
		this.ihm.connect();
		this.ihm.repaint();
		while(true) {
			try {Thread.sleep(35);} catch (InterruptedException e) {e.printStackTrace();}
			ihm.repaint(); 
		}
	}
	
}
