package com.mygdx.magegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class GameScreen implements Screen {
    final MageGame game;

    Array<GameObject> objects;
    TileSet tileSet;

    public GameScreen(final MageGame game){
        this.game = game;
        objects = new Array<>();
        tileSet = new TileSet(Gdx.files.internal("spriteset_0.png"), 32);

        // Тестовое создание объектов
        for(int i = 0; i< 10; i++){
            for (int j=0;j<10;j++){
                GameObject go = new GameObject(tileSet, 0, (i*32)-100, (j*32)-100);
                objects.add(go);
            }
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f,0.2f,0.2f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        Iterator<GameObject> iter = objects.iterator();
        while (iter.hasNext()){
            GameObject current_object = iter.next();
            current_object.object_sprite.draw(game.batch);
        }
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
