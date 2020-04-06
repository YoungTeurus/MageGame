package com.mygdx.magegame.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.magegame.MageGame;
import com.mygdx.magegame.objects.GameObject;
import com.mygdx.magegame.objects.MapTile;
import com.mygdx.magegame.objects.TextObject;
import com.mygdx.magegame.world.World;

import static com.mygdx.magegame.Consts.*;
import static com.mygdx.magegame.Consts.window_h;

public class EditorScreen implements Screen {
    final MageGame game;
    World world;

    // debug-штуки:
    TextObject debug_tool;
    TextObject debug_tool2;
    TextObject debug_tool3;
    TextObject debug_tool4;
    TextObject debug_tool5;
    Texture pixmaptex; // Текстура сетки для отрисовки

    boolean was_tile_set = true; // Был ли уставновлен тайл
    // По умолчанию установлен в true, чтобы первый клик не спавнил тайл

    boolean need_to_draw_grid = true; // Нужно ли отрисовывать сетку

    // Тайл, который будет устанавливаться
    MapTile place_tile;
    int id_of_place_tile = 0; // Айди данного тайла

    EditorScreen(final MageGame game)
    {
        this.game = game;
        // Создание мира
        world = new World(world_w,world_h, false);

        // Отладчный текст или текст интерфейса:
        debug_tool = new TextObject(world,50,100,0,"Text 1",false);
        debug_tool2 = new TextObject(world,50,80,0,"Text 2", false);
        debug_tool3 = new TextObject(world,50, 60,0, "Text 3", false);
        debug_tool4 = new TextObject(world, 50, 40, 0, "Text 4", false); // ID устанавливаемого тайла
        debug_tool5= new TextObject(world, 50, 20, 0, "Text 5", false); // Название устанавливаемого тайла
        debug_tool3.set_text(String.format("Camera coords: (%d, %d)",
                (int)world.getCamera().position.x,
                (int)world.getCamera().position.y));
        place_tile = new MapTile(world, 0,id_of_place_tile,0,0,0,false);
        updateTextsForPlaceTile();
        world.getTexts().add(debug_tool, debug_tool2,debug_tool3, debug_tool4);
        world.getTexts().add(debug_tool5);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // Зарисовка фона
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // обновит каждого актера на время delta
        // world.act(Gdx.graphics.getDeltaTime());
        world.getBatch().disableBlending();
        world.draw(); // вызовет actor.draw каждому актеру

        game.getBatch().begin();

        game.getBatch().draw(place_tile.getObject_texture_region(), 50, 100+place_tile.getSize());

        for (GameObject current_text : world.getTexts()) {
            current_text.draw(game.getBatch(), 1);
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
                    pixmap.drawLine(window_w / 2 + x * 32, 0, window_w / 2 + x * 32, window_h);
                }
                pixmap.setColor(0.2f, 0.8f, 0.8f, 0.75f);
                pixmap.drawLine(window_w / 2, 0, window_w / 2, window_h);
                pixmap.drawLine(0, window_h / 2, window_w, window_h / 2);
                pixmaptex = new Texture(pixmap); // Перевод в текстуру
                pixmap.dispose();
            }
            // Отрисовка сетки
            game.getBatch().draw(pixmaptex,
                    (world.getCamera().position.x % 32),
                    (world.getCamera().position.y % 32));
        }
        game.getBatch().end();

        // Обработка нажатий на окно
        if (Gdx.input.isTouched() && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) { // Реагирует на нажатия ЛКМ
            if (!was_tile_set) {
                // Создание нового объекта в месте нажатия

                // Получение места нажатия
                Vector3 touchPos = new Vector3();
                touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);

                // Координаты нажатия в окне
                debug_tool2.set_text(String.format("Clicked coords: (%d, %d, %d)",
                        (int) touchPos.x,
                        (int) touchPos.y,
                        world.getCurrent_z())
                );
                world.getCamera().unproject(touchPos); // Не нужно СЕЙЧАС, но очень пригодится, когда камера будет двигаться
                int actual_x = (int) touchPos.x;
                int actual_y = (int) touchPos.y;

                // Не работаем в отрицательных областях! Иначе ловим баг с неправильным размещением!

                // Координаты добавляемого объекта
                debug_tool.set_text(String.format("World coords: (%d, %d, %d)", (int)world.getPlayer().position.x,
                        (int)world.getPlayer().position.y, (int)world.getPlayer().position.z));

                if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                    // Если зажат SHIFT, то пробуем удалить тайл
                    world.remove_object(actual_x, actual_y, world.getCurrent_z());
                } else {
                    // Иначе доавбляем его
                    MapTile new_go = new MapTile(world, 0, id_of_place_tile, actual_x, actual_y, world.getCurrent_z(), true);
                    new_go.is_passable = false;

                    world.add_object(new_go);
                    was_tile_set = true;
                }
            }
        } else {
            was_tile_set = false;
        }
//
        // Обработка нажатий на клавиатуру
        if (Gdx.input.isKeyJustPressed(Input.Keys.MINUS)) { // По нажатию на минус можно переключить отображение сетки
            need_to_draw_grid = !need_to_draw_grid;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.EQUALS)) { // По нажатию на равно можно переключить отображение других уровеней
            world.need_to_draw_other_level_rather_than_current = !world.need_to_draw_other_level_rather_than_current;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            world.getCamera().translate(-1, 0, 0);
            debug_tool3.set_text(String.format("Camera coords: (%d, %d, %d)", (int) world.getCamera().position.x, (int) world.getCamera().position.y, world.getCurrent_z()));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            world.getCamera().translate(1, 0, 0);
            debug_tool3.set_text(String.format("Camera coords: (%d, %d, %d)", (int) world.getCamera().position.x, (int) world.getCamera().position.y, world.getCurrent_z()));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            world.getCamera().translate(0, 1, 0);
            debug_tool3.set_text(String.format("Camera coords: (%d, %d, %d)", (int) world.getCamera().position.x, (int) world.getCamera().position.y, world.getCurrent_z()));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            world.getCamera().translate(0, -1, 0);
            debug_tool3.set_text(String.format("Camera coords: (%d, %d, %d)", (int) world.getCamera().position.x, (int) world.getCamera().position.y, world.getCurrent_z()));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            world.setCurrent_z(world.getCurrent_z() - 1);
            debug_tool3.set_text(String.format("Camera coords: (%d, %d, %d)", (int) world.getCamera().position.x, (int) world.getCamera().position.y, world.getCurrent_z()));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            world.setCurrent_z(world.getCurrent_z() + 1);
            debug_tool3.set_text(String.format("Camera coords: (%d, %d, %d)", (int) world.getCamera().position.x, (int) world.getCamera().position.y, world.getCurrent_z()));
        }

        // Клавиши для установки тайлов
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
            id_of_place_tile = 0;
            updateTextsForPlaceTile();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.O)){
            changeIdOfPlaceTile(1);
            updateTextsForPlaceTile();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            changeIdOfPlaceTile(-1);
            updateTextsForPlaceTile();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)){
            changeIdOfPlaceTile(world.tileSets[0].num_of_tiles_in_row);
            updateTextsForPlaceTile();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.U)) {
            changeIdOfPlaceTile(-1 * world.tileSets[0].num_of_tiles_in_row);
            updateTextsForPlaceTile();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)){
            world.save(".//core//assets//maps//" + "map_0_new.txt");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.L)){
            world.load(".//core//assets//maps//" + "map_0_new.txt");
        }
    }

    private void changeIdOfPlaceTile(int difference){
        id_of_place_tile = (id_of_place_tile+difference)%(world.tileSets[world.id_of_current_tileSet].num_of_tiles_in_row * world.tileSets[world.id_of_current_tileSet].num_of_tiles_in_row);
        if (id_of_place_tile < 0)
            id_of_place_tile += world.tileSets[world.id_of_current_tileSet].num_of_tiles_in_row * world.tileSets[world.id_of_current_tileSet].num_of_tiles_in_row;
    }

    private void updateTextsForPlaceTile(){
        // Просто замена двух последующих строчек
        debug_tool4.set_text(String.format("Selected id: (%d)",
                id_of_place_tile));
        debug_tool5.set_text(String.format("Selected tile: (%s), is_passable: (%b), is_solid: (%b)",
                world.tileSets[world.id_of_current_tileSet].getHumanNameById(id_of_place_tile),
                world.tileSets[world.id_of_current_tileSet].getIsPassableById(id_of_place_tile),
                world.tileSets[world.id_of_current_tileSet].getIsSolidById(id_of_place_tile)));
        place_tile.setTexture(id_of_place_tile);
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
    }
}
