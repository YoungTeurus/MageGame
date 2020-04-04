package com.mygdx.magegame.collision;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.mygdx.magegame.objects.GameObject;

public class CollisionListener implements EventListener {

    public CollisionListener() {
    }

    @Override
    public boolean handle(Event e) {
        if (!(e instanceof CollisionEvent)) {
            return false;
        } else {
            CollisionEvent event = (CollisionEvent) e;
            switch (event.getType()){
                case ON_COLLISION: {
                    this.processCollision(event.getCollidedGameObject(), event.getObjectType());
                    return true;
                }
                default:
                    return false;
            }
        }
    }
    public void processCollision(GameObject gameObject, CollisionEvent.CollisionObjectType type){
    }
}
