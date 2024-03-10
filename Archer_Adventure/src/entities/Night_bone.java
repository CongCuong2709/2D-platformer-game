package entities;

import static utilz.Constants.Directions.RIGHT;
import static utilz.Constants.EnemyConstants.*;



import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import main.Game;

public class Night_bone extends Enemy {

	private Rectangle2D.Float attackBox;
	private int attackBoxOffsetX;
	public boolean isDead = false;

	public Night_bone(float x, float y) {
		super(x, y, NIGHTBONE_WIDTH, NIGHTBONE_HEIGHT, NIGHTBONE);
		initHitbox(x, y, (int) (25 * Game.SCALE), (int) (28 * Game.SCALE));
		initAttackBox();
	}
 
	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y , (int) (64 * Game.SCALE), (int) (28 * Game.SCALE));
		attackBoxOffsetX = (int) (Game.SCALE * 22);
	} 

	public void update(int[][] lvlData, Player player) {
		updateBehavior(lvlData, player);
		updateAnimationTick();
		updateAttackBox();
	}

	private void updateAttackBox() {
		attackBox.x = hitbox.x - attackBoxOffsetX;
		attackBox.y = hitbox.y;
	}

	private void updateBehavior(int[][] lvlData, Player player) {
		if (firstUpdate)
			firstUpdateCheck(lvlData);

		if (inAir)
			updateInAir(lvlData);
		else {
			switch (enemyState) {
			case IDLE:
				newState(RUNNING);
				break;
			case RUNNING:
				if(isPlayerInRange(player)) {
					turnTowardsPlayer(player);
					if (canSeePlayer(lvlData, player)) {
						if (isPlayerCloseForAttack(player)) {
							newState(ATTACK);
						}			
					}moveAttack(lvlData);
					} 
				else move(lvlData);
				
				break;
			case ATTACK:
				if (aniIndex == 0)
					attackChecked = false;
				if (aniIndex == 6 && !attackChecked) 
					checkPlayerHit(attackBox, player);
				break;
			case HIT:
				break;
			}
		}
	}

	public void drawAttackBox(Graphics g, int xLvlOffset) {
		g.setColor(Color.red);
		g.drawRect((int) (attackBox.x - xLvlOffset), (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
	}

	public int flipX() {
		if (walkDir == RIGHT)
			return width;
		else
			return 0;
	}

	public int flipW() {
		if (walkDir == RIGHT)
			return -1;
		else
			return 1;
	}
}
