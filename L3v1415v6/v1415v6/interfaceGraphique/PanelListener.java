package interfaceGraphique;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class PanelListener implements MouseListener {
	
	IHM ihm;
	
	public PanelListener(IHM ihm) {
		this.ihm = ihm;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		try {
			ihm.setView(ihm.getElemAt(e.getX(), e.getY()));
			if(e.getClickCount() == 2){
				//new InfoWindow(ihm.getElemAt(e.getX(), e.getY()));
				new FichePerso(ihm.getElemAt(e.getX(), e.getY()));
			}
		} catch (Exception e1) {
			ihm.setView(null);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
