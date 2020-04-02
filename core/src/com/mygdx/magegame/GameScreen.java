package com.mygdx.magegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.mygdx.magegame.model.World;
import com.mygdx.magegame.objects.GameObject;
import com.mygdx.magegame.objects.MapTile;
import com.mygdx.magegame.objects.TextObject;

import static com.mygdx.magegame.Consts.window_h;
import static com.mygdx.magegame.Consts.window_w;


public class GameScreen implements Screen, InputProcessor {
    final MageGame game; // Сама игра(?) - взято из туториала
    World world;

    boolean was_tile_set = true; // Был ли уставновлен тайл
    // По умолчанию установлен в true, чтобы первый клик не спавнил тайл

    boolean need_to_draw_grid = true; // Нужно ли отрисовывать сетку

    // debug-штуки:
    TextObject debug_tool;
    TextObject debug_tool2;
    TextObject debug_tool3;
    Texture pixmaptex; // Текстура сетки для отрисовки

    // Тайл, который будет устанавливаться
    MapTile place_tile;
    int id_of_place_tile = 0; // Айди данного тайла

    //OrthographicCamera camera;

    Group topLevel;
    Group mapLevel;
    //тестовое
    //Texture texture;
    TextureRegion[] regions;

    public GameScreen(final MageGame game){
        this.game = game;

        // Создание объектов полей
        world = new World(50,20);


        // Создание камеры
        //camera = new OrthographicCamera();
        //camera.setToOrtho(true,0, 0);

        //topLevel = new Group(); // Actor-ы интерфейса идут сюда
        //mapLevel = new Group(); // Actor-ы мира идут сюда

        //regions = new TextureRegion[8 * 8];
        Gdx.input.setInputProcessor(world);

        //// texture = new Texture();
        //for (int y = 0; y < max_y; y++) {
        //    for (int x = 0; x < max_x; x++) {
        //        regions[x + y * max_x] = new TextureRegion(world.tileSet.texture,
        //                x * world.tileSet.size,
        //                y * world.tileSet.size,
        //                world.tileSet.size,
        //                world.tileSet.size);
        //    }
        //}

        //Random rand = new Random();
        //for (int y = 0, i = 0; y < world.worldHeight; y++) {
        //    for (int x = 0; x < world.worldWidth; x++) {
        //        Image img = new Image(regions[rand.nextInt(max_x*max_y)]);
        //        img.setBounds(x, y, 1, 1);
        //        mapLevel.addActor(img);
        //        i++;
        //    }
        //}
        //Image img = new Image(regions[0]);
        //img.setBounds(0,0,1,1);
        //mapLevel.addActor(img);

        //MapTile mt = new MapTile(tileSet, world, 0, 1,0,false);

        //topLevel.addActor(mt);

        // Отладчный текст или текст интерфейса:
        debug_tool = new TextObject(world,50,100,"Text 1",false);
        debug_tool2 = new TextObject(world,50,80,"Text 2", false);
        debug_tool3 = new TextObject(world,50, 60, "Text 3", false);
        debug_tool3.set_text(String.format("Camera coords: (%d, %d)",
                (int)world.getCamera().position.x,
                (int)world.getCamera().position.y));
        world.texts.add(debug_tool, debug_tool2,debug_tool3);
        //topLevel.addActor(debug_tool);
        //topLevel.addActor(debug_tool2);
        //topLevel.addActor(debug_tool3);

        // interface_objects.add(debug_tool, debug_tool2, debug_tool3);

        // Объект интерфейса
        //place_tile = new MapTile(tileSet, world, id_of_place_tile, 0, 0, false);
        //topLevel.addActor(place_tile);
        //topLevel.addActor(new TextObject(world,-window_w/2 + tileSet.size, window_h/2 - tileSet.size/2, "<- This tile will be set", false));


        world.setKeyboardFocus(world.getPlayer());
        world.addActor(world.getPlayer());
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // Зарисовка фона
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // обновит каждого актера на время delta
        world.act(Gdx.graphics.getDeltaTime());
        world.getBatch().disableBlending();
        //
        // Group root = world.getRoot();
        // Array<Actor> actors = root.getChildren();
        //for(int i = 0; i < actors.size; i++) {
        //	actors.get(i).setRotation(actors.get(i).getRotation() + 45 * Gdx.graphics.getDeltaTime());
        //}

        world.draw(); // вызовет actor.draw каждому актеру
        world.act();

        //Gdx.gl.glClearColor(0.2f,0.2f,0.2f,1);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //camera.update(); // Обновление камеры

        game.batch.begin();
        for (GameObject current_text: world.texts){
            current_text.draw(game.batch, 1);
        }

        // Отрисовка сетки:
        if (need_to_draw_grid) {
            if (pixmaptex == null) { // Если текстуры сетки ещё нет
                Pixmap pixmap = new Pixmap(window_w, window_h, Pixmap.Format.RGBA8888); // Штука для хранения пикселей
                pixmap.setColor(0.8f, 0.8f, 0.8f, 0.75f);

                // Горизонтальные линии
                for (int y = 10; y >= -10; y--) {
                    if (y <= 0) {
                        // Красным рисуется всё, что находится в БОЛЬШЕЙ половине (положительной?)
                        pixmap.setColor(1.0f, 0.2f, 0.2f, 0.9f);
                    }
                    //TODO: Сделать размеры сетки НОРМАЛЬНЫМИ и правильными (вместо "32).
                    pixmap.drawLine(0, window_h / 2 + y * 32, window_w, window_h / 2 + y * 32);
                }
                pixmap.setColor(0.8f, 0.8f, 0.8f, 0.75f);
                // Вертикальные линии
                for (int x = -10; x <= 10; x++) {
                    if (x >= 0) {
                        // Красным рисуется всё, что находится в БОЛЬШЕЙ половине (положительной?)
                        pixmap.setColor(1.0f, 0.2f, 0.2f, 0.9f);
                    }
                    //TODO: Сделать размеры сетки НОРМАЛЬНЫМИ и правильными (вместо "32).
                    pixmap.drawLine(window_w / 2 + x * 32, 0, window_w / 2 + x * 32, window_h);
                }
                pixmap.setColor(0.2f, 0.8f, 0.8f, 0.75f);
                pixmap.drawLine(window_w / 2, 0, window_w / 2, window_h);
                pixmap.drawLine(0, window_h / 2, window_w, window_h / 2);
                pixmaptex = new Texture(pixmap); // Перевод в текстуру
                pixmap.dispose();
            }
            // Отрисовка сетки
            game.batch.draw(pixmaptex,
                    (world.getCamera().position.x%32),
                    (world.getCamera().position.y%32));
        }
        game.batch.end();
//
//
        // Обработка нажатий на окно
        if (Gdx.input.isTouched() && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) { // Реагирует на нажатия ЛКМ
            if (!was_tile_set) {
                // Создание нового объекта в месте нажатия

                // Получение места нажатия
                Vector3 touchPos = new Vector3();
                touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);

                // Координаты нажатия в окне
                debug_tool2.set_text(String.format("Clicked coords: (%d, %d)",
                        (int)touchPos.x,
                        (int) touchPos.y)
                );

                world.getCamera().unproject(touchPos); // Не нужно СЕЙЧАС, но очень пригодится, когда камера будет двигаться

                //// Собственно создание объекта
                int actual_x = (int) touchPos.x ;
                int actual_y = (int) touchPos.y ;

                // Не работаем в отрицательных областях! Иначе ловим баг с неправильным размещением!

                //int grid_x = (actual_x/tileSet.size * tileSet.size );
                //int grid_y = (actual_y/tileSet.size * tileSet.size );

                // Координаты добавляемого объекта
                debug_tool.set_text(String.format("World coords: (%d, %d)",actual_x,actual_y));

                MapTile new_go = new MapTile(world,0,id_of_place_tile,actual_x, actual_y, true);

                // TODO: Нужно ЗАМЕЩАТЬ тайлы на полу (если они там есть), а не накладывать новые*
                // * - На самом деле верхний комментарий не верен на все 100. Есть некоторые текстуры, которые ДОЛЖНЫ накладываться
                // поверх других, например, колонны. Но так как мир у нас с "глубиной", есть смысл выносить их на отдельный уровень.
                // Так что да: действительно нужно ЗАМЕЩАТЬ тайлы.
                //
                // Очень грубо такое можно сделать с помощью проверки всех тайлов данного уровня и удаления того, чьи координаты
                // совпадают с добавляемым тайлом. Почему плохо - такое сработает, если все тайлы поставлены ОЧЕНЬ чётко и
                // точно (что, в общем-то, пока что соблюдается).

                world.add_object(new_go);

                //world.add_object(new_go, actual_x, actual_y);

                //Gdx.app.log("Tag",String.format("%d, %d", (int)camera.position.x, (int)camera.position.y));

                was_tile_set = true;
            }
        }
        else {
            was_tile_set = false;
        }
//
        // Обработка нажатий на клавиатуру
        //if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)){
        //    place_tile.set_texture(0);
        //    id_of_place_tile = 0;
        //}
        //if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)){
        //    place_tile.set_texture(1);
        //    id_of_place_tile = 1;
        //}
        //if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)){
        //    place_tile.set_texture(2);
        //    id_of_place_tile = 2;
        //}
        //if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)){
        //    place_tile.set_texture(3);
        //    id_of_place_tile = 3;
        //}
        if(Gdx.input.isKeyJustPressed(Input.Keys.MINUS)){ // По нажатию на минус можно переключить отображение сетки
            need_to_draw_grid = !need_to_draw_grid;
        }
//

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            world.getCamera().translate(-1, 0 ,0);
            //debug_tool3.set_text(String.format("Camera coords: (%d, %d)", (int)world.getCamera().position.x, (int)world.getCamera().position.y));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            world.getCamera().translate(1, 0 ,0);
            //debug_tool3.set_text(String.format("Camera coords: (%d, %d)", (int)world.getCamera().position.x, (int)world.getCamera().position.y));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)){
            world.getCamera().translate(0, 1 ,0);
            //debug_tool3.set_text(String.format("Camera coords: (%d, %d)", (int)world.getCamera().position.x, (int)world.getCamera().position.y));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            world.getCamera().translate(0, -1 ,0);
            //debug_tool3.set_text(String.format("Camera coords: (%d, %d)", (int)world.getCamera().position.x, (int)world.getCamera().position.y));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.P)){
            world.getViewport().update(
                    world.getViewport().getScreenWidth() + 10,
                    world.getViewport().getScreenHeight() + 10,
                    false);
            Gdx.app.log("Viewport",String.format("current width/height %d, %d",
                    world.getViewport().getScreenWidth(),
                    world.getViewport().getScreenHeight())
            );
        }
        if (Gdx.input.isKeyPressed(Input.Keys.O)){
            world.getViewport().update(
                    world.getViewport().getScreenWidth() - 10,
                    world.getViewport().getScreenHeight() - 10,
                    false);
            Gdx.app.log("Viewport",String.format("current width/height %d, %d",
                    world.getViewport().getScreenWidth(),
                    world.getViewport().getScreenHeight())
            );
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)){
            world.save("map.txt");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.L)){
            world.load("map.txt");
        }
    }

    @Override
    public void resize(int width, int height) {
        world.getCamera().viewportWidth = width;
        world.getCamera().viewportHeight = height;
        world.getViewport().update(width, height, false);
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
        world.dispose();
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public boolean keyDown(int keycode) {
        return world.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return world.keyUp(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return world.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return world.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
