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
				
				
				if (currentCaracs.get("vitesse") < 0) {
					if (getCaract("vitesse") == 1) {
//						 On ne veut pas être immobile
						continue;
					} else {
						if (currentCaracs.get("force") + (currentCaracs.get("defense") * 10/6) + currentCaracs.get("vie") <= 90) {
//							 La potion ne vaut pas le coup
							continue;
						} else {
							potions.put(i, current);
						}
					}
				} else if (currentCaracs.get("vitesse") > 0) {
//					 Semble être une bonne potion mais : 
//					 en a t'on besoin
					if (getCaract("vitesse") == 4) {
//						Non
						continue;
					} else {
//						Oui
						if (currentCaracs.get("vie") < -20 && getCaract("vie") < 21) {
//							La potion nous ferait mourrir
							continue;
						}
						
						if (currentCaracs.get("charisme") < -15) {
//							on perdrait trop de charisme
							continue;
						}
						
						if (currentCaracs.get("force") + getCaract("force") <= -15) {
//							potentiellement bon, puisque nous avons atténué les effets négatifs par notre absance de force
							if (currentCaracs.get("charisme") + currentCaracs.get("defense") * 10/6 + currentCaracs.get("vie") < -30) {
								// Trop de malus malgrès le malus compensé de force
								continue;
							}
						}
						
						if ((currentCaracs.get("defense") + getCaract("defense"))*10/6 <= -15) {
//							potentiellement bon, puisque nous avons atténué les effets négatifs par notre absance de force
							if (currentCaracs.get("charisme") + currentCaracs.get("force") + currentCaracs.get("vie") < -30) {
								// Trop de malus malgrès le malus compensé de force
								continue;
							}
						}
						
						potions.put(i, current);
						// TODO reprendre ici
					}
				}
				
				
			}
		}
		
		
	}
}
