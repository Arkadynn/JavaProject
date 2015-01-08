package element.potions;

import element.Potion;

/**
 * Un Fortifiant est une Potion offrant un bonus de 50 en force.
 * @author Alexandre CANNY
 *
 */
public class Gonflette extends Potion {

	private static final long serialVersionUID = 1817574549681894795L;

	public Gonflette(String nom) {
		super(nom, 50, 0, 0, 0, 0);
	}

}
