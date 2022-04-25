package com.max.javagame.graphics.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import com.max.javagame.entity.Entity;
import com.max.javagame.entity.mob.NetPlayer;
import com.max.javagame.entity.mob.Player;
import com.max.javagame.graphics.Button;
import com.max.javagame.graphics.Main;
import com.max.javagame.graphics.Render;
import com.max.javagame.input.Keyboard;

public class GuiHandler {
	public static enum State {
		MAIN_MENU, SELECT_GAMEMODE, SINGLE_PLAYER, JOIN_GAME, CREATE_GAME, IN_GAME, PAUSED;
	}

	private State state;

	private Keyboard input;
	private Button buttonPlay;
	private Button buttonQuit;

	private Button singlePlayer;
	private Button joinGame;
	private Button createGame;

	public Button editVar1, editVar2;
	public Button startGame;

	public boolean buttonAlreadyClicked = false;

	public GuiHandler(State state, Keyboard input) {
		this.state = state;
		this.input = input;
	}

	public void update() {
		switch (state) {
		case MAIN_MENU:
			if (buttonPlay != null) {
				buttonPlay.update();

				if (buttonPlay.isClicked()) {
					state = State.SELECT_GAMEMODE;
					buttonPlay.setDestroyed(true);
				}
			}
			if (buttonQuit != null) {
				buttonQuit.update();

				if (buttonQuit.isClicked()) {
					System.exit(0);
				}
			}
			break;
		case SELECT_GAMEMODE:
			singlePlayer = new Button((Main.getWindowWidth() / 2) - 100, Main.getWindowHeight() / 3, 150, 50,
					Color.BLUE);

			joinGame = new Button((Main.getWindowWidth() / 2) - 100, (Main.getWindowHeight() / 3) + 75, 150, 50,
					Color.BLUE);

			createGame = new Button((Main.getWindowWidth() / 2) - 100, (Main.getWindowHeight() / 3) + 150, 150, 50,
					Color.BLUE);

			singlePlayer.update();
			joinGame.update();
			createGame.update();

			if (input.keys[KeyEvent.VK_ESCAPE]) {

				state = State.MAIN_MENU;
				buttonPlay.setDestroyed(false);
			}

			if (singlePlayer.isClicked()) {
				input.resetTyped();
				state = State.SINGLE_PLAYER;
			}
			if (joinGame.isClicked()) {
				input.resetTyped();
				state = State.JOIN_GAME;
			}
			if (createGame.isClicked()) {
				input.resetTyped();
				state = State.CREATE_GAME;
			}
			break;
		case SINGLE_PLAYER:
			singlePlayer.setDestroyed(true);
			createGame.setDestroyed(true);
			joinGame.setDestroyed(true);

			editVar1 = new Button((Main.getWindowWidth()) - 290, (Main.getWindowHeight() / 4) + 15, 150, 50,
					Color.BLACK);

			if (input.keys[KeyEvent.VK_ESCAPE]) {
				singlePlayer.setDestroyed(false);
				createGame.setDestroyed(false);
				joinGame.setDestroyed(false);
				state = State.SELECT_GAMEMODE;
				input.resetTyped();
				input.keys[KeyEvent.VK_ESCAPE] = false;
			}
			editVar1.update();
			startGame.update();

			break;
		case JOIN_GAME:
			singlePlayer.setDestroyed(true);
			createGame.setDestroyed(true);
			joinGame.setDestroyed(true);

			editVar1 = new Button((Main.getWindowWidth()) - 290, (Main.getWindowHeight() / 4) + 15, 150, 50,
					Color.BLACK);
			editVar2 = new Button((Main.getWindowWidth()) - 290, (Main.getWindowHeight() / 2) + 15, 150, 50,
					Color.BLACK);
			startGame = new Button((Main.getWindowWidth() / 2) - 100, Main.getWindowHeight() - 100, 200, 75,
					Color.BLACK);

			if (input.keys[KeyEvent.VK_ESCAPE]) {
				singlePlayer.setDestroyed(false);
				createGame.setDestroyed(false);
				joinGame.setDestroyed(false);
				state = State.SELECT_GAMEMODE;
				input.resetTyped();
				input.keys[KeyEvent.VK_ESCAPE] = false;
			}
			editVar1.update();
			editVar2.update();
			startGame.update();
			break;
		case CREATE_GAME:
			singlePlayer.setDestroyed(true);
			createGame.setDestroyed(true);
			joinGame.setDestroyed(true);

			editVar1 = new Button((Main.getWindowWidth()) - 290, (Main.getWindowHeight() / 4) + 15, 150, 50,
					Color.BLACK);
			startGame = new Button((Main.getWindowWidth() / 2) - 100, Main.getWindowHeight() - 100, 200, 75,
					Color.BLACK);

			if (input.keys[KeyEvent.VK_ESCAPE]) {
				singlePlayer.setDestroyed(false);
				createGame.setDestroyed(false);
				joinGame.setDestroyed(false);
				state = State.SELECT_GAMEMODE;
				input.resetTyped();
				input.keys[KeyEvent.VK_ESCAPE] = false;
			}
			editVar1.update();
			startGame.update();
			break;
		case IN_GAME:

			break;
		case PAUSED:
			break;
		default:
			break;

		}
		// Pause and Unpause:
		if (input.keys[KeyEvent.VK_ESCAPE] && state.equals(State.IN_GAME)) {
			input.keys[KeyEvent.VK_ESCAPE] = false;
			state = State.PAUSED;
		} else if (input.keys[KeyEvent.VK_ESCAPE] && state.equals(State.PAUSED)) {
			input.keys[KeyEvent.VK_ESCAPE] = false;
			state = State.IN_GAME;
		}
	}

	public void render(Render renderer, Graphics g, Main main) {
		Color oldColor = g.getColor();
		Font oldFont = g.getFont();
		switch (state) {
		case MAIN_MENU:
			g.setColor(Color.GRAY);
			g.fillRect(0, 0, Main.windowWidth * Main.windowScale, Main.windowHeight * Main.windowScale);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Verdana", Font.BOLD, 25));

			g.drawString("World of Cubes -" + Main.VERSION, (Main.windowWidth * Main.windowScale) / 3,
					(Main.windowHeight * Main.windowScale) / 3);
			g.setFont(new Font("Verdana", Font.BOLD, 15));
			g.drawString("Made with JAVA - Made By: Maxim Creanga", (Main.windowWidth * Main.windowScale) / 3,
					(Main.windowHeight * Main.windowScale) / 3 + 50);

			buttonPlay = new Button(100, 150, 150, 50, Color.BLUE);
			buttonPlay.render(g);

			buttonQuit = new Button(100, 225, 150, 50, Color.BLUE);
			buttonQuit.render(g);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Verdana", Font.BOLD, 25));
			g.drawString("Play", 145, 182);
			g.drawString("Quit", 145, 257);
			g.setFont(oldFont);
			g.setColor(oldColor);

			// frame.setSize(Main.getWindowWidth(),Main.getWindowHeight());
			break;
		case SELECT_GAMEMODE:
			g.setColor(Color.GRAY);
			g.fillRect(0, 0, Main.windowWidth * Main.windowScale, Main.windowHeight * Main.windowScale);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Verdana", Font.BOLD, 20));
			g.drawString("Select a gamemode: ", Main.getWindowWidth() / 3, 40);

			if (singlePlayer != null) {
				singlePlayer.render(g);
			}
			if (joinGame != null) {
				joinGame.render(g);
			}
			if (createGame != null) {
				createGame.render(g);
			}

			g.drawString("Singleplayer", (Main.getWindowWidth() / 2) - 95, (Main.getWindowHeight() / 3) + 30);
			g.drawString("Join Game", (Main.getWindowWidth() / 2) - 90, (Main.getWindowHeight() / 3) + 105);
			g.drawString("Create Game", (Main.getWindowWidth() / 2) - 95, (Main.getWindowHeight() / 3) + 180);
			g.setFont(oldFont);
			g.setColor(oldColor);
			break;

		case SINGLE_PLAYER:

			g.setColor(Color.GRAY);
			g.fillRect(0, 0, Main.windowWidth * Main.windowScale, Main.windowHeight * Main.windowScale);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Verdana", Font.BOLD, 40));
			g.drawString("Singleplayer: ", (Main.getWindowWidth() / 3) - 10, 55);
			if (editVar1 != null) {
				editVar1.render(g);
			}
			g.setFont(new Font("Monospaced", Font.BOLD, 40));
			g.drawString("Edit", (Main.getWindowWidth()) - 260, (Main.getWindowHeight() / 4) + 50);

			g.setFont(new Font("Verdana", Font.BOLD, 40));
			g.drawString("Name: " + Main.name, 50, (Main.getWindowHeight() / 4) + 50);

			startGame = new Button((Main.getWindowWidth() / 2) - 100, Main.getWindowHeight() - 100, 200, 75,
					Color.BLACK);
			startGame.render(g);
			g.setFont(new Font("Monospaced", Font.BOLD, 40));
			g.drawString("Start!", (Main.getWindowWidth() / 2) - 70, Main.getWindowHeight() - 50);

			break;
		case CREATE_GAME:
			g.setColor(Color.GRAY);
			g.fillRect(0, 0, Main.windowWidth * Main.windowScale, Main.windowHeight * Main.windowScale);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Verdana", Font.BOLD, 40));
			g.drawString("Create a new game", (Main.getWindowWidth() / 3) - 70, 55);

			g.setFont(new Font("Verdana", Font.BOLD, 40));
			g.drawString("Name: " + Main.name, 50, (Main.getWindowHeight() / 4) + 50);

			if (editVar1 != null) {
				editVar1.render(g);
			}
			g.setFont(new Font("Monospaced", Font.BOLD, 40));
			g.drawString("Edit", (Main.getWindowWidth()) - 260, (Main.getWindowHeight() / 4) + 50);

			if (startGame != null) {
				startGame.render(g);
			}
			g.setFont(new Font("Monospaced", Font.BOLD, 40));
			g.drawString("Start!", (Main.getWindowWidth() / 2) - 70, Main.getWindowHeight() - 50);
			break;
		case JOIN_GAME:
			g.setColor(Color.GRAY);
			g.fillRect(0, 0, Main.windowWidth * Main.windowScale, Main.windowHeight * Main.windowScale);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Verdana", Font.BOLD, 40));
			g.drawString("Join an existing game", (Main.getWindowWidth() / 3) - 10, 55);

			g.setFont(new Font("Verdana", Font.BOLD, 40));
			g.drawString("Name: " + Main.name, 50, (Main.getWindowHeight() / 4) + 50);

			if (editVar1 != null) {
				editVar1.render(g);
			}
			g.setFont(new Font("Monospaced", Font.BOLD, 40));
			g.drawString("Edit", (Main.getWindowWidth()) - 260, (Main.getWindowHeight() / 4) + 50);

			g.setFont(new Font("Verdana", Font.BOLD, 40));
			g.drawString("IP Address: " + Main.ipAddress, 50, (Main.getWindowHeight() / 2) + 50);
			if (editVar2 != null) {
				editVar2.render(g);
			}

			g.setFont(new Font("Monospaced", Font.BOLD, 40));
			g.drawString("Edit", (Main.getWindowWidth()) - 260, (Main.getWindowHeight() / 2) + 50);

			if (startGame != null) {
				startGame.render(g);
			}
			g.setFont(new Font("Monospaced", Font.BOLD, 40));
			g.drawString("Start!", (Main.getWindowWidth() / 2) - 70, Main.getWindowHeight() - 50);
			break;
		case IN_GAME:
			if (input.keys[KeyEvent.VK_ENTER]) {
				// Show list of players connected:
				g.setColor(Color.GRAY);
				g.fillRect(((Main.windowWidth * Main.windowScale) / 3) - 70, 20,
						((Main.windowWidth * Main.windowScale) / 3) + 80, (Main.windowHeight * Main.windowScale) / 3);
				g.setColor(Color.WHITE);
				g.setFont(new Font("Verdana", Font.BOLD, 20));
				g.drawString("Players Connected: ", Main.getWindowWidth() / 3 + 10, 40);
				int numOfPlayers = 1;

				for (Entity entity : main.level.getEntites()) {
					g.setColor(Color.WHITE);
					g.setFont(new Font("Verdana", Font.ITALIC, 20));
					if (entity instanceof Player && !(entity instanceof NetPlayer)) {
						g.drawString(
								"Player " + numOfPlayers + ": " + ((Player) entity).getUserName() + " HP: "
										+ ((Player) entity).getHealthPoints(),

								Main.getWindowWidth() / 3 + 10, 45 + (25 * numOfPlayers));
					} else if (entity instanceof NetPlayer) {
						g.drawString(
								"Player " + numOfPlayers + ": " + ((NetPlayer) entity).getUserName() + " HP: "
										+ ((NetPlayer) entity).getHealthPoints(),
								Main.getWindowWidth() / 3 + 10, 45 + (25 * numOfPlayers));

					}
					numOfPlayers++;
				}
				g.setColor(oldColor);
			}
			break;
		case PAUSED:
			g.setColor(Color.GRAY);
			g.fillRect(0, 0, Main.windowWidth * Main.windowScale, Main.windowHeight * Main.windowScale);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Verdana", Font.BOLD, 30));
			g.drawString("Game Paused. Press ESC to Resume", (Main.getWindowWidth() / 3)-140, 40);
			g.setColor(oldColor);
			break;

		default:

			break;
		}
	}
	public State getGuiState() {
		return state;
	}

	public void setGuiState(State state) {
		this.state = state;
	}
}
