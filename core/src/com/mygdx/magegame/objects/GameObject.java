package com.mygdx.magegame.objects;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.magegame.world.World;

public abstract class GameObject extends Actor {
    public Vector3 position;
    boolean is_camera_oriented;
    World parent_world;
    int tileset_id;
    int id;
    boolean isActiv = true;


    GameObject(World world){
        parent_world = world;
        position = new Vector3();
        is_camera_oriented = true;
    }

    public void kill(){

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    public void set_pos(int x, int y, int z) {
        // Устанавилвает позицию вектора положения
        position.set(x, y, z);
    }

    // вернет индекс слоя, на котором находится обьект
    public int getLayer(){
        return (int)position.z;
    }

    public void onCollision(GameObject gameObject){

    }

    public boolean isActiv() {
        return isActiv;
    }

    public void setActiv(boolean activ) {
        isActiv = activ;
    }

    @Override
    public String toString() {
        return String.format("GameObject %f %f %f",
                position.x, position.y, position.z);
    }
}
