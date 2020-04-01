package com.mygdx.magegame.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.magegame.GameObjectInterface;

public class World {
    // наш игрок
    public Player player;
    // массив объектов на карте
    Array<GameObjectInterface> all_objects;
    // ширина и высота мира
    int width;
    int height;

    World()
    {
        width = 8;
        height = 5;
        createWorld();
    }

    private void createWorld(){
        player = new Player(new Vector2(6,2));
    }

    public Player getPlayer() {
        return player;
    }

    public Array<GameObjectInterface> getAll_objects() {
        return all_objects;
    }
}
