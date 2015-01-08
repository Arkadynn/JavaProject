package interfaceGraphique;

import controle.IConsole;

public class ElemBoard {

	private IConsole contenu;
	private int x,y;
	
	public ElemBoard(IConsole elem,int x,int y) {
		this.contenu = elem;
		this.x = x;
		this.y = y;
	}
	
	public boolean isVisibleAt(int x, int y){
		if(x >= this.x && x <= this.x+32){
			if(y >= this.y && y <= this.y+32){
				return true;
			}
		}
		return false;
	}
	
	public IConsole getContent(){
		return contenu;
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
	
}
