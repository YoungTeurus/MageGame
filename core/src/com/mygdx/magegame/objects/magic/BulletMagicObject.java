package com.mygdx.magegame.objects.magic;

import com.mygdx.magegame.objects.GameObject;
import com.mygdx.magegame.objects.Player;
import com.mygdx.magegame.world.World;

public class BulletMagicObject extends SimpleMagicObject implements BulletMagic {

    BulletMagicObject(World world, Player parent_player){
        super(world, parent_player);
    }

    @Override
    public void onCollision(GameObject collision_target) {

    }
}
