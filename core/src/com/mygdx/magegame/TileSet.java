package com.mygdx.magegame;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

public class TileSet {
    public Texture texture;
    public int size;

    // Массив, которые содержат значения соответствующих полей объектов
    public boolean[] is_passable_array;
    public boolean[] is_solid_array;
    public String[] human_name_array;

    public TileSet(FileHandle path_to_texture, int size){
        texture = new Texture(path_to_texture);
        this.size = size;
        // is_passable_array и другие массивы распарсиваются при загрузке
    }

    public void dispose(){
        texture.dispose();
    }
}
