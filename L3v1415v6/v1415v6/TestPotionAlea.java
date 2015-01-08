

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;

import controle.Console;
import element.Element;
import element.potions.DentsBlanches;
import element.potions.Gonflette;
import element.potions.JoieDeVivre;
import element.potions.LeverLaLance;
import element.potions.MortHAL;

/**
 * Lance une potion aléatoire à une position aléatoire
 * @author Quentin
 *
 */
public class TestPotionAlea {

	public static void main(String[] args) {

		ArrayList<Element> potions = new ArrayList<Element>();
		
		potions.add(new DentsBlanches("DentsBlanches"));
		potions.add(new Gonflette("Gonflette"));
		potions.add(new JoieDeVivre("Joie De Vivre"));
		potions.add(new LeverLaLance("Lever La Lance"));
		potions.add(new MortHAL("MortHAL"));
		
		try {
			int port = 5099; // par defaut, 5099
			if (args.length > 0) {
				port = Integer.parseInt(args[0]);
			}
			
			String ipArene = "localhost"; // par défaut, localhost
			if (args.length > 1) { 
				ipArene = args[1];
			}
			
			Random r = new Random();
			Element potion = potions.get(r.nextInt(potions.size()));
	
			new Console(potion, r.nextInt(100), r.nextInt(100), port, ipArene);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}