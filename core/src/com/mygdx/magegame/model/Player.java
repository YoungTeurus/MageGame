//package com.mygdx.magegame.model;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.Batch;
//import com.badlogic.gdx.graphics.g2d.Sprite;
//import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import com.badlogic.gdx.math.Rectangle;
//import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.scenes.scene2d.Actor;
//import com.badlogic.gdx.scenes.scene2d.InputEvent;
//import com.badlogic.gdx.scenes.scene2d.InputListener;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class Player extends Actor {
//
//    //состояние
//    public enum State {
//        NONE, WALKING, DEAD
//    }
//    public static enum Direction {
//        LEFT, RIGHT, UP, DOWN, NONE
//    }
//    static Map<Keys, Boolean> direction = new HashMap<Keys, Boolean>();
//    // скорость движения, мб её будем менять от каких-нибудь тапочек, так что не константа
//    public static final float SPEED = 2f;
//    // размер конст
//    public static final float SIZE = 0.25f;
//
//    //позиция в мире
//    Vector2 position = new Vector2();
//    //используется для вычисления движения
//    Vector2 velocity = new Vector2();
//    boolean	facingLeft = true;
//    TextureRegion texture;
//
//    //будет использоваться в будущем для нахождения коллизий (столкновение со стенкой и т.д.
//    Rectangle bounds = new Rectangle();
//    //текущее состояние
//    State state = State.NONE;
//
//    public Player(Vector2 position) {
//        this.position = position;
//        this.bounds.height = SIZE;
//        this.bounds.width = SIZE;
//        //setHeight(SIZE);
//        //setWidth(SIZE);
//        //setX(position.x);
//        //setY(position.y);
//        //texture = new Texture(Gdx.files.internal("player.jpg"),);
//        texture = new TextureRegion(new Texture(Gdx.files.internal("player.jpg")),
//                (int)position.x,
//                (int)position.y,
//                (int)(32 * SIZE),
//                (int)(32 * SIZE));
//        addListener(new InputListener() {
//            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
//                //Log.e("td","1");
//	        	ChangeNavigation(x, y);
//	    		processInput();
//                return true;
//            }
//
//            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
//	        	/*ChangeNavigation(x, y);
//	    		processInput();*/
//            }
//        });
//
//    }
//
//    @Override
//    public void draw(Batch batch, float parentAlpha) {
//        //super.draw(batch, parentAlpha);
//        batch.draw(texture, position.x, position.y);
//    }
//
//    @Override
//    public void act(float delta) {
//        super.act(delta);
//        position.add(velocity.scl(delta));
//        setX(position.x);
//        setY(position.y);
//
//    }
//
//    public Actor hit(float x, float y, boolean touchable) {
//    //Процедура проверки. Если точка в прямоугольнике актёра, возвращаем актёра.
//        return x > 0 && x < getWidth() && y> 0 && y < getHeight()?this:null;
//    }
//
//    public Rectangle getBounds() {
//        return bounds;
//    }
//
//    public Vector2 getVelocity() {
//        return velocity;
//    }
//
//    public Vector2 getPosition() {
//        return position;
//    }
//
//    //обновления движения
//    public void update(float delta) {
//        position.add(velocity.x * delta, velocity.y*delta);
//    }
//
//    public void ChangeNavigation(float x, float y){
//        //Log.e("Player ", "["+x+":"+y+"] --- ["+this.getX()+":"+this.getY()+"]");
//        resetWay();
//        if(y > getY())
//            upPressed();
//        if(y <  getY())
//            downPressed();
//        if ( x< getX())
//            leftPressed();
//        if (x> (getPosition().x +SIZE))
//            rightPressed();
//        processInput();
//    }
//
//    public void resetWay(){
//        rightReleased();
//        leftReleased();
//        downReleased();
//        upReleased();
//        getVelocity().x = 0;
//        getVelocity().y  = 0;
//    }
//
//    private void processInput() {
//        if (direction.get(Keys.LEFT))
//            getVelocity().x = -Player.SPEED;
//        if (direction.get(Keys.RIGHT))
//            getVelocity().x =Player.SPEED;
//        if (direction.get(Keys.UP))
//            getVelocity().y = Player.SPEED;
//        if (direction.get(Keys.DOWN))
//            getVelocity().y = -Player.SPEED;
//        if ((direction.get(Keys.LEFT) && direction.get(Keys.RIGHT)) ||
//                (!direction.get(Keys.LEFT) && (!direction.get(Keys.RIGHT))))
//            getVelocity().x = 0;
//
//        if ((direction.get(Keys.UP) && direction.get(Keys.DOWN)) ||
//                (!direction.get(Keys.UP) && (!direction.get(Keys.DOWN))))
//            getVelocity().y = 0;
//    }
//
//    enum Keys {
//        LEFT, RIGHT, UP, DOWN
//    }
//
//
//    static {
//        direction.put(Keys.LEFT, false);
//        direction.put(Keys.RIGHT, false);
//        direction.put(Keys.UP, false);
//        direction.put(Keys.DOWN, false);
//    };
//
//
//
//    public void leftPressed() {
//        direction.get(direction.put(Keys.LEFT, true));
//    }
//
//    public void rightPressed() {
//        direction.get(direction.put(Keys.RIGHT, true));
//    }
//
//    public void upPressed() {
//        direction.get(direction.put(Keys.UP, true));
//    }
//
//    public void downPressed() {
//        direction.get(direction.put(Keys.DOWN, true));
//    }
//
//    public void leftReleased() {
//        direction.get(direction.put(Keys.LEFT, false));
//    }
//
//    public void rightReleased() {
//        direction.get(direction.put(Keys.RIGHT, false));
//    }
//
//    public void upReleased() {
//        direction.get(direction.put(Keys.UP, false));
//    }
//
//    public void downReleased() {
//        direction.get(direction.put(Keys.DOWN, false));
//    }
//}