package gamestates;

import java.awt.Graphics;
import java.awt.MenuBar;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import ui.AudioOption;
import ui.PauseButton;
import ui.UrmButton;
import utilz.LoadSave;
import static utilz.Constants.UI.URMButtons.*;

public class GameOption extends State implements Statemethods {
	
	private AudioOption audioOption;
	private BufferedImage backgroundImg, optionBackgroundImg;
	private int bgX, bgY, bgH, bgW;
	private UrmButton menuB;
	
	public GameOption(Game game) {
		super(game);
		loadImg();
		loadButton();
		audioOption= game.getAudioOption();
	}

	private void loadButton() {
		int menuX = (int) (387 * Game.SCALE);
		int menuY = (int) (325 * Game.SCALE);
		
		menuB = new UrmButton(menuX, menuY, URM_SIZE, URM_SIZE, 2);
		
	}

	private void loadImg() {
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
		optionBackgroundImg = LoadSave.GetSpriteAtlas(LoadSave.OPTION_MENU);
		
		bgW = (int) (optionBackgroundImg.getWidth() * game.SCALE);
		bgH = (int) (optionBackgroundImg.getHeight() * game.SCALE);
		bgX = Game.GAME_WIDTH / 2- bgW / 2;
		bgY = (int) (33 * game.SCALE);
	}

	@Override
	public void update() {
		menuB.update();
		audioOption.update();

	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(optionBackgroundImg, bgX, bgY, bgW, bgH, null);

		menuB.draw(g);
		audioOption.draw(g);

	}

	public void mouseDragged(MouseEvent e) {
		audioOption.mouseDragged(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (isIn(e, menuB)) {
			menuB.setMousePressed(true);
		} else
			audioOption.mousePressed(e);

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (isIn(e, menuB)) {
			if (menuB.isMousePressed())
				Gamestate.state = Gamestate.MENU;
		} else
			audioOption.mouseReleased(e);

		menuB.resetBools();

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		menuB.setMouseOver(false);

		if (isIn(e, menuB))
			menuB.setMouseOver(true);
		else
			audioOption.mouseMoved(e);

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			Gamestate.state = Gamestate.MENU;

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	private boolean isIn(MouseEvent e, PauseButton b) {
		return b.getBounds().contains(e.getX(), e.getY());
	}
	
	
}
