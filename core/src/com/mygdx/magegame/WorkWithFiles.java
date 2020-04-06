package com.mygdx.magegame;

import com.badlogic.gdx.utils.Array;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WorkWithFiles {

    public static Array<String[]> get_params_from_file(String filename){
        // Возвращает массив считанных параметров из файла
        Array<String[]> return_array = new Array<>();

        try {

            FileReader fr = new FileReader(filename);
            Scanner scan = new Scanner(fr);

            Pattern pattern1 = Pattern.compile("#.+"); // Поиск строк с комментариями

            while (scan.hasNextLine()){
                String cur_line = scan.nextLine();

                Matcher matcher1 = pattern1.matcher(cur_line);
                if (matcher1.find()){ // Если текущая строка - комментарий, пропускаем её
                    continue;
                }

                String[] temp_read_params = cur_line.split("\\s");

                return_array.add(temp_read_params);
            }

            fr.close();

        } catch (IOException e){
            e.printStackTrace();
        }

        return return_array;
    }
}
