package com.mygdx.magegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import static com.mygdx.magegame.Consts.window_h;
import static com.mygdx.magegame.Consts.window_w;


public class GameScreen implements Screen {
    final MageGame game; // Сама игра(?) - взято из туториала

    Array<GameObject> objects; // Все объекты с текстурами
    Array<TextObject> text_objects; // Все текстовые объекты
    TileSet tileSet; // Тайлсет со всеми тайлами карты - возможно в будущем сделать массив

    boolean was_tile_set = true; // Был ли уставновлен тайл
    // По умолчанию установлен в true, чтобы первый клик не спавнил тайл

    boolean need_to_draw_grid = true; // Нужно ли отрисовывать сетку

    // debug-штуки:
    TextObject debug_tool;
    TextObject debug_tool2;

    // Тайл, который будет устанавливаться
    GameObject place_tile;
    int id_of_place_tile = 0; // Айди данного тайла

    OrthographicCamera camera;

    public GameScreen(final MageGame game){
        this.game = game;

        // Создание объектов полей
        objects = new Array<>();
        text_objects = new Array<>();
        tileSet = new TileSet(Gdx.files.internal("spriteset_0.png"), 32); // Загрузка тайлсета

        // Создание камеры
        camera = new OrthographicCamera();
        camera.setToOrtho(true,window_w, window_h);

        // Тестовое создание объектов
        Vector3 spawn_place = new Vector3();
        for(int i = 0; i< 10; i++){
            for (int j=0;j<10;j++){
                spawn_place.set((i*32) - (float)window_w/2, (j*32) - (float)window_h/2, 0);
                //camera.unproject(spawn_place);
                GameObject go = new GameObject(tileSet, 0, (int)spawn_place.x, (int)spawn_place.y);
                objects.add(go);
            }
        }

        // Тестовое создание текста:
        debug_tool = new TextObject(0,0,"Text 1");
        debug_tool2 = new TextObject(0,10,"Text 2");
        text_objects.add(debug_tool, debug_tool2);

        place_tile = new GameObject(tileSet, id_of_place_tile, -window_w/2, window_h/2-tileSet.size);
        objects.add(place_tile);
        text_objects.add(new TextObject(-window_w/2 + tileSet.size, window_h/2 - tileSet.size/2, "<- This tile will be set"));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // Зарисовка фона
        Gdx.gl.glClearColor(0.2f,0.2f,0.2f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update(); // Обновление камеры

        // Отрисовка всех спрайтов должна происходить вот здесь
        game.batch.begin();
        for (GameObject current_object : objects) {
            current_object.object_sprite.draw(game.batch);
            // TODO: Нужно как-то двигать все тексутурки относительно камеры
        }
        for (TextObject current_text_object: text_objects){
            game.font.draw(game.batch,
                    current_text_object.text,
                    current_text_object.screen_x,
                    current_text_object.screen_y);
        }

        // Отрисовка сетки:
        if (need_to_draw_grid) {
            Pixmap pixmap = new Pixmap(window_w, window_h, Pixmap.Format.RGBA8888); // Штука для хранения пикселей
            pixmap.setColor(0.8f, 0.8f, 0.8f, 0.75f);

            // Горизонтальные линии
            for (int y = 0; y <= window_h; y += tileSet.size) {
                pixmap.drawLine(0, y, window_w, y);
            }
            // Вертикальные линии
            for (int x = 0; x <= window_w; x += tileSet.size) {
                pixmap.drawLine(x, 0, x, window_h);
            }
            // TODO: двигать сетку относительно положения камеры
            Texture pixmaptex = new Texture(pixmap); // Перевод в текстуру
            pixmap.dispose();
            game.batch.draw(pixmaptex, 0 - (float) window_w / 2, -window_h + (float) window_h / 2); // Отрисовка сетки
        }
        game.batch.end();


        // Обработка нажатий на окно
        if (Gdx.input.isTouched()) { // Реагирует на нажатия мыши и пальца
            if (!was_tile_set) {
                // Создание нового объекта в месте нажатия

                // Получение места нажатия
                Vector3 touchPos = new Vector3();
                touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);

                debug_tool2.set_text(String.format("Coord before unproject (%d, %d)", (int) touchPos.x, (int) touchPos.y));

                // camera.unproject(touchPos);

                // Собственно создание объекта
                // TODO: Пофиксить баг с неправильным размещением тайлов
                int actual_x = ((int) touchPos.x - tileSet.size / 2 - window_w / 2)/tileSet.size * tileSet.size - tileSet.size/2;
                int actual_y = (window_h - (int) touchPos.y - tileSet.size / 2 - window_h / 2)/tileSet.size * tileSet.size - tileSet.size/2;

                // TODO: Нужно разобраться, как правильно интерпретировать координаты
                GameObject new_go = new GameObject(tileSet,
                        id_of_place_tile,
                        //(int) touchPos.x - tileSet.size / 2 - window_w / 2,
                        actual_x,
                        //window_h - (int) touchPos.y - tileSet.size / 2 - window_h / 2
                        actual_y);

                // TODO: Нужно ЗАМЕЩАТЬ тайлы на полу (если они там есть), а не накладывать новые
                objects.add(new_go);

                debug_tool.set_text(String.format("Coord after unproject (%d, %d)", (int) touchPos.x, (int) touchPos.y));
                was_tile_set = true;
            }
        }
        else {
            was_tile_set = false;
        }

        // Обработка нажатий на клавиатуру
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)){
            place_tile.set_id(0);
            id_of_place_tile = 0;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)){
            place_tile.set_id(1);
            id_of_place_tile = 1;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)){
            place_tile.set_id(2);
            id_of_place_tile = 2;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)){
            place_tile.set_id(3);
            id_of_place_tile = 3;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.MINUS)){ // По нажатию на минус можно переключить отображение сетки
            need_to_draw_grid = !need_to_draw_grid;
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
