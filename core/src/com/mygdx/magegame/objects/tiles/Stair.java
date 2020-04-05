package com.mygdx.magegame.objects.tiles;

import com.mygdx.magegame.objects.MapTile;
import com.mygdx.magegame.objects.Player;
import com.mygdx.magegame.world.World;

public class Stair extends MapTile implements ActiveOnPlayerTouch {

    public Stair(World world, int tileset_id, int id, int x, int y, int z, boolean is_camera_oriented) {
        super(world, tileset_id, id, x, y, z, is_camera_oriented);
    }

    @Override
    public void active(Player player) {
        // четаделаем
    }
}
