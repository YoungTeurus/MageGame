package com.mygdx.magegame.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.mygdx.magegame.TileSet;
import com.mygdx.magegame.collision.CollisionEvent;
import com.mygdx.magegame.collision.CollisionListener;
import com.mygdx.magegame.objects.tiles.ActiveOnPlayerTouch;
import com.mygdx.magegame.world.TiledLayer;
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
    public static final float SIZE = 1f; // В размерах одной клетки (32*32)
    private static final float EPS = SIZE / 15;

    //состояние
    public enum State {
        NONE, WALKING, STOPED
    }
    // enum Keys {LEFT, RIGHT, UP, DOWN}
    enum Keys {STOP, ABILITY_1, ABILITY_2, ABILITY_3, ABILITY_4, ABILITY_5, ABILITY_6}
    static Map<Keys, Boolean> direction = new HashMap<Keys, Boolean>();
    static void initDirection(){
        direction.put(Keys.STOP, false);
        direction.put(Keys.ABILITY_1, false);
        direction.put(Keys.ABILITY_2, false);
        direction.put(Keys.ABILITY_3, false);
        direction.put(Keys.ABILITY_4, false);
        direction.put(Keys.ABILITY_5, false);
        direction.put(Keys.ABILITY_6, false);
    };

    //используется для вычисления движения
    Vector2 velocity = new Vector2();
    //конечная точка пути
    Vector3 endPoint = new Vector3();
    // предыдущая точка из которой перешли
    Vector3 previosPoint = new Vector3();
    // используется для нахождения угла под которым коснулись
    Vector2 collisionVector = new Vector2();
    // каким углом модельки коснулись
    float collisionAngle;
    // угол, по которому движется
    float angleDirection;

    TextureRegion object_texture_region; // То, что рисуется
    static final TileSet parent_tileSet = new TileSet(1); // Откуда берутся текстурки

    State state; //текущее состояние
    int type; // Тип мага: в данный момент от 0 до 3.

    public Player(World world, int x, int y, int z, int type){
        super(world);
        set_pos(x, y, z);
        this.type = type;
        set_texture();
        setRotation(270);
        setOrigin(SIZE/2, SIZE/2);
        setBounds(position.x, position.y, SIZE, SIZE);
        addListener(new CollisionListener(){
                        @Override
                        public void processCollision(GameObject gameObject, CollisionEvent.CollisionObjectType type) {
                            onCollision(gameObject);
                        }
                    }
        );
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
                        if ( keycode == Input.Keys.S )
                            StopReleased();
                        //processInput();
                        return true;
                    }
                    @Override
                    public  boolean touchDown (InputEvent event, float x, float y, int pointer, int button){
                        // клик на нашего игрока
                        return true;
                    }
                    @Override
                    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                        // если отжали кнопочку на нашем игроке
                    }
                }
        );
        initDirection();
    }

    @Override
    public void onCollision(GameObject gameObject) {

        //Gdx.app.log("PLAYER", "COLLISION WITH"+gameObject.getX()+" " + gameObject.getY());
        // находим вектор из центра обьекта, с кот столкнулись до центра обьекта который столкнулся

        // обработка сстолкновения со статическим тайлом
        if(gameObject instanceof MapTile)
        {
            // если тайл непроходимый
            if(((MapTile) gameObject).is_passable == false)
            {
                collisionVector.x = getX() + getWidth()/2 - gameObject.getX() - gameObject.getWidth()/2;
                collisionVector.y = getY() + getHeight()/2 - gameObject.getY() - gameObject.getHeight()/2;
                //Gdx.app.log("PLAYER", "COLLISION ANGLE" + collisionVector.angle());
                collisionAngle = collisionVector.angle(); // угол с которого пересечен обьект (0 справа)
                // зашли сверху
                if(collisionAngle > 45 && collisionAngle <= 135){
                    if(velocity.y != 0)
                        this.position.y = gameObject.getY()+gameObject.getHeight() + EPS;
                    this.setColor(255,0,0,1);
                    velocity.y = 0;
                }
                // зашли слева
                else if(collisionAngle > 135 && collisionAngle <= 225){
                    if(velocity.x != 0)
                        this.position.x = gameObject.getX() - this.getWidth() - EPS;
                    this.setColor(255,0,0,1);
                    velocity.x = 0;
                }
                // зашли снизу
                else if(collisionAngle > 225 && collisionAngle <= 315){
                    if(velocity.y != 0)
                        this.position.y = gameObject.getY() - this.getHeight() - EPS;
                    this.setColor(255,0,0,1);
                    velocity.y = 0;
                }
                // зашли справа
                else if(collisionAngle > 315 || collisionAngle <= 45){
                    if(velocity.x != 0)
                        this.position.x = gameObject.getX()+ gameObject.getWidth() + EPS;
                    this.setColor(255,0,0,1);
                    velocity.x = 0;
                }
                this.setPosition(position.x, position.y);
                //Gdx.app.log("PLAYER", "NEW POS " + position.toString());
            }
            if(((MapTile) gameObject).getId() == 96)
            {
                Gdx.app.log("PLAYER", " contact with" + ((MapTile)gameObject).toString());
                ((MapTile) gameObject).active(this, MapTile.Functions.RAISEPLAYER.getFunc());
                Gdx.app.log("PLAYER", " UP " + parent_world.getCurrent_z() + " " + this.getLayer() + " ");
            }
        }

    }

    private void handleInput(int keycode) {
        if ( keycode == Input.Keys.S )
            StopPressed();
    }



    public void handleMouseInput(Vector2 mouseCoords){
        //Gdx.app.log("Player",mouseCoords.toString() + position.toString()  + mouseCoords.angle());
        endPoint.x = mouseCoords.x;
        endPoint.y = mouseCoords.y;
        mouseCoords.sub(getX() + getOriginX(), getY() + getOriginY());
        if(state != State.WALKING)
            state = State.WALKING;
        else // если шли до этого
            resetVelocity();
        velocity.x += SPEED * Math.cos(mouseCoords.angle()/180.0*3.14);
        velocity.y += SPEED * Math.sin(mouseCoords.angle()/180.0*3.14);
        updateAngleDirection();
    }

    public void resetVelocity(){
        getVelocity().x = 0;
        getVelocity().y = 0;
    }

    private void processInput() {
        if (direction.get(Keys.STOP)){
            resetVelocity();
            state = State.STOPED;
        }
    }

    private void updateAngleDirection() { angleDirection = getVelocity().angle(); }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        // если смотрим не туда, куда идем
        if(getRotation() != angleDirection)
        {
            setRotation(angleDirection);
        }
        batch.setColor(this.getColor());
        //Gdx.app.log("PLAYER", "Rotation "+ getRotation());
        Texture t = object_texture_region.getTexture();
        batch.draw(object_texture_region, getX(), getY(),
                getOriginX(), getOriginY(), getWidth(), getHeight(),
                getScaleX(), getScaleY(), getRotation());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        this.setColor(255,255,255,1);
        // попробуем подвинуть
        updatePosition(delta);
        GameObject obj = parent_world.collisionDetector.checkCollisions(this);
        if( obj != null ){
            onCollision(obj);
        }
        //getX()+getHeight()/4;

        Group temp = parent_world.getMap().get_layer(this.getLayer()-1);
        Actor t;
        if((t = temp.hit(getX()+getWidth(), getY()+ getHeight(), false))!= null){
            if(t instanceof MapTile) {
                if (((MapTile) t).is_solid == false || ((MapTile) t).getLayer() != this.getLayer()-1) {
                    this.position.z -= 1;
                    parent_world.setCurrent_z(this.getLayer()-1);
                    Gdx.app.log("PLAYER", " DOWN " + parent_world.getCurrent_z() + " " + this.getLayer() + " " + ((MapTile) t).getLayer());
                }
            }
        }
        else
        {
            this.position.z -= 1;
            parent_world.setCurrent_z(this.getLayer()-1);
            Gdx.app.log("PLAYER", " DOWN " + parent_world.getCurrent_z() + " " + this.getLayer() + " ");
        }


         // здесь проверка
        /* if(endPoint.epsilonEquals(getX()+getOriginX(), getY()+getOriginY(), 0, EPS)) {
                state = State.NONE;
                resetVelocity();*/

    }

    public Actor hit(float x, float y, boolean touchable) {
        //Процедура проверки. Если точка в прямоугольнике актёра, возвращаем актёра.
        return x > 0 && x < getWidth() && y> 0 && y < getHeight()? this:null;
    }

    private void updatePosition(float delta) {
        if(state == State.WALKING)
        {
            //Gdx.app.log("PLAYER",  position.toString()+" "+endPoint.toString() + " " + velocity.toString());
            this.setPreviosPoint(position);
            this.position.add(velocity.x*delta, velocity.y*delta, 0);
            this.setPosition(position.x, position.y);

            if(endPoint.epsilonEquals(getX()+getOriginX(), getY()+getOriginY(), 0, EPS)) {
                state = State.NONE;
                resetVelocity();
            }
        }
    }


    public void StopPressed() {
        direction.get(direction.put(Keys.STOP, true));
    }

    public void StopReleased() {
        direction.get(direction.put(Keys.STOP, false));
    }


    public void set_texture(){
        int srcX = type*parent_tileSet.size;
        int srcY = 0;

        object_texture_region = new TextureRegion(parent_tileSet.texture,
                srcX, srcY, parent_tileSet.size, parent_tileSet.size);
        setBounds(position.x, position.y,
                2, 2);
    }

    public  Vector2     getVelocity()                           {return velocity;}
    public  State       getState()                              {return state;}
    public  Vector3     getPreviosPoint()                       {return previosPoint;}
    public  Vector3     getPosition()                           {return position;}

    public  void        setPreviosPoint(Vector3 previosPoint)   {this.previosPoint = previosPoint;}
    public  void        setVelocity(Vector2 velocity)           {this.velocity = velocity;}
}
