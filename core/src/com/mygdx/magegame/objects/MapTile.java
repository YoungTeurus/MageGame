package com.mygdx.magegame.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.magegame.objects.tiles.ActiveOnPlayerTouch;
import com.mygdx.magegame.world.World;

public class MapTile extends GameObject {
    TextureRegion object_texture_region;
    String human_name; // "Человеческое название" тайла, заданное заранее
    public boolean is_passable; // Можно ли проходить через этот тайл в горизонтальной плоскости?
    public boolean is_solid; // Можно ли стоять на этом тайле? (Падает ли персонаж при наступании на него?)
    public boolean is_activ; // Может ли взаимодейстовать это тайл

    enum Functions{
        RAISEPLAYER(1, new ActiveOnPlayerTouch(){
            @Override
            public void active(Player player) {
                player.parent_world.setCurrent_z(player.getLayer());
                player.position.z += 1;

            }
        }),
        DROPPLAYER(2, new ActiveOnPlayerTouch(){
            @Override
            public void active(Player player) {
                player.parent_world.setCurrent_z(player.getLayer());
                player.position.z += 1;
            }
        });
        private int id;
        private ActiveOnPlayerTouch func;

        Functions(int id, ActiveOnPlayerTouch func){
            this.func = func;
            this.id = id;
        }
        public ActiveOnPlayerTouch getFunc(){
            return func;
        }
    }

    public MapTile(World world,int tileset_id, int id, int x, int y, int z, boolean is_camera_oriented){
        // Создаёт квадратный объект из текстуры с размером стороны - size.
        // Текстура выбирается по index-у
        super(world);
        this.tileset_id = tileset_id;
        set_pos(x, y, z);
        this.is_camera_oriented = is_camera_oriented;
        setTexture(id);
        human_name = parent_world.tileSets[tileset_id].getHumanNameById(id);
        is_passable = parent_world.tileSets[tileset_id].getIsPassableById(id);
        is_solid = parent_world.tileSets[tileset_id].getIsSolidById(id);
    }

    public void setTexture(int new_id){
         // Координаты текстурки в тайлсете вычисляются по id
         // Каждый тайлсет может вмещать до X тайлов со следующими id:
         // 0                    1                      2                      ...  num_of_tiles_in_row-1
         // num_of_tiles_in_row  num_of_tiles_in_row+1  num_of_tiles_in_row+2  ...
         // ...

         id = new_id;
         int srcX = id%parent_world.tileSets[tileset_id].num_of_tiles_in_row * parent_world.tileSets[tileset_id].size;
         int srcY = id/parent_world.tileSets[tileset_id].num_of_tiles_in_row * parent_world.tileSets[tileset_id].size;

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
        return String.format("MapTile %f %f %f %d %d",
                position.x, position.y, position.z, tileset_id, id);
    }

    public void active(Player player, ActiveOnPlayerTouch func) {
        func.active(player);
    }

    public int getId() {
        return id;
    }
}
