package element.potions;

import element.Potion;

/**
 * DentsBlanches est une Potion conférant un bonus de 100 en Charisme,
 * mais infligeant un malus de 25 en force
 * @author Alexandre CANNY
 *
 */
public class DentsBlanches extends Potion {

	public DentsBlanches() {
		super("Dents Blances", -25, 100);
	}

}
