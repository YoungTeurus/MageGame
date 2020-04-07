package com.mygdx.magegame.objects.magic;

import com.badlogic.gdx.utils.Array;
import com.mygdx.magegame.mechanics.MagicIfCanCastFunctions;
import com.mygdx.magegame.mechanics.MagicOnCastFunctions;
import com.mygdx.magegame.objects.GameObject;
import com.mygdx.magegame.objects.Player;
import com.mygdx.magegame.world.World;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SimpleMagicObject extends GameObject implements SimpleMagic {

    Player parent_player; // Игрок, который скастовал данное заклинание
    Array<Method> onCastMethods_array; // Методы, вызываемые при касте
    Array<String> onCastMethods_args_array; // Аргументы для этих методов
    Array<Method> ifMethods_array; // Методы, вызываемые при проверке на возможность каста
    Array<String> ifMethods_args_array; // Аргументы для этих методов

    public SimpleMagicObject(World world, Player parent_player){
        super(world);
        this.parent_player = parent_player;
        onCastMethods_array = new Array<>();
        onCastMethods_args_array = new Array<>();
        ifMethods_args_array = new Array<>();
        ifMethods_array = new Array<>();
    }

    public boolean addOnCast(String MethodName, String args){
        try {
            Method temp = MagicOnCastFunctions.class.getMethod(MethodName, Player.class, String.class, boolean.class);
            // Проверяем, верно ли указан метод
            if ((boolean)temp.invoke(new MagicOnCastFunctions(), parent_player, args, true)){
                onCastMethods_array.add(temp);
                onCastMethods_args_array.add(args);
                return true;
            }

        }
        catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean addIf(String MethodName, String args){
        // Добавляет условие для возможности каста заклинания
        try {
            Method temp = MagicIfCanCastFunctions.class.getMethod(MethodName, Player.class, String.class, boolean.class);
            // Проверяем, верно ли указан метод
            if ((boolean)temp.invoke(new MagicIfCanCastFunctions(), parent_player, args, true)){
                ifMethods_array.add(temp);
                ifMethods_args_array.add(args);
                return true;
            }

        }
        catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkIfCanCast(){
        // Проверяет, возможно ли кастануть заклинание
        // Вызывает все функции, ответственные за проверку возможности каста заклинания
        int i = 0;
        for(Method current_method: ifMethods_array){
            try{
                if(!(boolean) current_method.invoke(new MagicOnCastFunctions(), parent_player, ifMethods_args_array.get(i), false)){
                    return false;
                }
            }
            catch (InvocationTargetException | IllegalAccessException e){
                e.printStackTrace();
            }
            i++;
        }
        return true;
    }

    @Override
    public boolean onCast() {
        // Вызывает все функции, ответственные за каст заклинания
        // Если можно кастануть заклинание
        if (checkIfCanCast()) {
            int i = 0;
            for (Method current_method : onCastMethods_array) {
                try {
                    current_method.invoke(new MagicOnCastFunctions(), parent_player, onCastMethods_args_array.get(i), false);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                    return false; // Если какая-то функция сломалась
                }
                i++;
            }
            return true; // Если успешно прошли все функции
        }
        return false; // Если не смогли скастовать
    }
}
