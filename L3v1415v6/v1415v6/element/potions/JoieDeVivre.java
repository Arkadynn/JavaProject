package element.potions;

import element.Potion;


/**
 * JoieDeVivre est une Potion offrant un bonus de 50 en charisme.
 * @author Alexandre CANNY
 *
 */
public class JoieDeVivre extends Potion {

	private static final long serialVersionUID = 3147366421941177710L;

	public JoieDeVivre(String nom) {
		super(nom, 0, 50, -10, 0, -5);
	}
}
