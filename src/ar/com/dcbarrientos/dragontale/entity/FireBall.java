package ar.com.dcbarrientos.dragontale.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import ar.com.dcbarrientos.dragontale.tilemap.TileMap;

public class FireBall extends MapObject {
	private boolean hit;
	private boolean remove;
	private BufferedImage[] sprites;
	private int nSprites = 4;
	private BufferedImage[] hitSprites;
	private int nHitSprites = 3;

	public FireBall(TileMap tileMap, boolean right) {
		super(tileMap);

		facingRight = right;
		moveSpeed = 3.8;
		if (right)
			dx = moveSpeed;
		else
			dx = -moveSpeed;

		width = 30;
		height = 30;
		cWidth = 14;
		cHeight = 14;

		// load sprites
		try {
			BufferedImage spriteSheet = ImageIO.read(getClass().getResource("/Sprites/Player/fireball.gif"));

			sprites = new BufferedImage[nSprites];
			for (int i = 0; i < nSprites; i++)
				sprites[i] = spriteSheet.getSubimage(i * width, 0, width, height);

			hitSprites = new BufferedImage[nHitSprites];
			for (int i = 0; i < nHitSprites; i++)
				hitSprites[i] = spriteSheet.getSubimage(i * width, height, width, height);

			animation = new Animation();
			animation.setFrame(sprites);
			animation.setDelay(70);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setHit() {
		if (hit)
			return;

			hit = true;
		animation.setFrame(hitSprites);
		animation.setDelay(70);

		dx = 0;
	}
	
	public boolean shouldRemove() {
		return remove;
	}
	
	public void update() {
		checkTileMapCollision();
		setPosition(xTemp, yTemp);
		
		if(dx == 0 && !hit) {
			setHit();
		}
		
		animation.update();
		if(hit && animation.hasPlayedOnce()) {
			remove = true;
		}
	}
	
	public void draw(Graphics2D g) {
		setMapPosition();
		
		super.draw(g);
	}
}
