package ar.com.dcbarrientos.dragontale.main;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import ar.com.dcbarrientos.dragontale.gamestate.GameStateManager;

public class GamePanel extends JPanel implements Runnable, KeyListener {
	private static final long serialVersionUID = 1L;

	// Dimensions
	public static final int WIDTH = 320;
	public static final int HEIGHT = 240;
	public static final int SCALE = 2;

	// Threads
	private Thread thread;
	private boolean running;
	private int FPT = 60;
	private long targetTime = 1000 / FPT;

	// Image
	private BufferedImage image;
	private Graphics2D g;

	private GameStateManager gsm;

	public GamePanel() {
		super();
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setFocusable(true);
		requestFocus();
	}

	public void addNotify() {
		super.addNotify();
		if (thread == null) {
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}

	private void init() {
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_BGR);
		g = (Graphics2D) image.getGraphics();
		running = true;
		gsm = new GameStateManager();
	}

	public void run() {
		init();

		long start;
		long elapse;
		long wait;

		while (running) {
			start = System.nanoTime();
			update();
			draw();
			drawToScreen();

			elapse = System.nanoTime() - start;
			wait = targetTime - elapse / 1000000;
			if (Game.DEBUG) {
				if (wait < 0)
					System.out.println("Wait: " + wait);
			}
			try {
				Thread.sleep(wait);
			} catch (Exception e) {
				if (Game.DEBUG)
					e.printStackTrace();
			}
		}
	}

	private void drawToScreen() {
		Graphics2D g2 = (Graphics2D) getGraphics();
		g2.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, this);
		g2.dispose();
	}

	private void draw() {
		gsm.draw(g);
	}

	private void update() {
		gsm.update();
	}

	public void keyTyped(KeyEvent e) {

	}

	public void keyPressed(KeyEvent e) {
		gsm.keyPressed(e.getKeyCode());
	}

	public void keyReleased(KeyEvent e) {
		gsm.keyReleased(e.getKeyCode());
	}

}
