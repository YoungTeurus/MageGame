package com.mygdx.magegame.collision;

import com.badlogic.gdx.utils.Array;
import com.mygdx.magegame.objects.GameObject;

public class CollisionMap {
    // все недвигающиеся объекты (иниц при загрузке карты) стены, ловушки и всякие такие штуки
    // они ничего не проверяют
    private Array<GameObject> allStaticObjects;
    // все обьекты, которые двигаются и могут столкнуться (динамически добавляются и удаляются в ходе игры)
    // проверяют столкновения с статическими объектами и с контролирумыми
    private Array<GameObject> allDynamicObjects;
    // все управляемые нами обьекты(игроки)
    // проверяют столкновения с статическими и с другими контролируемыми обьектами
    private Array<GameObject> allControlledObjects;

    public CollisionMap() {
        this.allStaticObjects = new Array<GameObject>();
        this.allDynamicObjects = new Array<GameObject>();
        this.allControlledObjects = new Array<GameObject>();

    }

    public void addStatic(GameObject gameObject){
        allStaticObjects.add(gameObject);
    }

    public void addDynamic(GameObject gameObject){
        allDynamicObjects.add(gameObject);
    }

    public void addControlled(GameObject gameObject){
        allControlledObjects.add(gameObject);
    }

    public Array<GameObject> getAllStaticObjects() {
        return allStaticObjects;
    }

    public Array<GameObject> getAllDynamicObjects() {
        return allDynamicObjects;
    }

    public Array<GameObject> getAllControlledObjects() {
        return allControlledObjects;
    }
}
