package com.mygdx.magegame.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.magegame.objects.GameObject;
import com.mygdx.magegame.objects.Player;


public class World extends Stage {
    // наш игрок
    public Player player;
    // массив объектов на карте
    public Array<GameObject> texts;
    // ширина и высота мира
    public int worldWidth;
    public int worldHeight;
    // Штука для отрисовки текстов
    public BitmapFont font;

    public Actor selectedActor;
    // чтобы каждый раз их не создавать
    private Vector3 mouseCoords3 = new Vector3(0,0, 0);
    private Vector2 mouseCoords2 = new Vector2(0,0);

    public World(int worldWidth, int worldHeight)
    {
        super(new ExtendViewport(worldWidth, worldHeight));

        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        font = new BitmapFont();
        texts = new Array<>();
        createWorld();
    }

    private void createWorld(){
        player = new Player(this, 0,0,0);
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean keyDown(int keyCode) { return super.keyDown(keyCode); }

    @Override
    public boolean keyUp(int keyCode) {
        return super.keyUp(keyCode);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        boolean b = super.touchDown(screenX, screenY, pointer, button);
        mouseCoords3.x = screenX;
        mouseCoords3.y = screenY;
        getCamera().unproject(mouseCoords3);
        mouseCoords2.x = mouseCoords3.x;
        mouseCoords2.y = mouseCoords3.y;
        moveSelected(mouseCoords2);
        return true;
    }

    private void moveSelected(Vector2 mouseCoords){
        if(selectedActor != null && selectedActor instanceof Player)
            ((Player)selectedActor).handleMouseInput(mouseCoords);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        super.touchUp(screenX, screenY, pointer, button);
        resetSelected();
        return true;
    }

    private void resetSelected(){
        if(selectedActor != null && selectedActor instanceof Player)
            ((Player)selectedActor).resetWay();
    }


    @Override
    public Actor hit(float x, float y, boolean touchable) {
        Actor  actor  = super.hit(x,y,touchable);
        //если выбрали актёра
        if(actor != null)
            //запоминаем
            selectedActor = actor;
        return actor;
    }

}
