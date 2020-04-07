package com.mygdx.magegame.mechanics;

import com.mygdx.magegame.objects.Player;

import java.text.ParseException;

public class MagicOnCastFunctions {
    // Функции предназначенные для onCast должны принимать 3 параметра:
    // Player - тот игрок, котороый кастанул заклинание
    // String - строка параметров для функции
    // boolean - флаг проверки строки параметров на правильность

    // Если флаг выставлен в true, то при вызове функции будет выполнено не основное действие, а проверка на наличие всех
    // необходимых параметров в строке параметров. Функция вернёт true - если проверка пройдена, false - если нет.
    // При обычном выполнении функция не должна возвращать никакое полезное значение. (Она будет возвращать true, однако
    // при любом исходе выполнения.)

    public static boolean ChangeHp(Player player, String difference, boolean check){
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
        int int_difference = Integer.parseInt(difference);
        if (int_difference < 0) {
            player.current_hp = Math.max(0, player.current_hp + int_difference);
            return true;
        }
        player.current_hp = Math.min(player.max_hp, player.current_hp + int_difference);
        return true;
    }

    public static boolean ChangeMp(Player player, String difference, boolean check){
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
        int int_difference = Integer.parseInt(difference);
        if (int_difference < 0) {
            player.current_mp = Math.max(0, player.current_mp - int_difference);
            return true;
        }
        player.current_mp = Math.min(player.max_mp, player.current_mp + int_difference);
        return true;
    }
}
