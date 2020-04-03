package com.mygdx.magegame.world;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;

public class TiledLayer extends Group {

    Array<Group> layers;
    int top_level;
    int bottom_level;

    public TiledLayer(){
        layers = new Array<>();
        layers.add(new Group()); // Нулевой уровень
        top_level = bottom_level = 0;
    }

    private void add_layer(int z) {
        // Добавляет слой
        if (z <= top_level && z >= bottom_level) {
            return;
        }
        if (z < bottom_level) {
            // Если нужно добавить более ГЛУБОКИЙ уровень
            int difference = bottom_level - z; // Сколько слоёв нужно добавить
            for (int i = 0; i<difference; i++){
                layers.insert(0, new Group()); // Вставляем в начало пустую группу
            }
            bottom_level = z;
        }
        else{
            // Если нужно добавить более ВЫСКОКИЙ уровень
            int difference = z - top_level; // Сколько слоёв нужно добавить
            for (int i = 0; i<difference; i++){
                layers.add(new Group()); // Вставляем в конец пустую группу
            }
            top_level = z;
        }
    }

    public Group get_layer(int z){
        // Получает слой с определённым номеров
        if (z > top_level || z < bottom_level) {
            add_layer(z);
        }

        return layers.get(z - bottom_level);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //super.draw(batch, parentAlpha);
        for(int i = bottom_level; i <= top_level; i++){
            layers.get(i).draw(batch, parentAlpha);
        }
    }
}
