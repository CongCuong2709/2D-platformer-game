package entities;

import static main.Game.TILES_SIZE;
import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;


public class Player extends Entity {
	private BufferedImage[][] animations; //mảng các hoạt ảnh
	private int aniTick, aniIndex, aniSpeed = 15, playerAtk = 10;
	private int playerAction = IDLE; 	//Hành động của player : jump, attack...
	private boolean moving = false, attacking = false, isFacingLeft = true, kicking = false, 
	freefire = false, hitting = false;
	private boolean left, up, right, down, jump;		// Xem trạng thái
	private float playerSpeed = 1f * Game.SCALE;		//Tốc độ di chuyển
	private int[][] lvlData;							
	private float xDrawOffset = 21 * Game.SCALE;
	private float yDrawOffset = 8 * Game.SCALE;
 
	// Jumping / Gravity
	private float airSpeed = 0f;// tốc độ rơi
	private float gravity = 0.04f * Game.SCALE; // Gia tốc trọng trường 
	private float jumpSpeed = -2.25f * Game.SCALE;// tốc độ nhảy
	private float fallSpeedAfterCollision = 0.5f * Game.SCALE;// Tốc độ rơi sau khi va chạm ???????
	private boolean inAir = false;// Kiểm tra có đang ở trên không hay không

	// StatusBarUI
	private BufferedImage statusBarImg;

	private int statusBarWidth = (int) (192 * Game.SCALE);
	private int statusBarHeight = (int) (58 * Game.SCALE);
	private int statusBarX = (int) (10 * Game.SCALE);
	private int statusBarY = (int) (10 * Game.SCALE);

	private int healthBarWidth = (int) (150 * Game.SCALE);
	private int healthBarHeight = (int) (4 * Game.SCALE);
	private int healthBarXStart = (int) (34 * Game.SCALE);
	private int healthBarYStart = (int) (14 * Game.SCALE);

	
	private int currentHealth = maxHealth;
	private int healthWidth = healthBarWidth;
	

	// AttackBox
	private Rectangle2D.Float attackBox;


	private boolean attackChecked;
	private Playing playing;

	public Player(float x, float y, int width, int height, Playing playing) {
		super(x, y, width, height);
		this.playing = playing;
		loadAnimations();
		initHitbox(x, y, (int) (20 * Game.SCALE), (int) (32 * Game.SCALE));
		attackBox = new Rectangle2D.Float(x  , y   , (int) (35 * Game.SCALE), (int) (32 * Game.SCALE));
//		initAttackBox();
	}

	public void setSpawn(Point spawn) {
		this.x = spawn.x;
		this.y = spawn.y;
		hitbox.x = x;
		hitbox.y = y;
	}

	public void update() {
		updateHealthBar();

		if (currentHealth <= 0) {
			playing.setGameOver(true);
			playing.getGame().getAudioPlayer().playEffect(AudioPlayer.DIE);
			return;
		}

		updatePos();
		if(moving)
			checkPotionTouched();
		if (attacking)
			checkAttack();
		updateAttackBox();
		updateAnimationTick();
		setAnimation();
		
	}

	private void checkPotionTouched() {
		playing.checkPotionTouched(hitbox);
		
	}

	private void checkAttack() {
		if (attackChecked || aniIndex != 1)
			return;
		attackChecked = true;
		playing.checkEnemyHit(attackBox);
		playing.checkObjectHit(attackBox);
	
	}

	private void updateAttackBox() {
		if (right)
			attackBox.x = hitbox.x + (int) (Game.SCALE + 10); //+ hitbox.width 
		else if (left)
			attackBox.x = hitbox.x - hitbox.width ; //- (int) (Game.SCALE - 50)

		attackBox.y = hitbox.y ;//+ (Game.SCALE * 10 )
	}

	private void updateHealthBar() {
		healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBarWidth);
	}

	public void render(Graphics g, int lvlOffset) {
		if(isFacingLeft == true)
			g.drawImage(flipImage(animations[playerAction][aniIndex]), (int) (hitbox.x - xDrawOffset) - lvlOffset, (int) (hitbox.y - yDrawOffset), width, height, null);
		else 
			g.drawImage(animations[playerAction][aniIndex], (int) (hitbox.x - xDrawOffset) - lvlOffset, (int) (hitbox.y - yDrawOffset), width, height, null);


		drawUI(g);
	}
	 public BufferedImage flipImage(BufferedImage image) {
		    // Flip the image horizontally to make the character face left
		    AffineTransform transform = new AffineTransform();
		    transform.translate(image.getWidth() / 2, image.getHeight() / 2);
		    transform.scale(-1, 1);
		    transform.translate(-image.getWidth() / 2, -image.getHeight() / 2);
		    AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
		    return op.filter(image, null);
		}

	private void drawUI(Graphics g) {
		g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
		g.setColor(Color.red);
		g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
	}

	private void updateAnimationTick() {	
		aniTick++;
		if (aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= GetSpriteAmount(playerAction)) {
				aniIndex = 0;
				attacking = false;
				notAttack();
				attackChecked = false;
				hitting = false;
			}
		}
	}

	private void setAnimation() {
		int startAni = playerAction;

		if (moving)
			playerAction = RUN;
		else
			playerAction = IDLE;

		if (inAir) {
			if (airSpeed < 0)
				playerAction = JUMP;
			else
				playerAction = FALLING;
		}
		if(left == true) 
			isFacingLeft = true;
		if(right == true)
			setFacingLeft();

		if (attacking && kicking) {//
			right = false; left = false; jump =false;
			playerAction = KICK;
		}
		if(freefire && !inAir) {
			right = false; left = false; 
			playerAction = ATTACK_1;
			}
		if(inAir&& freefire && !kicking) 
			playerAction = ATTACK_JUMP_1;	
		if(hitting) {
			playerAction = HIT;
			playing.getGame().getAudioPlayer().playEffect(HIT);
		}
		if(up == true) {
			right = false; left = false;
			playerAction = AIR_ATTACK;
		}
		if (startAni != playerAction)
			resetAniTick();
	}

	private void resetAniTick() {
		aniTick = 0;
		aniIndex = 0;
	}
	public void setFacingLeft() {
        isFacingLeft = false;
    }
	public boolean isFacingLeft() {
        return isFacingLeft;
    }

	private void updatePos() {
		moving = false;
		
		if(jump) 
			jump();
		
		if (!left && !right && !inAir)
			return;

		float xSpeed = 0;


		if (left )
			xSpeed -= playerSpeed;
		if (right )
			xSpeed += playerSpeed;
		
		if(!inAir) {
			if(!IsEntityOnFloor(hitbox,lvlData)) 
				inAir = true; 
		}

		if(inAir) {
			if(CanMoveHere(hitbox.x, hitbox.y + airSpeed , hitbox.width, hitbox.height, lvlData)) {
				hitbox.y += airSpeed;
				airSpeed += gravity;
				updateXPos(xSpeed);
			}else {
				hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox,airSpeed) + TILES_SIZE; //+48
				if(airSpeed > 0)
					resetInAir();
				else 
					airSpeed = fallSpeedAfterCollision;	
				updateXPos(xSpeed);
			}
			
		}else 
			updateXPos(xSpeed);
		moving = true;
	}
	
	
	public void notAttack() {
		kicking = false;
		freefire =false;
	}

	private void jump() {
		if (inAir)
			return;
		playing.getGame().getAudioPlayer().playEffect(AudioPlayer.JUMP);
		inAir = true;
		airSpeed = jumpSpeed;
	}

	private void resetInAir() {
		inAir = false;
		airSpeed = 0;
	}

	private void updateXPos(float xSpeed) {
		if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
			hitbox.x += xSpeed;
		}else {
			hitbox.x = GetEntityXPosNextToWall(hitbox,xSpeed)  ;
		}
	}

	public void changeHealth(int value) {
		currentHealth += value;
		if (currentHealth <= 0)
			currentHealth = 0;
		else if (currentHealth >= maxHealth)
			currentHealth = maxHealth;
		hitting = true;
	}
	
	public void takeDamage(int value) {
		currentHealth -= value;
		if (currentHealth <= 0)
			currentHealth = 0;
		else if (currentHealth >= maxHealth)
			currentHealth = maxHealth;
		hitting = true;
	}
	

	private void loadAnimations() {
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
		animations = new BufferedImage[11][8];
		for (int j = 0; j < animations.length; j++)
			for (int i = 0; i < animations[j].length; i++)
				animations[j][i] = img.getSubimage(i * 64 , j * 64, 64, 64);

		statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
	}//Set up cho mảng animation hoạt ảnh

	public void loadLvlData(int[][] lvlData) {
		this.lvlData = lvlData;
		if (!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
	}

	public void resetDirBooleans() {
		left = false;
		right = false;
		up = false;
		down = false;
	}// Reset các boolean định hướng dùng cho hàm: ????

	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isUp() {
		return up;
	}
 
	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public void setJump(boolean jump) {
		this.jump = jump;
	}
	

	public boolean isKicking() {
		return kicking;
	}

	public void setKicking(boolean kicking) {
		this.kicking = true;
	}

	public boolean isFreefire() {
		return freefire;
	}

	public void setFreefire(boolean freefire) {
		this.freefire = true;
	}

	public void resetAll() {
		resetDirBooleans();
		inAir = false;
		attacking = false;
		kicking =false;
		moving = false;
		playerAction = IDLE;
		currentHealth = maxHealth;

		hitbox.x = x;
		hitbox.y = y;

		if (!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
	}

}