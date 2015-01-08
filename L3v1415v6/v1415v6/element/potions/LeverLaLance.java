package element.potions;

import element.Potion;


/**
 * LeverLaLance est une Potion offrant un bonus de 25 en force et en charisme.
 * @author Alexandre CANNY
 *
 */
public class LeverLaLance extends Potion {

	private static final long serialVersionUID = 4895455296441989247L;

	public LeverLaLance(String nom) {
		super(nom, 25, 25, -10, -1, 2);
	}
}
