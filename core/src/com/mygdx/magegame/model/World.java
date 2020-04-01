package com.mygdx.magegame.model;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.mygdx.magegame.legasy.GameObjectInterface;
import com.mygdx.magegame.objects.GameObject;
import com.mygdx.magegame.objects.Player;

public class World extends Stage {
    // наш игрок
    public Player player;
    // массив объектов на карте
    Array<GameObject> all_objects;
    // ширина и высота мира
    public int worldWidth;
    public int worldHeight;
    // Штука для отрисовки текстов
    public BitmapFont font;

    public World(int worldWidth, int worldHeight)
    {
        super(new ScalingViewport(Scaling.fit, worldWidth, worldHeight));
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        font = new BitmapFont();
        createWorld();
    }

    private void createWorld(){
        player = new Player(this, 0,0,0);
    }

    public Player getPlayer() {
        return player;
    }

    public Array<GameObject> getAll_objects() {
        return all_objects;
    }
}
