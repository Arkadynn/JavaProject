package element.potions;

import element.Potion;

/**
 * MortHAL est une Potion dites assassine, elle tue instantanement quiconque
 * la ramasse.
 * @author Alexandre CANNY
 *
 */
public class MortHAL extends Potion {

	private static final long serialVersionUID = -2589704733447119434L;

	public MortHAL(String nom) {
		super(nom, 10, 10, -99, 2, 0);
	}
}
