package com.mygdx.colosseum_of_tanks.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.colosseum_of_tanks.TheGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class StartScreen implements Screen {
    private final TheGame game;

    private final int lastState;

    private float WORLD_WIDTH;
    private float WORLD_HEIGHT;

    private Stage stage;
    private Skin skin;
    private Table table;
    private SpriteBatch batch;

    private Texture background1;
    private Texture background2;
    private int backgroundVelocity = 4;
    private int backgroundX = 0;

    private TextButton newButton;
    private TextButton continueButton;
    private TextButton quitButton;
    private Button musicButton;
    private Button soundButton;

    private Music music;
    private int musicState;
    private Preferences prefs = Gdx.app.getPreferences("PREFERENCES");

    public StartScreen(TheGame game, int lastState) {
        this.game = game;

        this.lastState = lastState;

        this.WORLD_WIDTH = Gdx.graphics.getWidth();
        this.WORLD_HEIGHT = Gdx.graphics.getHeight();

        this.stage = new Stage();
        this.skin = new Skin(Gdx.files.internal("skins/commodore/uiskin.json"));
        this.table = new Table();
        this.batch = new SpriteBatch();

        this.background1 = new Texture(Gdx.files.internal("images/tank-stars/tank_stars_blue_bg.png"));
        this.background2 = new Texture(Gdx.files.internal("images/tank-stars/tank_stars_blue_bg.png"));

        Label title = new Label("Colosseum of Tanks", skin);
        this.newButton = new TextButton("New Game", skin);
        this.continueButton = new TextButton("Continue", skin);
        this.quitButton = new TextButton("Quit Game", skin);
        this.musicButton = new Button(skin, "music");
        this.soundButton = new Button(skin, "sound");

        this.music = Gdx.audio.newMusic(Gdx.files.internal("music/rock_music.mp3"));

        if (this.lastState == TheGame.SAVE_GAME) {
            final Dialog dialog = new Dialog("Game saved", skin, "dialog");
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    dialog.show(stage);
                }
            }, 1);
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    dialog.hide();
                }
            }, 4);
            dialog.cancel();
        }

        Table soundTable = new Table();
        soundTable.add(musicButton);
        soundTable.add(soundButton).padLeft(10);

        table.setPosition(0, 0);
        table.setFillParent(true);
        table.center();
        table.add(title);
        table.row();
        table.add(newButton).padTop(30).width(190);
        table.row();
        table.add(continueButton).padTop(10).width(190);
        table.row();
        table.add(quitButton).padTop(10).width(190);
        table.row();
        table.add(soundTable).padTop(10);

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);

        this.musicState = prefs.getInteger("music", 1);
        if (this.musicState == 1) {
            this.music.play();
            this.music.setVolume(0.2f);
            this.music.setLooping(true);
            this.musicButton.toggle();
        }

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background1, backgroundX, 0, WORLD_WIDTH, WORLD_HEIGHT);
        batch.draw(background2, backgroundX + WORLD_WIDTH, 0, WORLD_WIDTH, WORLD_HEIGHT);
        batch.end();

        backgroundX -= backgroundVelocity;
        if (backgroundX + WORLD_WIDTH == 0) {
            backgroundX = 0;
        }

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        newButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                game.setScreen(new ChooseScreen(game));
            }
        });

        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //TO-DO
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        musicButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (musicState == 0) {
                    music.play();
                    musicState = 1;
                    music.setVolume(0.2f);
                    music.setLooping(true);
                    musicButton.toggle();
                    prefs.putInteger("music", 1);
                } else {
                    musicState = 1;
                    music.stop();
                    musicButton.toggle();
                    prefs.putInteger("music", 0);
                }
            }
        });
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
        skin.dispose();
        batch.dispose();

        background1.dispose();
        background2.dispose();
    }
}