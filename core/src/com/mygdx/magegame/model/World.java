package com.mygdx.magegame.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.magegame.TileSet;
import com.mygdx.magegame.legasy.GameObjectInterface;
import com.mygdx.magegame.objects.GameObject;
import com.mygdx.magegame.objects.MapTile;
import com.mygdx.magegame.objects.Player;
import com.mygdx.magegame.objects.TextObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class World extends Stage {
    // наш игрок
    public Player player;
    // массив объектов на карте
    public Array<GameObject> texts;

    public TileSet tileSet; // Тайлсет со всеми тайлами карты - возможно в будущем сделать массив

    // ширина и высота мира
    public int worldWidth;
    public int worldHeight;
    // Штука для отрисовки текстов
    public BitmapFont font;

    public Actor selectedActor;
    // чтобы каждый раз их не создавать
    private Vector3 mouseCoords3 = new Vector3(0,0, 0);
    private Vector2 mouseCoords2 = new Vector2(0,0);

    public World(int worldWidth, int worldHeight)
    {
        super(new ExtendViewport(worldWidth, worldHeight));

        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        tileSet = new TileSet(Gdx.files.internal("spriteset_0.png"), 32); // Загрузка тайлсета
        font = new BitmapFont();
        texts = new Array<>();
        createWorld();
    }

    public void add_object(GameObject added_object){
        Gdx.app.log("Children",String.format("cildern num %d", getRoot().getChildren().size));
        boolean is_blocked = false;
        for (int i = 0; i < getRoot().getChildren().size; i++) {
            if (getRoot().getChild(i).getX() == added_object.getX() &&
                    getRoot().getChild(i).getY() == added_object.getY()){
                Gdx.app.log("Children", "this tile is blocked");
                is_blocked = true;
            }
        }
        if (!is_blocked){
            Gdx.app.log("Children", "placed" + added_object.toString());
            addActor(added_object);
        }
    }

    private void createWorld(){
        player = new Player(this, 0,0,0);
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean keyDown(int keyCode) { return super.keyDown(keyCode); }

    @Override
    public boolean keyUp(int keyCode) {
        return super.keyUp(keyCode);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        boolean b = super.touchDown(screenX, screenY, pointer, button);
        mouseCoords3.x = screenX;
        mouseCoords3.y = screenY;
        getCamera().unproject(mouseCoords3);
        mouseCoords2.x = mouseCoords3.x;
        mouseCoords2.y = mouseCoords3.y;
        moveSelected(mouseCoords2);
        return true;
    }

    private void moveSelected(Vector2 mouseCoords){
        if(selectedActor != null && selectedActor instanceof Player)
            ((Player)selectedActor).handleMouseInput(mouseCoords);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        super.touchUp(screenX, screenY, pointer, button);
        resetSelected();
        return true;
    }

    private void resetSelected(){
        if(selectedActor != null && selectedActor instanceof Player)
            ((Player)selectedActor).resetWay();
    }


    @Override
    public Actor hit(float x, float y, boolean touchable) {
        Actor  actor  = super.hit(x,y,touchable);
        //если выбрали актёра
        if(actor != null)
            //запоминаем
            selectedActor = actor;
        return actor;
    }


    @Override
    public void dispose() {
        super.dispose();
        font.dispose();
        //tileSet.dispose();
    }

    public void save(String filename){
        try {
            FileWriter fw = new FileWriter(filename);

            for (Actor cur_actor: getRoot().getChildren()){
                fw.write(cur_actor.toString() + "\n");
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(String filename){
        try{
            FileReader fr = new FileReader(filename);
            Scanner scan = new Scanner(fr);

            int line = 0;
            Pattern pattern1 = Pattern.compile("\\w+\\{.+\\}"); // Поиск строк, подходящих для наших объектов
            Pattern pattern2 = Pattern.compile("((\\d+,\\d+)([,][\\s])?){3}"); // Поиск координат в каждой из строк
            Pattern pattern3 = Pattern.compile("\\d+,\\d+"); // Поиск одного float

            float[] for_coords = new float[3];

            while (scan.hasNextLine()){
                String cur_line = scan.nextLine();
                Gdx.app.log("Load", "String" + line + " :" + cur_line);
                Matcher matcher1 = pattern1.matcher(cur_line);
                Matcher matcher2 = pattern2.matcher(cur_line);
                Matcher matcher3 = pattern3.matcher(cur_line);
                if (matcher1.find()){
                    Gdx.app.log("Load", "String" + line + " : was found pattern 1");
                    if (matcher2.find()){ // Находим координаты
                        int start = matcher2.start();
                        int end = matcher2.end();
                        String coords = cur_line.substring(start, end);
                        Gdx.app.log("Load", "String" + line + " : was found pattern 2 :" + coords);
                        int i = 0;
                        while (matcher3.find()){
                            int startf = matcher3.start();
                            int endf = matcher3.end();
                            String stringf = cur_line.substring(startf, endf).replace(",",".");
                            float fl = Float.parseFloat(stringf);
                            Gdx.app.log("Load", "String" + line + " : was found pattern 3 :" + fl);
                            for_coords[i] = fl;
                            i++;
                        }

                        MapTile new_object = new MapTile(tileSet,this,0,(int)for_coords[0],(int)for_coords[1],true);
                        add_object(new_object);
                    }

                }
                line++;
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
