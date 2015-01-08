package interfaceGraphique;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class MP3Player extends Thread {

	Player lecteur;
	
	public MP3Player(String filepath){
		try {
            lecteur = new Player(MP3Player.class.getResourceAsStream(filepath));
        } catch (JavaLayerException e) {
           System.out.println(e);
        }
	}
	
	@Override
	public void run() {
		try {
			lecteur.play();
		} catch (JavaLayerException e) {
			e.printStackTrace();
		}
	}
	
}
