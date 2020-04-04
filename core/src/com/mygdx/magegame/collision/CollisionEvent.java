package com.mygdx.magegame.collision;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.mygdx.magegame.objects.GameObject;

public class CollisionEvent extends Event {
    // тип события коллизии
    public enum EventType {ON_COLLISION}
    // тип обьекта которого коснулись
    public enum CollisionObjectType {STATIC, DYNAMIC, CONTROLED}
    GameObject gameObject;
    GameObject collidedGameObject;
    EventType type;
    CollisionObjectType objectType;


    @Override
    public void reset() {
        super.reset();
        gameObject = null;
        collidedGameObject = null;
    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    public GameObject getCollidedGameObject() {
        return collidedGameObject;
    }

    public void setCollidedGameObject(GameObject collidedGameObject) {
        this.collidedGameObject = collidedGameObject;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public CollisionObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(CollisionObjectType objectType) {
        this.objectType = objectType;
    }
}
