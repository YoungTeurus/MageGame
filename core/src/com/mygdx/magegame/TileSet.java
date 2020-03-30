package com.mygdx.magegame;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

public class TileSet {
    Texture texture;
    int size;

    public TileSet(FileHandle path_to_texture, int size){
        texture = new Texture(path_to_texture);
        this.size = size;
    }

}
