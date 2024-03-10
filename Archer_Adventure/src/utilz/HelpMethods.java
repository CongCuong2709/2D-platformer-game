package utilz;

import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.ObjectConstants.*;
import static utilz.HelpMethods.getCurrentDateTime;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import entities.Night_bone;
import entities.Skeleton;
import main.Game;
import objects.Arrow;
import objects.Bubble;
import objects.Coin;
import objects.Death_Zone;
import objects.GameContainer;
import objects.Potion;


public class HelpMethods {

	public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
		if (!IsSolid(x, y, lvlData))
			if (!IsSolid(x + width, y + height, lvlData))
				if (!IsSolid(x + width, y, lvlData))
					if (!IsSolid(x, y + height, lvlData))
						return true;
		return false;
	}// CanMove nếu ô chứa thực thể không phải là gạch rắn // dùng trong ???

	
	private static boolean IsSolid(float x, float y, int[][] lvlData) {
		int maxWidth = lvlData[0].length * Game.TILES_SIZE;
		if (x < 0 || x >= maxWidth)
			return true;
		if (y < 0 || y >= Game.GAME_HEIGHT)
			return true;
		float xIndex = x / Game.TILES_SIZE;
		float yIndex = y / Game.TILES_SIZE;

		return IsTileSolid((int) xIndex, (int) yIndex, lvlData);
	}//  toạ độ x, y có phải là gạch rắn hay không ????
	
	public static boolean IsHittingLevel(Arrow a, int[][] lvlData) {
		return IsSolid(a.getHitbox().x + a.getHitbox().width / 2, a.getHitbox().y + a.getHitbox().height / 2, lvlData);
	}

	public static boolean IsTileSolid(int xTile, int yTile, int[][] lvlData) {
		int value = lvlData[yTile][xTile];
		if (value < 96 && value != 11)
			return true;
		return false;
	}//ô xTile, yTile Có phải gạch rắn hay không
	
	

	public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
		int currentTile = (int)(hitbox.x / Game.TILES_SIZE);
		if(xSpeed > 0) {
			//right
			int tileXPos = currentTile * Game.TILES_SIZE ;
			int xOffSet = (int)(Game.TILES_SIZE - hitbox.width) ;
			return tileXPos + xOffSet  - 1 ;
		}else {
			//left
			return currentTile * Game.TILES_SIZE  + 1 ;
		}
	}
	// trả ra khoảng cách sắp chạm gạch trái hoặc phải 

	public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {	
		int currentTile = (int)(hitbox.y / Game.TILES_SIZE);
		if(airSpeed > 0) {
			//falling
			int tileYPos = currentTile * Game.TILES_SIZE ;
			int yOffSet = (int)(Game.TILES_SIZE - hitbox.height);
			return tileYPos + yOffSet - 1;
		}else {
			//jumping
			return currentTile * Game.TILES_SIZE - Game.TILES_SIZE;
		}
	 
	}// trả ra khoảng cách sắp chạm gạch dưới và trên

	public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
		if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
			if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
				return false;
		return true;
	}//Kiểm tra. nếu toạ độ (hitbox + 1) là sàn thì trả ra đang ở trên sàn  

	public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
		if (xSpeed > 0)
			return IsSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
		else
			return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
	}//Nơi đang đứng + 1px có phải sàn không

	public static boolean IsAllTilesWalkable(int xStart, int xEnd, int y, int[][] lvlData) {
		for (int i = 0; i < xEnd - xStart; i++) {
			if (IsTileSolid(xStart + i, y, lvlData))
				return false;
			if (!IsTileSolid(xStart + i, y + 1, lvlData))
				return false;
		}
		return true;
	}//?????????????

	public static boolean IsSightClear(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile) {
		int firstXTile = (int) (firstHitbox.x / Game.TILES_SIZE);
		int secondXTile = (int) (secondHitbox.x / Game.TILES_SIZE);

		if (firstXTile > secondXTile)
			return IsAllTilesWalkable(secondXTile, firstXTile, yTile, lvlData);
		else
			return IsAllTilesWalkable(firstXTile, secondXTile, yTile, lvlData);
	}

	public static int[][] GetLevelData(BufferedImage img) {
		int[][] lvlData = new int[img.getHeight()][img.getWidth()];
		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getRed();
				if (value >= 256 || value < 0)
					value = 11;
				lvlData[j][i] = value;
			}
		return lvlData;
	}// Lấy dữ liệu map bằng màu đỏ. cường độ màu đỏ tương tứng với chỉ số mảng các ô gạch 


	public static ArrayList<Night_bone> GetNight_bones(BufferedImage img) {
		ArrayList<Night_bone> list = new ArrayList<>();
		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getGreen();
				if (value == NIGHTBONE)
					list.add(new Night_bone(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
			}
		return list;
	}// đọc cường độ màu xanh lá của ảnh, dùng hàm tạo thêm vào mảng, trả ra mảng darkMagiction

	public static ArrayList<Skeleton> getSkeletons(BufferedImage img) {
		ArrayList<Skeleton> list = new ArrayList<>();
		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getGreen();
				if (value == SKELETON)
					list.add(new Skeleton(i * Game.TILES_SIZE, j * Game.TILES_SIZE ));
			}
		return list;
	}
	
	public static Point GetPlayerSpawn(BufferedImage img) {
		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getGreen();
				if (value == 100)
					return new Point(i * Game.TILES_SIZE, j * Game.TILES_SIZE);
			}
		return new Point(1 * Game.TILES_SIZE, 1 * Game.TILES_SIZE);
	}// Hàm trả toạ độ Nhân vật được đọc từ ảnh map
	
	
	
	public static ArrayList<Potion> GetPotions(BufferedImage img) {
		ArrayList<Potion> list = new ArrayList<>();
		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getBlue();
				if (value == RED_POTION || value == BLUE_POTION)
					list.add(new Potion(i * Game.TILES_SIZE, j * Game.TILES_SIZE, value));				
			}
		return list;
	}
	
	public static ArrayList<GameContainer> GetContainers(BufferedImage img) {
		ArrayList<GameContainer> list = new ArrayList<>();
		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getBlue();
				if (value == BOX || value == BARREL)
					list.add(new GameContainer(i * Game.TILES_SIZE, j * Game.TILES_SIZE, value));				
			}
		return list;
	}
	
	public static ArrayList<Coin> GetCoins(BufferedImage img) {
		ArrayList<Coin> list = new ArrayList<>();
		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getBlue();
				if (value == COIN)
					list.add(new Coin(i * Game.TILES_SIZE, j * Game.TILES_SIZE, value));				
			}
		return list;
	}
	public static ArrayList<Arrow> GetArrow(BufferedImage img) {
		ArrayList<Arrow> list = new ArrayList<>();
		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getBlue();
				if (value == ARROW)
					list.add(new Arrow(i * Game.TILES_SIZE, j * Game.TILES_SIZE, value));				
			}
		return list;
	}
	
	public static ArrayList<Bubble> GetBubble(BufferedImage img) {
		ArrayList<Bubble> list = new ArrayList<>();
		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getBlue();
				if (value == BUBBLE)
					list.add(new Bubble(i * Game.TILES_SIZE, j * Game.TILES_SIZE, value));				
			}
		return list;
	}
	
	public static ArrayList<Death_Zone> GetZone(BufferedImage img) {
		ArrayList<Death_Zone> list = new ArrayList<>();
		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getBlue();
				if (value == DEATH_ZONE)
					list.add(new Death_Zone(i * Game.TILES_SIZE, j * Game.TILES_SIZE, value));				
			}
		return list;
	}
	
	public static String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return now.format(formatter);
    }
	
	public static void updateScore(Game game) {
		String fileName = "score.txt";
		
		try {
		    FileWriter fileWriter = new FileWriter(fileName, true);
		    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		    PrintWriter printWriter = new PrintWriter(bufferedWriter);

		    // Lấy thời gian hiện tại đến giây
		    String time = getCurrentDateTime();

		    // Ghi thời gian và điểm số vào file
		    printWriter.println("Time: " + time + ", Score: " + game.getScore());

		    // Đóng các luồng ghi dữ liệu
		    printWriter.close();
		    bufferedWriter.close();
		    fileWriter.close();
		    saveScore(time, game.getScore());
		    
		 
		} catch (IOException e) {
		    e.printStackTrace();
		}

	}
	
	public static void saveScore(String time, int score) {
			String URL = "jdbc:mysql://localhost/score"; 
		    String USERNAME = "root"; 
		    String PASSWORD = "";
	    try {
	        // Tạo kết nối đến database
	        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

	        // Thực hiện truy vấn để lưu kết quả
	        String sql = "INSERT INTO score (Score, Time) VALUES (?, ?)";
	        PreparedStatement statement = conn.prepareStatement(sql);
	        statement.setString(2, time);
	        statement.setInt(1, score);
	        statement.executeUpdate();

	        // Đóng kết nối
	        conn.close();
	    } catch (SQLException ex) {
	        System.out.println("Lỗi kết nối");
	    }
	}


	

}