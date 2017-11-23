package net.joedoe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import net.joedoe.GameInfo;
import net.joedoe.GameMain;
import net.joedoe.entities.Player;
import net.joedoe.helpers.GameManager;

public class Start extends MyScreen {
    private Skin skin;
    private Stage stage;
    private Table table;
    private BitmapFont title;
    private TextField text;
    private TextButton button;
    private CheckBox check1, check2;
    private ClickListener listener;
    private Sound sound;
    private float timer;

    public Start(GameMain game) {
        super(game);
        sound = Gdx.audio.newSound(Gdx.files.internal("sounds/start.wav"));
        sound.play();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("fonts/uiskin.json"));
        title = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
        title.setColor(GameInfo.RED[0], GameInfo.RED[1], GameInfo.RED[2], 1);
        title.getData().setScale(2.0f);
        listener = new ClickListener() {
            @Override
            public boolean touchDown(InputEvent e, float x, float y, int point, int btn) {
                chooseGender(e.getListenerActor().toString());
                return false;
            }
        };
        createTable();
    }

    void createTable() {
        text = new TextField("", skin);
        text.setMaxLength(12);
        text.setMessageText("PLAYER");
        check1 = new CheckBox(" male ", skin);
        check1.setChecked(true);
        check1.addListener(listener);
        check2 = new CheckBox(" female ", skin);
        check2.addListener(listener);
        button = new TextButton("Start game", skin);
        button.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int btn) {
                startGame();
            }
        });
        table = new Table(skin);
        // table.debug();
        table.setPosition(0, -25);
        table.setFillParent(true);
        table.add("Controls:").top().left();
        table.add(GameInfo.CONTROLS).padLeft(10).width(50).left();
        table.row().padTop(10);
        table.add("Choose a gender:").left();
        table.add(check1).left().padLeft(10);
        table.add(check2).left().padLeft(-80);
        table.row().padTop(10);
        table.add("Enter a name:").left();
        table.add(text).left().padLeft(10);
        table.add(button).left().padLeft(10);
        stage.addActor(table);
    }

    void chooseGender(String checkedBox) {
        if (checkedBox.contains("female")) {
            check1.setChecked(false);
        } else {
            check2.setChecked(false);
        }
    }

    void startGame() {
        String name = text.getText().trim();
        if (name.equals("")) {
            name = "player";
        }
        int gender = 0;
        if (check2.isChecked()) {
            gender = 1;
        }
        GameManager.getInstance().initializeGameData(name, gender);
        GameManager.player = new Player();
        game.setScreen(new City(game));
    }

    @Override
    public void render(float delta) {
        timer += delta;
        if (timer >= 0.3) {
            if (title.getColor().toString().equals("ffff00ff")) {
                title.setColor(GameInfo.RED[0], GameInfo.RED[1], GameInfo.RED[2], 1);
            } else if (title.getColor().toString().equals("d96459ff")) {
                title.setColor(Color.YELLOW);
            }
            timer = 0;
        }
        if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
            startGame();
        }
        Gdx.gl.glClearColor(GameInfo.GREY[0], GameInfo.GREY[1], GameInfo.GREY[2], 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.getBatch().begin();
        title.draw(game.getBatch(), "THE ASSASSIN", GameInfo.WIDTH / 2f - 400, GameInfo.HEIGHT / 2f + 200);
        game.getBatch().end();
        stage.draw();
        stage.act();
    }

    @Override
    public void dispose() {
        sound.dispose();
        title.dispose();
        skin.dispose();
        stage.dispose();
    }
}
