package com.mygdx.magegame;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class TextObject implements GameObjectInterface{
    boolean is_camera_oriented; // Должен ли объект отрисовываться с поправкой на камеру
    String text;
    Vector2 world_pos;

    public TextObject(int x, int y, String text, boolean is_camera_oriented) {
        this.text = text;
        this.is_camera_oriented = is_camera_oriented;
        world_pos = new Vector2();
        set_pos(x, y);
    }

    public void set_pos(int x, int y){
        world_pos.set(x, y);
    }

    public void set_text(String text){
        this.text = text;
    }

    @Override
    public void draw(SpriteBatch batch, BitmapFont font, OrthographicCamera camera) {
        if (is_camera_oriented){
            font.draw(batch,
                    text,
                    world_pos.x + camera.position.x,
                    world_pos.y + camera.position.y);
        }
        else {
            font.draw(batch,
                    text,
                    world_pos.x,
                    world_pos.y);
        }
    }
}
