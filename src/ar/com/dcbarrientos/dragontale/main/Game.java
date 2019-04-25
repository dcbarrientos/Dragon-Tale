package ar.com.dcbarrientos.dragontale.main;

import javax.swing.JFrame;

public class Game {
	public static final String TITLE = "Dragon Tale";
	public static final boolean DEBUG = false;
	
	public static void main(String[] args) {
		JFrame window = new JFrame(TITLE);
//		window.setUndecorated(true);
		window.setContentPane(new GamePanel());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setVisible(true);
		window.setLocationRelativeTo(null);
	}
}
