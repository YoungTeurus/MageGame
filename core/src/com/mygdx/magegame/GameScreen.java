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

    Array<GameObjectInterface> all_objects; // Все объекты
    //Array<GameObject> objects; // Все объекты с текстурами
    //Array<GameObject> non_movable_objects; // Сюда должны помещаться все объекты, которые не должны двигаться вместе с камерой
    //Array<TextObject> text_objects; // Все текстовые объекты
    //Array<TextObject> non_movable_text_objects; // Все независящие от камеры объекты
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
        all_objects = new Array<>();
        tileSet = new TileSet(Gdx.files.internal("spriteset_0.png"), 32); // Загрузка тайлсета

        // Создание камеры
        camera = new OrthographicCamera();
        camera.setToOrtho(true,window_w, window_h);

        // Тестовое создание текста:
        debug_tool = new TextObject(0,0,"Text 1",false);
        debug_tool2 = new TextObject(0,10,"Text 2", false);
        all_objects.add(debug_tool, debug_tool2);

        place_tile = new GameObject(tileSet, id_of_place_tile, -window_w/2, window_h/2-tileSet.size, false);
        all_objects.add(place_tile);
        all_objects.add(new TextObject(-window_w/2 + tileSet.size, window_h/2 - tileSet.size/2, "<- This tile will be set", false));
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
        for (GameObjectInterface current_object: all_objects){
            current_object.draw(game.batch, game.font, camera);
        }
        //for (GameObject current_object : objects) {
        //    game.batch.draw(current_object.object_texture_region,
        //            current_object.world_x + camera.position.x,
        //            current_object.world_y + camera.position.y);
        //    // current_object.object_sprite.draw(game.batch);
        //}
        //for (GameObject current_object : non_movable_objects) {
        //    // Отрисовка всех объектов, чьё положение не должно зависеть от камеры
        //    game.batch.draw(current_object.object_texture_region,
        //            current_object.world_x,
        //            current_object.world_y);
        //    // current_object.object_sprite.draw(game.batch);
        //}
        //for (TextObject current_text_object: text_objects){
        //    game.font.draw(game.batch,
        //            current_text_object.text,
        //            current_text_object.world_x + camera.position.x,
        //            current_text_object.world_y + camera.position.y);
        //}
        //for (TextObject current_text_object: non_movable_text_objects){
        //    game.font.draw(game.batch,
        //            current_text_object.text,
        //            current_text_object.world_x,
        //            current_text_object.world_y);
        //}

        // Отрисовка сетки:
        if (need_to_draw_grid) {
            Pixmap pixmap = new Pixmap(window_w, window_h, Pixmap.Format.RGBA8888); // Штука для хранения пикселей
            pixmap.setColor(0.8f, 0.8f, 0.8f, 0.75f);

            // Горизонтальные линии
            for (int y = 0; y <= window_h; y += tileSet.size) {
                if (y >= window_h/2){
                    // Красным рисуется всё, что находится в БОЛЬШЕЙ половине (положительной?)
                    pixmap.setColor(1.0f,0.2f,0.2f, 0.9f);
                }
                pixmap.drawLine(0, y, window_w, y);
            }
            pixmap.setColor(0.8f, 0.8f, 0.8f, 0.75f);
            // Вертикальные линии
            for (int x = 0; x <= window_w; x += tileSet.size) {
                if (x >= window_w/2){
                    // Красным рисуется всё, что находится в БОЛЬШЕЙ половине (положительной?)
                    pixmap.setColor(1.0f,0.2f,0.2f, 0.9f);
                }
                pixmap.drawLine(x, 0, x, window_h);
            }
            pixmap.setColor(0.8f, 0.8f, 0.8f, 0.75f);
            pixmap.drawLine(-window_w,-window_h,window_w,window_h); // Диагональная линия
            // TODO: двигать сетку относительно положения камеры
            Texture pixmaptex = new Texture(pixmap); // Перевод в текстуру
            pixmap.dispose();
            //game.batch.draw(pixmaptex, 0 - (float) window_w / 2, -window_h + (float) window_h / 2); // Отрисовка сетки
            game.batch.draw(pixmaptex,
                    (float)-window_w/2 + camera.position.x,
                    (float)-window_h/2 + camera.position.y);
        }
        game.batch.end();


        // Обработка нажатий на окно
        if (Gdx.input.isTouched() && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) { // Реагирует на нажатия ЛКМ
            if (!was_tile_set) {
                // Создание нового объекта в месте нажатия

                // Получение места нажатия
                Vector3 touchPos = new Vector3();
                touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);

                // Координаты нажатия в мире
                debug_tool2.set_text(String.format("Clicked coords: (%d, %d)",
                        (int)touchPos.x,
                        window_h - (int) touchPos.y)
                );

                camera.unproject(touchPos); // Не нужно СЕЙЧАС, но очень пригодится, когда камера будет двигаться

                // Собственно создание объекта
                // TODO: Пофиксить баг с неправильным размещением тайлов
                int actual_x = ((int) touchPos.x);
                int actual_y = (window_h - (int) touchPos.y);

                // Координаты добавляемого объекта
                debug_tool.set_text(String.format("Placed coords: (%d, %d)",actual_x,actual_y));

                // TODO: Нужно разобраться, как правильно интерпретировать координаты
                GameObject new_go = new GameObject(tileSet,
                        id_of_place_tile,
                        //(int) touchPos.x - tileSet.size / 2 - window_w / 2,
                        actual_x,
                        //window_h - (int) touchPos.y - tileSet.size / 2 - window_h / 2
                        actual_y,
                        true);

                // TODO: Нужно ЗАМЕЩАТЬ тайлы на полу (если они там есть), а не накладывать новые*
                // * - На самом деле верхний комментарий не верен на все 100. Есть некоторые текстуры, которые ДОЛЖНЫ накладываться
                // поверх других, например, колонны. Но так как мир у нас с "глубиной", есть смысл выносить их на отдельный уровень.
                // Так что да: действительно нужно ЗАМЕЩАТЬ тайлы.
                //
                // Очень грубо такое можно сделать с помощью проверки всех тайлов данного уровня и удаления того, чьи координаты
                // совпадают с добавляемым тайлом. Почему плохо - такое сработает, если все тайлы поставлены ОЧЕНЬ чётко и
                // точно (что, в общем-то, пока что соблюдается).
                all_objects.add(new_go);

                Gdx.app.log("Tag",String.format("%d, %d", (int)camera.position.x, (int)camera.position.y));

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

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            camera.translate(-3, 0 ,0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            camera.translate(3, 0 ,0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)){
            camera.translate(0, 3 ,0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            camera.translate(0, -3 ,0);
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
