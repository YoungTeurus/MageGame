package com.mygdx.magegame.objects;

import com.badlogic.gdx.maps.Map;
import com.mygdx.magegame.world.World;

public class SpawnPoint extends GameObject {
    // обьект который, создаем
    private int spawnedObjectId;
    // время последнего спавна
    private float lastTimeSpawn;

    public SpawnPoint(World world, int spawnedObjectId) {
        super(world);
        this.spawnedObjectId = spawnedObjectId;
    }

    public SpawnPoint(World world, int spawnedObjectId, int x, int y, int z) {
        super(world);
        this.spawnedObjectId = spawnedObjectId;
        set_pos(x,y,z);
    }

    public int getSpawnedObjectId() {
        return spawnedObjectId;
    }

    public void setSpawnedObjectId(int spawnedObjectId) {
        this.spawnedObjectId = spawnedObjectId;
    }

    public float getLastTimeSpawn() {
        return lastTimeSpawn;
    }

    public void setLastTimeSpawn(float lastTimeSpawn) {
        this.lastTimeSpawn = lastTimeSpawn;
    }
}
