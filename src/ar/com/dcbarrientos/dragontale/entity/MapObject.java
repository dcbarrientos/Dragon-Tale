package ar.com.dcbarrientos.dragontale.entity;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import ar.com.dcbarrientos.dragontale.main.GamePanel;
import ar.com.dcbarrientos.dragontale.tilemap.Tile;
import ar.com.dcbarrientos.dragontale.tilemap.TileMap;

public abstract class MapObject {
	protected TileMap tileMap;
	protected int tileSize;
	protected double xMap;
	protected double yMap;

	// posicion y vector
	protected double x;
	protected double y;
	protected double dx;
	protected double dy;

	// dimensiones
	protected int width;
	protected int height;

	// collision box
	protected int cWidth;
	protected int cHeight;

	// collision
	protected int currRow;
	protected int currCol;
	protected double xDest;
	protected double yDest;
	protected double xTemp;
	protected double yTemp;
	protected boolean topLeft;
	protected boolean topRight;
	protected boolean bottomLeft;
	protected boolean bottomRight;

	// animation
	protected Animation animation;
	protected int currentAction;
	protected int previousAction;
	protected boolean facingRight;

	// movement
	protected boolean left;
	protected boolean right;
	protected boolean up;
	protected boolean down;
	protected boolean jumping;
	protected boolean falling;

	// movement attributes
	protected double moveSpeed;
	protected double maxSpeed;
	protected double stopSpeed;
	protected double fallSpeed;
	protected double maxFallSpeed;
	protected double jumpStart;
	protected double stopJumpSpeed;

	public MapObject(TileMap tileMap) {
		this.tileMap = tileMap;
		tileSize = tileMap.getTileSize();
	}

	public boolean intersects(MapObject o) {
		Rectangle r1 = getRectangle();
		Rectangle r2 = o.getRectangle();
		return r1.intersects(r2);
	}

	public Rectangle getRectangle() {
		return new Rectangle((int) x - cWidth, (int) y - cHeight, (int) cWidth, (int) cHeight);
	}

	public void checkTileMapCollision() {

		currCol = (int) x / tileSize;
		currRow = (int) y / tileSize;

		xDest = x + dx;
		yDest = y + dy;

		xTemp = x;
		yTemp = y;

		calculateCorners(x, yDest);

		// Se mueve arriba
		if (dy < 0) {
			if (topLeft || topRight) {
				dy = 0;
				yTemp = currRow * tileSize + cHeight / 2;
			} else {
				yTemp += dy;
			}
		}

		// Se mueve abajo
		if (dy > 0) {
			if (bottomLeft || bottomRight) {
				dy = 0;
				falling = false;
				yTemp = (currRow + 1) * tileSize - cHeight / 2;
			} else {
				yTemp += dy;
			}
		}

		calculateCorners(xDest, y);
		// Se mueve a la izquierda
		if (dx < 0) {
			if (topLeft || bottomLeft) {
				dx = 0;
				xTemp = currCol * tileSize + cWidth / 2;
			} else {
				xTemp += dx;
			}
		}

		// Se mueve a la derecha
		if (dx > 0) {
			if (topRight || bottomRight) {
				dx = 0;
				xTemp = (currCol + 1) * tileSize - cWidth / 2;
			} else {
				xTemp += dx;
			}
		}

		// Si no estoy cayendo verifico si hay piso.
		// Si hay y no estoy cayendo no hago nada, si
		// no hay comienzo a caer.
		if (!falling) {
			calculateCorners(x, yDest + 1);
			if (!bottomLeft && !bottomRight) {
				falling = true;
			}
		}
	}

	private void calculateCorners(double x, double y) {

		int leftTile = (int) (x - cWidth / 2) / tileSize;
		int rightTile = (int) (x + cWidth / 2 - 1) / tileSize;
		int topTile = (int) (y - cHeight / 2) / tileSize;
		int bottomTile = (int) (y + cHeight / 2 - 1) / tileSize;

		if (topTile < 0 || bottomTile >= tileMap.getNumRows() || leftTile < 0 || rightTile >= tileMap.getNumCols()) {
			topLeft = topRight = bottomLeft = bottomRight = false;
			return;
		}

		int bl = tileMap.getType(bottomTile, leftTile);
		int br = tileMap.getType(bottomTile, rightTile);
		int tl = tileMap.getType(topTile, leftTile);
		int tr = tileMap.getType(topTile, rightTile);

		topLeft = tl == Tile.BLOCKED;
		topRight = tr == Tile.BLOCKED;
		bottomLeft = bl == Tile.BLOCKED;
		bottomRight = br == Tile.BLOCKED;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getCWidth() {
		return cWidth;
	}

	public int getCHeight() {
		return cHeight;
	}

	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void setPosition(Point point) {
		x = point.x;
		y = point.y;
	}

	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = y;
	}

	public void setMapPosition() {
		xMap = tileMap.getX();
		yMap = tileMap.getY();
	}

	public void setLeft(boolean b) {
		left = b;
	}

	public void setRight(boolean b) {
		right = b;
	}

	public void setUp(boolean b) {
		up = b;
	}

	public void setDown(boolean b) {
		down = b;
	}

	public void setJumping(boolean b) {
		jumping = b;
	}

	public boolean notOnScreen() {
		return (x + xMap + width < 0 || x + xMap - width > GamePanel.WIDTH || y + yMap + height < 0
				|| y + yMap - height > GamePanel.HEIGHT);
	}

	public void draw(Graphics2D g) {
		if (facingRight) {
			g.drawImage(animation.getImage(), (int) (x + xMap - width / 2), (int) (y + yMap - height / 2), null);
		} else {
			g.drawImage(animation.getImage(), (int) (x + xMap - width / 2 + width), (int) (y + yMap - height / 2),
					-width, height, null);
		}
	}
}
