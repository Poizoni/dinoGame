package com.mygdx.dino;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.dino.screens.MenuScreen;
import com.badlogic.gdx.Game;

public class DinoGame extends Game {
	public static final int WIDTH = 1200;
	public static final int HEIGHT = 300;

	public static Texture spriteSheet;
	public static SpriteBatch batch;

	@Override
	public void create () {
		spriteSheet = new Texture("spritesheet.png");
		batch = new SpriteBatch();

		setScreen(new MenuScreen(this));
	}
	
	@Override
	public void dispose () {
		spriteSheet.dispose();
		batch.dispose();
	}
}
