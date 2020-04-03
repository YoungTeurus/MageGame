package com.mygdx.magegame.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;

public class TiledLayer extends Group {

    World parent_world;
    Array<Group> layers;
    public int top_level;
    public int bottom_level;

    public TiledLayer(World parent_world){
        layers = new Array<>();
        layers.add(new Group()); // Нулевой уровень
        this.parent_world = parent_world;
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
        parent_world.getBatch().enableBlending();
        parent_world.getBatch().setBlendFunctionSeparate(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA,
                GL20.GL_ONE, GL20.GL_ZERO);
        //super.draw(batch, parentAlpha);
        for(int i = bottom_level; i <= top_level; i++){
            if (i <= parent_world.current_z) {
                get_layer(i).draw(batch, parentAlpha);
                continue;
            }
            if (i == parent_world.current_z + 1){ // Следующий слой над текущим
                parent_world.getBatch().setColor(1,1,1,0.5f);
                get_layer(i).draw(batch, parentAlpha);
                parent_world.getBatch().setColor(1,1,1,1);
            }
        }
    }

    @Override
    public void clear(){
        for(int i = bottom_level; i <= top_level; i++){
            get_layer(i).clear();
        }
    }
}
