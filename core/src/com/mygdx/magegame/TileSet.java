package com.mygdx.magegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.mygdx.magegame.objects.MapTile;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mygdx.magegame.Consts.*;
import static com.mygdx.magegame.WorkWithFiles.get_params_from_file;

public class TileSet {
    public Texture texture;
    public int size;
    public int num_of_tiles_in_row;

    // Массив, которые содержат значения соответствующих полей объектов
    String[] human_name_array;
    public Boolean[] is_passable_array;
    public Boolean[] is_solid_array;

    public TileSet(int tileset_id){
        // tileset_id смотрится в Consts
        texture = new Texture(Gdx.files.internal(".//core//assets//spritesets//"+tilesets_filenames[tileset_id] + ".png"));
        load_info(tilesets_filenames[tileset_id] + ".txt");
        // is_passable_array и другие массивы распарсиваются при загрузке
    }

    private void load_info(String filename){
        // Загрузка информации о тайлсете из текстового файла

        Array<String[]> params = get_params_from_file(".//core//assets//spritesets//" + filename);

        int num_of_params = params.size - 3; // Минус 3 обязательных в начале

        int size_of_tileset = Integer.parseInt(params.get(0)[0]); // Размер всего тайлсета
        size = Integer.parseInt(params.get(1)[0]); // Размер одного тайла
        String class_name = params.get(2)[0]; // Получаем название класса для которого предназначается этот массив

        num_of_tiles_in_row = size_of_tileset/size;
        human_name_array = new String[num_of_tiles_in_row*num_of_tiles_in_row];
        is_passable_array = new Boolean[num_of_tiles_in_row*num_of_tiles_in_row];
        is_solid_array = new Boolean[num_of_tiles_in_row*num_of_tiles_in_row];

        for(int i=0; i < num_of_params; i++){
            //Заполняем массивы в соответствии с тем, кому они предназначаются
            if (class_name.equals("MapTile")){
                int tile_id = Integer.parseInt(params.get(3+i)[0]);
                human_name_array[tile_id] = params.get(3+i)[1];
                is_passable_array[tile_id] = Boolean.parseBoolean(params.get(3+i)[2]);
                is_solid_array[tile_id] = Boolean.parseBoolean(params.get(3+i)[3]);

                Gdx.app.log("Load Tileset", "Line" + 3+i + " : was found tile: "
                        + tile_id + " " + human_name_array[tile_id] + " " + is_passable_array[tile_id] + " "+
                        is_solid_array[tile_id]);
                // TODO: заполнение свойств тайла из файла
            }
            if (class_name.equals("Player")) {
                int tile_id = Integer.parseInt(params.get(3+i)[0]);
                human_name_array[tile_id] = params.get(3+i)[1];
                Gdx.app.log("Load Tileset", "Line" + 3+i + " : was found tile: "
                        + tile_id + " " + human_name_array[tile_id]);
            }
        }



        //try{
//
        //    FileReader fr = new FileReader(".//core//assets//" + filename);
        //    Scanner scan = new Scanner(fr);
//
        //    num_of_tiles_in_row = size_of_tileset/size;
        //    human_name_array = new String[num_of_tiles_in_row*num_of_tiles_in_row];
        //    is_passable_array = new Boolean[num_of_tiles_in_row*num_of_tiles_in_row];
        //    is_solid_array = new Boolean[num_of_tiles_in_row*num_of_tiles_in_row];
//
        //    // Дальше должны идти строки в виде:
        //    // id_тайла human_name значение_is_passable значение_is_solid
        //    int line = 0;
//
        //    Pattern pattern1 = Pattern.compile("#.+"); // Поиск строк с комментариями
//
        //    String[] read_params;
//
        //    while (scan.hasNextLine()){
        //        String cur_line = scan.nextLine();
        //        Matcher matcher1 = pattern1.matcher(cur_line);
        //        if (matcher1.find()){ // Если текущая строка - комментарий, пропускаем её
        //            continue;
        //        }
//
        //        read_params = cur_line.split("\\s"); // Разбиваем строку на отдельные параметры.
//
        //        // Заполняем массивы в соответствии с тем, кому они предназначаются
        //        if (class_name.equals("MapTile")){
        //            int tile_id = Integer.parseInt(read_params[0]);
        //            human_name_array[tile_id] = read_params[1];
        //            is_passable_array[tile_id] = Boolean.parseBoolean(read_params[2]);
        //            is_solid_array[tile_id] = Boolean.parseBoolean(read_params[3]);
//
        //            Gdx.app.log("Load Tileset", "Line" + line + " : was found tile: "
        //                    + tile_id + " " + human_name_array[tile_id] + " " + is_passable_array[tile_id] + " "+
        //                    is_solid_array[tile_id]);
//
        //            // TODO: заполнение свойств тайла из файла
        //        }
        //        if (class_name.equals("Player")) {
        //            int tile_id = Integer.parseInt(read_params[0]);
        //            human_name_array[tile_id] = read_params[1];
        //            Gdx.app.log("Load Tileset", "Line" + line + " : was found tile: "
        //                    + tile_id + " " + human_name_array[tile_id]);
        //        }
//
        //        line++;
        //    }
        //    fr.close();
        //} catch (IOException e){
        //    e.printStackTrace();
        //}
    }

    public void dispose(){
        texture.dispose();
    }

    public String getHumanNameById(int id){
        if (human_name_array[id] != null){
            return human_name_array[id];
        }
        return String.format("tile_id_%d",id);
    }

    public boolean getIsPassableById(int id){
        if (is_passable_array[id] != null)
            return is_passable_array[id];
        return default_is_passable;
    }

    public boolean getIsSolidById(int id){
        if (is_solid_array[id] != null)
            return is_solid_array[id];
        return default_is_solid;
    }
}
