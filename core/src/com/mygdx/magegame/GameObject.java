package com.mygdx.magegame;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class GameObject {
    Sprite object_sprite; // Спрайт объекта

    int world_x;
    int world_y;

    int id;

    public GameObject(TileSet tileSet, int id){
        // Создаёт квадратный объект из текстуры с размером стороны - size.
        // Текстура выбирается по index-у

        // Координаты текстурки в тайлсете вычисляются по id
        // Каждый тайлсет может вмещать до 256 тайлов со следующими id:
        // 0   1  2 ... 15
        // 16 17 18 ...
        // ...
        int srcX = id%16 * tileSet.size;
        int srcY = id/16 * tileSet.size;

        this.id = id;

        object_sprite = new Sprite(tileSet.texture, srcX, srcY, tileSet.size, tileSet.size);

        set_coords(0, 0);
    }

    public GameObject(TileSet tileSet, int id, int x, int y){
        // Создаёт квадратный объект из текстуры с размером стороны - size.
        // Текстура выбирается по index-у

        // Координаты текстурки в тайлсете вычисляются по id
        // Каждый тайлсет может вмещать до 256 тайлов со следующими id:
        // 0   1  2 ... 15
        // 16 17 18 ...
        // ...
        int srcX = id%16 * tileSet.size;
        int srcY = id/16 * tileSet.size;

        this.id = id;

        object_sprite = new Sprite(tileSet.texture, srcX, srcY, tileSet.size, tileSet.size);

        set_coords(x, y);
    }

    public void set_coords(int x, int y){
        world_x = x;
        world_y = y;
        object_sprite.setPosition(world_x, world_y);
    }
}
