package element.personnage;

import interaction.Actions;
import interaction.Deplacements;
import interfaceGraphique.VueElement;

import java.awt.Point;
import java.rmi.RemoteException;
import java.util.Hashtable;

import element.Personnage;
import element.Potion;

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

		Actions a = new Actions(ve, voisins);
		Deplacements d = new Deplacements(ve, voisins);

		for (Integer i : voisins.keySet()) {
			VueElement current = voisins.get(i);
			Hashtable<String, Integer> currentCaracs = current.getControleur().getElement().getCaract();

			if (voisins.get(i).getControleur().getElement() instanceof Personnage) {
				// TODO Traitement si c'est un personnage
				if (!this.getEquipe().contains(i) && this.getLeader() != i) {
					// TODO Un ennemi
					ennemis.put(i, current);
				} else {
					// TODO c'est un allier
					allies.put(i, current);
				}
			} else {
				// TODO Traitement si c'est une potion

				if (currentCaracs.get("charisme") < 0) {
					//					on perdrait trop de charisme
					continue;
				}

				if (currentCaracs.get("hp") + getCaract("hp") <= 0) {
					//					La potion nous ferait mourrir
					continue;
				}

				if (currentCaracs.get("vitesse") < 0) {
					if (getCaract("vitesse") == 1) {
						//						 On ne veut pas être immobile
						continue;
					} else {
						if (currentCaracs.get("force") + (currentCaracs.get("defense") * 10/6) + currentCaracs.get("hp") <= 90) {
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
						if (currentCaracs.get("hp") < -20) {
							//							La potion nous ferait mourrir
							continue;
						}

						if (currentCaracs.get("force") + getCaract("force") <= -15) {
							//							potentiellement bon, puisque nous avons atténué les effets négatifs par notre absance de force
							if (currentCaracs.get("charisme") + currentCaracs.get("defense") * 10/6 + currentCaracs.get("hp") < -30) {
								// Trop de malus malgrès le malus compensé de force
								continue;
							}
						}

						if ((currentCaracs.get("defense") + getCaract("defense"))*10/6 <= -15) {
							//							potentiellement bon, puisque nous avons atténué les effets négatifs par notre absance de force
							if (currentCaracs.get("charisme") + currentCaracs.get("force") + currentCaracs.get("hp") < -30) {
								// Trop de malus malgrès le malus compensé de force
								continue;
							}
						}

						potions.put(i, current);
					}
				}
			}
		}

		Point pointOptimal = new Point();
		boolean first = true;
		int refPot = 0;
		double distance = 10000;

		if (getCharisme() != 100) {
			// On cherche a avoir 100 de charisme

			double dist;

			for (Integer i : potions.keySet()) {
				Potion current = (Potion)potions.get(i).getControleur().getElement();

				if (current.getCharisme() + getCharisme() >= 100) {
					dist = ve.getPoint().distance(potions.get(i).getPoint());
					if (dist < distance) {
						distance = dist;
						refPot = i;
					}
				}
			}
		}

		if (distance != 10000) { // TODO GERER LES POTIONS AUTRE QUE CHARISME
			// se diriger vers la potion pour la rammasser
			if (distance <= 2) {
				a.ramasser(ve.getRef(), refPot, ve.getControleur().getArene());
			} else {
				d.seDirigerVers(refPot);
			}
		} else {
			for (Integer i : ennemis.keySet()) {
				Personnage current = (Personnage)ennemis.get(i).getControleur().getElement();

				if (current.getForce() >= this.getCharisme() || current.getCharisme() > this.getCharisme()) {
					// TODO Fuire cet ennemi
					// il est dangereux pour notre santé, on a que 50% de chance de le convertir
					// ou on ne pourra pas faire de coup d'état 


					Point pM = ennemis.get(i).getPoint();
					Point pE = ve.getPoint();

					Point dest = new Point (
							(pM.x < pE.x) ? -1 : 1,
									(pM.y < pE.y) ? -1 : 1
							);
					dest.setLocation(dest.x * getVitesse(), dest.y * getVitesse());
					dest.translate(pM.x, pM.y);

					if (first) {
						pointOptimal = dest;
						first = false;
					} else {
						pointOptimal = new Point (
								(pointOptimal.x + dest.x) / 2,
								(pointOptimal.y + dest.y) / 2
								);

					}
				} else {
					// TODO Envisager un Free Hug

					Point pM = ennemis.get(i).getPoint();
					Point pE = ve.getPoint();

					Point dest = new Point (
							(pM.x < pE.x) ? 1 : -1,
									(pM.y < pE.y) ? 1 : -1
							);
					dest.setLocation(dest.x * getVitesse(), dest.y * getVitesse());
					dest.translate(pM.x, pM.y);

					if (first) {
						pointOptimal = dest;
						first = false;
					} else {
						pointOptimal = new Point (
								(pointOptimal.x + dest.x) / 2,
								(pointOptimal.y + dest.y) / 2
								);
					}
				}
			}
		}

		if (first) {
			d.seDirigerVers(pointOptimal);
		} else {
			d.seDirigerVers(0);
		}
	}
}
