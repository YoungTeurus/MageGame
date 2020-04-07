package com.mygdx.magegame.mechanics.magic;

import com.mygdx.magegame.objects.Player;

public class MagicIfCanCastFunctions {
    // Функции предназначенные для проверки возможности каста заклинания должны принимать 3 параметра:
    // Player - тот игрок, для которого проверяются условия
    // String - строка параметров для функции
    // boolean - флаг проверки строки параметров на правильность

    // Если флаг выставлен в true, то при вызове функции будет выполнено не основное действие, а проверка на наличие всех
    // необходимых параметров в строке параметров. Функция вернёт true - если проверка пройдена, false - если нет.

    public static boolean HpEqualMoreThan(Player player, String difference, boolean check){
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
        int minimal_hp = Integer.parseInt(difference);
        if (player.current_hp >= minimal_hp) {
            return true;
        }
        return false;
    }

    public static boolean MpEqualMoreThan(Player player, String difference, boolean check){
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
        int minimal_mp = Integer.parseInt(difference);
        if (player.current_mp >= minimal_mp) {
            return true;
        }
        return false;
    }
}
