package com.mygdx.magegame.legasy;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public interface GameObjectInterface {
    void draw(SpriteBatch batch, BitmapFont font, OrthographicCamera camera); // Отрисовывает объект в мире

    void set_pos(int x, int y); // Устанавилвает позицию вектора
}
