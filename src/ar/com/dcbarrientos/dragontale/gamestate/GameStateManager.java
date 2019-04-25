package ar.com.dcbarrientos.dragontale.gamestate;

import java.awt.Graphics2D;

public class GameStateManager {
	private GameState[] gameStates;
	private int currentState;

	public static final int NUMGAMESTATES = 3;
	public static final int MENUSTATE = 0;
	public static final int LEVEL1STATE = 1;
	public static final int GAMEOVER = 2;

	public GameStateManager() {
		gameStates = new GameState[NUMGAMESTATES];

		currentState = MENUSTATE;
		loadState(currentState);
	}

	private void loadState(int state) {
		if (state == MENUSTATE)
			gameStates[state] = new MenuState(this);
		if (state == LEVEL1STATE)
			gameStates[state] = new Level1State(this);
		if (state == GAMEOVER)
			gameStates[GAMEOVER] = new GameOverState(this);
	}

	private void unloadState(int state) {
		gameStates[state] = null;
	}

	public void setState(int state) {
		currentState = state;
		unloadState(currentState);
		loadState(currentState);
	}

	public void update() {
		try {
			gameStates[currentState].update();
		} catch (Exception e) {

		}
	}

	public void draw(Graphics2D g) {
		try {
			gameStates[currentState].draw(g);
		} catch (Exception e) {

		}
	}

	public void keyPressed(int e) {
		gameStates[currentState].keyPressed(e);
	}

	public void keyReleased(int k) {
		if(gameStates[currentState] == null)
			System.out.println("es null: " + currentState);
		gameStates[currentState].keyReleased(k);
	}
}
