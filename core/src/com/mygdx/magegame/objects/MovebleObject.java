package com.mygdx.magegame.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.mygdx.magegame.world.World;

public class MovebleObject extends GameObject{

    float SPEED = 2f;
    static final float EPS = 0.000001f;
    //используется для вычисления движения
    //конечная точка пути
    Vector2 velocity = new Vector2();
    Vector3 endPoint = new Vector3();

    Vector2 temp = new Vector2();
    public MovebleObject(World world) {
        super(world);
    }

    // сообщает обьекту двигаться к точке
    public void moveTo(float worldX, float worldY){
        endPoint.set(worldX, worldY, 0);

        temp.x = worldX;
        temp.y = worldY;
        temp.sub(getX()+getOriginX(), getY()+getOriginY());
        //Gdx.app.log("MOVEOBJ", temp.toString() +  temp.angle());

        velocity.x += SPEED * Math.cos(temp.angle()/180.0*3.14);
        velocity.y += SPEED * Math.sin(temp.angle()/180.0*3.14);
    }

    // перемещает к конечной точке, если ее достигли, вернет тру
    public boolean updatePosition(float delta){
        this.position.add(velocity.x*delta, velocity.y*delta, 0);
        if(endPoint.epsilonEquals(getX()+getOriginX(), getY()+getOriginY(), 0, EPS)) {
            velocity.set(0,0);
            return  true;
        }
        return false;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        // подвинем этот обьект к конечной точке
        updatePosition(delta);
        // проверим и обработаем коллизии
        processCollisions();
    }

    // метод обрабатывает коллизии со всеми обьектами
    private void processCollisions() {
        Array<GameObject> objs = parent_world.collisionDetector.checkCollisions(this);
        if( objs.size > 0 ){
            for (GameObject obj:objs) {
                onCollision(obj);
            }
        }
    }

    // Действия при коллизиях, его нужно перегружать
    @Override
    public void onCollision(GameObject gameObject) {
        Gdx.app.log("MOVEOBJ", "COLLISION WITH " + gameObject.position.toString());
    }

    public float getSPEED() {
        return SPEED;
    }

    protected void setSPEED(float SPEED) {
        this.SPEED = SPEED;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    protected void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }
}
