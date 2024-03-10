package objects;

import java.awt.geom.Rectangle2D;

import main.Game;

import static utilz.Constants.ObjectConstants.*;

public class Arrow {

	public Rectangle2D.Float hitbox;
	private int dir;
	private boolean active = true;
	
	
	public Arrow(int x, int y, int dir) {
		int xOffset = (int) (0 * Game.SCALE);
		int yOffset = (int) (20 * Game.SCALE);
		
		if(dir == 1)
			xOffset = (int) (29 * Game.SCALE);
			
			
		hitbox = new Rectangle2D.Float(x + xOffset, y + yOffset, ARROW_WIDTH, ARROW_HEIGHT);
		this.dir = dir;
	}
	
	public void updatePos() {
		hitbox.x += dir * ARROW_SPEED;
	}
	
	public void setPos(int x, int y) {
		hitbox.x = x;
		hitbox.y = y;
	}

	public Rectangle2D.Float getHitbox() {
		return hitbox;
	}

	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}

	public int getDir() {
		return dir;
	}

	public void setDir(int dir) {
		this.dir = dir;
	}
	
}
