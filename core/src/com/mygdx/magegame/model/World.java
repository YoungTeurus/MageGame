package com.mygdx.magegame.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.magegame.legasy.GameObjectInterface;
import com.mygdx.magegame.objects.GameObject;
import com.mygdx.magegame.objects.Player;
import com.mygdx.magegame.objects.TextObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

import static com.mygdx.magegame.Consts.window_h;
import static com.mygdx.magegame.Consts.window_w;

public class World extends Stage {
    // наш игрок
    public Player player;
    // массив объектов на карте
    public Array<GameObject> texts;

    // ширина и высота мира
    public int worldWidth;
    public int worldHeight;
    // Штука для отрисовки текстов
    public BitmapFont font;

    public World(int worldWidth, int worldHeight)
    {
        super(new ExtendViewport(worldWidth, worldHeight));

        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        //map = new GameObject[worldWidth][worldHeight];
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
    public void dispose() {
        super.dispose();
        font.dispose();
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
            Pattern pattern2 = Pattern.compile("((\d+,\d+)([,][ ])?){3}"); // Поиск координат в каждой из строк

            while (scan.hasNextLine()){
                String cur_line = scan.nextLine();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
