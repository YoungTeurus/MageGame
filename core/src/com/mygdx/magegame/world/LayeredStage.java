package com.mygdx.magegame.world;
/*
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

public class LayeredStage extends Stage {
        public static final int WALL_LAYER = 0;
        public static final int FLOOR_LAYER = 1;
        public static final int TILE_LAYER = 2;

        public static final int[] backgroundLayers = { WALL_LAYER, FLOOR_LAYER, TILE_LAYER };

        public static final int OBJECT_LAYER = 3;

        public static final int[] foregroundLayers = { OBJECT_LAYER };

        public static final int TOP_LAYER = 4;

        public LayeredStage(Viewport viewport, Batch batch) {
            super(viewport, batch);
            initLayers();
        }

        public LayeredStage(Viewport viewport) {
            super(viewport);
            initLayers();
        }

        private void initLayers() {
            addActor(new Group());
            addActor(new Group());
            addActor(new TiledLayer(this));
            addActor(new Group());
            addActor(new Group());
        }

        public void addActor(Group group) {
            super.addActor(group);
        }

        @Override
        public void addActor(Actor actor) {
            throw new UnsupportedOperationException("Cant add Type as Layer: " + actor.getClass());
        }

        public void addActor(Actor actor, int layer) {
            getLayer(layer).addActor(actor);
        }

        public Group getLayer(int layer) {
            return (Group) this.getActors().get(layer);
        }

        public TiledLayer getTileLayer() {
            return (TiledLayer) this.getActors().get(TILE_LAYER);
        }
    }
}


 */