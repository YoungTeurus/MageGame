package com.mygdx.magegame.mechanics.magic;

import com.mygdx.magegame.objects.GameObject;
import com.mygdx.magegame.objects.Player;

public class MagicOnCollisionFunctions {
    // Функции предназначенные для onCollision должны принимать 3 параметра:
    // GameObject - тот объект, в который попало заклинание
    // String - строка параметров для функции
    // boolean - флаг проверки строки параметров на правильность

    // Если флаг выставлен в true, то при вызове функции будет выполнено не основное действие, а проверка на наличие всех
    // необходимых параметров в строке параметров. Функция вернёт true - если проверка пройдена, false - если нет.
    // При обычном выполнении функция не должна возвращать никакое полезное значение. (Она будет возвращать true, однако
    // при любом исходе выполнения.)

    public static boolean ChangeHp(GameObject gameObject, String difference, boolean check){
        if (check == true){
            // Проверка правильности переданных параметров.
            // В данной функции используется один параметр - int, поэтому пытаемся преобразовать строку к int-у.
            try{
                int temp = Integer.parseInt(difference); // Если преобразование удалось
                return true;
            }
            catch (NumberFormatException e){ // Если преобразование не удалось
                return false;
            }
        }
        if(gameObject instanceof Player){
            int int_difference = Integer.parseInt(difference);
            if (int_difference < 0) {
                ((Player)gameObject).current_hp = Math.max(0, ((Player)gameObject).current_hp + int_difference);
                return true;
            }
            ((Player)gameObject).current_hp = Math.min(((Player)gameObject).max_hp, ((Player)gameObject).current_hp + int_difference);
        }
        return true;
    }
}
