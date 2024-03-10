package objects;

import java.awt.Graphics;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import static utilz.Constants.PlayerConstants.changePlayerAtk;


import entities.Player;
import gamestates.Playing;
import levels.Level;
import main.Game;
import utilz.LoadSave;
import static utilz.Constants.ObjectConstants.*;
import static utilz.HelpMethods.IsHittingLevel;

public class ObjectManager {

	private Playing playing;
	private BufferedImage[][] potionImg, containerImg, coinImg, bubbleImg;
	private BufferedImage arrowImg;
	private ArrayList<Potion> potions;
	private ArrayList<GameContainer> containers;
	private ArrayList<Coin> coins;
	private ArrayList<Arrow> arrows = new ArrayList<>();
	private ArrayList<Bubble> bubbles;
	private ArrayList<Death_Zone> zones;
	
	public ObjectManager(Playing playing) {
		this.playing = playing;
		loadImgs();

	}
	
	public void checkObjectTouched(Rectangle2D.Float hitbox) {
		for(Potion p : potions )
			if(p.isActive())
				if(hitbox.intersects(p.getHitbox())) {
					p.setActive(false);
					applyEffectToPlayer(p);				
				}
		for(Coin c : coins )
			if(c.isActive())
				if(hitbox.intersects(c.getHitbox())) {
					c.setActive(false);
					collectedCoin(c);				
				}
		for(Bubble b : bubbles )
			if(b.isActive()) 
				if(hitbox.intersects(b.getHitbox())) {
					playing.getPlayer().takeDamage(BUBBLE_DMG_VALUE);	
					b.setActive(false);
					playing.changeScore(-10);
				}
		for(Death_Zone z : zones )
				if(hitbox.intersects(z.getHitbox())) 
					playing.getPlayer().takeDamage(DEATH_ZONE_DMG);
					
	}
	
	public void checkHitting() {	
		for(Arrow a : arrows ) {
			if(a.isActive())	{		
					if(playing.getEnemyManager().checkArrowHit(a.hitbox))
						a.setActive(false);
					if(playing.getObjectManager().checkObjectHit(a.hitbox))
						a.setActive(false);	
			}
		}
		for(Bubble b : bubbles ) {
			if(b.isActive())	{		
					if(playing.getEnemyManager().checkObjectHit(b.hitbox))
						b.setActive(false);
			}
		}
	}
	
	public void applyEffectToPlayer(Potion p) {
		if(p.getObjType() == RED_POTION)
			playing.getPlayer().changeHealth(RED_POTION_VALUE);
		if(p.getObjType() == BLUE_POTION)
			changePlayerAtk(10);
	}
	
	public void collectedCoin(Coin c) {
		if(c.getObjType() == COIN) 
			playing.changeScore(COIN_VALUE);					
	}
	
	
	public boolean checkObjectHit(Rectangle2D.Float attackbox) {
		for(GameContainer gc : containers)
			if(gc.isActive()) {
				if(gc.getHitbox().intersects(attackbox)) {
					gc.setAnimation(true);
					int type = 0;
					if(gc.getObjType() == BARREL)
						type =1;
					potions.add(new Potion((int)(gc.getHitbox().x + gc.getHitbox().width / 2),
							(int) (gc.getHitbox().y + gc.getHitbox().height / 2),
							type));
					return true;
				}
			}
		for(Bubble b : bubbles)
			if(b.isActive())
				if(attackbox.intersects(b.getHitbox())) {
					b.setAnimation(true);
					return true;
				}	
		return false;
	}
	
	
	public void loadObject(Level newLevel) {
		potions = newLevel.getPotions();
		containers = newLevel.getContainers();	
		coins = newLevel.getCoins();
		bubbles = newLevel.getBubbles();
		zones = newLevel.getZone();
		arrows.clear();
	}
	
	private void loadImgs() {
		BufferedImage potionSprite = LoadSave.GetSpriteAtlas(LoadSave.POTION_ATLAS);
		potionImg = new BufferedImage[2][7];

		for (int j = 0; j < potionImg.length; j++)
			for (int i = 0; i < potionImg[j].length; i++)
				potionImg[j][i] = potionSprite.getSubimage(12 * i, 16 * j, 12, 16);

		BufferedImage containerSprite = LoadSave.GetSpriteAtlas(LoadSave.CONTAINER_ATLAS);
		containerImg = new BufferedImage[2][8];

		for (int j = 0; j < containerImg.length; j++)
			for (int i = 0; i < containerImg[j].length; i++)
				containerImg[j][i] = containerSprite.getSubimage(40 * i, 30 * j, 40, 30);
		
		BufferedImage CoinSprite = LoadSave.GetSpriteAtlas(LoadSave.COIN_ATLAS);
		coinImg = new BufferedImage[1][6];

		for (int j = 0; j < coinImg.length; j++)
			for (int i = 0; i < coinImg[j].length; i++)
				coinImg[j][i] = CoinSprite.getSubimage(120 * i, 120 * j, 120, 120);	
		
		arrowImg = LoadSave.GetSpriteAtlas(LoadSave.ARROW_ATLAS);
		
		BufferedImage BubbleSprite = LoadSave.GetSpriteAtlas(LoadSave.BUBBLE_ATLAS);
		bubbleImg = new BufferedImage[1][5];

		for (int j = 0; j < bubbleImg.length; j++)
			for (int i = 0; i < bubbleImg[j].length; i++)
				bubbleImg[j][i] = BubbleSprite.getSubimage(200 * i, 200 * j, 200, 200);	


	}
	
	public void update(int[][] lvlData, Player player) {
		for(Potion p : potions)
			if(p.isActive())
				p.update();
		
		for(GameContainer gc : containers)
			if(gc.isActive())
				gc.update();
		
		for(Coin c : coins)
			if(c.isActive())
				c.update();
		
		updateArrow(lvlData, player);
		
		for(Bubble b : bubbles)
			if(b.isActive()) {
				b.update();
				b.updatePos();
			}
		for(Death_Zone z : zones)
				z.update();
	}
	
	private void updateArrow(int[][] lvlData, Player player) {
		for(Arrow a: arrows)
			if(a.isActive()) {
				a.updatePos();
				if(IsHittingLevel(a, lvlData))
					a.setActive(false);
			}
	}
	
	
	public void fireArrow(Player p) {
			if(!p.isKicking() && p.isFreefire()) {
				int dir = 1;
				if(p.isFacingLeft()) 
					dir = -1;
				arrows.add(new Arrow((int)p.getHitbox().x, (int)(p.getHitbox().y - Game.TILES_SIZE ), dir));
			}
	}

	public void draw(Graphics g, int xLvlOffset) {	
		drawPotion(g, xLvlOffset);
		drawContainer(g, xLvlOffset);
		drawCoin(g, xLvlOffset);
		drawArrow(g, xLvlOffset);
		drawBubble(g, xLvlOffset);
	}

	private void drawArrow(Graphics g, int xLvlOffset) {
		for(Arrow a : arrows)
			if(a.isActive())
					g.drawImage(arrowImg, (int) (a.getHitbox().x - xLvlOffset),(int) a.getHitbox().y ,ARROW_WIDTH, ARROW_HEIGHT, null);	
	}

	private void drawPotion(Graphics g, int xLvlOffset) {
		for(Potion p : potions)
			if(p.isActive()) {
				int type = 0;
				if(p.getObjType() == RED_POTION)
					type = 1;
				g.drawImage(potionImg[type][p.getAniIndex()],
						(int) p.getHitbox().x - p.getxDrawOffset() - xLvlOffset,
						(int) p.getHitbox().y - p.getyDrawOffset(),
						POTION_WIDTH,
						POTION_HEIGHT,
						null);
			}
		
	}

	private void drawContainer(Graphics g, int xLvlOffset) {
		for(GameContainer gc : containers)
			if(gc.isActive()) {
				int type = 0;
				if(gc.getObjType() == BARREL) 
					type = 1;
			    g.drawImage(containerImg[type][gc.getAniIndex()],
					(int) gc.getHitbox().x - gc.getxDrawOffset() - xLvlOffset ,
					(int) gc.getHitbox().y - gc.getyDrawOffset(),
					CONTAINER_WIDTH,
					CONTAINER_HEIGHT,
					null);	
			}
	}
	
	private void drawCoin(Graphics g, int xLvlOffset) {
		for(Coin c : coins)
			if(c.isActive()) {
			    g.drawImage(coinImg[0][c.getAniIndex()],
					(int) c.getHitbox().x - c.getxDrawOffset() - xLvlOffset,
					(int) c.getHitbox().y - c.getyDrawOffset(),
					COIN_WIDTH ,
					COIN_HEIGHT ,
					null);	
			}
	}
	
	private void drawBubble(Graphics g, int xLvlOffset) {
		for(Bubble b : bubbles)
			if(b.isActive()) {
			    g.drawImage(bubbleImg[0][b.getAniIndex()],(int) b.getHitbox().x - b.getxDrawOffset() - xLvlOffset,(int) b.getHitbox().y - b.getyDrawOffset(),BUBBLE_WIDTH ,BUBBLE_HEIGHT ,null);	
		}
	}

	
	public void resetAllObject() {
		for(GameContainer gc : containers)
			gc.reset();
		for(Potion p : potions)
			p.reset();
		for(Coin c : coins)
			c.reset();
		for(Bubble b : bubbles)
			b.reset();
	}
	
	public ArrayList<Arrow> getArrow(){
		return arrows;
	}
	
}
