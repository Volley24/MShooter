package com.max.javagame.graphics;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JFrame;

import com.max.javagame.entity.mob.NetPlayer;
import com.max.javagame.entity.mob.Player;
import com.max.javagame.graphics.gui.GuiHandler;
import com.max.javagame.graphics.gui.GuiHandler.State;
import com.max.javagame.input.Keyboard;
import com.max.javagame.input.Mouse;
import com.max.javagame.level.Level;
import com.max.javagame.net.GameClient;
import com.max.javagame.net.GameServer;
import com.max.javagame.net.packets.Packet00Login;
import com.max.javagame.net.packets.Packet01Disconnect;

public class Main extends Canvas implements Runnable {
	private static final long serialVersionUID = 2L;

	public static final String TITLE = "World of Squares";
	public static final String VERSION = " alpha release 0.8";
	public static String name = "Enter Name";
	public static String ipAddress = "Enter IP";
	public static Main main;
	
	private int port = 3012;
	
	private int FPS = 0;
	private int UPS = 0;
	
	long lastTime;
	float deltaTime;

	/**
	 * ~ World of Squares~
	 * 
	 * Made by: Maxim Creanga Version: alpha 0.8
	 * 
	 */

	// Window parameters:
	public final static int windowWidth = 300;
	public final static int windowHeight = windowWidth / 16 * 9; // Maintain an aspect ratio of 16:9 based on the window
																// width
	public static int windowScale = 3; // By how much the window gets scaled. (Example: 3x)

	// Main class private parametres
	private Thread gameThread; // New Thread
	private JFrame jFrame;
	private boolean isRunning = false;

	public Keyboard keyboardInput;
	public Level level;

	public static Player player;

	public GameClient gameClient;
	public GameServer gameServer;
	private Render renderer;
	private GuiHandler guiHandler;

	private BufferedImage image = new BufferedImage(windowWidth, windowHeight, BufferedImage.TYPE_INT_RGB);
	private int pixels[] = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

	public Main() throws HeadlessException, UnknownHostException {
		main = this;
		Dimension preferredSize = new Dimension(windowWidth * windowScale, windowHeight * windowScale);
		setPreferredSize(preferredSize);

		jFrame = new JFrame();
		renderer = new Render(windowWidth, windowHeight);
		keyboardInput = new Keyboard();
		//com.max.javagame.level = Level.lakeVilleMap;
		guiHandler = new GuiHandler(State.MAIN_MENU, keyboardInput);

		setFocusable(true);

		Mouse mouse = new Mouse();
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		addKeyListener(keyboardInput);


		// jFrame.remove(pisica);
		jFrame.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent we) {
				if (guiHandler.getGuiState().equals(State.IN_GAME) || guiHandler.getGuiState().equals(State.PAUSED)) {
					try {
						Packet01Disconnect disconnectPacket = new Packet01Disconnect(Main.player.getUserName());
						if (gameServer != null) {
							disconnectPacket.writeData(gameServer);
						} else if (gameClient != null) {
							disconnectPacket.writeData(gameClient);
						}
					} catch (Exception e) {
						System.out.println("Error trying to disconnect");
						e.printStackTrace();
						System.exit(1);
					}
				}

			}
		});

	}

	public synchronized void start() {
		isRunning = true;
		gameThread = new Thread(this, "Main-Thread");// Thread is attached to Main game Object
		gameThread.start();
	}

	public synchronized void stop() {
		isRunning = false;
		try {
			gameThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		// When Thread starts, its runs code here:
		long lastTime = System.nanoTime();
		final double ns = 1_000_000_000.0 / 60.0;
		float deltaTime = 0f;

		int frames = 0;// How frames we have time to render every second
		int updates = 0;// How many times we update each second
		long timer = System.currentTimeMillis(); // One second timer
		long amount = 0;
		requestFocus();
		while (isRunning) {
			long currentTime = System.nanoTime();
			deltaTime += (currentTime - lastTime) / ns; // Adding to delta
			lastTime = currentTime;
			while (deltaTime >= 1) {
				update(); // Update 60 times per second
				updates++;
				deltaTime--;
			}

			render();
			frames++;

			if ((System.currentTimeMillis() - (timer + (1000 * amount))) > 1000) {
				amount++;
				jFrame.setTitle(TITLE + VERSION + " UPS: " + updates + " FPS: " + frames);
				FPS = frames;
				UPS = updates;
				updates = 0;
				frames = 0;
			}
		}
		stop();
	}

	public void update() {

		
		guiHandler.update();
		keyboardInput.update();
		if (guiHandler.getGuiState().equals(State.IN_GAME)) {
			level.update();
		}
		switch (guiHandler.getGuiState()) {
		case CREATE_GAME:
			
			if (guiHandler.editVar1 != null && guiHandler.editVar1.isClicked()) {
				keyboardInput.startListening1 = true;
			}
			else if (Mouse.getB()==1) {
				keyboardInput.startListening1 = false;
			}
			
			name = String.valueOf(keyboardInput.chars1).trim();
			if (guiHandler.startGame != null && guiHandler.startGame.isClicked()) {

				int playerXSpawn = 16;
				int playerYSpawn = 16;
				
				if(keyboardInput.chars1[keyboardInput.charIndex1]==95) {
					keyboardInput.chars1[keyboardInput.charIndex1] = 0;
				}
				name = String.valueOf(keyboardInput.chars1).trim();
				keyboardInput.resetTyped();
				
				gameServer = new GameServer(this, port);
				gameServer.start();

				player = new Player(name, Sprite.playerSprite, playerXSpawn, playerYSpawn, keyboardInput);
				level = Level.lakeVilleMap;
				
				level.add(player);

				guiHandler.setGuiState(State.IN_GAME);
			}
			break;
		case JOIN_GAME:
			if (guiHandler.editVar1 != null && guiHandler.editVar1.isClicked()) {
				keyboardInput.startListening1 = true;
			}
			else if (Mouse.getB()==1) {
				keyboardInput.startListening1 = false;
			}
			
			name = String.valueOf(keyboardInput.chars1).trim();
			if (guiHandler.editVar2 != null && guiHandler.editVar2.isClicked()) {
				keyboardInput.startListening2 = true;
			}
			else if (Mouse.getB()==1) {
				keyboardInput.startListening2 = false;
			}
			
			ipAddress = String.valueOf(keyboardInput.chars2).trim();
			if (guiHandler.startGame != null && guiHandler.startGame.isClicked()) {
				int playerXSpawn = 16;
				int playerYSpawn = 16;
				
				if(keyboardInput.chars1[keyboardInput.charIndex1]==95) {
					keyboardInput.chars1[keyboardInput.charIndex1] = 0;
				}
				name = String.valueOf(keyboardInput.chars1).trim();
				if(keyboardInput.chars2[keyboardInput.charIndex2]==95) {
					keyboardInput.chars2[keyboardInput.charIndex2] = 0;
				}
				ipAddress = String.valueOf(keyboardInput.chars1).trim();
				keyboardInput.resetTyped();
				gameClient = new GameClient(this, ipAddress, port);
				gameClient.start();
				
				try {
					player = new NetPlayer(name, Sprite.playerSprite, playerXSpawn, playerYSpawn, keyboardInput, InetAddress.getByName(ipAddress), port);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				level = Level.lakeVilleMap;
				level.add(player);
				Packet00Login loginPacket = new Packet00Login(player.getUserName(),playerXSpawn,playerYSpawn);
				loginPacket.writeData(gameClient);
				guiHandler.setGuiState(State.IN_GAME);
			}
			break;
		case SINGLE_PLAYER:
			if (guiHandler.editVar1 != null && guiHandler.editVar1.isClicked()) {
				keyboardInput.startListening1 = true;
			}
			else if (Mouse.getB()==1) {
				keyboardInput.startListening1 = false;
			}
			name = String.valueOf(keyboardInput.chars1).trim();
			if (guiHandler.startGame != null && guiHandler.startGame.isClicked() && !(name.equals("Enter a username"))) {
				
				int playerXSpawn = 16;
				int playerYSpawn = 16;
				if(keyboardInput.chars1[keyboardInput.charIndex1]==95) {
					keyboardInput.chars1[keyboardInput.charIndex1] = 0;
				}
				name = String.valueOf(keyboardInput.chars1).trim();
				keyboardInput.resetTyped();
				
				player = new Player(name, Sprite.playerSprite, playerXSpawn, playerYSpawn, keyboardInput);
				level = Level.lakeVilleMap;
				level.add(player);

				guiHandler.setGuiState(State.IN_GAME);
			}
			break;
		default:
			break;

		}

	}

	public void render() {
		// Displaying to the jFrame will be made here, called multiple times per second
		BufferStrategy bufferStrategy = getBufferStrategy();// Temporary storage for com.max.javagame.graphics

		if (bufferStrategy == null) { // If the current BufferStartegy used by our Main Game class is null, create a
										// // buffer strategy
			createBufferStrategy(3); // Create 3 Buffer Startegys
			return;
		}
		renderer.clear();
		int xScroll = 0;
		int yScroll = 0;
		if (guiHandler.getGuiState().equals(State.IN_GAME)) {
			xScroll = (int) ((player.x - (renderer.width / 2)) + 16);
			yScroll = (int) ((player.y - (renderer.height / 2)) + 16);
		}

		if (guiHandler.getGuiState().equals(State.IN_GAME)) {
			level.render(xScroll, yScroll, renderer);
		}

		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = renderer.pixels[i];
		}

		Graphics graphics = bufferStrategy.getDrawGraphics();// Set com.max.javagame.graphics to the draw com.max.javagame.graphics of the buffer strategy
		graphics.drawImage(image, 0, 0, getWidth(), getHeight(), null);// Draw the bufferedImage with all the pixel

		guiHandler.render(renderer, graphics,this);
		
		graphics.setColor(Color.WHITE);
		graphics.setFont(new Font("Verdana", 20, 20));

		graphics.drawString("FPS: "+FPS+" UPS: "+UPS, 0, 20);
		if (player != null && guiHandler.getGuiState().equals(State.IN_GAME)) {
			player.drawWeaponInfo(graphics);
			player.renderPlayerInfo(graphics);
		}

		graphics.dispose();// Dispose of previous com.max.javagame.graphics
		bufferStrategy.show();// Make the next availible bufferStrategy be visible
	}

	public static void main(String args[]) {
		Main mainGame = null;
		try {
			mainGame = new Main();
		} catch (HeadlessException | UnknownHostException e) {

			e.printStackTrace();
		}
		mainGame.jFrame.setResizable(false);
		mainGame.jFrame.setTitle(TITLE + VERSION);
		mainGame.jFrame.add(mainGame);
		mainGame.jFrame.pack(); // Causes the JFrame to be the same size as the mainGame Class prefferedSize

		mainGame.jFrame.setLocationRelativeTo(null);// Centers JFrame
		mainGame.jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// Adds functionallity to terminate the
																		// aplication upon pressing close the window.
		mainGame.jFrame.setVisible(true);// Makes the jFrame visible

		mainGame.start();

	}

	public static int getWindowWidth() {
		return windowWidth * windowScale;
	}

	public static int getWindowHeight() {
		return windowHeight * windowScale;
	}

}
