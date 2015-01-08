package element.potions;

import element.Potion;

/**
 * DentsBlanches est une Potion conférant un bonus de 100 en Charisme,
 * mais infligeant un malus de 25 en force
 * @author Alexandre CANNY
 *
 */
public class DentsBlanches extends Potion {

	private static final long serialVersionUID = -4579204319165694516L;

	public DentsBlanches(String nom) {
		super(nom, -25, 50, 0, 0, 5);
	}
}
