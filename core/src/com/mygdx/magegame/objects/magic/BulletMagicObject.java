package com.mygdx.magegame.objects.magic;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.mygdx.magegame.mechanics.magic.MagicOnCollisionFunctions;
import com.mygdx.magegame.objects.GameObject;
import com.mygdx.magegame.objects.MapTile;
import com.mygdx.magegame.objects.Player;
import com.mygdx.magegame.world.World;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BulletMagicObject extends SimpleMagicObject implements BulletMagic {
    TextureRegion bullet_texture;
    Array<Method> onCollisionMethods_array; // Методы, вызываемые при столкновении
    Array<String> onCollisionMethods_args_array; // Аргументы для этих методов

    public BulletMagicObject(World world, Player parent_player){
        super(world, parent_player);

        onCollisionMethods_array = new Array<>();
        onCollisionMethods_args_array = new Array<>();

        bullet_texture = new TextureRegion(parent_world.tileSets[0].texture,
                0,0,
                32, 32);
        set_pos(parent_player.position.x,
                parent_player.position.y,
                parent_player.position.z
        );
        setBounds(position.x,
                position.y, 1, 1);
        parent_world.collisionDetector.addDynamicObject(this, (int) parent_player.position.z);
    }

    public void draw(Batch batch, float parentAlpha){
        batch.draw(bullet_texture, getX(),getY(),
                getOriginX(), getOriginY(), getWidth(), getHeight(),
                getScaleX(), getScaleY(), getRotation());
    }

    public void start_moving(){
        moveTo(position.x+500, position.y);
    }

    public boolean addOnCollision(String MethodName, String args){
        try {
            Method temp = MagicOnCollisionFunctions.class.getMethod(MethodName, GameObject.class, String.class, boolean.class);
            // Проверяем, верно ли указан метод
            if ((boolean)temp.invoke(new MagicOnCollisionFunctions(), null, args, true)){
                onCollisionMethods_array.add(temp);
                onCollisionMethods_args_array.add(args);
                return true;
            }

        }
        catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onCollision(GameObject collision_target) {
        // Если столкнулись с тем, кто кастовал
        if(collision_target == parent_player){
            return;
        }

        if (collision_target instanceof MapTile){
            if(!((MapTile) collision_target).is_passable){
                parent_world.objects_to_delete.add(this);
            }
        }
        else{
            parent_world.objects_to_delete.add(this);
        }

        int i = 0;
        for (Method current_method : onCollisionMethods_array) {
            try {
                current_method.invoke(new MagicOnCollisionFunctions(), collision_target, onCollisionMethods_args_array.get(i), false);
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
            i++;
        }

        // parent_world.getRoot().removeActor(this);

        // Если столкнулись с чем-то твёрдым, то удаляем данный Bullet
        // parent_world.collisionDetector.removeDynamicObject(this, (int) this.position.z);
    }
}