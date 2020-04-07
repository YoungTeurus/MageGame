package com.mygdx.magegame.mechanics;

import com.mygdx.magegame.objects.SpawnPoint;

public interface Spawned {
    void spawn(SpawnPoint spawnPoint);

    float getSpawnTime();

    int getIdSpawnedObject();
}
