package com.mygdx.magegame;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface GameObjectInterface {
    void draw(SpriteBatch batch, BitmapFont font, OrthographicCamera camera);
}
