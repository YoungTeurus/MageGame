package com.mygdx.magegame.objects;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.magegame.world.World;

public abstract class GameObject extends Actor {
    Vector3 position;
    boolean is_camera_oriented;
    World parent_world;

    GameObject(World world){
        parent_world = world;
        position = new Vector3();
        is_camera_oriented = true;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    public void set_pos(int x, int y) {
        // Устанавилвает позицию вектора положения
        position.set(x, y, 0);
    }


    @Override
    public String toString() {
        return String.format("GameObject{(%f, %f, %f)}",
                position.x, position.y, position.z);
    }
}
