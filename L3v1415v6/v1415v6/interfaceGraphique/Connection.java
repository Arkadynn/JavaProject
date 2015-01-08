package interfaceGraphique;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.xml.bind.Marshaller.Listener;

public class Connection extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	JTextField ip, port;
	Thread music;
	
	public Connection(){ 
		super("Se connecter");
		music = new MP3Player("splash.mp3");
		music.start();
		this.setUndecorated(true);
		ActionListener al = new ConnButtons();
		Container contain = getContentPane();
		Dimension resol = Toolkit.getDefaultToolkit().getScreenSize();
		JButton cancel, ok;
		JLabel logo = new JLabel(new ImageIcon("ressources/images/login.png"));
		JPanel logopan = new JPanel();
		logopan.add(logo);
		cancel = new JButton("Quitter");
		ok = new JButton("Connecter");
		ok.setPreferredSize(new Dimension(120,25));
		cancel.addActionListener(al);
		cancel.setPreferredSize(new Dimension(120,25));
		ok.addActionListener(al);
		this.ip = new JTextField();
		ip.setPreferredSize(new Dimension(200, 30));
		this.port = new JTextField();
		port.setPreferredSize(new Dimension(200, 30));
		this.setPreferredSize(new Dimension(350,250));
		this.setLocation(resol.width/2-175, resol.height/2-125);
		this.setResizable(false);
		this.requestFocus();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contain.setLayout(new BoxLayout(contain, BoxLayout.Y_AXIS));
		logo.setPreferredSize(new Dimension(350, 100));
		this.add(logopan);
		JPanel ipl = new JPanel();
		ipl.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 0));
		ipl.add(new JLabel("IP du serveur : "));
		ipl.add(this.ip);
		this.add(ipl);
		JPanel portl = new JPanel();
		portl.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 0));
		portl.add(new JLabel("Port :"));
		portl.add(port);
		this.add(portl);
		JPanel buttons = new JPanel();
		buttons.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 0));
		buttons.add(cancel);
		buttons.add(ok);
		this.add(buttons);
		this.pack();
		this.setVisible(true);
	}

	class ConnButtons implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton bouton = ((JButton) (e.getSource()));
			if(bouton.getText().equals("Quitter")){
				music.stop();
				dispose();
			}else{
				Thread ihm = new ThreadIHM(Integer.parseInt(port.getText()), ip.getText());
				ihm.start();
				music.stop();
				dispose();
			}
		}
		
	}

}
