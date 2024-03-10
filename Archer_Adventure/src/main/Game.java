package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.time.LocalDateTime;

import audio.AudioPlayer;
import gamestates.GameOption;
import gamestates.Gamestate;
import gamestates.Menu;
import gamestates.Playing;
import objects.GameObject;
import ui.AudioOption;
import utilz.LoadSave;

public class Game implements Runnable {

	private GameWindow gameWindow;
	private GamePanel gamePanel;
	private Thread gameThread;
	private final int FPS_SET = 120;
	private final int UPS_SET = 200;

	private Playing playing;
	private Menu menu;
	private GameOption gameOption;
	private AudioOption audioOption;
	private AudioPlayer audioPlayer;
	
	public int Score = 0;
	public LocalDateTime time = LocalDateTime.now();
	
	public final static int TILES_DEFAULT_SIZE = 32;
	public final static float SCALE = 1.0f;
	public final static int TILES_IN_WIDTH = 26;
	public final static int TILES_IN_HEIGHT = 14;
	public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
	public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
	public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

	public Game() {
		initClasses();
		gamePanel = new GamePanel(this);
		gameWindow = new GameWindow(gamePanel);
		gamePanel.setFocusable(true);
		gamePanel.requestFocus();
		startGameLoop();
	}

	private void initClasses() {
		audioOption = new AudioOption(this);
		audioPlayer = new AudioPlayer();
		menu = new Menu(this);
		playing = new Playing(this);
		gameOption = new GameOption(this);

	}

	private void startGameLoop() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	public void update() {
		switch (Gamestate.state) {
		case MENU:
			menu.update();
			break;
		case PLAYING:
			playing.update();
			break;
		case OPTIONS:
			gameOption.update();
			break;
		case QUIT:
		default:
			System.exit(0);
			break;
		}
		time = LocalDateTime.now();
	}

	public void render(Graphics g) {
		switch (Gamestate.state) {
		case MENU:
			menu.draw(g);
			break;
		case PLAYING:{
			playing.draw(g);
			g.setColor(Color.WHITE);
	        g.setFont(new Font("Arial", Font.PLAIN, 24));
	        g.drawString("Score: " + getScore(), GAME_WIDTH - 150, 30);
			break;}
		case OPTIONS:
			gameOption.draw(g);
			break;
		default:
			break;
		}
	}

	@Override
	public void run() {

		double timePerFrame = 1000000000.0 / FPS_SET;
		double timePerUpdate = 1000000000.0 / UPS_SET;

		long previousTime = System.nanoTime();

		int frames = 0;
		int updates = 0;
		long lastCheck = System.currentTimeMillis();

		double deltaU = 0;
		double deltaF = 0;

		while (true) {
			long currentTime = System.nanoTime();

			deltaU += (currentTime - previousTime) / timePerUpdate;
			deltaF += (currentTime - previousTime) / timePerFrame;
			previousTime = currentTime;

			if (deltaU >= 1) {
				update();
				updates++;
				deltaU--;
			}

			if (deltaF >= 1) {
				gamePanel.repaint();
				frames++;
				deltaF--;
			}

			if (System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
				System.out.println("FPS: " + frames + " | UPS: " + updates);
				frames = 0;
				updates = 0;

			}
		}

	}
	
	public void setScore(int value) {
		this.Score += value;
	}
	public int getScore() {
		return Score;
	}
	
	public void resetScore() {
		this.Score = 0;
	}

	public void windowFocusLost() {
		if (Gamestate.state == Gamestate.PLAYING)
			playing.getPlayer().resetDirBooleans();
	}

	public Menu getMenu() {
		return menu;
	}

	public Playing getPlaying() {
		return playing;
	}
	
	public GameOption getGameOption() {
		return gameOption;
	}
	
	public AudioOption getAudioOption() {
		return audioOption;
	}
	public AudioPlayer getAudioPlayer() {
		return audioPlayer;
	}
	
}