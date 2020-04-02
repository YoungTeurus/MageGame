package com.mygdx.magegame.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.magegame.model.World;

public class TextObject extends GameObject {
    boolean is_camera_oriented; // Должен ли объект отрисовываться с поправкой на камеру
    String text;

    public TextObject(World world, int x, int y, String text, boolean is_camera_oriented) {
        super(world);
        this.text = text;
        this.is_camera_oriented = is_camera_oriented;
        set_pos(x, y);
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        if (is_camera_oriented){
            parent_world.font.draw(batch,
                    text,
                    position.x+parent_world.getCamera().position.x,
                    position.y+parent_world.getCamera().position.y);
        }
        else {
            parent_world.font.draw(batch,
                    text,
                    position.x,
                    position.y);
        }
    }

    public void set_text(String text){
        this.text = text;
    }

    public void act(){

    }
}
