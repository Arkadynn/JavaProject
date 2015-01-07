package element.personnage;

import interfaceGraphique.VueElement;

import java.rmi.RemoteException;
import java.util.Hashtable;

import element.Personnage;

public class TeaPot extends Personnage {

	private static final long serialVersionUID = 1L;

	public TeaPot(String nom) {
		super(nom, 0, 99, 0, 1, 0);
	}
	
	@Override
	public void strategie(VueElement ve, Hashtable<Integer, VueElement> voisins, Integer refRMI) throws RemoteException {
		Hashtable<Integer, VueElement> ennemis = new Hashtable<Integer, VueElement>();
		Hashtable<Integer, VueElement> potions = new Hashtable<Integer, VueElement>();
		Hashtable<Integer, VueElement> allies = new Hashtable<Integer, VueElement>();
		
		for (Integer i : voisins.keySet()) {
			VueElement current = voisins.get(i);
			Hashtable<String, Integer> currentCaracs = current.getControleur().getElement().getCaract();
			
			if (voisins.get(i).getControleur().getElement() instanceof Personnage) {
				// TODO Traitement si c'est un personnage
				if (!this.getEquipe().contains(i)) {
					// TODO Un ennemi
					ennemis.put(i, current);
				} else {
					// TODO c'est un allier
					allies.put(i, current);
				}
			} else {
				// TODO Traitement si c'est une potion
				
				for (String s : currentCaracs.keySet()) {
					switch (s) {
					case "defense":
						if (currentCaracs.get(s) + getCaract(s) <= 60) {
							// TODO potentiellement bon
						} else {
							// TODO forcement mauvais --> potentiellement kick
						}
						break;
					case "vitesse":
						if (currentCaracs.get(s) + getCaract(s) <= 4) {
							// TODO potentiellement bon
						} else {
							// TODO forcement mauvais --> potentiellement kick
						}
						break;
					case "vie":
					case "force":
					case "charisme":
						if (currentCaracs.get(s) + getCaract(s) <= 100) {
							// TODO potentiellement bon
						} else {
							// TODO forcement mauvais --> potentiellement kick
						}
						break;
					default:
						System.err.println("Stat non reconnue pour cette potion : RMIID = " + i);	
					}
				}
				potions.put(i, current);
			}
		}
		
		
	}
}
