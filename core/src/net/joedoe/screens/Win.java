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

public class Win extends MyScreen {
    private Skin skin;
    private Stage stage;
    private Table table;
    private BitmapFont title;
    private TextButton button;
    private float timer;
    private Sound sound;

    public Win(GameMain game) {
        super(game);
        sound = Gdx.audio.newSound(Gdx.files.internal("sounds/win.wav"));
        sound.play();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("fonts/uiskin.json"));
        title = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
        title.setColor(GameInfo.BLUE[0], GameInfo.BLUE[1], GameInfo.BLUE[2], 1);
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
        timer += delta;
        if (timer >= 0.3) {
            if (title.getColor().toString().equals("4ba6e0ff")) {
                title.setColor(GameInfo.GREEN[0], GameInfo.GREEN[1], GameInfo.GREEN[2], 1);
            } else if (title.getColor().toString().equals("97ce68ff")) {
                title.setColor(GameInfo.BLUE[0], GameInfo.BLUE[1], GameInfo.BLUE[2], 1);
            }
            timer = 0;
        }
        Gdx.gl.glClearColor(GameInfo.RED[0], GameInfo.RED[1], GameInfo.RED[2], 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.getBatch().begin();
        title.draw(game.getBatch(), "You won!", GameInfo.WIDTH / 2f - 120, GameInfo.HEIGHT / 2f + 25);
        game.getBatch().end();
        stage.draw();
        stage.act();
    }

    @Override
    public void dispose() {
        sound.dispose();
        skin.dispose();
        stage.dispose();
        title.dispose();
    }

}