package com.mygdx.magegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import static com.mygdx.magegame.Consts.window_h;
import static com.mygdx.magegame.Consts.window_w;

public class MainMenuScreen implements Screen{
    final MageGame game;
    private Stage gui;

    private Label gameName;
    private TextButton buttonStart;
    private TextButton buttonExit;

    OrthographicCamera camera;

    public MainMenuScreen(final MageGame game)
    {
        this.game = game;
        gui = new Stage(new ScreenViewport(), game.batch);

        Gdx.input.setInputProcessor(gui);

        gameName = new Label("MAGE GAME", game.skin);
        gameName.setPosition(Gdx.graphics.getWidth()/2-gameName.getWidth()/2, Gdx.graphics.getHeight()-100);
        gui.addActor(gameName);

        buttonStart = new TextButton("Play", game.skin);
        buttonStart.setPosition(Gdx.graphics.getWidth()/2-buttonStart.getWidth()/2, Gdx.graphics.getHeight()-200);
        gui.addActor(buttonStart);

        buttonExit = new TextButton("Exit", game.skin);
        buttonExit.setPosition(Gdx.graphics.getWidth()/2-buttonExit.getWidth()/2, Gdx.graphics.getHeight()-300);
        gui.addActor(buttonExit);

        buttonStart.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new GameScreen(game));
                dispose();
            }
        });

        buttonExit.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y){
                Gdx.app.exit();
                dispose();
            }
        });

        //camera = new OrthographicCamera();
        //camera.setToOrtho(false, window_w, window_h);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //Gdx.gl.glClearColor(0.8f,0.8f,0.9f, 1);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //camera.update();
        // game.batch.setProjectionMatrix(camera.projection);

        gui.act(delta); // обновляем состояние всех компонентов на сцене
        gui.draw(); // рисуем их
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
        gui.dispose();
    }
}
