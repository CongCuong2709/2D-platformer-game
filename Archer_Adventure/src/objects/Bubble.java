package objects;

import static utilz.Constants.ObjectConstants.BUBBLE_SPEED;
import static main.Game.*;

import main.Game;

public class Bubble extends GameObject {
	
	private int dir;
	private int s = 5 * TILES_SIZE;

	public Bubble(int x, int y, int objType) {
		super(x, y, objType);
		doAnimation = false;
		initHitbox(40, 40);
		xDrawOffset = (int) (3 *Game.SCALE);
		yDrawOffset = (int) (3 *Game.SCALE);
	}
	
	public void update() {
        if(doAnimation == true) 		
        	updateAnimationTick();
		updatePos();
	}
	
	public void updatePos() {
		hitbox.x += dir * BUBBLE_SPEED;
	}
	public void setActive(boolean dir) {
		this.active = dir;
	}
	
	
	
}
