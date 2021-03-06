package ar.com.dcbarrientos.dragontale.gamestate;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import ar.com.dcbarrientos.dragontale.audio.AudioPlayer;
import ar.com.dcbarrientos.dragontale.entity.Enemy;
import ar.com.dcbarrientos.dragontale.entity.Explosion;
import ar.com.dcbarrientos.dragontale.entity.HUD;
import ar.com.dcbarrientos.dragontale.entity.Player;
import ar.com.dcbarrientos.dragontale.entity.enemies.Slugger;
import ar.com.dcbarrientos.dragontale.main.GamePanel;
import ar.com.dcbarrientos.dragontale.tilemap.Background;
import ar.com.dcbarrientos.dragontale.tilemap.TileMap;

public class Level1State extends GameState {
	private TileMap tileMap;
	private Background bg;

	private Player player;
	private ArrayList<Enemy> enemies;
	private ArrayList<Explosion> explosions;

	private HUD hud;
	
	private AudioPlayer bgMusic;
	private AudioPlayer gameOver;

	public Level1State(GameStateManager gsm) {
		this.gsm = gsm;
		init();
	}

	@Override
	public void init() {
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/grasstileset.gif");
		tileMap.loadMap("/Maps/level1-1.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);

		bg = new Background("/Backgrounds/grassbg1.gif", 0.1);

		player = new Player(tileMap);
		player.setPosition(100, 100);

		populateEnemies();

		explosions = new ArrayList<Explosion>();

		hud = new HUD(player);
		bgMusic = new AudioPlayer("/Music/level1-1.mp3");
		bgMusic.loops();
		
		gameOver = new AudioPlayer("/SFX/gameover.mp3");
	}

	private void populateEnemies() {
		enemies = new ArrayList<Enemy>();

		Slugger s;

		Point[] points = new Point[] { new Point(200, 200), new Point(860, 200), new Point(1525, 200), new Point(1680, 200),
				new Point(1800, 200) };

		for (int i = 0; i < points.length; i++) {
			s = new Slugger(tileMap);
			s.setPosition(points[i]);
			enemies.add(s);
		}

	}

	public void update() {
		player.update();
		tileMap.setPosition(GamePanel.WIDTH / 2 - player.getX(), GamePanel.HEIGHT / 2 - player.getY());

		// update background
		bg.setPosition(tileMap.getX(), tileMap.getY());

		// attack enemies
		player.checkAttack(enemies);

		// Update all enemies
		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.update();
			if (e.isDead()) {
				enemies.remove(i);
				i--;
				explosions.add(new Explosion((int) e.getX(), (int) e.getY()));
			}
		}

		for (int i = 0; i < explosions.size(); i++) {
			explosions.get(i).update();
			if (explosions.get(i).shouldRemove()) {
				explosions.remove(i);
				i--;
			}
		}
		
		
		if(player.getHealth() == 0) {
			bgMusic.stop();
			gameOver.play();
			gsm.setState(GameStateManager.GAMEOVER);
		}
	}

	@Override
	public void draw(Graphics2D g) {
		// Dibujo el fondo
		bg.draw(g);

		// Dibujo el tileMap
		tileMap.draw(g);

		player.draw(g);

		// draw all enemies
		for (int i = 0; i < enemies.size(); i++)
			enemies.get(i).draw(g);

		for (int i = 0; i < explosions.size(); i++) {
			explosions.get(i).setMapPosition((int)tileMap.getX(), (int)tileMap.getY());
			explosions.get(i).draw(g);
		}
		
		hud.draw(g);
		
		g.drawString("X: " + (int)player.getX() + "Y: " + (int)player.getY(), 200, 10);
	}

	@Override
	public void keyPressed(int k) {
		if (k == KeyEvent.VK_LEFT)
			player.setLeft(true);
		if (k == KeyEvent.VK_RIGHT)
			player.setRight(true);
		if (k == KeyEvent.VK_UP)
			player.setUp(true);
		if (k == KeyEvent.VK_DOWN)
			player.setDown(true);
		if (k == KeyEvent.VK_W)
			player.setJumping(true);
		if (k == KeyEvent.VK_E)
			player.setGliding(true);
		if (k == KeyEvent.VK_R)
			player.setScratching();
		if (k == KeyEvent.VK_F)
			player.setFiring();

	}

	@Override
	public void keyReleased(int k) {
		if (k == KeyEvent.VK_LEFT)
			player.setLeft(false);
		if (k == KeyEvent.VK_RIGHT)
			player.setRight(false);
		if (k == KeyEvent.VK_UP)
			player.setUp(false);
		if (k == KeyEvent.VK_DOWN)
			player.setDown(false);
		if (k == KeyEvent.VK_W)
			player.setJumping(false);
		if (k == KeyEvent.VK_E)
			player.setGliding(false);
	}

}
