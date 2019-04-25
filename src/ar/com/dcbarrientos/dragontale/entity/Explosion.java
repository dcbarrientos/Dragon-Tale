package ar.com.dcbarrientos.dragontale.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import ar.com.dcbarrientos.dragontale.main.Game;

public class Explosion{
	private int x;
	private int y;
	private int xmap;
	private int ymap;

	private int height;
	private int width;

	private Animation animation;
	private int nSprites = 6;
	private BufferedImage[] sprites;
	private int animationDelay = 70;

	private boolean remove;

	public Explosion(int x, int y) {
		this.x = x;
		this.y = y;

		width = 30;
		height = 30;

		try {
			BufferedImage spriteSheet = ImageIO.read(getClass().getResource("/Sprites/Enemies/explosion.gif"));
			sprites = new BufferedImage[nSprites];
			for (int i = 0; i < sprites.length; i++) {
				sprites[i] = spriteSheet.getSubimage(i * width, 0, width, height);
			}
		} catch (Exception e) {
			if (Game.DEBUG)
				e.printStackTrace();
		}

		animation = new Animation();
		animation.setFrame(sprites);
		animation.setDelay(animationDelay);
	}

	public void update() {
		animation.update();
		if (animation.hasPlayedOnce())
			remove = true;
	}

	public boolean shouldRemove() {
		return remove;
	}

	public void setMapPosition(int x, int y) {
		xmap = x;
		ymap = y;
	}
	
	public void draw(Graphics2D g) {
		g.drawImage(animation.getImage(), x + xmap - width / 2, y + ymap - height / 2, null);
	}
}
