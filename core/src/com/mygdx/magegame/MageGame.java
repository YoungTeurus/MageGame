package com.mygdx.magegame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.magegame.screen.MainMenuScreen;

public class MageGame extends Game {

	SpriteBatch getBatch;
	BitmapFont font;
	Skin skin;


	@Override
	public void create () {
		getBatch = new SpriteBatch();
		font = new BitmapFont();
		skin = new Skin(Gdx.files.internal("UI/uiskin.json"));

		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		getBatch.dispose();
		font.dispose();
	}

	public SpriteBatch getBatch() {
		return getBatch;
	}

	public BitmapFont getFont() {
		return font;
	}

	public Skin getSkin() {
		return skin;
	}
}