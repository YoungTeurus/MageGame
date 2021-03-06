package com.mygdx.magegame.objects;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.mygdx.magegame.TileSet;
import com.mygdx.magegame.collision.CollisionEvent;
import com.mygdx.magegame.collision.CollisionListener;
import com.mygdx.magegame.mechanics.Spawned;
import com.mygdx.magegame.objects.additional.AnimatedTextureRegion;
import com.mygdx.magegame.mechanics.Dropable;
import com.mygdx.magegame.world.World;

import java.util.HashMap;
import java.util.Map;

/**
Класс Player.
 Для клиента: обрабатывает нажатия с клавиатуры, управление мышкой осуществляется миром
 Для сетевой игры: нажатия клавиатуры обрабатываем здесь, записывая в массив, что была нажата кнопка, игрок дальше сам
 обработает нужную функцию,
*/
public class Player extends MovableObject implements Dropable, Spawned {

    // скорость движения, мб её будем менять от каких-нибудь тапочек, так что не константа
    // размер конст
    public static final float SIZE = 1f; // В размерах одной клетки (32*32)
    private static final float EPS = SIZE / 15;
    private static final float SPAWN_TIME = 10f;
    private static final int SPAWN_ID = 0;

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


    // предыдущая точка из которой перешли
    Vector3 previosPoint = new Vector3();
    // используется для нахождения угла под которым коснулись
    Vector2 collisionVector = new Vector2();
    private Vector2 tempMouseCoords = new Vector2();
    // каким углом модельки коснулись
    float collisionAngle;
    // угол, по которому движется
    float angleDirection;
    // угол, куда смотрит
    float angleGazeDirection;

    TextureRegion object_texture_region_body; // То, что рисуется
    TextureRegion object_texture_region_head;
    //TextureRegion object_texture_region_hands;
    AnimatedTextureRegion object_texture_region_hands;
    TextureRegion object_texture_region_legs;
    static final TileSet parent_tileSet = new TileSet(1); // Откуда берутся текстурки

    State state; //текущее состояние
    int type; // Тип мага: в данный момент от 0 до 3.

    // Временно public
    public int max_hp; // Максимальный запас жизней мага
    public int current_hp; // Текущий запас жизней мага
    public int max_mp; // Максимальный запас маны мага
    public int current_mp; // Текущий запас маны мага

    public Player(World world, int x, int y, int z, int type){
        super(world);
        set_pos(x, y, z);
        this.type = type;
        set_stats();
        setSPEED(2f);
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

    private void handleInput(int keycode) {
        if ( keycode == Input.Keys.S )
            StopPressed();
    }

    public void handleMouseInput(Vector2 mouseCoords){
        //Gdx.app.log("Player",mouseCoords.toString() + position.toString()  + mouseCoords.angle());
        if(state != State.WALKING)
            state = State.WALKING;
        else // если шли до этого
            resetVelocity();
        moveTo(mouseCoords.x, mouseCoords.y);

        updateAngleDirection();

        // Тупо для теста
        // TODO: сделать запуск изменения текстур в другом месте
        object_texture_region_hands.setFreeze(false);
        object_texture_region_hands.setFrame(0);
    }

    public void resetVelocity(){
        getVelocity().x = 0;
        getVelocity().y = 0;

        // Тупо для теста
        // TODO: сделать остановку изменения текстур в другом месте
        object_texture_region_hands.setFreeze(true);
        object_texture_region_hands.setFrame(0);
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
        if(getRotation() != angleDirection){
            setRotation(angleDirection);
        }
        batch.setColor(this.getColor());
        // Texture t = object_texture_region_body.getTexture();
        batch.draw(object_texture_region_legs, getX(), getY(),
                getOriginX(), getOriginY(), getWidth(), getHeight(),
                getScaleX(), getScaleY(), getRotation());
        batch.draw(object_texture_region_hands, getX(), getY(),
                getOriginX(), getOriginY(), getWidth(), getHeight(),
                getScaleX(), getScaleY(), getRotation());
        object_texture_region_hands.next();
        batch.draw(object_texture_region_body, getX(), getY(),
                getOriginX(), getOriginY(), getWidth(), getHeight(),
                getScaleX(), getScaleY(), getRotation());
        batch.draw(object_texture_region_head, getX(), getY(),
                getOriginX(), getOriginY(), getWidth(), getHeight(),
                getScaleX(), getScaleY(), angleGazeDirection);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        // обрабатываем падение, если вышли с твердого тайла
        processDrop();
    }

    public void processDrop() {
        if(parent_world.dropController.checkFloorUnderObject(this) == false){
            parent_world.dropController.dropGameObject(this);
        }
    }

    @Override
    public void onCollision(GameObject gameObject) {

        //Gdx.app.log("PLAYER", "COLLISION WITH"+gameObject.getX()+" " + gameObject.getY());
        // обработка сстолкновения со статическим тайлом
        if(gameObject instanceof MapTile)
        {
            // если тайл непроходимый
            if(((MapTile) gameObject).is_passable == false)
            {
                // находим вектор из центра обьекта, с кот столкнулись до центра обьекта который столкнулся
                collisionVector.x = getX() + getWidth()/2 - gameObject.getX() - gameObject.getWidth()/2;
                collisionVector.y = getY() + getHeight()/2 - gameObject.getY() - gameObject.getHeight()/2;
                //Gdx.app.log("PLAYER", "COLLISION ANGLE" + collisionVector.angle());
                collisionAngle = collisionVector.angle(); // угол с которого пересечен обьект (0 справа)
                handleCollisionAngle(gameObject);
                this.setPosition(position.x, position.y);
                //Gdx.app.log("PLAYER", "NEW POS " + position.toString());
            }
            // если этот тайл лестница
            if(((MapTile) gameObject).getId() == 96)
            {
                collisionVector.x = gameObject.getX() + gameObject.getWidth()/2;
                collisionVector.y = gameObject.getY() + gameObject.getHeight()/2;
                // если наш центр пересек границу лестницы, тогда поднимемся на нее
                //Gdx.app.log("PLAYER", collisionVector.toString());
                if(collisionVector.dst(getX()+getWidth()/2, getY()+getHeight()/2) <= SIZE/1.7f){
                    //Gdx.app.log("PLAYER", " contact with" + ((MapTile)gameObject).toString());
                    ((MapTile) gameObject).active(this, MapTile.Functions.RAISEPLAYER.getFunc());
                    //Gdx.app.log("PLAYER", " UP " + parent_world.getCurrent_z() + " " + this.getLayer() + " ");
                }
            }
        }
    }

    /**
     * @param gameObject тот обьект с которым столкнулся наш игрок, исходя их того с какой стороны этого обьекта
     *                   игрок подошел, делаем откат в туже сторону позиции игрока, чтобы он стоял почти вплотную
     */
    private void handleCollisionAngle(GameObject gameObject) {
        if(collisionAngle > 45 && collisionAngle <= 135){
            if(velocity.y != 0)
                this.position.y = gameObject.getY()+gameObject.getHeight() + EPS;
            velocity.y = 0;
        }
        // зашли слева
        else if(collisionAngle > 135 && collisionAngle <= 225){
            if(velocity.x != 0)
                this.position.x = gameObject.getX() - this.getWidth() - EPS;
            velocity.x = 0;
        }
        // зашли снизу
        else if(collisionAngle > 225 && collisionAngle <= 315){
            if(velocity.y != 0)
                this.position.y = gameObject.getY() - this.getHeight() - EPS;
            velocity.y = 0;
        }
        // зашли справа
        else if(collisionAngle > 315 || collisionAngle <= 45){
            if(velocity.x != 0)
                this.position.x = gameObject.getX()+ gameObject.getWidth() + EPS;
            velocity.x = 0;
        }
    }

    public Actor hit(float x, float y, boolean touchable) {
        //Процедура проверки. Если точка в прямоугольнике актёра, возвращаем актёра.
        return x > 0 && x < getWidth() && y> 0 && y < getHeight()? this:null;
    }

    public boolean updatePosition(float delta) {
        if(state == State.WALKING)
        {
            //Gdx.app.log("PLAYER",  position.toString()+" "+endPoint.toString() + " " + velocity.toString());
            this.setPreviosPoint(position);
            this.position.add(velocity.x*delta, velocity.y*delta, 0);
            this.setPosition(position.x, position.y);

            if(endPoint.epsilonEquals(getX()+getOriginX(), getY()+getOriginY(), 0, EPS)) {
                state = State.NONE;
                resetVelocity();
                return true;
            }
        }
        return false;
    }

    public void updateAngleGlazDirection(Vector2 mouseCoords){
        tempMouseCoords.set(mouseCoords);
        tempMouseCoords.sub(getX() + getOriginX(), getY() + getOriginY());
        setAngleGazeDirection(tempMouseCoords.angle());
        //Gdx.app.log("Player", tempMouseCoords.angle()+ " " + getAngleGazeDirection() + "" + getRotation());
    }

    public void StopPressed() {
        direction.get(direction.put(Keys.STOP, true));
    }

    public void StopReleased() {
        direction.get(direction.put(Keys.STOP, false));
    }

    @Override
    public void onFallDown() {
        setVisible(false);
        setActiv(false);
        parent_world.spawnController.addGameObjectInQueue(this);
    }

    @Override
    public void spawn(SpawnPoint spawnPoint) {
        setVisible(true);
        setActiv(true);
        set_stats();
        set_pos((int)spawnPoint.position.x, (int)spawnPoint.position.y, (int)spawnPoint.position.z);
        // временно
        if(this == parent_world.getPlayer()){
            parent_world.setCurrent_z(this.getLayer()-1);
        }

    }

    private void set_stats(){
        // Устанавливает начальные значения показателей
        max_hp = current_hp = 100;
        max_mp = current_mp = 100;
    }

    public void set_texture(){
        int srcX = type*parent_tileSet.size;
        // int srcY = 0;

        object_texture_region_body = new TextureRegion(parent_tileSet.texture,
                srcX, 0, parent_tileSet.size, parent_tileSet.size);
        object_texture_region_head = new TextureRegion(parent_tileSet.texture,
                srcX, parent_tileSet.size, parent_tileSet.size, parent_tileSet.size);
        object_texture_region_hands = new AnimatedTextureRegion(parent_tileSet.texture,
                new int[]{0,parent_tileSet.size*2,parent_tileSet.size,parent_tileSet.size,
                        parent_tileSet.size*4,parent_tileSet.size*2,parent_tileSet.size,parent_tileSet.size,
                        0,parent_tileSet.size*2,parent_tileSet.size,parent_tileSet.size,
                        parent_tileSet.size*5,parent_tileSet.size*2,parent_tileSet.size,parent_tileSet.size}
                );
        object_texture_region_hands.set_timer(10);
        //object_texture_region_hands = new TextureRegion(parent_tileSet.texture,
        //        srcX, parent_tileSet.size * 2, parent_tileSet.size, parent_tileSet.size);
        object_texture_region_legs = new TextureRegion(parent_tileSet.texture,
                srcX, parent_tileSet.size * 3, parent_tileSet.size, parent_tileSet.size);
        setBounds(position.x, position.y,
                2, 2);
    }

    public  Vector2     getVelocity()                           {return velocity;}
    public  State       getState()                              {return state;}
    public  Vector3     getPreviosPoint()                       {return previosPoint;}
    public  Vector3     getPosition()                           {return position;}
    public  float       getAngleGazeDirection()                 {return angleGazeDirection;}

    public  void        setPreviosPoint(Vector3 previosPoint)   {this.previosPoint = previosPoint;}
    public  void        setVelocity(Vector2 velocity)           {this.velocity = velocity;}
    public  void        setAngleGazeDirection(float angle)      {this.angleGazeDirection = angle;}
    @Override
    public  float       getSpawnTime()                          {return SPAWN_TIME;}
    @Override
    public  int         getIdSpawnedObject()                    {return SPAWN_ID;}
}
