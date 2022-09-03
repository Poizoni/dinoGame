package com.mygdx.dino;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(DinoGame.WIDTH, DinoGame.HEIGHT);
		config.setWindowIcon("icon.png");
		config.setTitle("DinoGame");
		config.setForegroundFPS(60);
		config.setResizable(false);
		config.useVsync(true);
		new Lwjgl3Application(new DinoGame(), config);
	}
}
