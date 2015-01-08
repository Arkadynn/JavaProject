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
				// Traitement si c'est un personnage
				if (!this.getEquipe().contains(i) && this.getLeader() != i) {
					// Un ennemi
					ennemis.put(i, current);
				} else {
					// Un allie
					allies.put(i, current);
				}
			} else {
				// Traitement si c'est une potion

				if (currentCaracs.get("charisme") < 0) {
					// on perdrait trop de charisme
					continue;
				}

				if (currentCaracs.get("hp") + getCaract("hp") <= 0) {
					// La potion nous ferait mourrir
					continue;
				}

				if (currentCaracs.get("vitesse") < 0) {
					if (getCaract("vitesse") == 1) {
						// On ne veut pas être immobile
						continue;
					} else {
						if (currentCaracs.get("force") + (currentCaracs.get("defense") * 10/6) + currentCaracs.get("hp") <= 90) {
							// La potion ne vaut pas le coup
							continue;
						} else {
							potions.put(i, current);
						}
					}
				} else if (currentCaracs.get("vitesse") > 0) {
					// Semble être une bonne potion mais : 
					// en a t'on besoin ?
					if (getCaract("vitesse") == 4) {
						// Non
						continue;
					} else {
						// Oui
						if (currentCaracs.get("hp") < -20) {
							// Mais la potion nous ferait trop de degats pour etre acceptable
							continue;
						}

						if (currentCaracs.get("force") + getCaract("force") <= -15) {
							// potentiellement bon, puisque nous avons atténué les effets négatifs par notre absance de force
							if (currentCaracs.get("charisme") + currentCaracs.get("defense") * 10/6 + currentCaracs.get("hp") < -30) {
								// Trop de malus malgrès le malus compensé de force
								continue;
							}
						}

						if ((currentCaracs.get("defense") + getCaract("defense"))*10/6 <= -15) {
							// potentiellement bon, puisque nous avons atténué les effets négatifs par notre absance de force
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

		int refPot = 0; // reference de la potion a aller chercher, si aucune, deplacement aléatoire avec le 0 
		double distance = 10000; // distance de l'objectif, utilisé pour selectionner la meilleur cible selon les critères courrant
		double dist; // varible temporaire de calcul stockant la distance a l'objet courrant

		// On cherche a optimiser ses stats en buvant des potions
		for (Integer i : potions.keySet()) {
			Potion current = (Potion)potions.get(i).getControleur().getElement();
			// On cherche d'abord à avoir le meilleur charisme possible
			if (getCharisme() != 100) {
				if (current.getCharisme() + getCharisme() >= 100) {
					dist = ve.getPoint()
							.distance(potions.get(i).getPoint());
					if (dist < distance) {
						distance = dist;
						refPot = i;
					}
				}
			} else if (getHP() != 100) { // puis on maximise les hp pour mieux resister
				if (current.getCharisme() > 0 && current.getHP() > 0) {
					dist = ve.getPoint()
							.distance(potions.get(i).getPoint());
					if (dist < distance) {
						distance = dist;
						refPot = i;
					}
				}
			} else if (getVitesse() != 4) { // puis la vitesse pour fuire et rattraper nos proies
				if (current.getCharisme() > 0 && current.getVitesse() > 0) {
					dist = ve.getPoint()
							.distance(potions.get(i).getPoint());
					if (dist < distance) {
						distance = dist;
						refPot = i;
					}
				}
			} else if (getForce() != 100) { // et enfin la force, car on sait jamais
				if (current.getCharisme() > 0 && current.getVitesse() > 0 && current.getForce() > 0) {
					dist = ve.getPoint()
							.distance(potions.get(i).getPoint());
					if (dist < distance) {
						distance = dist;
						refPot = i;
					}
				}
			}
		}

		// Recherche d'un ennemi menacant
		Point pointOptimal = new Point();
		boolean first = true;
		
		for (Integer i : ennemis.keySet()) {
			Personnage current = (Personnage)ennemis.get(i).getControleur().getElement();
			
			// il est dangereux pour notre santé, on a que 50% de chance de le convertir
			// ou on ne pourra pas faire de coup d'état
			if (current.getForce() >= this.getCharisme() || current.getCharisme() > this.getCharisme()) {
				Point pM = ennemis.get(i).getPoint(); // Emplacement de l'ennemi
				Point pE = ve.getPoint(); // Emplacement de notre personnage

				Point dest = new Point (
						(pM.x < pE.x) ? -1 : 1,
								(pM.y < pE.y) ? -1 : 1
						); // vecteur unitaire de fuite
				dest.setLocation(dest.x * getVitesse(), dest.y * getVitesse()); // on l'adatpe a notre vitesse pour parcourir le maximum de cases
				dest.translate(pM.x, pM.y); // on calcul le point de chute théorique

				// si c'est la premiere distance --> simple affectation
				if (first) {
					pointOptimal = dest;
					first = false;
				} else { // sinon moyenne avec le point de chute precedent pour fuire a la fois les deux menaces
					pointOptimal = new Point (
							(pointOptimal.x + dest.x) / 2,
							(pointOptimal.y + dest.y) / 2
							);

				}
			} else {
				// Envisager un Free Hug (tentative de charme =D)

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

		if (first) { // si first est true, on a rencontré aucune menace ni aucune cible, donc on se dirige vers la potion le plus proche, ou on erre si 0
			if (distance <= 2) {
				a.ramasser(ve.getRef(), refPot, ve.getControleur().getArene());
			} else {
				d.seDirigerVers(refPot);
			}
		} else { // on fuit ou on se deplace vers notre cible
			d.seDirigerVers(pointOptimal);
		}
	}
}
