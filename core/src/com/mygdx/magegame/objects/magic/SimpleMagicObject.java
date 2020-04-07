package com.mygdx.magegame.objects.magic;

import com.badlogic.gdx.utils.Array;
import com.mygdx.magegame.mechanics.MagicFunctions;
import com.mygdx.magegame.objects.GameObject;
import com.mygdx.magegame.objects.Player;
import com.mygdx.magegame.world.World;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SimpleMagicObject extends GameObject implements SimpleMagic {

    Player parent_player; // Игрок, который скастовал данное заклинание
    Array<Method> methods_array;
    //Method m = null;
    //int power;
    Array<String> methods_args_array;

    public SimpleMagicObject(World world, Player parent_player){
        super(world);
        this.parent_player = parent_player;
        methods_array = new Array<>();
        methods_args_array = new Array<>();
    }

    public boolean addOnCast(String MethodName, String args){
        try {
            Method temp = MagicFunctions.class.getMethod(MethodName, Player.class, String.class, boolean.class);
            // Проверяем, верно ли указан метод
            if ((boolean)temp.invoke(new MagicFunctions(), parent_player, args, true)){
                methods_array.add(temp);
                methods_args_array.add(args);
                return true;
            }

        }
        catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onCast() {
        int i = 0;
        for(Method current_method: methods_array){
            try{
                current_method.invoke(new MagicFunctions(), parent_player, methods_args_array.get(i), false);
            }
            catch (InvocationTargetException | IllegalAccessException e){
                e.printStackTrace();
            }
            i++;
        }
    }
}
