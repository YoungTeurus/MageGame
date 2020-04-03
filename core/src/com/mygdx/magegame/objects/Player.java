package com.mygdx.magegame.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.mygdx.magegame.TileSet;
import com.mygdx.magegame.world.World;

import java.util.HashMap;
import java.util.Map;

/**
Класс Player.
 Для клиента: обрабатывает нажатия с клавиатуры, управление мышкой осуществляется миром
 Для сетевой игры: нажатия клавиатуры обрабатываем здесь, записывая в массив, что была нажата кнопка, игрок дальше сам
 обработает нужную функцию,
*/
public class Player extends GameObject {

    // скорость движения, мб её будем менять от каких-нибудь тапочек, так что не константа
    public static final float SPEED = 2f;
    // размер конст
    public static final float SIZE = 1.5f; // В размерах одной клетки (32*32)

    //состояние
    public enum State {
        NONE, WALKING, DEAD
    }
    enum Keys {
        LEFT, RIGHT, UP, DOWN
    }
    static Map<Keys, Boolean> direction = new HashMap<Keys, Boolean>();
    static void initDirection(){
        direction.put(Keys.LEFT, false);
        direction.put(Keys.RIGHT, false);
        direction.put(Keys.UP, false);
        direction.put(Keys.DOWN, false);
    };

    //используется для вычисления движения
    Vector2 velocity = new Vector2();
    //конечная точка пути
    Vector3 endPoint = new Vector3();

    // угол, по которому движется
    float angleDirection;

    TextureRegion object_texture_region; // То, что рисуется
    static final TileSet parent_tileSet = new TileSet(Gdx.files.internal("spriteset_mages.png"), 32); // Откуда берутся текстурки
    // угол, куда смотрит текстура
    float angleTexture;


    State state; //текущее состояние
    int type; // Тип мага: в данный момент от 0 до 3.

    public Player(World world, int x, int y, int type){
        super(world);
        set_pos(x, y);
        this.type = type;
        set_texture();
        setBounds(position.x, position.y, SIZE, SIZE);
        addListener(
                new InputListener(){
                    @Override
                    public boolean keyDown(InputEvent event, int keycode) {
                        handleInput(keycode);
                        processInput();
                        return true;
                    }
                    @Override
                    public boolean keyUp(InputEvent event, int keycode) {
                        if ( keycode == Input.Keys.W )
                            upReleased();
                        if ( keycode == Input.Keys.S )
                            downReleased();
                        if ( keycode == Input.Keys.A )
                            leftReleased();
                        if ( keycode == Input.Keys.D )
                            rightReleased();
                        processInput();
                        return true;
                    }
                    @Override
                    public  boolean touchDown (InputEvent event, float x, float y, int pointer, int button){
                        // клик на нашего игрока
                        return true;
                    }
                    @Override
                    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                        // если отжали кнопочку на нашем игроке никуда не идем
	        	        resetWay();
                    }
                }
        );
        initDirection();
    }

    private void handleInput(int keycode) {
        if ( keycode == Input.Keys.W )
            upPressed();
        if ( keycode == Input.Keys.S )
            downPressed();
        if ( keycode == Input.Keys.A )
            leftPressed();
        if ( keycode == Input.Keys.D )
            rightPressed();
    }

    public Vector3 getPosition() {
        return position;
    }

    public void handleMouseInput(Vector2 mouseCoords){
        Gdx.app.log("Velos",mouseCoords.toString() + position.toString()  + mouseCoords.angle());
        endPoint.x = mouseCoords.x;
        endPoint.y = mouseCoords.y;
        mouseCoords.sub(position.x + SIZE/2, position.y + SIZE/2);
        if(state != State.WALKING)
            state = State.WALKING;
        else // если шли до этого
            resetVelocity();
        velocity.x += SPEED * Math.cos(mouseCoords.angle()/180.0*3.14);
        velocity.y += SPEED * Math.sin(mouseCoords.angle()/180.0*3.14);
        updateAngleDirection();
        Gdx.app.log("Velos", velocity.toString());
    }

    public void resetWay(){
        rightReleased();
        leftReleased();
        downReleased();
        upReleased();
    }

    public void resetVelocity(){
        getVelocity().x = 0;
        getVelocity().y = 0;
    }

    private void processInput() {
        if (direction.get(Keys.LEFT))
            getVelocity().x = -SPEED;
        if (direction.get(Keys.RIGHT))
            getVelocity().x = SPEED;
        if (direction.get(Keys.UP))
            getVelocity().y = SPEED;
        if (direction.get(Keys.DOWN))
            getVelocity().y = -SPEED;
        if ((direction.get(Keys.LEFT) && direction.get(Keys.RIGHT)) ||
                (!direction.get(Keys.LEFT) && (!direction.get(Keys.RIGHT))))
            getVelocity().x = 0;
        if ((direction.get(Keys.UP) && direction.get(Keys.DOWN)) ||
                (!direction.get(Keys.UP) && (!direction.get(Keys.DOWN))))
            getVelocity().y = 0;
        updateAngleDirection();
    }

    private void updateAngleDirection() { angleDirection = getVelocity().angle(); }


    public Vector2 getVelocity() { return velocity; }

    public void set_texture(){

        int srcX = type*parent_tileSet.size;
        int srcY = 0;

        object_texture_region = new TextureRegion(parent_tileSet.texture,
                srcX, srcY, parent_tileSet.size, parent_tileSet.size);
        setBounds(position.x, position.y,
                2, 2);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // если смотрим не туда, куда идем
        if(angleTexture!= angleDirection)
        {
            angleTexture = angleDirection;
        }
        batch.draw(object_texture_region, getX(),getY(),
                getOriginX(), getOriginY(), getWidth(), getHeight(),
                getScaleX(), getScaleY(), angleTexture);

    }

    @Override
    public void act(float delta) {
        super.act(delta);
        updatePosition(delta);
        // Gdx.app.log("Player coord", position.toString() + velocity.toString());
    }

    public Actor hit(float x, float y, boolean touchable) {
        //Процедура проверки. Если точка в прямоугольнике актёра, возвращаем актёра.
        return x > 0 && x < getWidth() && y> 0 && y < getHeight()? this:null;
    }

    private void updatePosition(float delta) {
        if(state == State.WALKING)
        {
            this.position.add(velocity.x*delta, velocity.y*delta, 0);
            this.setPosition(position.x, position.y);
            if(position.epsilonEquals(endPoint, SIZE/2)) {
                state = State.NONE;
                resetVelocity();
            }
        }
    }


    public void leftPressed() {
        direction.get(direction.put(Keys.LEFT, true));
    }

    public void rightPressed() {
        direction.get(direction.put(Keys.RIGHT, true));
    }

    public void upPressed() {
        direction.get(direction.put(Keys.UP, true));
    }

    public void downPressed() {
        direction.get(direction.put(Keys.DOWN, true));
    }

    public void leftReleased() {
        direction.get(direction.put(Keys.LEFT, false));
    }

    public void rightReleased() {
        direction.get(direction.put(Keys.RIGHT, false));
    }

    public void upReleased() {
        direction.get(direction.put(Keys.UP, false));
    }

    public void downReleased() {
        direction.get(direction.put(Keys.DOWN, false));
    }
}
