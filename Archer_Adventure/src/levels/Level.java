package levels;

import static utilz.HelpMethods.GetLevelData;
import static utilz.HelpMethods.GetPlayerSpawn;
import static utilz.HelpMethods.getSkeletons;
import static utilz.HelpMethods.GetNight_bones;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Night_bone;
import entities.Skeleton;
import main.Game;
import objects.*;
import utilz.HelpMethods;

public class Level {

	private BufferedImage img;// ảnh chứa map
	private int[][] lvlData; // Ma trận map
	private ArrayList<Night_bone> night_bones; // mảng quái
	private ArrayList<Skeleton> skeletons;
	private ArrayList<Potion> potions; 
	private ArrayList<GameContainer> containers; 
	private ArrayList<Coin> coins;
	private ArrayList<Bubble> bubbles;
	private ArrayList<Death_Zone> zones;
	private int lvlTilesWide; // Chiều rộng của ảnh ( đơn vị px )
	private int maxTilesOffset; // 
	private int maxLvlOffsetX;
	private Point playerSpawn;
 
	public Level(BufferedImage img) {
		this.img = img;
		createLevelData();// gán ma trận map cho lvData
		createEnemies(); // tạo dữ liệu quái
		createPotion();
		createContainer();
		createCoin();
		createBubble();
		createZone();
		calcLvlOffsets();
		calcPlayerSpawn();// Tính toán điểm thả nhân vật vào game
	}

	private void createCoin() {
		coins = HelpMethods.GetCoins(img);	
	}

	private void createContainer() {
		containers = HelpMethods.GetContainers(img);
	}

	private void createPotion() {
		potions = HelpMethods.GetPotions(img);	
	}
	
	private void createBubble() {
		bubbles = HelpMethods.GetBubble(img);	
	}
	
	private void createZone() {
		zones = HelpMethods.GetZone(img);	
	}

	private void calcPlayerSpawn() {
		playerSpawn = GetPlayerSpawn(img);
	}// gán toạ độ player được lấy từ HelpMethod.java (getGreen == 100 thì trả ra toạ độ trong map, không thì mặc định điểm map (1;1)

	private void calcLvlOffsets() {
		lvlTilesWide = img.getWidth();
		maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
		maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset;
	} // Thiết lập ô gạch 

	private void createEnemies() {
		night_bones = GetNight_bones(img);
		skeletons = getSkeletons(img);
	}// Gán và tạo các kẻ địch vào araylist bằng helpMethod (hàm dùng đọc mã màu, thêm quái vào mảng)

	private void createLevelData() {
		lvlData = GetLevelData(img);
	}// thiết lập ma trận map bằng cách gọi hàm đọc map từ helpMethod.java

	public int getSpriteIndex(int x, int y) {
		return lvlData[y][x];
	}

	public int[][] getLevelData() {
		return lvlData;
	}// trả về dữ liệu ma trận của map

	public int getLvlOffset() {
		return maxLvlOffsetX;
	}

	
	public ArrayList<Night_bone> getNight_bones(){
		return night_bones;
	}
	
	public ArrayList<Skeleton> get_Skeletons(){
		return skeletons;
	}

	public Point getPlayerSpawn() {
		return playerSpawn;
	}
	
	public ArrayList<Potion> getPotions(){
		return potions;
	}
	
	public ArrayList<GameContainer> getContainers(){
		return containers;
	}
	
	public ArrayList<Coin> getCoins(){
		return coins;
	}
	
	public ArrayList<Bubble> getBubbles(){
		return bubbles;
	}
	
	public ArrayList<Death_Zone> getZone(){
		return zones;
	}

}
