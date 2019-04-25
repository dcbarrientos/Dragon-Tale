package ar.com.dcbarrientos.dragontale.entity.enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import ar.com.dcbarrientos.dragontale.entity.Animation;
import ar.com.dcbarrientos.dragontale.entity.Enemy;
import ar.com.dcbarrientos.dragontale.tilemap.TileMap;

public class Slugger extends Enemy {
	private BufferedImage[] sprites;

	public Slugger(TileMap tileMap) {
		super(tileMap);

		moveSpeed = 0.3;
		maxSpeed = 0.3;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;

		width = 30;
		height = 30;
		cWidth = 20;
		cHeight = 20;

		health = 2;
		maxHealth = 2;
		damage = 1;
		right = true;
		facingRight = true;
		dead = false;

		// load sprites
		sprites = new BufferedImage[3];
		try {
			BufferedImage spriteSheet = ImageIO.read(getClass().getResource("/Sprites/Enemies/slugger.gif"));
			for (int i = 0; i < sprites.length; i++)
				sprites[i] = spriteSheet.getSubimage(i * width, 0, width, height);
		} catch (Exception e) {
			e.printStackTrace();
		}

		animation = new Animation();
		animation.setFrame(sprites);
		animation.setDelay(300);

	}
	
	private void getNextPosition() {
		// movement
		if (left) {
			dx -= moveSpeed;
			if (dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		} else if (right) {
			dx += moveSpeed;
			if (dx > maxSpeed) {
				dx = maxSpeed;
			}
		}

		if (falling) {
			dy += fallSpeed;
		}
	}

	public void update() {
		//update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xTemp, yTemp);
		
		//check flinching
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 400) {
				flinching = false;
			}
		}
		
		//if hit a wall, go other direction
		if(right && dx == 0) {
			right = false;
			left = true;
			facingRight = false;
		}else if(left && dx == 0) {
			right = true;
			left = false;
			facingRight = true;
		}
		
		animation.update();
	}

	public void draw(Graphics2D g) {
//		if (notOnScreen())
//			return;

		setMapPosition();
		super.draw(g);
	}
	

}
