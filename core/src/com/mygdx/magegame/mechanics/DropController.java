package com.mygdx.magegame.mechanics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.mygdx.magegame.objects.GameObject;
import com.mygdx.magegame.objects.MapTile;
import com.mygdx.magegame.world.World;

public class DropController {
    Array<Vector2> contactPoints = new Array<Vector2>(){};
    World world;

    public DropController(World world) {
        this.world = world;
        contactPoints.add(new Vector2());
        contactPoints.add(new Vector2());
        contactPoints.add(new Vector2());
        contactPoints.add(new Vector2());
    }

    private void calculateContactPoints(GameObject gameObject){
        contactPoints.get(0).set(gameObject.getX()+gameObject.getWidth()/3,gameObject.getY()+gameObject.getHeight()/3);
        contactPoints.get(1).set(gameObject.getX()+gameObject.getWidth()*2/3,gameObject.getY()+gameObject.getHeight()/3);
        contactPoints.get(2).set(gameObject.getX()+gameObject.getWidth()/3,gameObject.getY()+gameObject.getHeight()*2/3);
        contactPoints.get(3).set(gameObject.getX()+gameObject.getWidth()*2/3,gameObject.getY()+gameObject.getHeight()*2/3);
    }

    /**
     * @param gameObject - проверяемый обьект, если он не Dropable вернет true
     * @return true - под нами есть что-то твердое
     * @return false - под нами ничего твердого нет
     */
    public boolean checkFloorUnderObject(GameObject gameObject){
        if(!(gameObject instanceof Dropable))
            return true;
        calculateContactPoints(gameObject);
        Group temp = world.getMap().get_layer(gameObject.getLayer()-1);
        Actor t;
        // есть ли под ногами что-то твердое
        boolean floorIsSolid = false;
        int tem = 0;

        for (Vector2 point:contactPoints) {
            if ((t = temp.hit(point.x, point.y, false)) != null) {
                if (t instanceof MapTile) { //
                    if (((MapTile) t).is_solid && ((MapTile) t).getLayer() == gameObject.getLayer() - 1) {
                        floorIsSolid = true;
                        tem++;
                        if(tem > 1)
                            break;
                    }
                }
            }
        }
        // если под ногами ничего нет или точка опоры всего одна
        if(!floorIsSolid || tem == 1){
          return false;
        }
        return true;
    }

    /**
     * @param gameObject обьект опускается по своей координате z на 1 вниз
     *                   если этот обьект был текущим игроком мира, то камера опускается вместе с ним
     */
    public void dropGameObject(GameObject gameObject){
        gameObject.position.z -= 1;
        if(gameObject == world.getPlayer())
            world.setCurrent_z(gameObject.getLayer()-1);
        //Gdx.app.log("PLAYER", " DOWN " + world.getCurrent_z() + " " + gameObject.getLayer() + " ");
    }
}
