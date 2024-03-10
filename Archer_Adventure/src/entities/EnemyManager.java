package entities;

import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.PlayerConstants.*;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.Playing;
import levels.Level;
import main.Game;
import objects.Arrow;
import utilz.LoadSave;
import static utilz.Constants.ObjectConstants.*;

public class EnemyManager {

	private Playing playing; // Trạng thái của GameState
	private BufferedImage[][] night_boneArr;// hoạt ảnh quái
	private BufferedImage[][] skeletonArr;
	private ArrayList<Night_bone> night_bones = new ArrayList<>();// Mảng dữ liêu quái
	private ArrayList<Skeleton> skeletons = new ArrayList<>();

	public EnemyManager(Playing playing) {
		this.playing = playing;
		loadEnemyImgs();
	}

	public void loadEnemies(Level level) {
		night_bones = level.getNight_bones();
		skeletons = level.get_Skeletons();
	}// truyền quái vào mảng bằng phương thức đọc quái bằng blue bên helpmethod

	public void update(int[][] lvlData, Player player) {
		boolean isAnyActive = false;
		for (Night_bone c : night_bones) {
			if (c.isActive()) {
				c.update(lvlData, player);
				isAnyActive = true;
			} else if (!c.isDead) {
				c.isDead = true;
				playing.changeScore(NIGHTBONE_SCORE);
			}
		}

		for (Skeleton c : skeletons) {
			if (c.isActive()) {
				c.update(lvlData, player);
				isAnyActive = true;
			} else if (!c.isDead) {
				c.isDead = true;
				playing.changeScore(SKELETON_SCORE);
			}
		}
		if (!isAnyActive)
			playing.setLevelCompleted(true);
	}// Xem thử xong ải chưa nếu quái die hết

	public void draw(Graphics g, int xLvlOffset) {
		drawNightBone(g, xLvlOffset);
		drawSkeleton(g, xLvlOffset);
	}// vẽ hết toàn bộ quái

	private void drawNightBone(Graphics g, int xLvlOffset) {
		for (Night_bone c : night_bones)
			if (c.isActive()) {
				g.drawImage(night_boneArr[c.getEnemyState()][c.getAniIndex()],
						(int) c.getHitbox().x - xLvlOffset - NIGHTBONE_DRAWOFFSET_X + c.flipX() ,
						(int) c.getHitbox().y - NIGHTBONE_DRAWOFFSET_Y, NIGHTBONE_WIDTH * c.flipW(), NIGHTBONE_HEIGHT,
						null);
//				c.drawHitbox(g, xLvlOffset);
//				c.drawAttackBox(g, xLvlOffset);

			}
	}

	private void drawSkeleton(Graphics g, int xLvlOffset) {
		for (Skeleton c : skeletons)
			if (c.isActive()) {
				g.drawImage(skeletonArr[c.getEnemyState()][c.getAniIndex()],
						(int) c.getHitbox().x - xLvlOffset - SKELETON_DRAWOFFSET_X + c.flipX(),
						(int) c.getHitbox().y - SKELETON_DRAWOFFSET_Y / 2 , SKELETON_WIDTH * c.flipW(),(int) (SKELETON_HEIGHT - 32 * Game.SCALE) ,
						null);
//				c.drawHitbox(g, xLvlOffset);
//				c.drawAttackBox(g, xLvlOffset);
			}
	}

	public boolean checkPlayerHit(Rectangle2D.Float attackBox) {
		for (Night_bone c : night_bones)
			if (c.isActive())
				if (attackBox.intersects(c.getHitbox())) {
					c.hurt(PLAYER_DMG);
					return true;
				}

		for (Skeleton c : skeletons)
			if (c.isActive())
				if (attackBox.intersects(c.getHitbox())) {
					c.hurt(PLAYER_DMG);
					return true;
				}
		return false;
	}// Kiểm tra kẻ địch bị đánh

	public boolean checkArrowHit(Rectangle2D.Float hitbox) {
		for (Night_bone c : night_bones)
			if (c.isActive())
				if (hitbox.intersects(c.getHitbox())) {
					c.hurt(PLAYER_DMG);
					return true;
				}

		for (Skeleton c : skeletons)
			if (c.isActive())
				if (hitbox.intersects(c.getHitbox())) {
					c.hurt(PLAYER_DMG);
					return true;
				}
		return false;
	}

	public boolean checkObjectHit(Rectangle2D.Float hitbox) {
		for (Night_bone c : night_bones)
			if (c.isActive())
				if (hitbox.intersects(c.getHitbox())) {
					c.hurt(BUBBLE_DMG_VALUE);
					return true;
				}
		return false;
	}

	private void loadEnemyImgs() {

		night_boneArr = new BufferedImage[5][23];
		BufferedImage tem1 = LoadSave.GetSpriteAtlas(LoadSave.NIGHTBONE_ATLAS);
		for (int j = 0; j < night_boneArr.length; j++)
			for (int i = 0; i < night_boneArr[j].length; i++) {
				night_boneArr[j][i] = tem1.getSubimage(i * NIGHTBONE_WIDTH_DEFAULT, j * NIGHTBONE_HEIGHT_DEFAULT, NIGHTBONE_WIDTH_DEFAULT,
						NIGHTBONE_HEIGHT_DEFAULT);
			}

		skeletonArr = new BufferedImage[5][8];
		BufferedImage tem2 = LoadSave.GetSpriteAtlas(LoadSave.SKELETON_ATLAS);
		for (int j = 0; j < skeletonArr.length; j++)
			for (int i = 0; i < skeletonArr[j].length; i++) {
				skeletonArr[j][i] = tem2.getSubimage(i * SKELETON_WIDTH_DEFAULT, j * SKELETON_HEIGHT_DEFAULT, SKELETON_WIDTH_DEFAULT, SKELETON_HEIGHT_DEFAULT);
			}
	}// Tải hoạt ảnh

	public void resetAllEnemies() {
		for (Night_bone c : night_bones) {
			c.resetEnemy();
			c.isDead = false;
		}
		
		for (Skeleton c : skeletons)
			c.resetEnemy();
	}
	

}
