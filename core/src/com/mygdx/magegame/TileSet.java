package com.mygdx.magegame;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

public class TileSet {
    public Texture texture;
    public int size;

    public TileSet(FileHandle path_to_texture, int size){
        texture = new Texture(path_to_texture);
        this.size = size;

    }

    public void dispose(){
        texture.dispose();
    }
}
