package com.mygdx.magegame.objects;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.magegame.model.World;
import org.graalvm.compiler.word.Word;

public abstract class GameObject extends Actor {
    Vector2 position;
    boolean is_camera_oriented;
    World parent_world;

    GameObject(World world){
        parent_world = world;
        position = new Vector2();
        is_camera_oriented = true;
    }

    protected abstract void draw(SpriteBatch batch, float parentAlpha); // Отрисовывает объект
    abstract void act(); // Отвечает за изменение логики
    public void set_pos(int x, int y) {
        // Устанавилвает позицию вектора положения
        position.set(x, y);
    }
}
