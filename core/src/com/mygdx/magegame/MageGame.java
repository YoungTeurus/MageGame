package com.mygdx.magegame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MageGame extends Game {
	SpriteBatch batch;
	BitmapFont font;

	@Override
	public void create() {
		batch = new SpriteBatch();

		font = new BitmapFont();
		this.setScreen(new MainMenuScreen(this));
	}

	public void render(){
		super.render();
	}

	public void dispose(){
		batch.dispose();
		font.dispose();
	}
}
