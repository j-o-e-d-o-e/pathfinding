package net.joedoe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import net.joedoe.GameInfo;
import net.joedoe.GameMain;

public class GameOver extends MyScreen {
    private Skin skin;
    private Stage stage;
    private Table table;
    private BitmapFont title;
    private TextButton button;
    private Sound sound;

    public GameOver(GameMain game) {
        super(game);
        this.game = game;
        sound = Gdx.audio.newSound(Gdx.files.internal("sounds/gameOver.wav"));
        sound.play();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("fonts/uiskin.json"));
        title = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
        title.setColor(GameInfo.GREY[0], GameInfo.GREY[1], GameInfo.GREY[2], 1);
        createTable();
    }

    void createTable() {
        button = new TextButton("Main Menu", skin);
        button.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int btn) {
                goToMainMenu();
            }
        });
        table = new Table(skin);
        table.setPosition(0, -50);
        table.setFillParent(true);
        table.add(button);
        stage.addActor(table);
    }

    void goToMainMenu() {
        dispose();
        game.setScreen(new Start(game));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(GameInfo.RED[0], GameInfo.RED[1], GameInfo.RED[2], 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.getBatch().begin();
        title.draw(game.getBatch(), "Game Over!", GameInfo.WIDTH / 2f - 150, GameInfo.HEIGHT / 2f + 25);
        game.getBatch().end();
        stage.draw();
        stage.act();
    }

    @Override
    public void dispose() {
        sound.dispose();
        stage.dispose();
        skin.dispose();
        title.dispose();
    }
}