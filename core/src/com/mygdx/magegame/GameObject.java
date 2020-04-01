package com.mygdx.magegame;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameObject implements GameObjectInterface {
    TextureRegion object_texture_region;
    boolean is_camera_oriented; // Должен ли объект отрисовываться с поправкой на камеру

    int world_x;
    int world_y;
    TileSet parent_tileSet;

    int id;

    public GameObject(TileSet tileSet, int id, int x, int y, boolean is_camera_oriented){
        // Создаёт квадратный объект из текстуры с размером стороны - size.
        // Текстура выбирается по index-у

        // Координаты текстурки в тайлсете вычисляются по id
        // Каждый тайлсет может вмещать до 256 тайлов со следующими id:
        // 0   1  2 ... 15
        // 16 17 18 ...
        // ...

        parent_tileSet = tileSet;
        world_x = x;
        world_y = y;
        this.is_camera_oriented = is_camera_oriented;
        set_id(id);
    }

    public void set_coords(int x, int y){
        world_x = x;
        world_y = y;
    }

    public void set_id(int new_id){
        // Координаты текстурки в тайлсете вычисляются по id
        // Каждый тайлсет может вмещать до 256 тайлов со следующими id:
        // 0   1  2 ... 15
        // 16 17 18 ...
        // ...

        id = new_id;
        int srcX = id%16 * parent_tileSet.size;
        int srcY = id/16 * parent_tileSet.size;

        object_texture_region = new TextureRegion(parent_tileSet.texture,srcX, srcY, parent_tileSet.size, parent_tileSet.size);
    }

    @Override
    public void draw(SpriteBatch batch, BitmapFont font, OrthographicCamera camera) {
        if (is_camera_oriented){
            batch.draw(object_texture_region,
                    world_x+camera.position.x,
                    world_y+camera.position.y);
        }
        else {
            batch.draw(object_texture_region,
                    world_x,
                    world_y);
        }
    }
}
