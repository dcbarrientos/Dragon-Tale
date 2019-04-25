package ar.com.dcbarrientos.dragontale.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import ar.com.dcbarrientos.dragontale.audio.AudioPlayer;
import ar.com.dcbarrientos.dragontale.tilemap.TileMap;

public class Player extends MapObject {
	private int health;
	private int maxHealth;
	private int fire;
	private int maxFire;
//	private boolean dead;
	private boolean flinching;
	private long flinchTimer;

	private boolean firing;
	private int fireCost;
	private int fireBallDamage;
	private ArrayList<FireBall> fireBalls;

	// scratch
	private boolean scratching;
	private int scratchDamage;
	private int scratchRange;

	// gliding
	private boolean gliding;

	// Animation
	private ArrayList<BufferedImage[]> sprites;
	// Nro de elementos en cada fila de la spritesheet del jugador.
	private final int[] numFrames = { 2, 8, 1, 2, 4, 2, 5 };

	// animation actions.
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int GLIDING = 4;
	private static final int FIREBALL = 5;
	private static final int SCRATCHING = 6;

	private HashMap<String, AudioPlayer> sfx;

	public Player(TileMap tileMap) {
		super(tileMap);
		width = 30;
		height = 30;
		cWidth = 20;
		cHeight = 20;

		moveSpeed = 0.3;
		maxSpeed = 1.6;
		stopSpeed = 0.4;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;

		facingRight = true;

		health = maxHealth = 5;
		fire = maxFire = 2500;

		fireCost = 200;
		fireBallDamage = 5;
		fireBalls = new ArrayList<FireBall>();

		scratchDamage = 8;
		scratchRange = 40;

		sprites = new ArrayList<BufferedImage[]>();

		try {
			BufferedImage spriteSheet = ImageIO.read(getClass().getResource("/Sprites/Player/playersprites.gif"));

			for (int i = 0; i < 7; i++) {
				BufferedImage[] bi = new BufferedImage[numFrames[i]];
				for (int j = 0; j < numFrames[i]; j++) {
					if (i != 6) {
						bi[j] = spriteSheet.getSubimage(j * width, i * height, width, height);
					} else {
						bi[j] = spriteSheet.getSubimage(j * width * 2, i * height, width * 2, height);

					}
				}
				sprites.add(bi);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		animation = new Animation();
		setAction(IDLE, 30, 400);

		sfx = new HashMap<String, AudioPlayer>();
		sfx.put("jump", new AudioPlayer("/SFX/jump.mp3"));
		sfx.put("scratch", new AudioPlayer("/SFX/scratch.mp3"));
	}

	public int getHealth() {
		return health;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public int getFire() {
		return fire;
	}

	public int getMaxFire() {
		return maxFire;
	}

	public void setFiring() {
		this.firing = true;
	}

	public void setScratching() {
		this.scratching = true;
	}

	public void setGliding(boolean gliding) {
		this.gliding = gliding;
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
		} else {
			if (dx > 0) {
				dx -= stopSpeed;
				if (dx < 0)
					dx = 0;
			} else if (dx < 0) {
				dx += stopSpeed;
				if (dx > 0)
					dx = 0;
			}
		}

		// cannot move while attacking, except in air
		if ((currentAction == SCRATCHING || currentAction == FIREBALL) && !(jumping || falling)) {
			dx = 0;
		}

		// jumping
		if (jumping && !falling) {
			sfx.get("jump").play();
			dy = jumpStart;
			falling = true;
		}

		// falling
		if (falling) {
			if (dy > 0 && gliding)
				dy += fallSpeed * 0.1;
			else 
				dy += fallSpeed;
			
			if (dy > 0)
				jumping = false;
			
			if (dy < 0 && !jumping)
				dy += stopJumpSpeed;

			if (dy > maxFallSpeed)
				dy = maxFallSpeed;
		}
	}

	public void update() {
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xTemp, yTemp);

		// Check attack has stopped
		if (currentAction == SCRATCHING) {
			if (animation.hasPlayedOnce())
				scratching = false;

		}
		if (currentAction == FIREBALL) {
			if (animation.hasPlayedOnce())
				firing = false;
		}

		// Fireball attack
		fire++;
		if (fire > maxFire)
			fire = maxFire;
		if (firing && currentAction != FIREBALL) {
			if (fire > fireCost) {
				fire -= fireCost;
				FireBall fb = new FireBall(tileMap, facingRight);
				fb.setPosition(x, y);
				fireBalls.add(fb);
			}
		}

		for (int i = 0; i < fireBalls.size(); i++) {
			fireBalls.get(i).update();
			if (fireBalls.get(i).shouldRemove()) {
				fireBalls.remove(i);
				i--;
			}
		}

		// check for flinching
		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed > 1000) {
				flinching = false;
			}
		}

		// Set animation
		if (scratching) {
			if (currentAction != SCRATCHING) {
				sfx.get("scratch").play();

				setAction(SCRATCHING, 60, 50);
			}
		} else if (firing) {
			if (currentAction != FIREBALL) {
				setAction(FIREBALL, 30, 100);
			}
		} else if (dy > 0) {
			if (gliding) {
				if (currentAction != GLIDING) {
					setAction(GLIDING, 30, 100);
				}
			} else if (currentAction != FALLING) {
				setAction(FALLING, 30, 100);
			}
		} else if (dy < 0) {
			if (currentAction != JUMPING) {
				setAction(JUMPING, 30, -1);
			}
		} else if (left || right) {
			if (currentAction != WALKING) {
				setAction(WALKING, 30, 40);
			}
		} else {
			if (currentAction != IDLE) {
				setAction(IDLE, 30, 400);
			}
		}
		animation.update();

		// set direction
		if (currentAction != SCRATCHING && currentAction != FIREBALL) {
			if (right)
				facingRight = true;
			if (left)
				facingRight = false;
		}
		
		if (notOnScreen() ) {
			health = 0;
		}
	}

	public void draw(Graphics2D g) {
		setMapPosition();

		// draw fireball
		for (int i = 0; i < fireBalls.size(); i++) {
			fireBalls.get(i).draw(g);
		}

		// draw player
		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed / 100 % 2 == 0) {
				return;
			}
		}

		super.draw(g);
	}

	private void setAction(int action, int width, long delay) {
		currentAction = action;
		animation.setFrame(sprites.get(action));
		animation.setDelay(delay);
		this.width = width;
	}

	public void checkAttack(ArrayList<Enemy> enemies) {
		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);

			if (scratching) {
				if (facingRight) {
					if (e.getX() > x && e.getX() < x + scratchRange && e.getY() > y - height / 2
							&& e.getY() < y + height / 2) {
						e.hit(scratchDamage);
					}
				} else {
					if (e.getX() < x && e.getX() > x - scratchRange && e.getY() > y - height / 2
							&& e.getY() < y + height / 2) {
						e.hit(scratchDamage);
					}
				}
			}

			for (int j = 0; j < fireBalls.size(); j++) {
				if (fireBalls.get(j).intersects(e)) {
					e.hit(fireBallDamage);
					fireBalls.get(j).setHit();
					break;
				}
			}

			// check for enemy collision
			if (intersects(e)) {
				hit(e.getDamage());
			}
		}

	}

	public void hit(int damage) {
		if (flinching)
			return;
		health -= damage;

		if (health < 0)
			health = 0;
//		if (health == 0)
//			dead = true;
		flinching = true;
		flinchTimer = System.nanoTime();
	}
}
