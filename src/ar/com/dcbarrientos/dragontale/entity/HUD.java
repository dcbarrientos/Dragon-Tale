package ar.com.dcbarrientos.dragontale.entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class HUD {
	private int x = 0;
	private int y = 10;

	private Player player;
	private BufferedImage image;
	private Font font;

	public HUD(Player player) {
		this.player = player;
		try {
			image = ImageIO.read(getClass().getResource("/HUD/hud.gif"));
			font = new Font("Arial", Font.PLAIN, 14);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void draw(Graphics2D g) {
		g.drawImage(image, x, y, null);
		
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString(player.getHealth() + "/" + player.getMaxHealth(), x + 30, y + 15);
		g.drawString(player.getFire() / 100 + "/" + player.getMaxFire() / 100, x + 30, y + 35);

	}
}
