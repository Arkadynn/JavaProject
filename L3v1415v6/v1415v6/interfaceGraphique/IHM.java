package interfaceGraphique;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import controle.IConsole;
import element.Element;
import element.Personnage;
import element.Potion;
import serveur.IArene;
/**
 * Definit l'interface graphique. Allure des fenetres, connexion, recup des elements sur l'arene et les dessines (a partir de VueElement)
 */
public class IHM extends JFrame {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Port par defaut pour communiquer avec l'arene;
	 */
	private int port=5099;
	/**
	 * Adresse IP de la machine hebergeant l'arene.
	 */
	private String ipArene="localhost";
	
	/**
	 * Enumeration des etats de l'interface.
	 */
	enum State{INIT,PLAYING};
	/**
	 * Etat de l'interface.
	 */
	private State state=State.INIT;
	/**
	 * Serveur.
	 */
	private Remote serveur;
	/**
	 * Thread de connexion au serveur.
	 */
	private Thread connection = null;
	/**
	 * Vrai si erreur de connexion.
	 */
	private boolean cnxError = false;
	/**
	 * Liste de tous les elements connectes a l'interface.
	 */
	private ArrayList<VueElement> world=new ArrayList<VueElement>();
	
	private ArrayList<ElemBoard> clicable = new ArrayList<ElemBoard>();
	
	private Element toDisplay;
	private IConsole refDisplay=null;
	
	private InfoJPanel infos;
	
	private Font fontPerso;
	
	/**
	 * Definit la fenetre de dialogue pour visualiser les arrivees, interactions, messages...
	 */
	private class AreneJTextArea extends JTextArea {
		private static final long serialVersionUID = 1L;
		AreneJTextArea() {
			super("Connexion...",10,10);
			setEditable(false);
		}
	}
	
	/** 
	 * Definit la fenetre de l'arene. Si le serveur de l'arene est connecte, recolte la VueElement des elements connectes et les dessine
	 */
	private class AreneJPanel extends JPanel {

		private static final long serialVersionUID = 1L;
		private JTextArea jta;
		
		//Conteneur qui affiche l'arene de jeu
		AreneJPanel(JTextArea jta) {
			this.jta=jta;
			setSize(new Dimension(800, 600));
			setPreferredSize(new Dimension(800, 600));
			setMinimumSize(new Dimension(800, 600));
			setMaximumSize(new Dimension(800, 600));
			setResizable(false);
		}
		
		public void paintComponent(Graphics g) {
			//affiche l'arene comme un rectangle
			Rectangle rect=this.getBounds();
			clicable = new ArrayList<ElemBoard>();
			
			//si la connexion est en cours ou il y a une erreur
			if ((state==State.INIT) || (cnxError)) {
				Font of=g.getFont();
				//g.setFont(new Font("Arial",Font.BOLD,20));
				g.setFont(fontPerso.deriveFont(20f));
				//affiche le message correspondant
				if (!cnxError) 
					g.drawString("Connexion en cours sur le serveur Arene...",20, rect.height-20);
				else 
					g.drawString("Erreur de connexion !",20, rect.height-20);
				g.setFont(of);
				
				//verifie si la connexion a ete realisee - isAlive (Thread)==true si on est en cours de connexion
				if ((connection!=null) && (! connection.isAlive())) {
					//affiche le message correspondant
					if (!cnxError) 
						jta.append("ok !"); 
					else 
						jta.append("erreur !");
					//mets a jour l'etat de l'arene
					state=State.PLAYING;
					//remets la connexion a null pour une autre execution
					connection=null;
				}
			} 
			else {
				try {
					BufferedImage back;
					try {
						back = ImageIO.read(new File("ressources/images/background.png"));
						g.drawImage(back, 0, 0, null);
					} catch (IOException e) {
						e.printStackTrace();
					}
					//mets a jour la liste des elements en vie sur l'arene
					world=((IArene) serveur).getWorld();
					
					int ref,cx,cy;
					String dial;
					
					//reinitialise l'affichage de l'arene
					jta.setText("");
					
					//pour chaque element en vie sur l'arene
					for(VueElement s:world) {
						//recupere sa reference
						ref=s.getRef();
						
						Random r=new Random(ref);
						//calcule une couleur pour la representation
						g.setColor(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255), 200));
						
						//recupere les coordonnes de l'element
						cx=s.getPoint().x*rect.width/100;
						cy=s.getPoint().y*rect.height/100;
						
						
						BufferedImage img;
						if(s.getControleur().getElement() instanceof Personnage){
							try {
								img = ImageIO.read(new File("ressources/images/char.png"));
								g.drawImage(img, cx, cy, null);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}else if(s.getControleur().getElement() instanceof Potion){
							Potion pot = (Potion) s.getControleur().getElement();
							int carac[] = new int[5];
							carac[0] = pot.getCharisme();
							carac[1] = pot.getDefense();
							carac[2] = pot.getForce();
							carac[3] = pot.getHP();
							carac[4] = pot.getVitesse();
							int max = carac[0],retenu=0;
							for(int i=1;i<5;i++){
								if(carac[i] > max){
									max = carac[i];
									retenu = i;
								}
							}
							try{
								switch (retenu) {
								case 0:
										img = ImageIO.read(new File("ressources/images/potion1.png"));
										g.drawImage(img, cx, cy, null);
									break;
								case 1:
										img = ImageIO.read(new File("ressources/images/potion2.png"));
										g.drawImage(img, cx, cy, null);
									break;
								case 2:
										img = ImageIO.read(new File("ressources/images/potion3.png"));
										g.drawImage(img, cx, cy, null);
									break;
								case 3:
										img = ImageIO.read(new File("ressources/images/potion4.png"));
										g.drawImage(img, cx, cy, null);
									break;
								default:
										img = ImageIO.read(new File("ressources/images/potion5.png"));
										g.drawImage(img, cx, cy, null);
									break;
								}
							}catch(IOException ex){
								ex.printStackTrace();
							}
							
						}
						clicable.add(new ElemBoard(s.getControleur(), cx, cy));
						//recupere les phrases dites par l'element
						dial=(s.getPhrase()==null)?"":" : "+s.getPhrase();
						
						//affiche au dessus du point ses informations
						// TODO Chaine lisible
						g.setColor(Color.WHITE);
						//g.setFont(new Font("Arial", Font.BOLD, 14));
						g.setFont(fontPerso.deriveFont(14f));
						g.drawString("" + s.getRef(), cx+34, cy+14);
						g.drawString(s.getControleur().getElement().getNom(), cx+34, cy+30);
						
						//g.drawString(s.afficher(), cx+10, cy);
						
						//affiche dans la fenetre a cote ses informations
						jta.append(s.afficher()+dial+"\n");
					}
					infos.repaint();
				} 
				
				catch (RemoteException e) {
					//en cas de deconnexion ou erreur du serveur
					//remets l'etat de l'arene a jour
					state=State.INIT;
					//affiche un dialog avec le message d'erreur
					JOptionPane.showMessageDialog(this,"Erreur de connection !\nRaison : "+e.getMessage(),"Message",JOptionPane.ERROR_MESSAGE);
					cnxError=true;
					e.printStackTrace();
				}
			}
			
			//affiche l'heure courante
			g.setColor(Color.BLACK);
			g.drawString(DateFormat.getTimeInstance().format(new Date()),rect.width-60,20);
		}
	}

	
	private class InfoJPanel extends JPanel{
		private static final long serialVersionUID = 1L;
		
		public InfoJPanel() {
			this.setSize(800, 125);
			this.setPreferredSize(new Dimension(800,125));
			this.setMaximumSize(new Dimension(800,125));
			this.setMinimumSize(new Dimension(800,125));
			this.setVisible(true);
			this.setBackground(Color.WHITE);
			this.repaint();
		}
		
		@Override
		public void paintComponent(Graphics g) {
			BufferedImage back;
			try {
				back = ImageIO.read(new File("ressources/images/stat.png"));
				g.drawImage(back, 0, 0, null);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(refDisplay != null){
				try {
					toDisplay = refDisplay.getElement();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				if(toDisplay instanceof Personnage){
					//Font of=g.getFont();
					//g.setFont(new Font("Arial",Font.BOLD,32));
					g.setFont(fontPerso.deriveFont(32f));
					g.setColor(Color.WHITE);
					g.drawString(toDisplay.getNom(), 20, 55);
					//g.setFont(new Font("Arial",Font.PLAIN,18));
					g.setFont(fontPerso.deriveFont(18f));
					if(((Personnage) toDisplay).getEquipe().size() != 0){
						//g.setFont(new Font("Arial",Font.PLAIN,14));
						//g.drawString("Est un team leader.", 10, 60);
						g.drawString("Team Leader", 20, 75);
					}else{
						//g.setFont(new Font("Arial",Font.PLAIN,14));
						//g.drawString("Double cliquez ici pour le leader.", 10, 60);
						g.drawString("Personnage", 20, 75);
					}
					
				}else if(toDisplay instanceof Potion){
					//g.setFont(new Font("Arial",Font.BOLD,32));
					g.setFont(fontPerso.deriveFont(32f));
					g.setColor(Color.WHITE);
					g.drawString(toDisplay.getNom(), 20, 55);
					//g.setFont(new Font("Arial",Font.PLAIN,18));
					g.setFont(fontPerso.deriveFont(18f));
					g.drawString("Potion", 20, 75);
					//g.setFont(new Font("Arial",Font.BOLD,32));
					g.setFont(fontPerso.deriveFont(32f));
				}
				//g.setFont(new Font("Arial",Font.BOLD,32));
				g.setFont(fontPerso.deriveFont(32f));
				g.setColor(Color.WHITE);
				g.drawString(""+toDisplay.getCaract("force"),235,75);
				g.drawString(""+toDisplay.getCaract("defense"),370,75);
				g.drawString(""+toDisplay.getCaract("vitesse"),495,75);
				g.drawString(""+toDisplay.getCaract("hp"),615,75);
				g.drawString(""+toDisplay.getCaract("charisme"),720,75);
			}else{
				//g.setFont(new Font("Arial",Font.BOLD,32));
				g.setFont(fontPerso.deriveFont(32f));
				g.setColor(Color.WHITE);
				g.drawString("ARENA", 20, 75);
			}
		}
	}
	
	/**
	 * Dessine les fenetres et leur contenu
	 * @param port
	 * @param ipArene
	 */
	public IHM(int port, String ipArene) {
		this.port = port;
		this.ipArene = ipArene;
		Toolkit kit=Toolkit.getDefaultToolkit();
		
		try {
            fontPerso = Font.createFont(Font.TRUETYPE_FONT, IHM.class.getResourceAsStream("losbanditos.ttf"));
        } catch (FontFormatException e) {
            e.printStackTrace();  
        } catch (IOException e) {
            e.printStackTrace();  
        }
		
		//personnalise et positionne la fenetre par rapport a l'ecran
		Dimension size=kit.getScreenSize();
		setSize(800, 765);
		setLocation(size.width/4, size.height/4);
		//setResizable(false);
		
		//cree un titre de la fenetre
		setTitle("IHM - Arene / UPS - Projet Prog");
		
		//ajout une operation si le bouton de fermeture de la fenetre est clique
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		getContentPane().setLayout(new FlowLayout(FlowLayout.TRAILING,0,0));
		
		//ajout d'une action pour arreter l'execution de l'interface graphique
		Action exitAction=new AbstractAction("Quitter") {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent ae) {
				System.exit(0);
			}
		};
		
		Action aboutAction=new AbstractAction("A propos") {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent ae) {
				JOptionPane.showMessageDialog(null,"Arene\nInspiree des TP de L3");
			}
		};
		
		//creation d'un menu Fichier avec deux options - quitter et a propos 	
		JMenuBar m=new JMenuBar();
		JMenu file=new JMenu("Fichier");
		file.add(aboutAction);
		file.add(exitAction);
		m.add(file);
		setJMenuBar(m);
				
		//ajout de l'arene dans la fenetre
		AreneJTextArea ajta=new AreneJTextArea();
		AreneJPanel ajp = new AreneJPanel(ajta);
		ajp.addMouseListener(new PanelListener(this));
		getContentPane().add(ajp);
		
		
		//Ajout d'informations en bas de la fenetre
		infos = new InfoJPanel();
		getContentPane().add(infos);
		
		setVisible(true);
		//pack();
		//Fenetre qui affiche les messages des console
		JFrame jf=new JFrame();
		jf.setSize(550, 200);
		jf.setLocation(size.width*3/5, size.height/10);
		jf.getContentPane().add(new JScrollPane(ajta));
		jf.setTitle("The Rectangle Ring");
		jf.setVisible(true);
		jf.setAlwaysOnTop(true);
	}
	
	/**
	 * Lance une connexion au serveur dans un thread separe
	 */
	public void connect() {
		connection=new Thread() {
			public void run() {
				try {
					//pour utiliser les parametres
					serveur=Naming.lookup("rmi://"+ipArene+":"+port+"/Arene");
					//pour machine locale
					//serveur=Naming.lookup("rmi://localhost:"+port+"/Arene");
					//pour machine des salles TP TODO : changer en azteca ?
					//serveur=Naming.lookup("rmi://ouvea.edu.ups-tlse.fr:5099/Arene");
				} 
				catch (Exception e) {
					cnxError=true;
					JOptionPane.showMessageDialog(null,"Impossible de se connecter au serveur Arene:"+port+" !\n(le serveur ne doit pas etre actif...)\nRaison : "+e.getMessage(),"Message",JOptionPane.ERROR_MESSAGE);
				}
			}
		};
		connection.start();
	}
	
	public IConsole getElemAt(int x, int y) throws Exception{
		for(ElemBoard eb:clicable){
			if(eb.isVisibleAt(x, y)){
				return eb.getContent();
			}
		}
		throw new Exception("No element here.");
	}
	
	public void setView(IConsole elem){
		this.refDisplay = elem;
	}
	
}
