package com.mygdx.magegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import static com.mygdx.magegame.Consts.window_h;
import static com.mygdx.magegame.Consts.window_w;


public class GameScreen implements Screen {
    final MageGame game; // Сама игра(?) - взято из туториала

    Array<GameObject> objects; // Все объекты с текстурами
    Array<TextObject> text_objects; // Все текстовые объекты
    TextObject debug_tool;
    TextObject debug_tool2;
    TileSet tileSet; // Тайлсет со всеми тайлами карты - возможно в будущем сделать массив

    OrthographicCamera camera;

    public GameScreen(final MageGame game){
        this.game = game;

        // Создание объектов полей
        objects = new Array<>();
        text_objects = new Array<>();
        tileSet = new TileSet(Gdx.files.internal("spriteset_0.png"), 32); // Загрузка тайлсета

        // Создание камеры
        camera = new OrthographicCamera();
        camera.setToOrtho(false,window_w, window_h);

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
        game.batch.end();


        // Обработка нажатий на окно
        if (Gdx.input.isTouched()) { // Реагирует на нажатия мыши и пальца
            // Создание нового объекта в месте нажатия

            // Получение места нажатия
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);

            debug_tool2.set_text(String.format("Coord before unproject (%d, %d)", (int)touchPos.x, (int)touchPos.y));

            camera.unproject(touchPos);

            // Собственно создание объекта
            // TODO: Нужно разобраться, как правильно интерпретировать координаты
            GameObject new_go = new GameObject(tileSet,
                    0,
                    (int)touchPos.x - tileSet.size/2 - window_w/2,
                    (int)touchPos.y- tileSet.size/2 - window_h/2);
            objects.add(new_go);

            debug_tool.set_text(String.format("Coord after unproject (%d, %d)", (int)touchPos.x, (int)touchPos.y));
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
