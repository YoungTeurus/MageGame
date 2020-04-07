package com.mygdx.magegame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.mygdx.magegame.MageGame;
import com.mygdx.magegame.objects.TextObject;
import com.mygdx.magegame.world.World;
import space.earlygrey.shapedrawer.ShapeDrawer;

import static com.badlogic.gdx.graphics.GL20.*;
import static com.mygdx.magegame.Consts.*;


public class GameScreen implements Screen, InputProcessor {
    final MageGame game; // Сама игра(?) - взято из туториала
    World world;

    // Всё для отрисовки интерфейса
    Texture drawerTexture;
    ShapeDrawer drawer;
    FrameBuffer fbo;
    SpriteBatch fb;
    Sprite game_interface;
    boolean need_to_update_interface; // Если этот флаг - false, значит не перерисовываем интерфейс
    OrthographicCamera c;

    BitmapFont font;
    String clock = "12:34";

    public GameScreen(final MageGame game){
        this.game = game;
        fbo = new FrameBuffer(Pixmap.Format.RGBA8888,Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        fb = new SpriteBatch();

        // Всё ниже этого - создание ShapeDrawer
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.drawPixel(0, 0);
        drawerTexture = new Texture(pixmap); //remember to dispose of later
        pixmap.dispose();
        TextureRegion region = new TextureRegion(drawerTexture, 0, 0, 1, 1);
        drawer = new ShapeDrawer(fb, region);

        need_to_update_interface = true;
        // Исправление координат у интерфейса
        c = new OrthographicCamera();
        c.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.getBatch().setProjectionMatrix(c.combined);

        font = new BitmapFont();

        // Создание мира
        world = new World(world_w,world_h,true);

        Gdx.input.setInputProcessor(world);

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

        world.draw(); // вызовет actor.draw каждому актеру
        world.act();

        // Ниже будет отрисовываться интерфейс
        draw_interface();

        // Обработка нажатий на клавиатуру
        if (Gdx.input.isKeyJustPressed(Input.Keys.L)){
            world.load(".//core//assets//maps//" + "map_0_new.txt");
        }

        // Чисто для теста
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            world.getCamera().translate(-1, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            world.getCamera().translate(1, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            world.getCamera().translate(0, 1, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            world.getCamera().translate(0, -1, 0);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            world.cast_spell();
            update_interface();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            world.getPlayer().current_hp = Math.max(world.getPlayer().current_hp - 3, 0);
            update_interface();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.O)) {
            world.getPlayer().current_hp = Math.min(world.getPlayer().current_hp + 3, world.getPlayer().max_hp);
            update_interface();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.I)) {
            world.getPlayer().current_mp = Math.max(world.getPlayer().current_mp - 3, 0);
            update_interface();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.U)) {
            world.getPlayer().current_mp = Math.min(world.getPlayer().current_mp + 3, world.getPlayer().max_mp);
            update_interface();
        }
        // Чисто для теста - конец
    }

    private void draw_interface(){
        if (game_interface == null || need_to_update_interface) {
            fbo.begin();
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            fb.begin();
            // Секторы здоровья и маны
            drawer.setColor(0.85f, 0.1f, 0.1f, 1f);
            drawer.sector(0, 0, (float) window_w / 10, 0, (float) (3.15 / 2));
            drawer.setColor(0.1f, 0.35f, 0.85f, 1f);
            drawer.sector(window_w, 0, (float) window_w / 10, (float) (3.14 / 2), 3.15f);

            drawer.getBatch().enableBlending();
            drawer.getBatch().setBlendFunction(GL_ONE_MINUS_SRC_ALPHA, GL_ZERO);
//
            drawer.setColor(0f, 0f, 0f, 1f);

            float percent_of_hp = world.getPlayer().current_hp / (float)world.getPlayer().max_hp;
            float percent_of_mp = world.getPlayer().current_mp / (float)world.getPlayer().max_mp;

            drawer.filledRectangle(0, (window_w / 10f )* percent_of_hp, window_w/10f, window_h);
            drawer.filledRectangle(window_w*0.9f,(window_w / 10f )* percent_of_mp,window_w/10f, window_h);
//

            drawer.getBatch().setBlendFunction(GL_ONE_MINUS_DST_ALPHA, GL_ONE);
            drawer.setColor(0.45f, 0.45f, 0.45f, 1f);
            drawer.sector(0, 0, window_w / 9.5f, 0, (float) (3.15 / 2));
            drawer.sector(window_w, 0, window_w / 9.5f, (float) (3.14 / 2), 3.15f);

            drawer.getBatch().disableBlending();

            // Прямоугольник для заклинаний
            int width_of_spells = 400;
            int height_of_spells = 64;

            drawer.setColor(0.6f, 0.6f, 0.6f, 1f);
            drawer.filledRectangle(window_w/2f - (width_of_spells/2f), 0, width_of_spells, height_of_spells);

            drawer.getBatch().enableBlending();
            drawer.getBatch().setBlendFunction(GL_ONE_MINUS_DST_ALPHA, GL_ONE);
            drawer.setColor(0.5f, 0.5f, 0.5f, 1f);
            drawer.filledCircle(window_w/2f - (width_of_spells/2f) - 25, 25, height_of_spells * 0.85f);

            drawer.getBatch().disableBlending();

            // Квадраты для заклинаний
            drawer.setColor(0.2f, 0.2f, 0.2f, 0.95f);

            int size_of_spell = 64; // Размер квадрата
            float spacing = (width_of_spells-(64*4))/5f; // Расстояние между элементами

            for(int i = 0;i<4;i++){
                drawer.filledRectangle(window_w/2f - (width_of_spells/2f) + (size_of_spell + spacing)*i + spacing, 0, 64, 64);
            }

            // Прямоугольник для камней
            drawer.setColor(0.4f, 0.4f, 0.4f, 0.65f);
            drawer.filledRectangle(window_w/2f - 75, height_of_spells,150,35);
            // Кружки для камней
            drawer.setColor(0.2f, 0.2f, 0.2f, 0.95f);

            int radius_for_gem = 16; // Размер кружка
            spacing = 10; // Расстояние между элементами
            for(int i = -1; i <=1; i++){
                drawer.filledCircle(window_w/2f + (radius_for_gem*2 + spacing)*i, 65+(35/2f),radius_for_gem); // В один такой кружок можно будет разместить текстурку 32*32
            }

            // Прямоугольник для времени
            int width_of_clock = 120;
            int height_of_clock = 50;
            drawer.setColor(0.9f, 0.9f, 0.9f, 0.65f);
            drawer.filledRectangle(window_w/2f - (width_of_clock/2f), window_h - height_of_clock,width_of_clock,height_of_clock);

            fb.enableBlending();
            fb.setBlendFunction(GL_ONE_MINUS_DST_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

            font.draw(fb,clock,window_w/2f - (width_of_clock/2f),window_h - (height_of_clock/2f),width_of_clock,1,false);

            fb.disableBlending();

            fb.end();
            fbo.end();

            game_interface = new Sprite(fbo.getColorBufferTexture());
            need_to_update_interface = false;
        }
        // TODO: Было бы круто рисовать поверх здоровья какую-нибудь анимированную текстурку, чтобы жизни были более красивыми.
        game.getBatch().begin();
        game_interface.draw(game.getBatch());
        game.getBatch().end();
    }

    private void update_interface(){
        need_to_update_interface = true;
    }

    @Override
    public void resize(int width, int height) {
        world.getCamera().viewportWidth = width;
        world.getCamera().viewportHeight = height;
        world.getViewport().update(width, height, false);
        window_w = width;
        window_h = height;
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
        drawerTexture.dispose();
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
