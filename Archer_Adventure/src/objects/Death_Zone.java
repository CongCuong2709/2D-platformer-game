package objects;

import main.Game;

public class Death_Zone extends GameObject {

	public Death_Zone(int x, int y, int objType) {
		super(x, y, objType);
		initHitbox(32, 32);
		xDrawOffset = (int)(1 * Game.SCALE);
		yDrawOffset = (int)(25 * Game.SCALE);
	}
	public void update() {
		
	}

}
