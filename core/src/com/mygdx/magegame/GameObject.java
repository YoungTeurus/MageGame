package com.mygdx.magegame;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class GameObject {
    Sprite object_sprite; // Спрайт объекта

    int world_x;
    int world_y;
    TileSet parent_tileSet;

    int id;

    public GameObject(TileSet tileSet, int id, int x, int y){
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
        set_id(id);
    }

    public void set_coords(int x, int y){
        world_x = x;
        world_y = y;
        object_sprite.setPosition(world_x, world_y);
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

        object_sprite = new Sprite(parent_tileSet.texture, srcX, srcY, parent_tileSet.size, parent_tileSet.size);
        object_sprite.setPosition(world_x, world_y);
    }
}
