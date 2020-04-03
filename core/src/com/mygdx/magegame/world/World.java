package com.mygdx.magegame.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.magegame.TileSet;
import com.mygdx.magegame.objects.GameObject;
import com.mygdx.magegame.objects.MapTile;
import com.mygdx.magegame.objects.Player;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mygdx.magegame.Consts.*;

public class World extends Stage {
    public TileSet[] tileSets = new TileSet[num_of_tilesets]; // Все тайлсеты для данного мира

    // наш игрок
    public Player player;
    public int current_z; // Текущий слой, на котором находится игрок (камера?). Может быть и не нужна, но пока что пусть будет
    // массив объектов на карте
    TiledLayer map;
    public Array<GameObject> texts;

    // ширина и высота мира
    public int worldWidth;
    public int worldHeight;
    // Штука для отрисовки текстов
    public BitmapFont font;

    public Actor selectedActor;
    // чтобы каждый раз их не создавать
    private Vector3 mouseCoords3 = new Vector3(0,0, 0);
    private Vector2 mouseCoords2 = new Vector2(0,0);
    private boolean mouseRightButtonPressed = false;

    public World(int worldWidth, int worldHeight)
    {
        super(new ExtendViewport(worldWidth, worldHeight));

        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;

        // Загрузка всех тайлсетов
        for(int tileset_id = 0; tileset_id < num_of_tilesets; tileset_id++){
            tileSets[tileset_id] = new TileSet(Gdx.files.internal(tilesets_filenames[tileset_id]), tilesets_sizes[tileset_id]);
        }

        font = new BitmapFont();
        texts = new Array<>();
        map = new TiledLayer();

        createWorld();
    }

    public void add_object(GameObject added_object){
        boolean is_blocked = false;
        Group layer_to_work_with = map.get_layer((int)added_object.position.z);
        Gdx.app.log("Children",String.format("cildern num %d", layer_to_work_with.getChildren().size));
        for (int i = 0; i < layer_to_work_with.getChildren().size; i++) {
            if ( // Если координаты полностью совпадают
               ((GameObject)layer_to_work_with.getChild(i)).position.x == added_object.position.x
            && ((GameObject)layer_to_work_with.getChild(i)).position.y == added_object.position.y
            // && ((GameObject)layer_to_work_with.getChild(i)).position.z == added_object.position.z // Z не нужно проверять, ведь все сейчас на одном уровне?
            )
            {
                Gdx.app.log("Children", "this tile is blocked");
                is_blocked = true;
                break;
            }
            //if (getRoot().getChild(i).getX() == added_object.getX() &&
            //        getRoot().getChild(i).getY() == added_object.getY()){
            //
            //}
        }
        if (!is_blocked){
            Gdx.app.log("Children", "placed" + added_object.toString());

            map.get_layer((int)added_object.position.z).addActor(added_object);

            //addActor(added_object);
        }
    }

    private void createWorld(){
        current_z = 0;
        player = new Player(this, 0,0,0, 0);
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

        updateMouseCoords(screenX, screenY);
        // пока что всегда передаем правый клик игроку
        if(button == Input.Buttons.RIGHT) {
            Gdx.app.log("Mouse", "PRESSED");
            player.handleMouseInput(mouseCoords2);
            mouseRightButtonPressed = true;
        }
        //moveSelected(mouseCoords2);
        return true;
    }

    private void updateMouseCoords(int screenX, int screenY) {
        mouseCoords3.x = screenX;
        mouseCoords3.y = screenY;
        // получили координаты клика мышкой относительно нашего мира
        getCamera().unproject(mouseCoords3);
        mouseCoords2.x = mouseCoords3.x;
        mouseCoords2.y = mouseCoords3.y;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        super.touchDragged(screenX, screenY, pointer);
        // если зажата правая кнопка мышки, и она перемещается, за ней едет наш игрок
        if(mouseRightButtonPressed && player.getState() == Player.State.WALKING) {
            updateMouseCoords(screenX, screenY);
            player.handleMouseInput(mouseCoords2);
        }
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        //при движении мышью
        return super.mouseMoved(screenX, screenY);
    }

    private void moveSelected(Vector2 mouseCoords){
        if(selectedActor != null); // какие-то события мира актерам от мышки
            //((Player)selectedActor).handleMouseInput(mouseCoords);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        super.touchUp(screenX, screenY, pointer, button);
        if(button == Input.Buttons.RIGHT)
            Gdx.app.log("Mouse", "RELEASED");
            mouseRightButtonPressed = false;
        return true;
    }

    private void resetSelected(){
        if(selectedActor != null && selectedActor instanceof Player);
            //((Player)selectedActor).resetWay();
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
        for (TileSet current_tileset: tileSets){
            current_tileset.dispose();
        }
    }

    public void save(String filename){
        // Сохраняет мир в файл
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
        // Загрузка мира из файла
        try{
            getRoot().clear();

            FileReader fr = new FileReader(filename);
            Scanner scan = new Scanner(fr);

            int line = 0;
            Pattern pattern1 = Pattern.compile("\\w+\\{.+}"); // Поиск строк, подходящих для наших объектов
            Pattern pattern2 = Pattern.compile("((-?\\d+,-?\\d+)([,][\\s])?){3}"); // Поиск координат в каждой из строк
            Pattern pattern3 = Pattern.compile("-?\\d+,-?\\d+"); // Поиск одного float
            Pattern pattern4 = Pattern.compile("\\w+\\{"); // Поиск имени объекта
            Pattern pattern5 = Pattern.compile("-?\\d+"); // Поиск всех остальных аргуметов

            float[] for_coords = new float[3];
            String[] other_params = new String[10]; // До 10-и аргументов

            while (scan.hasNextLine()){
                String cur_line = scan.nextLine();
                Gdx.app.log("Load", "String" + line + " :" + cur_line);
                Matcher matcher1 = pattern1.matcher(cur_line);
                Matcher matcher2 = pattern2.matcher(cur_line);
                Matcher matcher3 = pattern3.matcher(cur_line);
                Matcher matcher4 = pattern4.matcher(cur_line);
                if (matcher1.find() && matcher4.find()){
                    Gdx.app.log("Load", "String" + line + " : was found pattern 1");
                    // Находим название класса
                    int start = matcher4.start();
                    int end = matcher4.end();
                    String name_of_class = cur_line.substring(start, end-1);

                    if (matcher2.find()){ // Находим координаты
                        start = matcher2.start();
                        end = matcher2.end();
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
                    }
                    // На данном этапе end хранит позицию закрывающей скобки после координат
                    String string_for_all_other_params = cur_line.substring(end);
                    Matcher matcher5 = pattern5.matcher(string_for_all_other_params);
                    int i = 0;
                    while (matcher5.find()){
                        start = matcher5.start();
                        end = matcher5.end();
                        other_params[i] = string_for_all_other_params.substring(start, end);
                        Gdx.app.log("Load", "String" + line + " : was found pattern 5 :" + other_params[i]);
                        i++;
                    }

                    if (name_of_class.equals("MapTile")){
                        MapTile new_object = new MapTile(this,
                                Integer.parseInt(other_params[0]),
                                Integer.parseInt(other_params[1]),
                                (int)for_coords[0],(int)for_coords[1],(int)for_coords[2],
                                true);
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