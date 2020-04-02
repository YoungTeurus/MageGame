package com.mygdx.magegame.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.magegame.TileSet;
import com.mygdx.magegame.model.World;

public class MapTile extends GameObject {
    TextureRegion object_texture_region;
    int id;
    int tileset_id;

    public MapTile(World world,int tileset_id, int id, int x, int y, boolean is_camera_oriented){
        // Создаёт квадратный объект из текстуры с размером стороны - size.
        // Текстура выбирается по index-у
        super(world);
        this.tileset_id = tileset_id;
        set_pos(x, y);
        this.is_camera_oriented = is_camera_oriented;
        set_texture(id);
    }

    public void set_texture(int new_id){
         // Координаты текстурки в тайлсете вычисляются по id
         // Каждый тайлсет может вмещать до 256 тайлов со следующими id:
         // 0   1  2 ... 15
         // 16 17 18 ...
         // ...

         id = new_id;
         int srcX = id%16 * parent_world.tileSets[tileset_id].size;
         int srcY = id/16 * parent_world.tileSets[tileset_id].size;

         object_texture_region = new TextureRegion(parent_world.tileSets[tileset_id].texture,
                 srcX, srcY,
                 parent_world.tileSets[tileset_id].size, parent_world.tileSets[tileset_id].size);
         setBounds(position.x, position.y,
                 1, 1);
     }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(object_texture_region, getX(),getY(),
                getOriginX(), getOriginY(), getWidth(), getHeight(),
                getScaleX(), getScaleY(), getRotation());
         //if (is_camera_oriented){
         //    batch.draw(object_texture_region,
         //            position.x + parent_world.getCamera().position.x,
         //            position.y + parent_world.getCamera().position.y);
         //}
         //else {
         //    batch.draw(object_texture_region,
         //            position.x,
         //            position.y);
         //}
    }

    @Override
    public String toString() {
        return String.format("MapTile{(%f, %f, %f), %d, %d}",
                position.x, position.y, position.z, tileset_id, id);
    }
}
