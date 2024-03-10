package entities;

import static utilz.Constants.Directions.RIGHT;
import static utilz.Constants.EnemyConstants.*;



import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import main.Game;

public class Skeleton extends Enemy {

	private Rectangle2D.Float attackBox;
	private int attackBoxOffsetX;
	public boolean isDead = false;

	public Skeleton(float x, float y) {
		super(x, y, SKELETON_WIDTH, SKELETON_HEIGHT, SKELETON);
		initHitbox(x, y , (int) (28 * Game.SCALE), (int) (30 * Game.SCALE));
		initAttackBox();
	}
 
	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y , (int) (128 * Game.SCALE), (int) (30 * Game.SCALE));
		attackBoxOffsetX = (int) (Game.SCALE * 50);
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
				if (aniIndex == 5 && !attackChecked) 
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
