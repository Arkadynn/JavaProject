


import java.rmi.RemoteException;

import controle.Console;
import element.Personnage;
import element.personnage.TeaPot;

/**
 * Test de la Console avec un Element qui s'ajoute a l'Arene (apres lancement Arene et IHM). A lancer en plusieurs exemplaires.
 */
public class TestPersonnageCentre {

	public static void main(String[] args) {

		try {
			int port = 5099; // par defaut, 5099
			if (args.length > 0) {
				port = Integer.parseInt(args[0]);
			}
			
			String ipArene = "172.28.112.180"; // par défaut, localhost
			if (args.length > 1) { 
				ipArene = args[1];
			}
			
			Personnage bidule = new TeaPot("ImATeaPot");
			
			new Console(bidule, 40, 40, port, ipArene);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}