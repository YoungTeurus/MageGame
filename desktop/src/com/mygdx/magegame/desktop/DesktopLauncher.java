package com.mygdx.magegame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.magegame.MageGame;
import static com.mygdx.magegame.Consts.window_h;
import static com.mygdx.magegame.Consts.window_w;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Drop";
		cfg.width = window_w;
		cfg.height = window_h;
		new LwjglApplication(new MageGame(), cfg);
	}
}
