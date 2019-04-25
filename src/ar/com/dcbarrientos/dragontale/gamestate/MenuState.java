package ar.com.dcbarrientos.dragontale.gamestate;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import ar.com.dcbarrientos.dragontale.audio.AudioPlayer;
import ar.com.dcbarrientos.dragontale.tilemap.Background;

public class MenuState extends GameState {
	private Background bg;

	private int currentChoice = 0;
	private String[] options = { "Start", "Help", "Quit" };
	private Color titleColor;
	private Font titleFont;
	private Font font;
	
	private AudioPlayer menuOptionSound;
	private AudioPlayer menuSelectSound;

	public MenuState(GameStateManager gsm) {
		this.gsm = gsm;
		try {
			menuOptionSound = new AudioPlayer("/SFX/menuoption.mp3");
			menuSelectSound = new AudioPlayer("/SFX/menuselect.mp3");
			bg = new Background("/Backgrounds/menubg.gif", 1);
			bg.setVector(-0.1, 0);
			titleColor = new Color(128, 0, 0);
			titleFont = new Font("Century Gothic", Font.PLAIN, 28);
			font = new Font("Arial", Font.PLAIN, 12);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void init() {

	}

	public void update() {
		bg.update();
	}

	public void draw(Graphics2D g) {
		bg.draw(g);

		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("Dragon Tale", 80, 70);

		g.setFont(font);
		for (int i = 0; i < options.length; i++) {
			if (i == currentChoice) {
				g.setColor(Color.BLACK);
			} else {
				g.setColor(Color.RED);
			}
			g.drawString(options[i], 145, 140 + 15 * i);
		}
	}

	public void keyPressed(int k) {
		if (k == KeyEvent.VK_ENTER) {
			menuSelectSound.play();
			select();
		} else if (k == KeyEvent.VK_UP) {
			menuOptionSound.play();
			currentChoice--;
			if (currentChoice < 0)
				currentChoice = options.length - 1;
		} else if (k == KeyEvent.VK_DOWN) {
			menuOptionSound.play();
			currentChoice++;
			if (currentChoice == options.length)
				currentChoice = 0;
		}
	}

	private void select() {
		if (currentChoice == 0) {
			gsm.setState(GameStateManager.LEVEL1STATE);
		} else if (currentChoice == 1) {
			// TODO help
			System.out.println("help");
		} else if (currentChoice == 2) {
			System.exit(0);
		}
	}

	public void keyReleased(int k) {

	}

}
