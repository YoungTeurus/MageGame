package com.mygdx.magegame;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TextObject implements GameObjectInterface{
    boolean is_camera_oriented; // Должен ли объект отрисовываться с поправкой на камеру
    String text;

    int world_x;
    int world_y;

    public TextObject(int x, int y, String text, boolean is_camera_oriented) {
        this.text = text;
        this.is_camera_oriented = is_camera_oriented;
        set_pos(x, y);
    }

    public void set_pos(int x, int y){
        world_x = x;
        world_y = y;
    }

    public void set_text(String text){
        this.text = text;
    }

    @Override
    public void draw(SpriteBatch batch, BitmapFont font, OrthographicCamera camera) {
        if (is_camera_oriented){
            font.draw(batch,
                    text,
                    world_x + camera.position.x,
                    world_y + camera.position.y);
        }
        else {
            font.draw(batch,
                    text,
                    world_x,
                    world_y);
        }
    }
}
