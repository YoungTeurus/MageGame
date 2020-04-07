package com.mygdx.magegame.mechanics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.magegame.objects.GameObject;
import com.mygdx.magegame.objects.SpawnPoint;
import com.mygdx.magegame.world.World;
import com.badlogic.gdx.utils.Queue;

public class SpawnController {
    class SpawnedObject{
        float timeDeath;
        float timeSpawn;
        Spawned spawnedObject;

        public SpawnedObject(float timeDeath, float timeSpawn, Spawned spawnedObject) {
            this.timeDeath = timeDeath;
            this.timeSpawn = timeSpawn;
            this.spawnedObject = spawnedObject;
        }

        public int getId(){
            return spawnedObject.getIdSpawnedObject();
        }
    }
    World world;
    Queue<SpawnedObject> spawnedPlayers;
    Queue<SpawnedObject> spawnedMobs;

    Array<SpawnPoint> spawnersPlayers;
    Array<SpawnPoint> spawnersMobs;


    public SpawnController(World world) {
        this.world = world;
        spawnedPlayers = new Queue<SpawnedObject>();
        spawnedMobs = new Queue<SpawnedObject>();

        spawnersPlayers = new Array<SpawnPoint>();
        spawnersMobs = new Array<SpawnPoint>();
    }

    public boolean spawn(){
        // проверим есть ли обьекты, для спавна
        SpawnedObject spawnedObject = checkReadinessSpawnedObject();
        if(spawnedObject == null){return false;}
        // если есть обьект для спавна
        // получим свободный спавнер для этого типа
        SpawnPoint spawnPoint = getFreeSpawnPoint(spawnedObject.spawnedObject.getIdSpawnedObject());

        if(spawnPoint == null){ return false;}
        // если все-таки есть спавнер
        if(spawnedObject.getId() == 0)
            spawnedPlayers.removeFirst();
        else if(spawnedObject.getId() == 1)
            spawnedMobs.removeFirst();
        spawnedObject.spawnedObject.spawn(spawnPoint);
        return true;
    }

    public void addSpawner(SpawnPoint spawnPoint){
        if(spawnPoint.getSpawnedObjectId() == 0)
            spawnersPlayers.add(spawnPoint);
        else if(spawnPoint.getSpawnedObjectId() == 1)
            spawnersMobs.add(spawnPoint);
    }

    public void addGameObjectInQueue(Spawned spawnedObject){
        if(spawnedObject.getIdSpawnedObject() == 0) // игрок
            spawnedPlayers.addLast(new SpawnedObject(TimeUtils.nanoTime(), spawnedObject.getSpawnTime(), spawnedObject));
        else if(spawnedObject.getIdSpawnedObject() == 1)
            spawnedMobs.addLast(new SpawnedObject(TimeUtils.nanoTime(), spawnedObject.getSpawnTime(), spawnedObject));

    }

    public SpawnPoint getFreeSpawnPoint(int idSpawnObject){
        switch (idSpawnObject){
            case 0:{
                return spawnersPlayers.get(Math.abs(world.random.nextInt() % spawnersPlayers.size));
            }
            case 1:{
                return spawnersMobs.get(Math.abs(world.random.nextInt() % spawnersMobs.size));
            }
            default: return null;
        }
    }

    // если есть, обьект, кот пора заспавнить вернет его
    public SpawnedObject checkReadinessSpawnedObject() {
        if (!spawnedPlayers.isEmpty()){
            // Если прошло время нужное
            if (TimeUtils.nanoTime() >= spawnedPlayers.first().timeSpawn + spawnedPlayers.first().timeDeath) {
                return spawnedPlayers.first();
            }
        }
        if(!spawnedMobs.isEmpty()) {
            // Если прошло время нужное
            if (TimeUtils.nanoTime() >= spawnedMobs.first().timeSpawn + spawnedMobs.first().timeDeath)
                return spawnedMobs.first();
        }
        return null;
    }
}
