package com.mygdx.magegame.collision;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mygdx.magegame.objects.GameObject;
import com.badlogic.gdx.math.Intersector;
import com.mygdx.magegame.world.World;

public class CollisionDetector {
    // ссылка на мир
    World world;
    // все недвигающиеся объекты (иниц при загрузке карты) стены, ловушки и всякие такие штуки
    public Array<CollisionMap> collisionMaps;
    CollisionEvent event = new CollisionEvent();

    Rectangle rect1 = new Rectangle();
    Rectangle rect2 = new Rectangle();
    Circle circle = new Circle();

    public CollisionDetector(World world){
        this.world = world;
        collisionMaps = new Array<CollisionMap>();
    }

    // проверяет не столкнулся ли наш обьект с чем-нибудь
    public GameObject checkCollisions(GameObject gameObject)
    {
        CollisionMap currentCollisionMap = this.getLayer(gameObject.getLayer());
        for (GameObject obj: currentCollisionMap.getAllStaticObjects()) {
            // обновили прямоугольнички относительно наших 2-х объектов
            updateRects(gameObject, obj);
            //if(Intersector.intersectSegmentRectangle(rect1.x, rect1.y,rect1.width+rect1.x,rect1.height+rect1.y, rect2)) {
            // для игрока сейчас используем кружочек, а все остальное прямоугольнички
            if(Intersector.overlaps(circle, rect2)){
                // действия при коллизии
               //event.setCollidedGameObject(obj);
               //event.setType(CollisionEvent.EventType.ON_COLLISION);
               //event.setObjectType(CollisionEvent.CollisionObjectType.STATIC);
               //gameObject.fire(event);
                Gdx.app.log("COLLISION", rect1.toString() + rect2.toString());
                return obj;
            }
        }
        return null;
    }

    public boolean checkCollisionRects(GameObject obj1, GameObject obj2){
        updateRects(obj1, obj2);
        return (Intersector.overlaps(rect1, rect2));
    }

    public boolean checkCollisionCircleRect(GameObject obj1, GameObject obj2){
        updateRects(obj1, obj2);
        return (Intersector.overlaps(circle, rect2));
    }


    private void updateRects(GameObject gameObject, GameObject obj) {
        circle.radius = gameObject.getHeight()/2;
        circle.x = gameObject.position.x + gameObject.getWidth()/2;
        circle.y = gameObject.position.y + gameObject.getHeight()/2;
        rect1.x = gameObject.position.x;
        rect1.y = gameObject.position.y;
        rect1.width = gameObject.getWidth();
        rect1.height = gameObject.getHeight();
        rect2.x = obj.position.x;
        rect2.y = obj.position.y;
        rect2.width = obj.getWidth();
        rect2.height = obj.getHeight();
    }

    public void addNewLayerCollisionMap(){
        collisionMaps.add(new CollisionMap());
    }

    public void addStaticObject(GameObject gameObject, int layer){
        this.getLayer(layer).addStatic(gameObject);
    }

    public void addDynamicObject(GameObject gameObject, int layer){
        this.getLayer(layer).addDynamic(gameObject);
    }

    public void addControlledObject(GameObject gameObject, int layer){
        this.getLayer(layer).addControlled(gameObject);
    }

    private CollisionMap    getLayer(int ind)       {return collisionMaps.get(ind);}
    public  int             getCountCollisionMaps() {return collisionMaps.size;}
}
