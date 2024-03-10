package utilz;

import main.Game;

public class Constants {

	public static final int ANI_SPEED = 25;

	public static class ObjectConstants {

		public static final int RED_POTION = 0;
		public static final int BLUE_POTION = 1;
		public static final int BARREL = 2;
		public static final int BOX = 3;
		public static final int COIN = 4;
		public static final int ARROW = 5;
		public static final int BUBBLE = 6;
		public static final int DEATH_ZONE = 7;

		public static final int RED_POTION_VALUE = 100;
		public static final int BLUE_POTION_VALUE = 10;
		public static final int COIN_VALUE = 10;

		public static final int CONTAINER_WIDTH_DEFAULT = 40;
		public static final int CONTAINER_HEIGHT_DEFAULT = 30;
		public static final int CONTAINER_WIDTH = (int) (Game.SCALE * CONTAINER_WIDTH_DEFAULT);
		public static final int CONTAINER_HEIGHT = (int) (Game.SCALE * CONTAINER_HEIGHT_DEFAULT);

		public static final int POTION_WIDTH_DEFAULT = 12;
		public static final int POTION_HEIGHT_DEFAULT = 16;
		public static final int POTION_WIDTH = (int) (Game.SCALE * POTION_WIDTH_DEFAULT);
		public static final int POTION_HEIGHT = (int) (Game.SCALE * POTION_HEIGHT_DEFAULT);

		public static final int COIN_WIDTH_DEFAULT = 20;
		public static final int COIN_HEIGHT_DEFAULT = 20;
		public static final int COIN_WIDTH = (int) (Game.SCALE * COIN_WIDTH_DEFAULT);
		public static final int COIN_HEIGHT = (int) (Game.SCALE * COIN_HEIGHT_DEFAULT);

		public static final float ARROW_SPEED = 2.5f;
		public static final int ARROW_WIDTH_DEFAULT = 40;
		public static final int ARROW_HEIGHT_DEFAULT = 40;
		public static final int ARROW_WIDTH = (int) (ARROW_WIDTH_DEFAULT * Game.SCALE);
		public static final int ARROW_HEIGHT = (int) (ARROW_HEIGHT_DEFAULT * Game.SCALE);

		public static final int BUBBLE_DMG_VALUE = 100;
		public static final float BUBBLE_SPEED = 1.5f;
		public static final int BUBBLE_WIDTH_DEFAULT = 40;
		public static final int BUBBLE_HEIGHT_DEFAULT = 40;
		public static final int BUBBLE_WIDTH = (int) (BUBBLE_WIDTH_DEFAULT * Game.SCALE);
		public static final int BUBBLE_HEIGHT = (int) (BUBBLE_HEIGHT_DEFAULT * Game.SCALE);
		
		public static final int DEATH_ZONE_DMG = PlayerConstants.maxHealth;
		public static final int DEATH_ZONE_WIDTH_DEFAULT = 32;
		public static final int DEATH_ZONE_HEIGHT_DEFAULT = 32;
		public static final int DEATH_ZONE_WIDTH = (int) (DEATH_ZONE_WIDTH_DEFAULT * Game.SCALE);
		public static final int DEATH_ZONE_HEIGHT = (int) (DEATH_ZONE_HEIGHT_DEFAULT * Game.SCALE);
		

		public static int GetSpriteAmount(int object_type) {
			switch (object_type) {
			case RED_POTION, BLUE_POTION:
				return 7;
			case BARREL, BOX:
				return 8;
			case COIN:
				return 6;
			case ARROW:
				return 3;
			case BUBBLE:
				return 5;
			}
			return 1;
		}
	}

	public static class EnemyConstants {
		// Animation player and enemy
		public static final int IDLE = 0;
		public static final int RUNNING = 1;
		public static final int ATTACK = 2;
		public static final int HIT = 3;
		public static final int DEAD = 4;
		// Animation arrow
		public static final int LAUNCH = 5;
		public static final int CRASH = 6;


		public static final int NIGHTBONE = 255;

		public static final int NIGHTBONE_WIDTH_DEFAULT = 80;
		public static final int NIGHTBONE_HEIGHT_DEFAULT = 64;
		public static final int NIGHTBONE_WIDTH = (int) (NIGHTBONE_WIDTH_DEFAULT * Game.SCALE);
		public static final int NIGHTBONE_HEIGHT = (int) (NIGHTBONE_HEIGHT_DEFAULT * Game.SCALE);
		public static final int NIGHTBONE_DRAWOFFSET_X = (int) (30 * Game.SCALE);
		public static final int NIGHTBONE_DRAWOFFSET_Y = (int) (35 * Game.SCALE);
		public static final int NIGHTBONE_SCORE = 20;
		public static final int NIGHTBONE_HEALTH = 20;
		public static final int NIGHTBONE_DMG = 100;

		// Skeleton

		public static final int SKELETON = 250;
		public static final int SKELETON_WIDTH_DEFAULT = 128;
		public static final int SKELETON_HEIGHT_DEFAULT = 96;
		public static final int SKELETON_WIDTH = (int) (SKELETON_WIDTH_DEFAULT * Game.SCALE);
		public static final int SKELETON_HEIGHT = (int) (SKELETON_HEIGHT_DEFAULT * Game.SCALE);
		public static final int SKELETON_DRAWOFFSET_X = (int) (50 * Game.SCALE);
		public static final int SKELETON_DRAWOFFSET_Y = (int) (68 * Game.SCALE);
		public static final int SKELETON_SCORE = 20;
		public static final int SKELETON_HEALTH = 50;
		public static final int SKELETON_DMG = 150;

		public static int GetSpriteAmount(int type_entity, int entity_state) {

			switch (type_entity) {
			case NIGHTBONE:
				switch (entity_state) {
				case IDLE:
					return 9;
				case RUNNING:
					return 6;
				case ATTACK:
					return 7;
				case HIT:
					return 4;
				case DEAD:
					return 5;
				}
				break;
			case SKELETON:
				switch (entity_state) {
				case IDLE:
					return 4;
				case RUNNING:
					return 6;
				case ATTACK:
					return 8;
				case HIT:
					return 3;
				case DEAD:
					return 4;
				}	
			}
			return 0;

		}

		public static int GetMaxHealth(int enemy_type) {
			switch (enemy_type) {
			case NIGHTBONE:
				return NIGHTBONE_HEALTH;
			case SKELETON:
				return SKELETON_HEALTH;	
			default:
				return 1;
			}
		}

		public static int GetEnemyDmg(int enemy_type) {
			switch (enemy_type) {
			case NIGHTBONE:
				return NIGHTBONE_DMG;
			case SKELETON:
				return SKELETON_DMG;	
			default:
				return 0;
			}

		}

	}

	public static class Environment {
		public static final int BIG_CLOUD_WIDTH_DEFAULT = 448;
		public static final int BIG_CLOUD_HEIGHT_DEFAULT = 101;
		public static final int SMALL_CLOUD_WIDTH_DEFAULT = 74;
		public static final int SMALL_CLOUD_HEIGHT_DEFAULT = 24;

		public static final int BIG_CLOUD_WIDTH = (int) (BIG_CLOUD_WIDTH_DEFAULT * Game.SCALE);
		public static final int BIG_CLOUD_HEIGHT = (int) (BIG_CLOUD_HEIGHT_DEFAULT * Game.SCALE);
		public static final int SMALL_CLOUD_WIDTH = (int) (SMALL_CLOUD_WIDTH_DEFAULT * Game.SCALE);
		public static final int SMALL_CLOUD_HEIGHT = (int) (SMALL_CLOUD_HEIGHT_DEFAULT * Game.SCALE);
	}

	public static class UI {
		public static class Buttons {
			public static final int B_WIDTH_DEFAULT = 140;
			public static final int B_HEIGHT_DEFAULT = 56;
			public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * Game.SCALE);
			public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * Game.SCALE);
		}

		public static class PauseButtons {
			public static final int SOUND_SIZE_DEFAULT = 42;
			public static final int SOUND_SIZE = (int) (SOUND_SIZE_DEFAULT * Game.SCALE);
		}

		public static class URMButtons {
			public static final int URM_DEFAULT_SIZE = 56;
			public static final int URM_SIZE = (int) (URM_DEFAULT_SIZE * Game.SCALE);

		}

		public static class VolumeButtons {
			public static final int VOLUME_DEFAULT_WIDTH = 28;
			public static final int VOLUME_DEFAULT_HEIGHT = 44;
			public static final int SLIDER_DEFAULT_WIDTH = 215;

			public static final int VOLUME_WIDTH = (int) (VOLUME_DEFAULT_WIDTH * Game.SCALE);
			public static final int VOLUME_HEIGHT = (int) (VOLUME_DEFAULT_HEIGHT * Game.SCALE);
			public static final int SLIDER_WIDTH = (int) (SLIDER_DEFAULT_WIDTH * Game.SCALE);
		}
	}

	public static class Directions {
		public static final int LEFT = 0;
		public static final int UP = 1;
		public static final int RIGHT = 2;
		public static final int DOWN = 3;
	}

	public static class PlayerConstants {
		public static final int IDLE = 0;
		public static final int RUN = 1;
		public static final int JUMP = 3;
		public static final int FALLING = 2;
		public static final int GROUND = 5;
		public static final int HIT = 4;
		public static final int AIR_ATTACK = 6;
		public static final int ATTACK_1 = 7;
		public static final int ATTACK_JUMP_1 = 8;
		public static final int KICK = 10;
		public static int PLAYER_DMG = 10;
		public static final int maxHealth = 500;

		public static void changePlayerAtk(int value) {
			PLAYER_DMG += value;
		}

		public static void resetPlayerAtk() {
			PLAYER_DMG = 10;
		}

		public static int GetSpriteAmount(int player_action) {
			switch (player_action) {
			case RUN: {
				return 8;
			}
			case IDLE: {
				return 4;
			}
			case HIT: {
				return 1;
			}
			case JUMP:
				return 1;
			case ATTACK_1:
				return 4;
			case ATTACK_JUMP_1:
				return 3;
			case AIR_ATTACK:
				return 3;
			case GROUND:
				return 2;
			case FALLING:
				return 1;
			case KICK:
				return 4;
			default:
				return 1;
			}
		}
	}

}