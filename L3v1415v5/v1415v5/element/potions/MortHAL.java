package element.potions;

import element.Potion;

/**
 * MortHAL est une Potion dites assassine, elle tue instantanement quiconque
 * la ramasse.
 * @author Alexandre CANNY
 *
 */
public class MortHAL extends Potion {

	public MortHAL() {
		super("Neuf vies...", 0, 0);
		this.ajouterCaract("assassine", 1);
	}

}
