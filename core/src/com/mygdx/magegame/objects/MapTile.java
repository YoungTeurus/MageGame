package com.mygdx.magegame.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.magegame.TileSet;
import com.mygdx.magegame.model.World;

public class MapTile extends GameObject {
    TextureRegion object_texture_region;
    TileSet parent_tileSet;
    int id;

    public MapTile(TileSet tileSet, World world, int id, int x, int y, boolean is_camera_oriented){
        // Создаёт квадратный объект из текстуры с размером стороны - size.
        // Текстура выбирается по index-у
        super(world);
        parent_tileSet = tileSet;
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
         int srcX = id%16 * parent_tileSet.size;
         int srcY = id/16 * parent_tileSet.size;

         object_texture_region = new TextureRegion(parent_tileSet.texture,srcX, srcY, parent_tileSet.size, parent_tileSet.size);
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
}
